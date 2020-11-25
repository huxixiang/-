package com.dcits.restapiapp.model;

import java.sql.Timestamp;

public class SchedulerJobLogModel {
    private String schedulerJobId;
    private String interfaceId;
    private String status;
    private String successDesc;
    private String failDesc;
    private String processClass;
    private Timestamp processStartTime;
    private Timestamp processEndTime;
    private Long processMilliseconds;

    private String interfaceName;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public Timestamp getProcessEndTime() {
        return processEndTime;
    }

    public void setProcessEndTime(Timestamp processEndTime) {
        this.processEndTime = processEndTime;
    }

    public Long getProcessMilliseconds() {
        return processMilliseconds;
    }

    public void setProcessMilliseconds(Long processMilliseconds) {
        this.processMilliseconds = processMilliseconds;
    }

    public Timestamp getProcessStartTime() {
        return processStartTime;
    }

    public void setProcessStartTime(Timestamp processStartTime) {
        this.processStartTime = processStartTime;
    }

    public String getSchedulerJobId() {
        return schedulerJobId;
    }

    public void setSchedulerJobId(String schedulerJobId) {
        this.schedulerJobId = schedulerJobId;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSuccessDesc() {
        return successDesc;
    }

    public void setSuccessDesc(String successDesc) {
        this.successDesc = successDesc;
    }

    public String getFailDesc() {
        return failDesc;
    }

    public void setFailDesc(String failDesc) {
        this.failDesc = failDesc;
    }

    public String getProcessClass() {
        return processClass;
    }

    public void setProcessClass(String processClass) {
        this.processClass = processClass;
    }
}
