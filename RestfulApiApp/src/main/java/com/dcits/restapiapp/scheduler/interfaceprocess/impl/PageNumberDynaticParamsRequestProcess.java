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

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @desc 执行分页请求的公共类，按照页码和每页size两个参数进行分页请求
 * @date 2020-05-06
 * @author huxx
 */
public class PageNumberDynaticParamsRequestProcess implements InterfaceCommon {

    public static void main(String[] args) throws Exception{
        String json = "{\"status\":\"success\",\"code\":200,\"data\":{\"list\":[{\"sn\":\"mdiot_Y4419000000103\",\"recordTime\":\"2020-09-14 10:47:03\",\"condTxt\":\"\",\"humidity\":45.3,\"rainfall\":0,\"temperature\":41,\"windDir\":\"0.6\",\"windSpeed\":\"0\",\"windLevel\":\"0\",\"pm\":10,\"pm10\":12.9,\"pm100\":0,\"tsp\":0,\"noise\":47.9},{\"sn\":\"mdiot_Y4419000000103\",\"recordTime\":\"2020-09-14 10:48:04\",\"condTxt\":\"\",\"humidity\":44.3,\"rainfall\":0,\"temperature\":41,\"windDir\":\"0.6\",\"windSpeed\":\"0\",\"windLevel\":\"0\",\"pm\":9.9,\"pm10\":13.2,\"pm100\":0,\"tsp\":0,\"noise\":47.9},{\"sn\":\"mdiot_Y4419000000103\",\"recordTime\":\"2020-09-14 10:49:03\",\"condTxt\":\"\",\"humidity\":44,\"rainfall\":0,\"temperature\":41,\"windDir\":\"0.7\",\"windSpeed\":\"0\",\"windLevel\":\"0\",\"pm\":10.3,\"pm10\":14.9,\"pm100\":0,\"tsp\":0,\"noise\":45.8},{\"sn\":\"mdiot_Y4419000000103\",\"recordTime\":\"2020-09-14 10:50:03\",\"condTxt\":\"\",\"humidity\":44.1,\"rainfall\":0,\"temperature\":41,\"windDir\":\"0.6\",\"windSpeed\":\"0\",\"windLevel\":\"0\",\"pm\":9.8,\"pm10\":12.2,\"pm100\":0,\"tsp\":0,\"noise\":47.1},{\"sn\":\"mdiot_Y4419000000103\",\"recordTime\":\"2020-09-14 10:51:04\",\"condTxt\":\"\",\"humidity\":44.7,\"rainfall\":0,\"temperature\":41,\"windDir\":\"0.6\",\"windSpeed\":\"0\",\"windLevel\":\"0\",\"pm\":10,\"pm10\":12.7,\"pm100\":0,\"tsp\":0,\"noise\":52.7},{\"sn\":\"mdiot_Y4419000000103\",\"recordTime\":\"2020-09-14 10:52:09\",\"condTxt\":\"\",\"humidity\":44.5,\"rainfall\":0,\"temperature\":41,\"windDir\":\"0.6\",\"windSpeed\":\"0\",\"windLevel\":\"0\",\"pm\":9.6,\"pm10\":13.1,\"pm100\":0,\"tsp\":0,\"noise\":54.3},{\"sn\":\"mdiot_Y4419000000103\",\"recordTime\":\"2020-09-14 10:53:04\",\"condTxt\":\"\",\"humidity\":44.3,\"rainfall\":0,\"temperature\":41,\"windDir\":\"0.6\",\"windSpeed\":\"0\",\"windLevel\":\"0\",\"pm\":9.9,\"pm10\":14.2,\"pm100\":0,\"tsp\":0,\"noise\":52.9},{\"sn\":\"mdiot_Y4419000000103\",\"recordTime\":\"2020-09-14 10:54:04\",\"condTxt\":\"\",\"humidity\":44.9,\"rainfall\":0,\"temperature\":41,\"windDir\":\"0.6\",\"windSpeed\":\"0\",\"windLevel\":\"0\",\"pm\":9.7,\"pm10\":12.1,\"pm100\":0,\"tsp\":0,\"noise\":53.1},{\"sn\":\"mdiot_Y4419000000103\",\"recordTime\":\"2020-09-14 10:55:10\",\"condTxt\":\"\",\"humidity\":45.9,\"rainfall\":0,\"temperature\":40,\"windDir\":\"0.6\",\"windSpeed\":\"0\",\"windLevel\":\"0\",\"pm\":9.9,\"pm10\":12.2,\"pm100\":0,\"tsp\":0,\"noise\":55.6},{\"sn\":\"mdiot_Y4419000000103\",\"recordTime\":\"2020-09-14 10:56:04\",\"condTxt\":\"\",\"humidity\":46.7,\"rainfall\":0,\"temperature\":40,\"windDir\":\"0.6\",\"windSpeed\":\"0\",\"windLevel\":\"0\",\"pm\":10,\"pm10\":13.5,\"pm100\":0,\"tsp\":0,\"noise\":53.8}],\"page\":{\"total\":180,\"currentPage\":1,\"pageSize\":10}},\"msg\":\"ok\"}";
        List<Map<String,Object>> jsonPrase = JsonPrase.praseJsonStr(json);
        String total = jsonPrase.get(0).get("data.page.total").toString();
        System.out.println(total);
    }

    protected String interfaceId;
    HttpBaseInfoMapper httpBaseInfoMapper;
    HeadersMapper headersMapper;
    ParametersMapper parametersMapper;
    TokensMapper tokensMapper;


    HttpClientCore httpClientCore;

    public PageNumberDynaticParamsRequestProcess(){}
    public PageNumberDynaticParamsRequestProcess(String interfaceId,
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
        String totalName = "";//全部记录数字段名称
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";//当前时间
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
                }else if("timestamp".equals(key)){
                    timestamp = parametersModel.getValue();
                    if(timestamp.trim().equals("")){
                        timestamp = simpleDateFormat.format(new Date());
                    }
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

        if(!timestamp.equals("")){
            parameters.put("timestamp",timestamp);
        }


        //根据分页请求参数
        if("get".equalsIgnoreCase(method)){
            List<HttpResult> httpResults = new ArrayList<>();
            HttpResult firstResult =  httpClientCore.doGet(url,parameters,headers);
            httpResults.add(firstResult);

            String body = firstResult.getBody();
            List<Map<String,Object>>jsonPrase = JsonPrase.praseJsonStr(body);

            int total = Integer.valueOf(jsonPrase.get(0).get(totalName).toString());//全部记录数
            while(pageNumber*pageSize<total){
                pageNumber++;
                parameters.put(pageNumberName,pageNumber);
                HttpResult tmpResult = httpClientCore.doGet(url,parameters,headers);
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
