package com.dcits.restapiapp.controller;


import com.dcits.restapiapp.common.response.ResponseUtil;
import com.dcits.restapiapp.service.MetaDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @desc 接口请求元数据控制类
 * @author huxx
 * @date 2020-03-10
 */
@RestController
@RequestMapping("/metadata/")
public class MetaDataController {

    @Autowired
    MetaDataService metaDataService;//接口调度元数据服务类

    @PostMapping("savemetadata")
    @ApiOperation("将传入的接口调度元数据信息保存到mysql数据库中"+":{\"metaDatas\":[{\"headers\":[{\"key\":\"headerKey_1\",\"value\":\"headerVal_1\"},{\"key\":\"headerKey_2\",\"value\":\"headerVal_2\"}],\"schedulerCron\":{\"className\":\"InterfaceProcessCommon\",\"cron\":\"0/5 * * * * ?\",\"kafkaTopic\":\"API_REQUEST_DEMO_TOPIC\"},\"httpBaseInfo\":{\"interfaceName\":\"胡西祥测试接口\",\"method\":\"GET\",\"url\":\"https://www.baidu.com\"},\"parameters\":[{\"key\":\"paramKey_1\",\"value\":\"paramVal_1\",\"valueType\":\"string\"},{\"key\":\"paramKey_2\",\"value\":\"paramVal_2\",\"valueType\":\"string\"}]}]}")
    public ResponseUtil addMetaData(@RequestParam(value = "metaData",required = true)String metaData){
        ResponseUtil responseUtil = new ResponseUtil();
        try{
            responseUtil = metaDataService.saveMetaDateToDb(metaData);
        }catch (Exception e){
            responseUtil.setCode("500");
            responseUtil.setBody(e.getMessage());
        }
        return responseUtil;
    }

    @PostMapping("searchmetadatabyinterfacename")
    @ApiOperation("根据接口名称查询接口的元数据信息")
    public ResponseUtil searchMetaDataByInterfaceName(@RequestParam(value = "interfaceName",required = false)String interfaceName ){
        ResponseUtil responseUtil = new ResponseUtil();
        try{
            responseUtil = metaDataService.searchMetaDataByInterfaceName(interfaceName);

        }catch (Exception e){
            responseUtil.setCode("500");
            responseUtil.setBody("【根据接口名称查询接口信息时出错："+e.getMessage()+"】");

        }
        return responseUtil;
    }


    @PostMapping("searchinterfaceidsbyname")
    @ApiOperation("根据接口名称获取接口的ID")
    public ResponseUtil searchIdsByName(@RequestParam(value = "interfaceName",required = false)String interfaceName){
        ResponseUtil responseUtil = new ResponseUtil();
        try{
            responseUtil = metaDataService.searchIdsByName(interfaceName);
        }catch (Exception e){
            responseUtil.setCode("500");
            responseUtil.setBody("【查询接口ID信息时出错："+e.getMessage()+"】");

        }
        return responseUtil;
    }

    @PostMapping("deletemetadata")
    @ApiOperation("根据接口id和名称删除接口相关元数据")
    public ResponseUtil delete(@RequestParam(value = "interfaceIdOrName",required = true)String interfaceIdOrName){
        ResponseUtil responseUtil = new ResponseUtil();
        try{
            responseUtil = metaDataService.deleteMetaData(interfaceIdOrName);
        }catch (Exception e){
            responseUtil.setCode("500");
            responseUtil.setBody("【删除接口失败："+e.getMessage()+"】");
        }
        return responseUtil;
    }

}
