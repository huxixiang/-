package com.dcits.restapiapp.service;


import com.alibaba.fastjson.JSONObject;
import com.dcits.restapiapp.common.response.ResponseUtil;
import com.dcits.restapiapp.mapper.*;
import com.dcits.restapiapp.model.HeadersModel;
import com.dcits.restapiapp.model.HttpBaseInfoModel;
import com.dcits.restapiapp.model.ParametersModel;
import com.dcits.restapiapp.model.SchedulerCronModel;
import com.dcits.restapiapp.scheduler.DynamicSchedulerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @desc 保存接口定时调度元数据服务类
 * @author huxx
 * @date 2020-03-13
 */
@Service
public class MetaDataService {
    @Autowired
    HttpBaseInfoMapper httpBaseInfoMapper;//url等基本信息
    @Autowired
    HeadersMapper headersMapper;//请求头参数
    @Autowired
    ParametersMapper parametersMapper;//请求参数

    @Autowired
    SchedulerCronMapper schedulerCronMapper;//接口调度元数据


    @Autowired
    DynamicSchedulerTask dynamicSchedulerTask;

    @Autowired
    TokensMapper tokensMapper;//接口鉴权元数据

    @Autowired
    SchedulerJobLogMapper schedulerJobLogMapper;//日志信息


    /**
     * @desc 解析json字符串中的接口元数据信息并保存到mysql数据库中
     * @param jsonData
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseUtil saveMetaDateToDb(String jsonData)throws Exception{
        ResponseUtil responseUtil = new ResponseUtil();
        try{
            Map<String,Object> metaDataMap = (Map<String,Object>)JSONObject.parseObject(jsonData);
            List<Object> metaDataList = ( List<Object>)metaDataMap.get("metaDatas");
            if(metaDataList!=null && !metaDataList.isEmpty()){
                for(int i = 0;i<metaDataList.size();i++){
                    String interfaceId = UUID.randomUUID().toString().replaceAll("-","");
                    Map<String,Object> metaData = (Map<String,Object>)JSONObject.parseObject(metaDataList.get(i).toString());
                    //接口基本信息
                    Map<String,String>httpBaseInfo = (Map<String,String>)metaData.get("httpBaseInfo");
                    HttpBaseInfoModel httpBaseInfoModel = new HttpBaseInfoModel();
                    if(httpBaseInfo!=null && !httpBaseInfo.isEmpty()){
                        String interfaceName = httpBaseInfo.get("interfaceName");
                        if(interfaceName==null || interfaceName.equals("")){
                            throw new Exception("【接口名称为空！】");
                        }
                        httpBaseInfoModel.setInterfaceName(interfaceName);
                        String url = httpBaseInfo.get("url");
                        if(url==null || url.equals("")){
                            throw new Exception("【接口："+interfaceName+"的url为空！】");
                        }
                        httpBaseInfoModel.setUrl(url);
                        String method = httpBaseInfo.get("method");
                        if(method==null || method.equals("")){
                            throw new Exception("【接口："+interfaceName+"的请求方法为空！】");
                        }
                        httpBaseInfoModel.setMethod(method);
                        httpBaseInfoModel.setInterfaceId(interfaceId);
                        httpBaseInfoMapper.saveHttpBaseInfo(httpBaseInfoModel);
                    }else{
                        throw new Exception("【接口的基本信息为空】");
                    }
                    //headers信息
                    List<Map<String,String>> headers = ( List<Map<String,String>>)metaData.get("headers");
                    if(headers!=null && !headers.isEmpty()){
                        List<HeadersModel> headersModels = new ArrayList<>(headers.size());
                        for(Map<String,String> header:headers){
                            HeadersModel headersModel = new HeadersModel();
                            headersModel.setInterfaceId(interfaceId);
                            headersModel.setKey(header.get("key"));
                            headersModel.setValue(header.get("value"));
                            headersModels.add(headersModel);
                        }
                        headersMapper.saveHeaderList(headersModels);
                    }
                    //params信息
                    List<Map<String,String>> params = (List<Map<String,String>>)metaData.get("parameters");
                    if(params!=null && !params.isEmpty()){
                        List<ParametersModel> parametersModels = new ArrayList<>(params.size());
                        for(Map<String,String> param:params){
                            ParametersModel parametersModel = new ParametersModel();
                            parametersModel.setInterfaceId(interfaceId);
                            parametersModel.setKey(param.get("key"));
                            parametersModel.setValue(param.get("value"));
                            parametersModel.setValueType(param.get("valueType"));
                            parametersModels.add(parametersModel);
                        }
                        parametersMapper.saveParametersList(parametersModels);
                    }
                    //schedulerCorn信息
                    Map<String,String> schedulerCorn =(Map<String,String>) metaData.get("schedulerCron");
                    if(schedulerCorn!=null && !schedulerCorn.isEmpty()){
                        SchedulerCronModel schedulerCronModel = new SchedulerCronModel();
                        schedulerCronModel.setInterfaceId(interfaceId);
                        String cron = schedulerCorn.get("cron");
                        if(cron==null || cron.equals("")){
                            throw new Exception("【接口："+httpBaseInfoModel.getInterfaceName()+"的cron为空！】");
                        }
                        schedulerCronModel.setCron(cron);
                        String kafkaTopic = schedulerCorn.get("kafkaTopic");
                        if(kafkaTopic==null || kafkaTopic.equals("")){
                            throw new Exception("【接口："+httpBaseInfoModel.getInterfaceName()+"的回写kafka主题名为空！】");
                        }
                        schedulerCronModel.setKafkaTopic(kafkaTopic);
                        String className = schedulerCorn.get("className");
                        if(className==null || className.equals("")){
                            throw new Exception("【接口："+httpBaseInfoModel.getInterfaceName()+"的对应的处理类为空！】");
                        }
                        schedulerCronModel.setClassName(className);
                        schedulerCronModel.setStatus("stopped");

                        schedulerCronMapper.saveScheduleCron(schedulerCronModel);
                    }else{
                        throw new Exception("【接口的调度信息为空】");
                    }
                }
            }else{
                throw new Exception("【接口定时调度的元数据为空！】");
            }
            responseUtil.setCode("200");
            responseUtil.setBody("【接口元数据配置保存成功。】");
        }catch (Exception e){
            responseUtil.setCode("500");
            responseUtil.setBody("【保存接口定时调度元数据信息到mysql数据库时失败:"+e.getMessage()+"】");
        }
        return responseUtil;
    }

    /**
     * @desc 根据接口名称查询接口的所有元数据信息
     * @param interfaceName
     * @return
     * @throws Exception
     */
    public ResponseUtil searchMetaDataByInterfaceName(String interfaceName)throws Exception{

        ResponseUtil responseUtil = new ResponseUtil();
        List<HttpBaseInfoModel> httpBaseInfoModels = httpBaseInfoMapper.searchInterfaceIdByName(interfaceName);
        if(httpBaseInfoModels==null || httpBaseInfoModels.isEmpty()){
            throw new Exception("【根据接口名称"+interfaceName+" 未找到对应的接口元数据信息！】");
        }
        List<Object> metaDataList = new ArrayList<>();//add metaDataMap
        for(HttpBaseInfoModel httpBaseInfoModel:httpBaseInfoModels){
            Map<String,Object> metaDataMap = new HashMap<>();

            //基本信息
            Map<String,String> httpBaseInfoMap = new HashMap<>();
            httpBaseInfoMap.put("interfaceId",httpBaseInfoModel.getInterfaceId());
            httpBaseInfoMap.put("interfaceName",httpBaseInfoModel.getInterfaceName());
            httpBaseInfoMap.put("url",httpBaseInfoModel.getUrl());
            httpBaseInfoMap.put("method",httpBaseInfoModel.getMethod());
            metaDataMap.put("httpBaseInfo",httpBaseInfoMap);

            //接口ID
            String interfaceId = httpBaseInfoModel.getInterfaceId();
            //请求header信息
            List<HeadersModel> headersModels = new ArrayList<>();
            List<Map<String,String>> headersModelList = new ArrayList<>();
            headersModels = headersMapper.searchHeadersByInterfaceId(interfaceId);
            if(headersModels!=null && !headersModels.isEmpty()){
                for(HeadersModel headersModel:headersModels){
                    Map<String,String>map = new HashMap<>();
                    map.put("key",headersModel.getKey());
                    map.put("value",headersModel.getValue());
                    headersModelList.add(map);
                }
                metaDataMap.put("headers",headersModelList);
            }
            //请求参数信息
            List<ParametersModel>parametersModels = parametersMapper.searchParametersByInterfaceId(interfaceId);
            if(parametersModels!=null && !parametersModels.isEmpty()){
                List<Map<String,String>> parametersList = new ArrayList<>();
                for(ParametersModel parametersModel:parametersModels){
                    Map<String,String> map = new HashMap<>();
                    map.put("key",parametersModel.getKey());
                    map.put("value",parametersModel.getValue());
                    map.put("valueType",parametersModel.getValueType());
                    parametersList.add(map);
                }
                metaDataMap.put("parameters",parametersList);
            }

            //接口调度元数据
            SchedulerCronModel schedulerCornModel = schedulerCronMapper.searchSchedulerCronByInterfaceId(interfaceId);
            if(schedulerCornModel!=null){
                Map<String,String> map = new HashMap<>();
                map.put("className",schedulerCornModel.getClassName());
                map.put("cron",schedulerCornModel.getCron());
                map.put("kafkaTopic",schedulerCornModel.getKafkaTopic());
                map.put("status",schedulerCornModel.getStatus());
                metaDataMap.put("schedulerCorn",map);

            }
            metaDataList.add(metaDataMap);


        }
        responseUtil.setCode("200");
        responseUtil.setBody(JSONObject.toJSONString(metaDataList));
        return responseUtil;

    }


