package com.dcits.restapiapp.scheduler.interfaceprocess.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcits.restapiapp.config.ConfigFileUtil;
import com.dcits.restapiapp.httpcore.HttpClientCore;
import com.dcits.restapiapp.httpcore.HttpResult;
import com.dcits.restapiapp.mapper.HeadersMapper;
import com.dcits.restapiapp.mapper.HttpBaseInfoMapper;
import com.dcits.restapiapp.mapper.ParametersMapper;
import com.dcits.restapiapp.mapper.TokensMapper;
import com.dcits.restapiapp.model.HeadersModel;
import com.dcits.restapiapp.model.HttpBaseInfoModel;
import com.dcits.restapiapp.model.ParametersModel;
import com.dcits.restapiapp.scheduler.interfaceprocess.InterfaceCommon;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @desc 处理水务抄表系统接口
 * @author huxx
 * @date 2020-11-03
 */

public class WaterMeterReadingSystemDevInfoInterfaceProcess implements InterfaceCommon {


    protected String interfaceId;
    HttpBaseInfoMapper httpBaseInfoMapper;
    HeadersMapper headersMapper;
    ParametersMapper parametersMapper;
    TokensMapper tokensMapper;


    HttpClientCore httpClientCore;

    public WaterMeterReadingSystemDevInfoInterfaceProcess(){}
    public WaterMeterReadingSystemDevInfoInterfaceProcess(String interfaceId,
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
//            String systemId = "watermeterreading_system";
            String appid = ConfigFileUtil.configProperties.getProperty("watermeterreadingsystem.appid");

            String tokenUrl = httpBaseInfoModel.getUrl();
            URI uri = URI.create(tokenUrl);
            tokenUrl = "http://"+uri.getHost()+":"+ConfigFileUtil.configProperties.getProperty("watermeterreadingsystem.token.port")+"/gdpay/api/netplus/getToken?appid="+appid;
            HttpResult token = httpClientCore.doGet(tokenUrl);
            String res = token.getBody();
            Map<String,Object> resMap = (Map<String,Object>)JSON.parse(res);
            String tokenStr = "";
            if(resMap.get("code").toString().equals("200")){
                Map<String,Object>dataMap = (Map<String,Object>)JSON.parse(resMap.get("data").toString());
                tokenStr = dataMap.get("token").toString();
            }else{
                throw new Exception("【获取水务抄表系统Token出错】");
            }
            if(tokenStr.equals("")){
                throw new Exception("【获取水务抄表系统Token为空】");
            }



            /**
             * 1、首先判断token是否过期，如果过期需要重新进行请求。
             * 2、如果重新请求获取token需要将最新的token更新到数据库中
             * 3、token有效后组装http请求体
             */
            String url = httpBaseInfoModel.getUrl();
            String method = httpBaseInfoModel.getMethod();
            Map<String,String> headers = new HashMap<>();
            if(headersModels!=null && !headersModels.isEmpty()){
                for(HeadersModel headersModel:headersModels){
                    headers.put(headersModel.getKey(),headersModel.getValue());
                }
            }
            headers.put("token",tokenStr);

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
            List<HttpResult> httpResults = new ArrayList<>();
            if("get".equalsIgnoreCase(method)){
                HttpResult httpResult = httpClientCore.doGet(url,parameters,headers);
                httpResults.add(httpResult);
                return httpResults;
            }else if("post".equalsIgnoreCase(method)){
                HttpResult httpResult = httpClientCore.doPost(url,parameters,headers);
                httpResults.add(httpResult);
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





    public static void main(String[] args) {
//        String url = "http://localhost:9092/1/2/3.html";
//
//
//
//        URI uri = URI.create(url);
//        System.out.println(uri.getHost());
//        List<Map<String,Object>>list = new ArrayList<>();
//        Map<String,Object>map = new HashMap<>();
//        map.put("lat",0);
//        map.put("lng",0);
//        list.add(map);
//        String jsonStr = JSON.toJSONString(list);
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
//        System.out.println(sdf.format(calendar.getTime()));

        String json =  "{\"code\":200,\"msg\":\"success \",\"data\":\"{\\\"token\\\":\\\"vtiAeddTDA6aF2We6A9F\\\"}\"}";
        Map<String,Object> resMap = (Map<String,Object>)JSON.parse(json);
        Map<String,Object>dataMap = (Map<String,Object>)JSON.parse(resMap.get("data").toString());
        System.out.println(dataMap.get("token"));



    }






}
