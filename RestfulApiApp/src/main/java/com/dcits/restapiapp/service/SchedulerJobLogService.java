package com.dcits.restapiapp.service;


import com.alibaba.fastjson.JSONObject;
import com.dcits.restapiapp.common.response.ResponseUtil;
import com.dcits.restapiapp.mapper.HttpBaseInfoMapper;
import com.dcits.restapiapp.mapper.SchedulerJobLogMapper;
import com.dcits.restapiapp.model.SchedulerJobLogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class SchedulerJobLogService {

    @Autowired
    SchedulerJobLogMapper schedulerJobLogMapper;
    @Autowired
    HttpBaseInfoMapper httpBaseInfoMapper;

    public ResponseUtil searchLogByInterfaceIdAndDate(String interfaceIdOrName, Long startTime, Long endTime)throws Exception{
        ResponseUtil responseUtil = new ResponseUtil();
        try{
            Timestamp start = new Timestamp(startTime);
            Timestamp end = new Timestamp(endTime);
            List<SchedulerJobLogModel> schedulerJobLogModels = schedulerJobLogMapper.searchLogByInterfaceIdAndDate(interfaceIdOrName, start, end);
            responseUtil.setCode("200");
            responseUtil.setBody(JSONObject.toJSONString(schedulerJobLogModels));
        }catch (Exception e){
            e.printStackTrace();
            responseUtil.setCode("500");
            responseUtil.setBody("【查询接口任务执行日志时出错:"+e.getMessage()+"】");
        }
        return responseUtil;


    }



}
