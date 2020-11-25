package com.dcits.restapiapp.model;

import java.util.Map;

/**
 * @desc 接口请求信息
 * @author huxx
 * @date 2020-02-18
 */
public class InterfaceRequestBody {
    private String interfaceId;
    private String url;
    private String method;
    private Map<String,String> header;
    private Map<String,Object> params;

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
