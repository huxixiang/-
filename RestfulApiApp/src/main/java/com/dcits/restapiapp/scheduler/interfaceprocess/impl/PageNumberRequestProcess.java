package com.dcits.restapiapp.scheduler.interfaceprocess.impl;

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
import com.dcits.restapiapp.scheduler.interfaceprocess.util.JsonPrase;
import com.dcits.restapiapp.scheduler.interfaceprocess.util.TypeTransform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @desc 执行分页请求的公共类，按照页码和每页size两个参数进行分页请求
 * @date 2020-05-06
 * @author huxx
 */
public class PageNumberRequestProcess implements InterfaceCommon {
    protected String interfaceId;
    HttpBaseInfoMapper httpBaseInfoMapper;
    HeadersMapper headersMapper;
    ParametersMapper parametersMapper;
    TokensMapper tokensMapper;


    HttpClientCore httpClientCore;

    public PageNumberRequestProcess(){}
    public PageNumberRequestProcess(String interfaceId,
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

        /**
         * 处理分页，首先拿到5个分页请求参数：
         * pageNumberName
         * pageNumber
         * pageSizeName
         * pageSize
         * totalName
         */
        String pageNumberName = null; //起始页码字段名称
        int pageNumber = 1;//起始页码
        String pageSizeName = null;//每页记录数名称
        int pageSize = 1;//每页记录数
        String totalName = null;//全部记录数字段名称
        Map<String,Object> parameters = new HashMap<>();
        if(parametersModels!=null && !parametersModels.isEmpty()){
            for(ParametersModel parametersModel:parametersModels){
                String key = parametersModel.getKey();
                if("pageNumberName".equals(key)){
                    pageNumberName = parametersModel.getValue();
                    continue;
                }else if("pageNumber".equals(key)){
                    pageNumber = Integer.valueOf(parametersModel.getValue());
                    continue;
                }else if("pageSizeName".equals(key)){
                    pageSizeName = parametersModel.getValue();
                    continue;
                }else if("pageSize".equals(key)){
                    pageSize = Integer.valueOf(parametersModel.getValue());
                    continue;
                }else if("totalName".equals(key)){
                    totalName = parametersModel.getValue();
                    continue;
                }
                Object value = TypeTransform.typeTransform(parametersModel.getValue(),parametersModel.getValueType());
                if(value!=null){//过滤掉空值字段
                    parameters.put(key,value);
                }

            }
        }
        //将起始页和每页大小放入到参数map中
        parameters.put(pageNumberName,pageNumber);
        parameters.put(pageSizeName,pageSize);


        //根据分页请求参数
        if("get".equalsIgnoreCase(method)){
            List<HttpResult> httpResults = new ArrayList<>();
            HttpResult firstResult =  httpClientCore.doGet(url,parameters,headers);
            httpResults.add(firstResult);

            String body = firstResult.getBody();
            List<Map<String,Object>>jsonPrase = JsonPrase.praseJsonStr(body);
            int total = (int)jsonPrase.get(0).get(totalName);//全部记录数
            while(pageNumber*pageSize<total){
                pageNumber++;
                parameters.put(pageNumberName,pageNumber);
                HttpResult tmpResult = httpClientCore.doPost(url,parameters,headers);
                httpResults.add(tmpResult);
            }
            return httpResults ;
        }else if("post".equalsIgnoreCase(method)){
            List<HttpResult> httpResults = new ArrayList<>();
            HttpResult firstResult = httpClientCore.doPost(url,parameters,headers);
            httpResults.add(firstResult);

            String body = firstResult.getBody();
            List<Map<String,Object>>jsonPrase = JsonPrase.praseJsonStr(body);
            int total = Integer.valueOf(jsonPrase.get(0).get(totalName).toString());//全部记录数
            while(pageNumber*pageSize<total){
                pageNumber++;
                parameters.put(pageNumberName,pageNumber);
                HttpResult tmpResult = httpClientCore.doPost(url,parameters,headers);
                httpResults.add(tmpResult);
            }
            return httpResults;

        }else{
            System.out.println("未来实现");
        }

        return null;


    }


}
