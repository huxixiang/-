package com.dcits.restapiapp.scheduler.interfaceprocess.impl;


import ch.qos.logback.core.encoder.EchoEncoder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import com.dcits.restapiapp.model.TokensModel;
import com.dcits.restapiapp.scheduler.interfaceprocess.InterfaceCommon;
import com.dcits.restapiapp.scheduler.tokenprocess.impl.BeiDouToken;
import com.dcits.restapiapp.util.ExceptionStack;
import com.sun.corba.se.spi.ior.ObjectKey;

import java.net.URI;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @desc 处理北斗导航接口
 * @author huxx
 * @date 2020-10-15
 */

public class BeiDouInterfaceProcess implements InterfaceCommon {

    protected String interfaceId;
    HttpBaseInfoMapper httpBaseInfoMapper;
    HeadersMapper headersMapper;
    ParametersMapper parametersMapper;
    TokensMapper tokensMapper;


    HttpClientCore httpClientCore;

    public BeiDouInterfaceProcess(){}
    public BeiDouInterfaceProcess(String interfaceId,
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
            String systemId = "beidou_system";
            TokensModel tokensModel = tokensMapper.searchTokenBySystemId(systemId);
            String tokenStr = "";
            String userName = ConfigFileUtil.configProperties.getProperty("beidou.username");
            String password = ConfigFileUtil.configProperties.getProperty("beidou.password");
            password = getMD5Str(password);
            String tokenUrl = httpBaseInfoModel.getUrl();
            URI uri = URI.create(tokenUrl);
            tokenUrl = "http://"+uri.getHost()+":"+ConfigFileUtil.configProperties.getProperty("beidou.token.port")+"/auth";
            Map<String,Object>tokenParams = new HashMap<>();
            tokenParams.put("userName",userName);
            tokenParams.put("password",password);
            if(tokensModel==null){//第一次请求token
                HttpResult token = httpClientCore.doPost(tokenUrl,tokenParams,null);
//                System.out.println("HttpResult:"+JSON.toJSONString(token));
                String res = token.getBody();
                Map<String,Object> resMap = (Map<String,Object>)JSON.parse(res);
//                System.out.println("resMap:"+JSON.toJSONString(resMap));
                if(resMap.get("code").toString().equals("200")){
                    Map<String,Object>dataMap = (Map<String,Object>)resMap.get("data");
                    int expiredTime = Integer.valueOf(dataMap.get("expiredTime").toString());//86400秒，24小时
                    tokenStr = "Bearer_"+dataMap.get("token").toString();
                    Long expireTimestamp = System.currentTimeMillis()+expiredTime*1000;
                    Map<String,Object>parmas = new HashMap<>();
                    parmas.put("system_id",systemId);
                    parmas.put("effective_end_time",new Timestamp(expireTimestamp));
                    parmas.put("interfaceId",interfaceId);
                    parmas.put("token",tokenStr);
                    tokensMapper.insertToken(parmas);

                }else{
                    throw new Exception("【获取北斗系统Token出错】");
                }

            }else{
                Long expireTime = Long.valueOf(tokensModel.getEffectiveEndTime().getTime());
                Long now = System.currentTimeMillis();
                if(now>expireTime-300000){//失效
                    HttpResult token = httpClientCore.doPost(tokenUrl,tokenParams,null);
                    String res = token.getBody();
                    Map<String,Object> resMap = (Map<String,Object>)JSON.parse(res);
                    if(resMap.get("code").toString().equals("200")){
                        Map<String,Object>dataMap = (Map<String,Object>)resMap.get("data");
                        int expiredTime = Integer.valueOf(dataMap.get("expiredTime").toString());//86400秒，24小时
                        tokenStr = "Bearer_"+dataMap.get("token").toString();
                        Long expireTimestamp = System.currentTimeMillis()+expiredTime*1000;
                        Map<String,Object>parmas = new HashMap<>();
                        parmas.put("system_id",systemId);
                        parmas.put("effective_end_time",new Timestamp(expireTimestamp));
                        parmas.put("interfaceId",interfaceId);
                        parmas.put("token",tokenStr);
                        tokensMapper.insertToken(parmas);

                    }else{
                        throw new Exception("【获取北斗系统Token出错】");
                    }
                }
                tokenStr = tokensModel.getToken();
            }
//            System.out.println("tokenStr:"+tokenStr);

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
            if(!tokenStr.equals("")){
                headers.put("authorization",tokenStr);
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
            List<HttpResult> httpResults = new ArrayList<>();
            if("get".equalsIgnoreCase(method)){
                HttpResult httpResult = httpClientCore.doGet(url,parameters,headers);
                httpResults.add(httpResult);
                return httpResults;
            }else if("post".equalsIgnoreCase(method)){
                HttpResult httpResult = httpClientCore.doPost(url,parameters,headers);
                httpResults.add(httpResult);
//                System.out.println("post httpResults:"+JSON.toJSONString(httpResults));
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


   public static String getMD5Str(String password)throws Exception{
       MessageDigest md = MessageDigest.getInstance("MD5");
       md.update(password.getBytes());
       byte b[] = md.digest();
       int i;
       StringBuffer buf = new StringBuffer("");
       for (int offset = 0; offset < b.length; offset++) {
           i = b[offset];
           if (i < 0)
               i += 256;
           if (i < 16)
               buf.append("0");
           buf.append(Integer.toHexString(i));
       }
       String result = buf.toString();
       return result;
   }



    public static void main(String[] args) {
//        String url = "http://localhost:9092/1/2/3.html";
//
//
//
//        URI uri = URI.create(url);
//        System.out.println(uri.getHost());
        List<Map<String,Object>>list = new ArrayList<>();
        Map<String,Object>map = new HashMap<>();
        map.put("lat",0);
        map.put("lng",0);
        list.add(map);
        String jsonStr = JSON.toJSONString(list);


    }






}
