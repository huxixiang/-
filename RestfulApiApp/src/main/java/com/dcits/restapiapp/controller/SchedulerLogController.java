package com.dcits.restapiapp.controller;


import com.dcits.restapiapp.common.response.ResponseUtil;
import com.dcits.restapiapp.service.SchedulerJobLogService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/schedulerlog")
public class SchedulerLogController {

    @Autowired
    SchedulerJobLogService schedulerJobLogService;

    @PostMapping("showlog")
    @ApiOperation("根据接口ID或者接口名称以及起始时间查询接口任务的调度日志信息")
    public ResponseUtil searchLog(@RequestParam(value = "interfaceIdOrName",required = true)String interfaceIdOrName,
                                  @RequestParam(value = "startTime",required = true)Long startTime,
                                  @RequestParam(value = "endTime",required = true)Long endTime){
        ResponseUtil responseUtil = new ResponseUtil();
        try{
            responseUtil = schedulerJobLogService.searchLogByInterfaceIdAndDate(interfaceIdOrName,startTime,endTime);
        }catch (Exception e){
            responseUtil.setCode("500");
            responseUtil.setBody(e.getMessage());

        }
        return responseUtil;

    }
}
