package com.dcits.restapiapp.scheduler;

import com.alibaba.fastjson.JSON;
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
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @desc 动态执行多线程定时任务
 * @author huxx
 * @date 2020-02018
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class DynamicSchedulerTask {
    private static final Logger logger = LoggerFactory.getLogger(DynamicSchedulerTask.class);

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    //定时任务执行返回结果,只保存任务启动成功的返回值
    protected static final ConcurrentHashMap<String, ScheduledFuture<?>> futureMap = new ConcurrentHashMap<>();

    @Bean
    private ThreadPoolTaskScheduler getThreadPoolTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(50);
//        threadPoolTaskScheduler.setThreadNamePrefix("job-");
        threadPoolTaskScheduler.setAwaitTerminationSeconds(300);

        return threadPoolTaskScheduler;
    }
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


    /**
     * 启动所有的定时任务
     */
    public ResponseUtil startSchedulerTask()throws Exception{
        //从数据库动态获取所有的接口配置信息
        ResponseUtil responseUtil = new ResponseUtil();
        //如果futureMap不为空说明定时任务已经全部启动过一次
        if(!futureMap.isEmpty()){
            responseUtil.setCode("300");
            responseUtil.setBody("接口定时调度任务已经全部启动过，不必再次重启！");
            return responseUtil;
        }
        /**
         * 1、首先从数据库中查出所有的接口ID和其对应的corn
         * 2、循环遍历查询结果，为每个接口启动一个定时任务
         * 3、统计接口任务的启动结果（哪些启动成功，哪些没有成功）
         * 4、将启动结果写入到数据库中TokenCommon
         */
        List<SchedulerCronModel> cronPatterns = null;
        try{
            cronPatterns = schedulerCronMapper.searchAll();
//            System.out.println(cronPatterns.toString());
        }catch (Exception e){
            responseUtil.setCode("500");
            responseUtil.setBody("获取接口任务执行时间时失败，"+e.getMessage());
            return responseUtil;
        }

        if(cronPatterns==null || cronPatterns.isEmpty()){
            responseUtil.setCode("404");
            responseUtil.setBody("接口任务定时调度配置信息为空，无法执行接口任务！");
            return responseUtil;
        }
        Map<String,String> jobResultMap = new HashMap<>();
        for(SchedulerCronModel cronPattern:cronPatterns){
            try{
                ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(new TaskRunnable(cronPattern),new Trigger(){
                    @Override
                    public Date nextExecutionTime(TriggerContext triggerContext){
                        return new CronTrigger(cronPattern.getCron()).nextExecutionTime(triggerContext);
                    }
                });
//                System.out.println(cronPattern.getInterfaceId());
                jobResultMap.put(cronPattern.getInterfaceId(),"success");
                futureMap.put(cronPattern.getInterfaceId(),future);
                schedulerCronMapper.updateStatusByInterfaceId(cronPattern.getInterfaceId(),"running");
            }catch (Exception e){
                jobResultMap.put(cronPattern.getInterfaceId(),"fail");
                schedulerCronMapper.updateStatusByInterfaceId(cronPattern.getInterfaceId(),"stopped");
            }
        }
        responseUtil.setCode("200");
        responseUtil.setBody(JSONObject.toJSONString(jobResultMap));
        return responseUtil;
    }

    public ResponseUtil stopAllSchedulerTask()throws Exception{
        ResponseUtil responseUtil = new ResponseUtil();
        Map<String,String> stopResult = new HashMap<>();
        if(!futureMap.isEmpty()){
            for(ConcurrentHashMap.Entry<String,ScheduledFuture<?>> entry:futureMap.entrySet()){
                String interfaceId = entry.getKey();
                ScheduledFuture<?> future = entry.getValue();
                try{
                    future.cancel(true);
                    futureMap.remove(interfaceId);
                    schedulerCronMapper.updateStatusByInterfaceId(interfaceId,"stopped");
                    stopResult.put(interfaceId,"stop success");
                }catch (Exception e){
                    stopResult.put(interfaceId,"stop fail &&"+e.getMessage());
                }
            }
            responseUtil.setCode("200");
            responseUtil.setBody(JSONObject.toJSONString(stopResult));
        }else{
            responseUtil.setCode("500");
            responseUtil.setBody("没有正在运行的接口任务，无法执行停止操作！");
        }
        return responseUtil;
    }


    public ResponseUtil startTaskByInterfaceId(String interfaceId)throws Exception{
        ResponseUtil responseUtil = new ResponseUtil();
        if(!futureMap.isEmpty()){
           ScheduledFuture<?> scheduledFuture = futureMap.get(interfaceId);
           if(scheduledFuture!=null){
               responseUtil.setCode("500");
               responseUtil.setBody("接口"+interfaceId+"定时调度任务正在运行，不必重启！");
               return responseUtil;
           }
        }
        SchedulerCronModel schedulerCornModel = schedulerCronMapper.searchSchedulerCronByInterfaceId(interfaceId);
        ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(new TaskRunnable(schedulerCornModel),new Trigger(){
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext){
                return new CronTrigger(schedulerCornModel.getCron()).nextExecutionTime(triggerContext);
            }
        });
