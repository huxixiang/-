package com.dcits.restapiapp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcits.restapiapp.common.response.ResponseUtil;
import com.dcits.restapiapp.mapper.PeopleMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/demo/")
public class DemoController {

    @Autowired
    PeopleMapper peopleMapper;

    @GetMapping("/getrequest001")
    @ApiOperation("get请求测试接口服务")
    public ResponseUtil GetTest(@RequestParam(value = "paramKey")String paramKey){
        ResponseUtil responseUtil = new ResponseUtil();
        if(paramKey==null || paramKey.trim().equals("")){
            responseUtil.setCode("404");
            responseUtil.setBody("测试成功，只是参数为空");

        }else{
            Map<String,String>res = new HashMap<>();
            if(paramKey.equals("paramValue")){
                responseUtil.setCode("200");
                res.put("name","胡西祥");
            }else{
                responseUtil.setCode("300");
                res.put("name","未找到");
            }
            responseUtil.setBody(JSONObject.toJSONString(res));
        }
        return responseUtil;


    }

    @GetMapping("/getrequest002")
    @ApiOperation("get请求测试接口服务")
    public ResponseUtil GetTest002(@PathVariable(value = "paramKey")String paramKey){
        ResponseUtil responseUtil = new ResponseUtil();
        if(paramKey==null || paramKey.trim().equals("")){
            responseUtil.setCode("404");
            responseUtil.setBody("测试成功，只是参数为空");

        }else{
            Map<String,String>res = new HashMap<>();
            if(paramKey.equals("paramValue")){
                responseUtil.setCode("200");
                res.put("name","胡西祥");
            }else{
                responseUtil.setCode("300");
                res.put("name","未找到");
            }
            responseUtil.setBody(JSONObject.toJSONString(res));
        }
        return responseUtil;


    }

    @PostMapping("/hu")
    public void Testxx(HttpServletRequest httpServletRequest){
        System.out.println(httpServletRequest.getQueryString());
        System.out.println(httpServletRequest.getParameter("key1"));
    }


    @PostMapping("page_request")
    public String pageRequest(@RequestParam(value = "pageNumber",required = true)int pageNumber,
                              @RequestParam(value="pageSize",required = true)int pageSize){

        try{
            PageHelper.startPage(pageNumber,pageSize);
            PageInfo<Map> pageInfo=new PageInfo(peopleMapper.search());
            return JSON.toJSONString(pageInfo);
        }catch (Exception e){
            e.printStackTrace();
            return null;

        }

    }


}
