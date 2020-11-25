package com.dcits.restapiapp.model;

import java.sql.Timestamp;

public class TokensModel {
    private String interfaceId;
    private String token;
    private Timestamp effectiveEndTime;
    private String systemId;

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getEffectiveEndTime() {
        return effectiveEndTime;
    }

    public void setEffectiveEndTime(Timestamp effectiveEndTime) {
        this.effectiveEndTime = effectiveEndTime;
    }
}
