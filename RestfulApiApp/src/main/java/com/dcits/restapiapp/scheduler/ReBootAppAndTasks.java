package com.dcits.restapiapp.scheduler;

import com.alibaba.fastjson.JSONObject;
import com.dcits.restapiapp.common.kafka.KafkaProducerUtil;
import com.dcits.restapiapp.common.response.ResponseUtil;
import com.dcits.restapiapp.httpcore.HttpClientCore;
import com.dcits.restapiapp.httpcore.HttpResult;
import com.dcits.restapiapp.mapper.*;
import com.dcits.restapiapp.model.SchedulerCronModel;
import com.dcits.restapiapp.model.SchedulerJobLogModel;
import com.dcits.restapiapp.scheduler.interfaceprocess.InterfaceCommon;
import com.dcits.restapiapp.util.ExceptionStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

@Component
public class ReBootAppAndTasks implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(ReBootAppAndTasks.class);
    @Autowired
    SchedulerCronMapper schedulerCronMapper;

    @Autowired
    HttpClientCore httpClientCore;//执行http请求的核心类

    @Autowired
    HttpBaseInfoMapper httpBaseInfoMapper;

    @Autowired
    HeadersMapper headersMapper;

    @Autowired
    ParametersMapper parametersMapper;

    @Autowired
    TokensMapper tokensMapper;

    @Autowired
    SchedulerJobLogMapper schedulerJobLogMapper;

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Bean("reBootApp")
    private ThreadPoolTaskScheduler getThreadPoolTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(50);
