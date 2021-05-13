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

package com.pamirs.tro.entity.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 说明: 一级链路实体类
 *
 * @author shulie
 * @version v1.0
 * @2018年4月20日
 */
@JsonIgnoreProperties(value = {"handler"})
public class TFirstLinkMnt extends BaseEntity {

    private static final long serialVersionUID = 1L;

    // @Field id : 链路管理id
    private String linkId;

    // @Field linkName : 链路名称
    private String linkName;

    // @Field secondLinks : 二级链路id字符串
    private String secondLinks;

    // @Field linkTps : 链路tps
    private long linkTps;

    // @Field linkTpsRule : 链路tps计算规则
    private String linkTpsRule;

    // @Field remark : 备注
    private String remark;

    // @Field useYn : 是否可用(0表示不可用;1表示可用)
    private int useYn;

    public TFirstLinkMnt() {
        super();
    }

    /**
     * 2018年5月17日
     *
     * @return the linkId
     * @author shulie
     * @version 1.0
     */
    public String getLinkId() {
        return linkId;
    }

    /**
     * 2018年5月17日
     *
     * @param linkId the linkId to set
     * @author shulie
     * @version 1.0
     */
    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

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
     * @return the secondLinks
     * @author shulie
     * @version 1.0
     */
    public String getSecondLinks() {
        return secondLinks;
    }

    /**
     * 2018年5月17日
     *
     * @param secondLinks the secondLinks to set
     * @author shulie
     * @version 1.0
     */
    public void setSecondLinks(String secondLinks) {
        this.secondLinks = secondLinks;
    }

    /**
     * 2018年5月17日
     *
     * @return the linkTps
     * @author shulie
     * @version 1.0
     */
    public long getLinkTps() {
        return linkTps;
    }

    /**
     * 2018年5月17日
     *
     * @param linkTps the linkTps to set
     * @author shulie
     * @version 1.0
     */
    public void setLinkTps(long linkTps) {
        this.linkTps = linkTps;
    }

    /**
     * 2018年5月17日
     *
     * @return the linkTpsRule
     * @author shulie
     * @version 1.0
     */
    public String getLinkTpsRule() {
        return linkTpsRule;
    }

    /**
     * 2018年5月17日
     *
     * @param linkTpsRule the linkTpsRule to set
     * @author shulie
     * @version 1.0
     */
    public void setLinkTpsRule(String linkTpsRule) {
        this.linkTpsRule = linkTpsRule;
    }

    /**
     * 2018年5月17日
     *
     * @return the remark
     * @author shulie
     * @version 1.0
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 2018年5月17日
     *
     * @param remark the remark to set
     * @author shulie
     * @version 1.0
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 2018年5月17日
     *
     * @return the useYn
     * @author shulie
     * @version 1.0
     */
    public int getUseYn() {
        return useYn;
    }

    /**
     * 2018年5月17日
     *
     * @param useYn the useYn to set
     * @author shulie
     * @version 1.0
     */
    public void setUseYn(int useYn) {
        this.useYn = useYn;
    }

    /**
     * 2018年5月17日
     *
     * @return 实体字符串
     * @author shulie
     * @version 1.0
     */
    @Override
    public String toString() {
        return "TFirstLinkMnt [linkId=" + linkId + ", linkName=" + linkName + ", secondLinks=" + secondLinks
            + ", linkTps=" + linkTps + ", linkTpsRule=" + linkTpsRule + ", remark=" + remark + "]";
    }

}
