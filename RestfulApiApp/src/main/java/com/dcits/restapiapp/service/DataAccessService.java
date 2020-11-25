package com.dcits.restapiapp.service;

import com.dcits.restapiapp.common.kafka.KafkaProducerUtil;
import com.dcits.restapiapp.mapper.DataAccessMapper;
import com.dcits.restapiapp.model.ExternalSystemModel;
import com.dcits.restapiapp.model.ExternalSystemSchedulerLogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;
import java.util.List;

/**
 * @desc 外系统数据接入服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DataAccessService {
    @Autowired
    DataAccessMapper dataAccessMapper;

    /**
     * @desc将请求获取的json Body体根据元数据映射关系推送到kafka主题中
     * @param systemId
     * @param bussinessType
     * @param body
     * @throws Exception
     */
    public void dataAccessService(String systemId,String bussinessType,String body)throws Exception{
        ExternalSystemSchedulerLogModel externalSystemSchedulerLogModel = new ExternalSystemSchedulerLogModel();
        long startTime = System.currentTimeMillis();
        externalSystemSchedulerLogModel.setStartTime(new Timestamp(startTime));
        externalSystemSchedulerLogModel.setExternalSystemId(systemId);
        try{
            List<ExternalSystemModel> externalSystemModels = dataAccessMapper.selectExternalSystemMetaDataBySysIdAndBussinessType(systemId,bussinessType);
            if(externalSystemModels==null || externalSystemModels.isEmpty()){

                long endTime = System.currentTimeMillis();
                externalSystemSchedulerLogModel.setEndTime(new Timestamp(endTime));
                externalSystemSchedulerLogModel.setStatus("fail");
                externalSystemSchedulerLogModel.setProcessMilliseconds((int)(endTime-startTime));
                externalSystemSchedulerLogModel.setDescription("【错误：未找到对应系统的元数据配置信息！】");
                dataAccessMapper.insertALog(externalSystemSchedulerLogModel);
                throw new Exception("【错误：未找到对应系统的元数据配置信息！】");
            }
            for(ExternalSystemModel externalSystemModel:externalSystemModels){
                KafkaProducerUtil.sendMessageToKafka(externalSystemModel.getKafkaTopic(),"",body);
                long endTime = System.currentTimeMillis();
                externalSystemSchedulerLogModel.setEndTime(new Timestamp(endTime));
                externalSystemSchedulerLogModel.setBussinessType(externalSystemModel.getBussinessType());
                externalSystemSchedulerLogModel.setStatus("success");
                externalSystemSchedulerLogModel.setProcessMilliseconds((int)(endTime-startTime));
                externalSystemSchedulerLogModel.setDescription("数据接收成功");
                dataAccessMapper.insertALog(externalSystemSchedulerLogModel);
            }
        }catch (Exception e){
            long endTime = System.currentTimeMillis();
            externalSystemSchedulerLogModel.setEndTime(new Timestamp(endTime));
            externalSystemSchedulerLogModel.setStatus("fail");
            externalSystemSchedulerLogModel.setProcessMilliseconds((int)(endTime-startTime));
            externalSystemSchedulerLogModel.setDescription("【数据接入时发生错误："+e.getMessage()+"】");
            dataAccessMapper.insertALog(externalSystemSchedulerLogModel);
            throw new Exception("【数据接入时发生错误："+e.getMessage()+"】");
        }


    }




}
