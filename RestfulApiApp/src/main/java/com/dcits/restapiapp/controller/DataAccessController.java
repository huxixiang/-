package com.dcits.restapiapp.controller;

import com.dcits.restapiapp.common.response.DataAccessResponse;
import com.dcits.restapiapp.common.response.ResponseUtil;
import com.dcits.restapiapp.service.DataAccessService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * @desc 数据接入 post请求 Content-Type=application/json
 * @author huxx
 * @date 2020-03-24
 */
@RestController
@RequestMapping("/rest/service")
public class DataAccessController {

//    private static final String DevInfo = "DevInfo";
//    private static final String DevDetailData = "DevDetailData";
//    private static final String DevEventInfo = "DevEventInfo";
//    private static final String DevAlarmInfo = "DevAlarmInfo";
//    private static final String DevfaultInfo = "DevfaultInfo";

    @Autowired
    DataAccessService dataAccessService;

//    @PostMapping("/devCollection_sendDevInfo")
//    public DataAccessResponse devInfoDataAccess(HttpServletRequest request){
//        return commonRequest(request,DevInfo);
//    }
//
//
//    @PostMapping("/devCollection_sendDevDetailData")
//    public DataAccessResponse devDetailDataDataAccess(HttpServletRequest request){
//        return commonRequest(request,DevDetailData);
//    }
//
//
//
//    @PostMapping("/devCollection_sendDevEventInfo")
//    public DataAccessResponse devEventInfoDataDataAccess(HttpServletRequest request){
//        return commonRequest(request,DevEventInfo);
//    }
//
//    @PostMapping("/devCollection_sendDevAlarmInfo")
//    public DataAccessResponse devAlarmInfoDataDataAccess(HttpServletRequest request){
//        return commonRequest(request,DevAlarmInfo);
//    }
//
//    @PostMapping("/devCollection_sendDevfaultInfo")
//    public DataAccessResponse devfaultInfoDataDataAccess(HttpServletRequest request){
//        return commonRequest(request,DevfaultInfo);
//    }



    /**
     * @desc 处理请求的公共方法
     * @param request
     * @param bussinessType
     * @return
     */
    public DataAccessResponse commonRequest(HttpServletRequest request,String bussinessType){
        DataAccessResponse dataAccessResponse = new DataAccessResponse();
        try{
            String systemId = request.getHeader("SOUCESYSTEM");

            BufferedReader streamReader = new BufferedReader( new InputStreamReader(request.getInputStream(), "UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null){
                stringBuilder.append(inputStr);
            }
//            System.out.println("值："+stringBuilder.toString());
            dataAccessService.dataAccessService(systemId,bussinessType,stringBuilder.toString());
            dataAccessResponse.setRTNCODE("2000");
            dataAccessResponse.setRTNMESSAGE("数据接收成功");
        }catch (Exception e){
            dataAccessResponse.setRTNCODE("1000");
            dataAccessResponse.setRTNMESSAGE("【数据接入错误："+e.getMessage()+"】");

        }
        return dataAccessResponse;

    }


    /**
     *
     * @param request
     * @return
     */
    @PostMapping("/devCollection")
    @ApiOperation("统一的对外接口服务，在header中添加两个参数：SOUCESYSTEM，SERVICE_NAME")
    public DataAccessResponse devCollection(HttpServletRequest request) {
        return commonRequest(request);

    }



    /**
     * @desc 处理请求的公共方法
     * @param request
     * @return
     */
    public DataAccessResponse commonRequest(HttpServletRequest request){
        DataAccessResponse dataAccessResponse = new DataAccessResponse();
        try{
            String systemId = request.getHeader("SOUCESYSTEM");
            String bussinessType = request.getHeader("SERVICE_NAME");

            BufferedReader streamReader = new BufferedReader( new InputStreamReader(request.getInputStream(), "UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null){
                stringBuilder.append(inputStr);
            }
//            System.out.println("值："+stringBuilder.toString());
            dataAccessService.dataAccessService(systemId,bussinessType,stringBuilder.toString());
            dataAccessResponse.setRTNCODE("2000");
            dataAccessResponse.setRTNMESSAGE("数据接收成功");
        }catch (Exception e){
            dataAccessResponse.setRTNCODE("1000");
            dataAccessResponse.setRTNMESSAGE("【数据接入错误："+e.getMessage()+"】");

        }
        return dataAccessResponse;

    }




}
