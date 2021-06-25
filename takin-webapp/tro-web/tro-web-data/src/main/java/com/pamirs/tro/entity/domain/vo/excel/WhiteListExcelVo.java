/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pamirs.tro.entity.domain.vo.excel;

import java.util.List;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

/**
 * @Auther: vernon
 * @Date: 2019/11/1 01:17
 * @Description:
 */
public class WhiteListExcelVo extends BaseRowModel {
    @ExcelProperty(value = "负责人工号", index = 0)
    private String principalNo;
    @ExcelProperty(value = "应用名称", index = 1)
    private String applicationName;
    @ExcelProperty(value = "白名单类型", index = 2)
    private String type;
    @ExcelProperty(value = "HTTP 类型", index = 3)
    private String httpType;
    @ExcelProperty(value = "JOB 调度间隔", index = 4)
    private String jobInterval;
    @ExcelProperty(value = "MQ类型选择框", index = 5)
    private String mqType;
    @ExcelProperty(value = "是否可用", index = 6)
    private String useYn;
    @ExcelProperty(value = "接口列表", index = 7)
    private List<String> list;

    public String getPrincipalNo() {
        return principalNo;
    }

    public void setPrincipalNo(String principalNo) {
        this.principalNo = principalNo;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHttpType() {
        return httpType;
    }

    public void setHttpType(String httpType) {
        this.httpType = httpType;
    }

    public String getJobInterval() {
        return jobInterval;
    }

    public void setJobInterval(String jobInterval) {
        this.jobInterval = jobInterval;
    }

    public String getMqType() {
        return mqType;
    }

    public void setMqType(String mqType) {
        this.mqType = mqType;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
