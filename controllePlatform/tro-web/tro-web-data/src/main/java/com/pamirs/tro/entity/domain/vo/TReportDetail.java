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

package com.pamirs.tro.entity.domain.vo;

import java.io.Serializable;
import java.util.List;

import com.pamirs.tro.entity.domain.entity.TAlarm;
import com.pamirs.tro.entity.domain.entity.TReport;

/**
 * 说明: 压测报告详情实体
 *
 * @author shulie
 * @version v1.0
 * @2018年5月17日
 * @since 2018/05/08
 */
public class TReportDetail implements Serializable {

    private static final long serialVersionUID = 3415195393695125154L;
    /**
     * 测试背景
     */
    private TReport tReport;

    /**
     * 告警情况
     */
    private List<TAlarm> tAlarms;

    //是否通过
    private Boolean pass;

    //报告结果
    private List<TReportResult> tReportResults;

    /**
     * 2018年5月17日
     *
     * @return the tReport
     * @author shulie
     * @version 1.0
     */
    public TReport gettReport() {
        return tReport;
    }

    /**
     * 2018年5月17日
     *
     * @param tReport the tReport to set
     * @author shulie
     * @version 1.0
     */
    public void settReport(TReport tReport) {
        this.tReport = tReport;
    }

    /**
     * 2018年5月17日
     *
     * @return the pass
     * @author shulie
     * @version 1.0
     */
    public Boolean getPass() {
        return pass;
    }

    /**
     * 2018年5月17日
     *
     * @param pass the pass to set
     * @author shulie
     * @version 1.0
     */
    public void setPass(Boolean pass) {
        this.pass = pass;
    }

    /**
     * 2018年5月17日
     *
     * @return the tAlarms
     * @author shulie
     * @version 1.0
     */
    public List<TAlarm> gettAlarms() {
        return tAlarms;
    }

    /**
     * 2018年5月17日
     *
     * @param tAlarms the tAlarms to set
     * @author shulie
     * @version 1.0
     */
    public void settAlarms(List<TAlarm> tAlarms) {
        this.tAlarms = tAlarms;
    }

    public List<TReportResult> gettReportResults() {
        return tReportResults;
    }

    public void settReportResults(List<TReportResult> tReportResults) {
        this.tReportResults = tReportResults;
    }
}
