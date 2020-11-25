package com.dcits.restapiapp.common.response;

public class DataAccessResponse {
    private String RTNCODE;
    private String RTNMESSAGE;

    public String getRTNCODE() {
        return RTNCODE;
    }

    public void setRTNCODE(String RTNCODE) {
        this.RTNCODE = RTNCODE;
    }

    public String getRTNMESSAGE() {
        return RTNMESSAGE;
    }

    public void setRTNMESSAGE(String RTNMESSAGE) {
        this.RTNMESSAGE = RTNMESSAGE;
    }
}
