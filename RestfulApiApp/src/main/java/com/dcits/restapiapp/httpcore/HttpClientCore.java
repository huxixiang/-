package com.dcits.restapiapp.httpcore;

import com.alibaba.fastjson.JSON;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理http各种请求
 */
@Service
public class HttpClientCore {
    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private RequestConfig config;

//    private static RequestConfig requestConfig;
//
//    static{
//        synchronized (HttpClientCore.class){
//            if(requestConfig==null){
//                requestConfig = RequestConfig.custom()
//                        .setConnectTimeout(120000)
//                        .setSocketTimeout(120000)
//                        .setConnectionRequestTimeout(70000)
//                        .build();
//            }
//        }
//    }



    /**
     * 不带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
     * @param url
     * @return
     * @throws Exception
     */
    public HttpResult doGet(String url) throws Exception {
        // 声明 http get 请求
        HttpGet httpGet = new HttpGet(url);
        // 装载配置信息
//        httpGet.setConfig(config);
        httpGet.setConfig(config);
        // 发起请求
        CloseableHttpResponse response = this.httpClient.execute(httpGet);

//        // 判断状态码是否为200
//        if (response.getStatusLine().getStatusCode() == 200) {
//            // 返回响应体的内容
//            String responseStr = EntityUtils.toString(response.getEntity(), "UTF-8");
//            HttpResult httpResult = new HttpResult();
//            httpResult.setCode(200);
//            httpResult.setBody(responseStr);
//            return httpResult;
//        }
        // 返回响应体的内容
        String responseStr = EntityUtils.toString(response.getEntity(), "UTF-8");
        HttpResult httpResult = new HttpResult();
        httpResult.setCode(response.getStatusLine().getStatusCode());
        httpResult.setBody(responseStr);
        return httpResult;

    }



    /**
     * 不带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
     * @param url
     * @return
     * @throws Exception
     */
    public HttpResult doGet(String url,Map<String,String> header) throws Exception {
        // 声明 http get 请求
        HttpGet httpGet = new HttpGet(url);
        //设置 http头信息
        if(header!=null){
            for(Map.Entry<String,String> entry:header.entrySet()){
                httpGet.setHeader(entry.getKey(),entry.getValue());
            }
        }
        // 装载配置信息
//        httpGet.setConfig(config);
        httpGet.setConfig(config);
        // 发起请求
        CloseableHttpResponse response = this.httpClient.execute(httpGet);
//        // 判断状态码是否为200
//        if (response.getStatusLine().getStatusCode() == 200) {
//            // 返回响应体的内容
//            return EntityUtils.toString(response.getEntity(), "UTF-8");
//        }
        // 返回响应体的内容
        String responseStr = EntityUtils.toString(response.getEntity(), "UTF-8");
        HttpResult httpResult = new HttpResult();
        httpResult.setCode(response.getStatusLine().getStatusCode());
        httpResult.setBody(responseStr);
        return httpResult;
    }

    /**
     * 带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
     *
     * @param url
     * @return
     * @throws Exception
     */
    public HttpResult doGet(String url, Map<String, Object> param,Map<String,String> header) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(url);

        if (param != null) {
            // 遍历map,拼接请求参数
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
            }
        }
        // 调用不带参数的get请求
        return this.doGet(uriBuilder.build().toString(),header);

    }

    /**
     * 带参数的post请求
     *
     * @param url
     * @param param
     * @param header
     * @return
     * @throws Exception
     */
    public HttpResult doPost(String url, Map<String, Object> param,Map<String,String> header) throws Exception {
        // 声明httpPost请求
        HttpPost httpPost = new HttpPost(url);

        //设置http post请求头部
        if(header!=null){
            for(Map.Entry<String,String> entry:header.entrySet()){
                httpPost.setHeader(entry.getKey(),entry.getValue());
            }
        }
        // 加入配置信息
//        httpPost.setConfig(config);
        httpPost.setConfig(config);


        //判断参数是否是json字符串
        if(header!=null && header.containsKey("Content-Type") && header.get("Content-Type").equals("application/json")){
            if(param!=null && !param.isEmpty()){
                StringEntity json = new StringEntity(JSON.toJSONString(param),"utf-8");
                httpPost.setEntity(json);
            }
        }else{
            // 判断map是否为空，不为空则进行遍历，封装from表单对象
            if (param != null) {
                List<NameValuePair> list = new ArrayList<NameValuePair>();
                for (Map.Entry<String, Object> entry : param.entrySet()) {
                    list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }
                // 构造from表单对象
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "UTF-8");

                // 把表单放到post里
                httpPost.setEntity(urlEncodedFormEntity);
            }
        }


        // 发起请求
        CloseableHttpResponse response = this.httpClient.execute(httpPost);
        return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
                response.getEntity(), "UTF-8"));
    }

    /**
     * 不带参数post请求
     *
     * @param url
     * @return
     * @throws Exception
     */
    public HttpResult doPost(String url) throws Exception {
        return this.doPost(url, null,null);
    }


    /**
     * put请求
     * @param url
     * @param paramJson
     * @param header
     * @return
     * @throws Exception
     */
    public HttpResult doPut(String url,String paramJson,Map<String,String>header)throws Exception{
        HttpPut httpPut = new HttpPut(url);
        if(header!=null){
            for(Map.Entry<String,String> entry:header.entrySet()){
                httpPut.setHeader(entry.getKey(),entry.getValue());
            }
        }
        StringEntity stringEntity = new StringEntity(paramJson, "UTF-8");
        httpPut.setEntity(stringEntity);
        CloseableHttpResponse response = this.httpClient.execute(httpPut);
        return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
                response.getEntity(), "UTF-8"));

    }

    public static void main(String[] args)throws Exception {
        String url = "http://10.21.112.35:8180/gdpay/api/netplus/getDevEventInfo";

        HttpPost httpPost = new HttpPost(url);
        Map<String,String> header = new HashMap<>();
        header.put("Content-Type","application/json");
        header.put("token","2wJV6RAVEGUYr8aKyDB1");

        //设置http post请求头部
        if(header!=null){
            for(Map.Entry<String,String> entry:header.entrySet()){
                httpPost.setHeader(entry.getKey(),entry.getValue());
            }
        }
        // 加入配置信息
//        RequestConfig config = new RequestConfig();
//        httpPost.setConfig(config);

        Map<String, Object> param = new HashMap<>();
        param.put("appid","5cf41869-43c2-4e5d-9262-150765375ade");



        // 判断map是否为空，不为空则进行遍历，封装from表单对象
        if (param != null) {
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
            // 构造from表单对象
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "UTF-8");

            // 把表单放到post里
            httpPost.setEntity(urlEncodedFormEntity);
        }

        // 发起请求
        System.out.println("Content-Type:"+JSON.toJSONString(httpPost.getHeaders("Content-Type")));
        System.out.println("uri:"+httpPost.getURI());
        System.out.println("entry:"+JSON.toJSONString(httpPost.getEntity()));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response =httpClient.execute(httpPost);
        HttpResult httpResult =  new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
                response.getEntity(), "UTF-8"));
        System.out.println(JSON.toJSONString(httpResult));

    }









}