    /**
     * @desc 根据接口名称查询对应的接口ID
     * @param interfaceName
     * @return
     * @throws Exception
     */
    public ResponseUtil searchIdsByName(String interfaceName)throws Exception{
        ResponseUtil responseUtil = new ResponseUtil();
        List<HttpBaseInfoModel> httpBaseInfoModels = httpBaseInfoMapper.searchInterfaceIdByName(interfaceName);
        if(httpBaseInfoModels==null || httpBaseInfoModels.isEmpty()){
            throw new Exception("【根据接口名："+interfaceName+"未查到相关接口信息！】");
        }
        Map<String,String> res = new HashMap<>();
        for(HttpBaseInfoModel httpBaseInfoModel:httpBaseInfoModels){
            res.put("接口名称："+httpBaseInfoModel.getInterfaceName(),"接口ID："+httpBaseInfoModel.getInterfaceId());
        }
        responseUtil.setCode("200");
        responseUtil.setBody(JSONObject.toJSONString(res));
        return responseUtil;
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseUtil deleteMetaData(String interfaceIdOrName)throws Exception{
        ResponseUtil responseUtil = new ResponseUtil();
        //首先根据接口ID或者接口名称获取接口ID的列表
        List<String> ids = httpBaseInfoMapper.searchIdsByIdOrName(interfaceIdOrName);
        if(ids!=null && !ids.isEmpty()){
            //首先停止这些接口的调度任务
            dynamicSchedulerTask.stopTaskByInterfaceIds(ids);
            httpBaseInfoMapper.deletebyInterfaceIds(ids);
            //删除header信息
            headersMapper.deleteByInerfaceIds(ids);
            //删除param信息
            parametersMapper.deleteByInterfaceIds(ids);
            //删除调度信息
            schedulerCronMapper.deleteByInterfaceIds(ids);
            //删除token信息
            tokensMapper.deleteByInterfaceIds(ids);

            //删除日志记录
            schedulerJobLogMapper.deleteByInterfaceIds(ids);

            responseUtil.setCode("200");
            responseUtil.setBody("【删除接口："+interfaceIdOrName+"的相关信息成功。】");


        }else{
            throw new Exception("【未查到"+interfaceIdOrName+"接口的信息】");
        }
        return responseUtil;
    }





}
