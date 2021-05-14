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

package com.pamirs.tro.entity.domain.query;

import java.util.List;

import com.pamirs.tro.entity.domain.entity.TAlarm;

/**
 * 说明：告警查询实体类
 *
 * @author shulie
 * @version v1.0
 * @2018年5月17日
 */
public class TAlarmQuery extends Query<TAlarm> {

    /**
     * 序列号
     */
    private static final long serialVersionUID = 1L;

    //告警开始日期
    private String beginAlarmDate;
    //告警结束日期
    private String endAlarmDate;
    //war包名称
    private List<String> warNames;

    /**
     * 2018年5月17日
     *
     * @return the beginAlarmDate
     * @author shulie
     * @version 1.0
     */
    public String getBeginAlarmDate() {
        return beginAlarmDate;
    }

    /**
     * 2018年5月17日
     *
     * @param beginAlarmDate the beginAlarmDate to set
     * @author shulie
     * @version 1.0
     */
    public void setBeginAlarmDate(String beginAlarmDate) {
        this.beginAlarmDate = beginAlarmDate;
    }

    /**
     * 2018年5月17日
     *
     * @return the endAlarmDate
     * @author shulie
     * @version 1.0
     */
    public String getEndAlarmDate() {
        return endAlarmDate;
    }

    /**
     * 2018年5月17日
     *
     * @param endAlarmDate the endAlarmDate to set
     * @author shulie
     * @version 1.0
     */
    public void setEndAlarmDate(String endAlarmDate) {
        this.endAlarmDate = endAlarmDate;
    }

    /**
     * 2018年5月17日
     *
     * @return the warNames
     * @author shulie
     * @version 1.0
     */
    public List<String> getWarNames() {
        return warNames;
    }

    /**
     * 2018年5月17日
     *
     * @param warNames the warNames to set
     * @author shulie
     * @version 1.0
     */
    public void setWarNames(List<String> warNames) {
        this.warNames = warNames;
    }
}
