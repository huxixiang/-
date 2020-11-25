package com.dcits.restapiapp.scheduler.interfaceprocess.impl;

import com.dcits.restapiapp.httpcore.HttpClientCore;
import com.dcits.restapiapp.httpcore.HttpResult;
import com.dcits.restapiapp.mapper.HeadersMapper;
import com.dcits.restapiapp.mapper.HttpBaseInfoMapper;
import com.dcits.restapiapp.mapper.ParametersMapper;
import com.dcits.restapiapp.mapper.TokensMapper;
import com.dcits.restapiapp.model.HttpBaseInfoModel;

import java.util.List;

public class InterfacePro extends InterfaceProcessCommon {

    public InterfacePro(String interfaceId,
                        HttpBaseInfoMapper httpBaseInfoMapper,
                        HeadersMapper headersMapper,
                        ParametersMapper parametersMapper,
                        TokensMapper tokensMapper,
                        HttpClientCore httpClientCore){
        super(interfaceId,httpBaseInfoMapper,headersMapper,parametersMapper,tokensMapper,httpClientCore);

    }


    @Override
    public List<HttpResult> startHttpRequestJob(){
        try{
            HttpBaseInfoModel httpBaseInfoModel = super.httpBaseInfoMapper.searchHttpBaseInfoByInterfaceId(super.interfaceId);
            System.out.println("自定义请求逻辑"+httpBaseInfoModel.getUrl());
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
