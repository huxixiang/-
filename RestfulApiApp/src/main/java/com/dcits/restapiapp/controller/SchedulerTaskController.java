package com.dcits.restapiapp.controller;


import com.dcits.restapiapp.common.response.ResponseUtil;
import com.dcits.restapiapp.scheduler.DynamicSchedulerTask;
import com.dcits.restapiapp.service.SchedulerCornService;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/schedulertask")
public class SchedulerTaskController {
    @Autowired
    DynamicSchedulerTask dynamicSchedulerTask;

    @Autowired
    SchedulerCornService schedulerCornService;

    @GetMapping("/start")
    @ApiOperation("启动全部的调度服务")
    public ResponseUtil startAllSchedulerTasks(){
        ResponseUtil responseUtil = new ResponseUtil();
        try{
            responseUtil = dynamicSchedulerTask.startSchedulerTask();
        }catch (Exception e){
            e.printStackTrace();
            responseUtil.setCode("500");
            responseUtil.setBody("启动失败！"+e.getMessage());
        }
        return responseUtil;
    }

    @GetMapping("/stopalltasks")
    @ApiOperation("关闭所有的接口调度任务")
    public ResponseUtil stopAllSchedulerTasks(){
        ResponseUtil responseUtil = new ResponseUtil();
        try{
            responseUtil = dynamicSchedulerTask.stopAllSchedulerTask();
        }catch (Exception e){
            responseUtil.setCode("500");
            responseUtil.setBody("接口任务停止失败，"+e.getMessage());
        }
        return responseUtil;
    }

    @PostMapping("/starttaskbyid")
    @ApiOperation("根据接口ID启动接口调度任务")
    public ResponseUtil startTaskByInterface(@RequestParam(value = "interfaceId",required = true)String interfaceId){
        ResponseUtil responseUtil = new ResponseUtil();
        try{
            responseUtil = dynamicSchedulerTask.startTaskByInterfaceId(interfaceId);
        }catch (Exception e){
            responseUtil.setCode("500");
            responseUtil.setBody("接口"+interfaceId+"启动失败，"+e.getMessage());
        }
        return responseUtil;
    }
    @PostMapping("/updatecron")
    @ApiOperation("更新cron")
    public ResponseUtil updateCorn(@RequestParam(value = "interfaceId",required = true) String interfaceId,
                                   @RequestParam(value = "cron",required = true)String cron){
        ResponseUtil responseUtil = new ResponseUtil();
        try{
            responseUtil = dynamicSchedulerTask.updateCronByInterfaceId(interfaceId,cron);
        }catch (Exception e){
            responseUtil.setCode("500");
            responseUtil.setBody("接口"+interfaceId+"任务调度时间更新失败，"+e.getMessage());
        }
        return responseUtil;
    }
    @PostMapping("/stoptaskbyid")
    @ApiOperation("根据接口ID停止该接口任务")
    public ResponseUtil stopTaskByInterfaceId(@RequestParam(value = "interfaceId",required = true) String interfaceId){
        ResponseUtil responseUtil = new ResponseUtil();
        try{
            responseUtil = dynamicSchedulerTask.stopTaskByInterfaceId(interfaceId);
        }catch (Exception e){
            responseUtil.setCode("500");
            responseUtil.setBody("接口"+interfaceId+"调度任务停止失败，"+e.getMessage());
        }
        return responseUtil;
    }

    @PostMapping("/lookupstatus")
    @ApiOperation("查看全部接口或者根据接口ID查询接口调度任务的状态")
    public ResponseUtil lookUpStatus(@RequestParam(value = "interfaceId",required = false)String interfaceId){
        ResponseUtil responseUtil = new ResponseUtil();
        try{
            responseUtil = schedulerCornService.getInterfaceTaskStatus(interfaceId);
        }catch (Exception e){
            responseUtil.setCode("500");
            responseUtil.setBody("查询出错："+e.getMessage());
        }
        return responseUtil;
    }


}
