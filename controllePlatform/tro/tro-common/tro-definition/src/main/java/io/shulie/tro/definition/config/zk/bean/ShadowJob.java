package io.shulie.tro.definition.config.zk.bean;

import java.io.Serializable;

import io.shulie.tro.definition.config.zk.enums.ShadowJobType;

/**
 * @author shiyajian
 * create: 2020-09-15
 */
public class ShadowJob implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * Job 类型
     */
    private ShadowJobType type;

    private String className;

    private String cron;

    private String jobDataType;

    private String listener;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShadowJobType getType() {
        return type;
    }

    public void setType(ShadowJobType type) {
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getJobDataType() {
        return jobDataType;
    }

    public void setJobDataType(String jobDataType) {
        this.jobDataType = jobDataType;
    }

    public String getListener() {
        return listener;
    }

    public void setListener(String listener) {
        this.listener = listener;
    }
}
