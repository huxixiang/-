package com.dcits.restapiapp.model;

public class SchedulerCronModel {
    private String InterfaceId;
    private String cron;
    private String status;
    private String className;
    private String kafkaTopic;
    private String systemId;

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getKafkaTopic() {
        return kafkaTopic;
    }

    public void setKafkaTopic(String kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInterfaceId() {
        return InterfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        InterfaceId = interfaceId;
    }




}
