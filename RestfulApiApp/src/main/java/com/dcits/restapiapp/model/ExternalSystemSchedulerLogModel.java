package com.dcits.restapiapp.model;

import java.sql.Timestamp;

public class ExternalSystemSchedulerLogModel {
    private String externalSystemId;
    private String bussinessType;
    private Timestamp startTime;
    private Timestamp endTime;
    private String status;
    private int processMilliseconds;

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExternalSystemId() {
        return externalSystemId;
    }

    public void setExternalSystemId(String externalSystemId) {
        this.externalSystemId = externalSystemId;
    }

    public String getBussinessType() {
        return bussinessType;
    }

    public void setBussinessType(String bussinessType) {
        this.bussinessType = bussinessType;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProcessMilliseconds() {
        return processMilliseconds;
    }

    public void setProcessMilliseconds(int processMilliseconds) {
        this.processMilliseconds = processMilliseconds;
    }
}
