package com.dcits.restapiapp.service;

import com.alibaba.fastjson.JSONObject;
import com.dcits.restapiapp.common.response.ResponseUtil;
import com.dcits.restapiapp.mapper.HttpBaseInfoMapper;
import com.dcits.restapiapp.mapper.SchedulerCronMapper;
import com.dcits.restapiapp.model.HttpBaseInfoModel;
import com.dcits.restapiapp.model.SchedulerCronModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SchedulerCornService {

    @Autowired
    SchedulerCronMapper schedulerCornMapper;

    @Autowired
    HttpBaseInfoMapper httpBaseInfoMapper;


    public ResponseUtil getInterfaceTaskStatus(String interfaceId)throws Exception{
        ResponseUtil responseUtil = new ResponseUtil();
        Map<String,String> schedulerStatusMap = new HashMap<>();
        if(interfaceId==null || interfaceId.trim().equals("")){
            List<SchedulerCronModel> schedulerCornModels = schedulerCornMapper.searchAll();
            if(schedulerCornModels!=null && !schedulerCornModels.isEmpty()){
                for(SchedulerCronModel schedulerCornModel:schedulerCornModels){
                    HttpBaseInfoModel httpBaseInfoModel = httpBaseInfoMapper.searchHttpBaseInfoByInterfaceId(schedulerCornModel.getInterfaceId());
                    schedulerStatusMap.put(httpBaseInfoModel.getInterfaceName(),schedulerCornModel.getStatus());
                }
                responseUtil.setCode("200");
                responseUtil.setBody(JSONObject.toJSONString(schedulerStatusMap));
            }else{
                responseUtil.setCode("404");
                responseUtil.setBody("未找到任何接口的状态信息！");
            }
            return responseUtil;
        }else{
            HttpBaseInfoModel httpBaseInfoModel = httpBaseInfoMapper.searchHttpBaseInfoByInterfaceId(interfaceId);
            SchedulerCronModel schedulerCornModel = schedulerCornMapper.searchSchedulerCronByInterfaceId(interfaceId);
            if(schedulerCornModel==null || schedulerCornModel.getInterfaceId().trim().equals("")){
                responseUtil.setCode("404");
                responseUtil.setBody("根据接口ID："+interfaceId+"，未找到改接口对应的状态信息！");
            }else{
                schedulerStatusMap.put(httpBaseInfoModel.getInterfaceName(),schedulerCornModel.getStatus());
                responseUtil.setCode("200");
                responseUtil.setBody(JSONObject.toJSONString(schedulerStatusMap));
            }
            return responseUtil;

        }
    }
}