//        threadPoolTaskScheduler.setThreadNamePrefix("job-");
        threadPoolTaskScheduler.setAwaitTerminationSeconds(300);

        return threadPoolTaskScheduler;
    }

        /**
     * 重启springboot应用时自动启动上次关闭是处于running状态的接口调度任务
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<SchedulerCronModel> cronPatterns = null;
        try{
            cronPatterns = schedulerCronMapper.searchRunningTask();
        }catch (Exception e){
            //查询处于运行状态的任务时出错，将所有的running状态的task置为stopped
            logger.error("【重启应用时查询所有处于running状态的task出错，将所有任务的状态设置为stopped!错误原因："+e.getMessage()+"】");
            schedulerCronMapper.updateAllTaskStatus();
            return;
        }
        if(cronPatterns==null || cronPatterns.isEmpty()){
            return;
        }
//        Map<String,String> jobResultMap = new HashMap<>();
        for(SchedulerCronModel cronPattern:cronPatterns){
            try{
                ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(new TaskRunnable(cronPattern),new Trigger(){
                    @Override
                    public Date nextExecutionTime(TriggerContext triggerContext){
                        return new CronTrigger(cronPattern.getCron()).nextExecutionTime(triggerContext);
                    }
                });
//                System.out.println(cronPattern.getInterfaceId());
//                jobResultMap.put(cronPattern.getInterfaceId(),"success");
                DynamicSchedulerTask.futureMap.put(cronPattern.getInterfaceId(),future);
//                schedulerCronMapper.updateStatusByInterfaceId(cronPattern.getInterfaceId(),"running");
            }catch (Exception e){
                e.printStackTrace();
                logger.error("接口ID:"+cronPattern.getInterfaceId()+" 对应的接口重启失败，状态被置为stopped，错误原因："+e.getMessage());
//                jobResultMap.put(cronPattern.getInterfaceId(),"fail");
                schedulerCronMapper.updateStatusByInterfaceId(cronPattern.getInterfaceId(),"stopped");
            }
        }


    }


    /**
     * @desc 接口请求逻辑
     */
    class TaskRunnable implements Runnable{
        private SchedulerCronModel schedulerCronModel;
        public TaskRunnable(SchedulerCronModel schedulerCronModel){
            this.schedulerCronModel = schedulerCronModel;
        }
        @Override
        @Transactional(rollbackFor = Exception.class)
        public void run() {
            /**
             * 1、根据interfaceId去数据库中查询该接口对应的请求配置信息（数据库建议使用redis，因为要经常改动）
             * 2、解析接口的配置信息，调用对应的http请求完成查询工作
             * 3、将查询后的数据写到数据库或者kafka topic中
             * 4、生成本次调度执行的唯一id，将本次任务执行的结果写到数据库中
             */
            SchedulerJobLogModel schedulerJobLogModel = new SchedulerJobLogModel();

            schedulerJobLogModel.setInterfaceId(schedulerCronModel.getInterfaceId());
            schedulerJobLogModel.setInterfaceId(schedulerCronModel.getInterfaceId());
            String interfaceName = "";
            try{
                interfaceName = httpBaseInfoMapper.searchNameById(schedulerCronModel.getInterfaceId());
            }catch (Exception e){}
            if(interfaceName.equals("")){
                interfaceName = "接口名称为空";
            }
//            System.out.println("接口名称-1："+interfaceName);
            schedulerJobLogModel.setInterfaceName(interfaceName);
            schedulerJobLogModel.setProcessClass(schedulerCronModel.getClassName());
            Long startTime = System.currentTimeMillis();
            Long endTime = -1l;
            schedulerJobLogModel.setProcessStartTime(new Timestamp(startTime));

            try{
                Class<?> classInfo = Class.forName("com.dcits.restapiapp.scheduler.interfaceprocess.impl."+schedulerCronModel.getClassName().trim());
                Class[] paramsClasses = {String.class,
                        HttpBaseInfoMapper.class,
                        HeadersMapper.class,
                        ParametersMapper.class,
                        TokensMapper.class,
                        HttpClientCore.class};
                InterfaceCommon interfaceProcessCommon =  (InterfaceCommon)classInfo.getConstructor(paramsClasses).newInstance(schedulerCronModel.getInterfaceId(),
                        httpBaseInfoMapper,
                        headersMapper,
                        parametersMapper,
                        tokensMapper,
                        httpClientCore);
                List<HttpResult> httpResults = interfaceProcessCommon.startHttpRequestJob();
                //将结果回写到kafka主题里
                if(httpResults!=null && !httpResults.isEmpty()){
                    for(HttpResult result:httpResults){
                        //将结果回写到kafka主题里
                        if(result.getCode()==200){
                            KafkaProducerUtil.sendMessageToKafka(schedulerCronModel.getKafkaTopic(),"",result.getBody());
                            schedulerJobLogModel.setStatus("success");
                            schedulerJobLogModel.setSuccessDesc("接口"+schedulerCronModel.getInterfaceId()+"执行成功");
                            endTime = System.currentTimeMillis();
                            schedulerJobLogModel.setProcessEndTime(new Timestamp(endTime));
                            schedulerJobLogModel.setProcessMilliseconds(endTime-startTime);
                        }else{
                            schedulerJobLogModel.setStatus("fail");
                            schedulerJobLogModel.setFailDesc("接口"+schedulerCronModel.getInterfaceId()
                                    +"请求成功，但服务器返回错误码："
                                    +result.getCode()
                                    +"，返回内容为："+result.getBody());
                            endTime = System.currentTimeMillis();
                            schedulerJobLogModel.setProcessEndTime(new Timestamp(endTime));
                            schedulerJobLogModel.setProcessMilliseconds(endTime-startTime);
                        }
                        //每次向kafka主题发送消息后插入一条记录
                        String schedulerJobId = UUID.randomUUID().toString().replaceAll("-","");
                        schedulerJobLogModel.setSchedulerJobId(schedulerJobId);
                        schedulerJobLogMapper.insertALog(schedulerJobLogModel);
                    }

                }else{//本次请求拿到的结果集为空
                    schedulerJobLogModel.setStatus("fail");
                    schedulerJobLogModel.setFailDesc("接口"+schedulerCronModel.getInterfaceId()+"请求返回的结果集为空！");
                    endTime = System.currentTimeMillis();
                    schedulerJobLogModel.setProcessEndTime(new Timestamp(endTime));
                    schedulerJobLogModel.setProcessMilliseconds(endTime-startTime);
                    String schedulerJobId = UUID.randomUUID().toString().replaceAll("-","");
                    schedulerJobLogModel.setSchedulerJobId(schedulerJobId);
                    schedulerJobLogMapper.insertALog(schedulerJobLogModel);
                }
                return;
            }catch (Exception e){
                schedulerJobLogModel.setStatus("fail");
                schedulerJobLogModel.setFailDesc("接口"+schedulerCronModel.getInterfaceId()+"执行失败，失败原因："+ ExceptionStack.exceptionStackPrint(e));
                endTime = System.currentTimeMillis();
                schedulerJobLogModel.setProcessEndTime(new Timestamp(endTime));
                schedulerJobLogModel.setProcessMilliseconds(endTime-startTime);
                e.printStackTrace();
            }
            //将任务执行日志写入到日志表中
            try{
//                System.out.println("LOG:"+JSONObject.toJSON(schedulerJobLogModel));
                if(schedulerJobLogModel.getStatus()!=null && !schedulerJobLogModel.getStatus().trim().equals("")){
                    String schedulerJobId = UUID.randomUUID().toString().replaceAll("-","");
                    schedulerJobLogModel.setSchedulerJobId(schedulerJobId);
                    schedulerJobLogMapper.insertALog(schedulerJobLogModel);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
