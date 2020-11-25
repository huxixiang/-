package com.dcits.restapiapp.scheduler.interfaceprocess;

import com.dcits.restapiapp.httpcore.HttpResult;

import java.util.List;

public interface InterfaceCommon {

//    public HttpResult startHttpRequestJob();
    public List<HttpResult> startHttpRequestJob()throws Exception;
}
