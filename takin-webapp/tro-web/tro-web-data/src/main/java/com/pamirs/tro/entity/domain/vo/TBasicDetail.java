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
import java.math.BigDecimal;

/**
 * 说明: 基础链路压测详情实体
 *
 * @author shulie
 * @version v1.0
 * @2018年5月17日
 * @since 2018/05/08
 */
public class TBasicDetail implements Serializable {

    //序列号
    private static final long serialVersionUID = 6134713629206586141L;

    /**
     * 链路名称
     */
    private String linkName;

    /**
     * tps
     */
    private Integer tps;

    /**
     * rt
     */
    private BigDecimal rt;

    /**
     * SA
     */
    private BigDecimal sa;

    /**
     * 事务成功率
     */
    private BigDecimal successRate;

    /**
     * 2018年5月17日
     *
     * @return the linkName
     * @author shulie
     * @version 1.0
     */
    public String getLinkName() {
        return linkName;
    }

    /**
     * 2018年5月17日
     *
     * @param linkName the linkName to set
     * @author shulie
     * @version 1.0
     */
    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    /**
     * 2018年5月17日
     *
     * @return the tps
     * @author shulie
     * @version 1.0
     */
    public Integer getTps() {
        return tps;
    }

    /**
     * 2018年5月17日
     *
     * @param tps the tps to set
     * @author shulie
     * @version 1.0
     */
    public void setTps(Integer tps) {
        this.tps = tps;
    }

    /**
     * 2018年5月17日
     *
     * @return the rt
     * @author shulie
     * @version 1.0
     */
    public BigDecimal getRt() {
        return rt;
    }

    /**
     * 2018年5月17日
     *
     * @param rt the rt to set
     * @author shulie
     * @version 1.0
     */
    public void setRt(BigDecimal rt) {
        this.rt = rt;
    }

    /**
     * 2018年5月17日
     *
     * @return the sa
     * @author shulie
     * @version 1.0
     */
    public BigDecimal getSa() {
        return sa;
    }

    /**
     * 2018年5月17日
     *
     * @param sa the sa to set
     * @author shulie
     * @version 1.0
     */
    public void setSa(BigDecimal sa) {
        this.sa = sa;
    }

    /**
     * 2018年5月17日
     *
     * @return the successRate
     * @author shulie
     * @version 1.0
     */
    public BigDecimal getSuccessRate() {
        return successRate;
    }

    /**
     * 2018年5月17日
     *
     * @param successRate the successRate to set
     * @author shulie
     * @version 1.0
     */
    public void setSuccessRate(BigDecimal successRate) {
        this.successRate = successRate;
    }

}