//        System.out.println(interfaceId);
        futureMap.put(interfaceId,future);
        schedulerCronMapper.updateStatusByInterfaceId(interfaceId,"running");
        responseUtil.setCode("200");
        responseUtil.setBody("接口"+interfaceId+"启动成功。");
        return responseUtil;

    }

    /**
     * @desc 根据接口ID更新接口任务的调度时间
     * @param interfaceId
     * @param cron
     * @return
     * @throws Exception
     */
    public ResponseUtil updateCronByInterfaceId(String interfaceId, String cron)throws Exception{
        ResponseUtil responseUtil = new ResponseUtil();
        ScheduledFuture<?> future = futureMap.get(interfaceId);
        if(future!=null){

            schedulerCronMapper.updateStatusByInterfaceId(interfaceId,"running");
            schedulerCronMapper.updateCronByInterfaceId(interfaceId,cron);
            future.cancel(true);
            SchedulerCronModel schedulerCornModel = schedulerCronMapper.searchSchedulerCronByInterfaceId(interfaceId);
            future = threadPoolTaskScheduler.schedule(new TaskRunnable(schedulerCornModel),new Trigger(){
                @Override
                public Date nextExecutionTime(TriggerContext triggerContext){
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
            });
//            future.get();//阻塞，直到执行结束返回
            futureMap.put(interfaceId,future);

            responseUtil.setCode("200");
            responseUtil.setBody("接口"+interfaceId+"调度周期更新成功。");
        }else{
            schedulerCronMapper.updateCronByInterfaceId(interfaceId,cron);
//            responseUtil.setCode("500");
//            responseUtil.setBody("接口"+interfaceId+"调度周期更新失败！");
            responseUtil.setCode("200");
            responseUtil.setBody("接口"+interfaceId+"调度周期更新成功！");
        }
        return responseUtil;
    }

    /**
     * @desc 根据接口ID停止正在执行的接口调度任务
     */
    public ResponseUtil stopTaskByInterfaceId(String interfaceId)throws Exception{
        ResponseUtil responseUtil = new ResponseUtil();
        if(futureMap.isEmpty()){
            responseUtil.setCode("500");
            responseUtil.setBody("接口"+interfaceId+"还未启动！");
        }else{
            ScheduledFuture<?> scheduledFuture = futureMap.get(interfaceId);
            if(scheduledFuture==null){
                responseUtil.setCode("500");
                responseUtil.setBody("接口"+interfaceId+"还未启动！");
            }else{
                scheduledFuture.cancel(true);
                futureMap.remove(interfaceId);
                schedulerCronMapper.updateStatusByInterfaceId(interfaceId,"stopped");
                responseUtil.setCode("200");
                responseUtil.setBody("接口"+interfaceId+"调度任务停止成功。");
            }
        }
        return responseUtil;
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
            String interfaceName = "";
            try{
                interfaceName = httpBaseInfoMapper.searchNameById(schedulerCronModel.getInterfaceId());
            }catch (Exception e){}
            if(interfaceName.equals("")){
                interfaceName = "接口名称为空";
            }
//            System.out.println("接口名称："+interfaceName);
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
//                System.out.println("httpResults:"+ JSON.toJSONString(httpResults));
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

//    /**
//     * @desc 程序停止前需要将数据库中接口调度任务的状态（status）更新
//     * @author huxx
//     * @date 2020-02-20
//     */
//    @PreDestroy
//    public void destroy(){
//        try{
//            schedulerCronMapper.updateAllTaskStatus();
//            System.out.println("更新所有任务的状态！");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }



    public void stopTaskByInterfaceIds(List<String> interfaceIds)throws Exception{
        for(String id:interfaceIds){
            stopTaskByInterfaceId(id);
        }
    }







}
