package com.dcits.restapiapp.scheduler.interfaceprocess.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcits.restapiapp.httpcore.HttpClientCore;
import com.dcits.restapiapp.httpcore.HttpResult;
import com.dcits.restapiapp.mapper.HeadersMapper;
import com.dcits.restapiapp.mapper.HttpBaseInfoMapper;
import com.dcits.restapiapp.mapper.ParametersMapper;
import com.dcits.restapiapp.mapper.TokensMapper;
import com.dcits.restapiapp.model.HeadersModel;
import com.dcits.restapiapp.model.HttpBaseInfoModel;
import com.dcits.restapiapp.model.ParametersModel;
import com.dcits.restapiapp.model.TokensModel;
import com.dcits.restapiapp.scheduler.interfaceprocess.InterfaceCommon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @desc 接口处理一般公共类，完成接口请求配置信息组装，接口返回值发送等功能
 * @author huxx
 * @date 2020-02-19
 */

public class BigDataInterfaceProcessCommon implements InterfaceCommon {

    protected String interfaceId;
    HttpBaseInfoMapper httpBaseInfoMapper;
    HeadersMapper headersMapper;
    ParametersMapper parametersMapper;
    TokensMapper tokensMapper;


    HttpClientCore httpClientCore;

    public BigDataInterfaceProcessCommon(){}
    public BigDataInterfaceProcessCommon(String interfaceId,
                                         HttpBaseInfoMapper httpBaseInfoMapper,
                                         HeadersMapper headersMapper,
                                         ParametersMapper parametersMapper,
                                         TokensMapper tokensMapper,
                                         HttpClientCore httpClientCore){
        this.interfaceId = interfaceId;
        this.httpBaseInfoMapper = httpBaseInfoMapper;
        this.headersMapper = headersMapper;
        this.parametersMapper = parametersMapper;
        this.tokensMapper = tokensMapper;
        this.httpClientCore = httpClientCore;
    }
    public List<HttpResult> startHttpRequestJob()throws Exception{
        try{
            HttpBaseInfoModel httpBaseInfoModel = httpBaseInfoMapper.searchHttpBaseInfoByInterfaceId(interfaceId);
            List<HeadersModel> headersModels = headersMapper.searchHeadersByInterfaceId(interfaceId);
            List<ParametersModel> parametersModels = parametersMapper.searchParametersByInterfaceId(interfaceId);
            TokensModel tokensModel = tokensMapper.searchTokenByInterfaceId(interfaceId);
            /**
             * 1、首先判断token是否过期，如果过期需要重新进行请求。
             * 2、如果重新请求获取token需要将最新的token更新到数据库中
             * 3、token有效后组装http请求体
             */
            String url = httpBaseInfoModel.getUrl();
            String method = httpBaseInfoModel.getMethod();
            Map<String,String> headers = null;
            if(headersModels!=null && !headersModels.isEmpty()){
                headers = new HashMap<>();
                for(HeadersModel headersModel:headersModels){
                    headers.put(headersModel.getKey(),headersModel.getValue());
                }
            }
            Map<String,Object> parameters = null;
            if(parametersModels!=null && !parametersModels.isEmpty()){
                parameters = new HashMap<>();
                for(ParametersModel parametersModel:parametersModels){
                    String valueType = parametersModel.getValueType();
                    if("int".equalsIgnoreCase(valueType)){
                        parameters.put(parametersModel.getKey(),Integer.valueOf(parametersModel.getValue()));
                    }else if("timestamp".equalsIgnoreCase(valueType)){
                        parameters.put(parametersModel.getKey(),Long.valueOf(parametersModel.getValue()));
                    }else if("boolean".equalsIgnoreCase(valueType)){
                        parameters.put(parametersModel.getKey(),Boolean.valueOf(parametersModel.getValue()));
                    }else{//默认是字符串类型
                        parameters.put(parametersModel.getKey(),parametersModel.getValue());
                    }
                }
            }
//            System.out.println("url:"+url);
//            System.out.println("method:"+method);
            List<HttpResult> httpResults = new ArrayList<>();
            if("get".equalsIgnoreCase(method)){
                HttpResult httpResult = httpClientCore.doGet(url,parameters,headers);
                httpResults.add(httpResult);
                return httpResults;
            }else if("post".equalsIgnoreCase(method)){
                HttpResult httpResult = httpClientCore.doPost(url,parameters,headers);
                String json = httpResult.getBody();
//                System.out.println("json:"+json);
                Map<String,Object>objectMap = (Map<String,Object>)JSON.parse(json);
                int code = Integer.valueOf(objectMap.get("code").toString());
                String Message = objectMap.get("Message").toString();
                Map<String,Object> result = new HashMap<>();
                result.put("code",code);
                result.put("Message",Message);
                List<Map<String,Object>> records = new ArrayList<>();
                result.put("tdTowerList",records);
                List<Map<String,Object>>tdTowerList = (List<Map<String,Object>>)JSON.parse(objectMap.get("tdTowerList").toString());
                for(Map<String,Object> map:tdTowerList){
                    if(map.get("insArea")!=null && map.get("insArea").equals("翠亨新区")){
                        records.add(map);
                    }
                }
                httpResult.setBody(JSON.toJSONString(result));
                httpResults.add(httpResult);
//                System.out.println("res:"+JSON.toJSONString(httpResults));
                return httpResults;
            }else if("put".equalsIgnoreCase(method)){
                String paramsJson = null;
                if(parameters!=null){
                    paramsJson = JSONObject.toJSONString(parameters);
                }
                HttpResult httpResult = httpClientCore.doPut(url,paramsJson ,headers);
                httpResults.add(httpResult);
                return httpResults;
            }else{
                System.out.println("未来实现");
            }


        }catch (Exception e){
            //调度失败
            e.printStackTrace();
        }
        return null;

    }








}
