package io.shulie.tro.definition.config.http.input;

import java.io.Serializable;
import java.util.Date;


public class ApplicationInsertInput implements Serializable {

    private static final long serialVersionUID = -1;

    private String id;
    private String applicationName;

    private Integer accessStatus;

    private Integer nodeNum;

    private String exceptionInfo;

    private String switchStutus;

    private Boolean pressureEnable = true;

    private Date updateTime;

    private String applicationDesc;

    private String ddlScriptPath;

    private String cleanScriptPath;

    private String readyScriptPath;

    private String basicScriptPath;

    private String cacheScriptPath;

    private String managerName;

    private Long managerId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Integer getAccessStatus() {
        return accessStatus;
    }

    public void setAccessStatus(Integer accessStatus) {
        this.accessStatus = accessStatus;
    }

    public Integer getNodeNum() {
        return nodeNum;
    }

    public void setNodeNum(Integer nodeNum) {
        this.nodeNum = nodeNum;
    }

    public String getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }

    public String getSwitchStutus() {
        return switchStutus;
    }

    public void setSwitchStutus(String switchStutus) {
        this.switchStutus = switchStutus;
    }

    public Boolean getPressureEnable() {
        return pressureEnable;
    }

    public void setPressureEnable(Boolean pressureEnable) {
        this.pressureEnable = pressureEnable;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getApplicationDesc() {
        return applicationDesc;
    }

    public void setApplicationDesc(String applicationDesc) {
        this.applicationDesc = applicationDesc;
    }

    public String getDdlScriptPath() {
        return ddlScriptPath;
    }

    public void setDdlScriptPath(String ddlScriptPath) {
        this.ddlScriptPath = ddlScriptPath;
    }

    public String getCleanScriptPath() {
        return cleanScriptPath;
    }

    public void setCleanScriptPath(String cleanScriptPath) {
        this.cleanScriptPath = cleanScriptPath;
    }

    public String getReadyScriptPath() {
        return readyScriptPath;
    }

    public void setReadyScriptPath(String readyScriptPath) {
        this.readyScriptPath = readyScriptPath;
    }

    public String getBasicScriptPath() {
        return basicScriptPath;
    }

    public void setBasicScriptPath(String basicScriptPath) {
        this.basicScriptPath = basicScriptPath;
    }

    public String getCacheScriptPath() {
        return cacheScriptPath;
    }

    public void setCacheScriptPath(String cacheScriptPath) {
        this.cacheScriptPath = cacheScriptPath;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }
}
