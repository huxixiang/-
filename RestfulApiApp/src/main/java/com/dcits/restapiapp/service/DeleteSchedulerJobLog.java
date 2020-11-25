package com.dcits.restapiapp.service;

import com.dcits.restapiapp.mapper.SchedulerJobLogMapper;
import com.dcits.restapiapp.util.ExceptionStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * @desc 删除30天前的执行日志信息
 * @date 2020-05-06
 * @author huxxe
 */
@Configuration
@EnableScheduling
public class DeleteSchedulerJobLog {
    private static final Logger logger = LoggerFactory.getLogger(DeleteSchedulerJobLog.class);
    @Autowired
    SchedulerJobLogMapper schedulerJobLogMapper;
    //3.添加定时任务
    @Scheduled(cron = "0 0 0 1/1 * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void deleteSchedulerJobLog() {
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
        try{
            schedulerJobLogMapper.deleteLogPre15Days();
        }catch (Exception e){
            logger.error(ExceptionStack.exceptionStackPrint(e));
        }

    }

}
