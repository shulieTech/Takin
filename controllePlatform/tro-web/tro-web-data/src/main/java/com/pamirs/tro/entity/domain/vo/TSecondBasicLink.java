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

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;
import com.pamirs.tro.common.util.LongToStringFormatSerialize;
import com.pamirs.tro.entity.domain.entity.BaseEntity;
import com.pamirs.tro.entity.domain.entity.TLinkMnt;

/**
 * 说明: 压测总览图封装实体类
 *
 * @author shulie
 * @version v1.0
 * @Date: Create in 2018/6/18 11:18
 */
public class TSecondBasicLink extends BaseEntity {

    /**
     * 二级链路id
     */
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    private long secondLinkId;

    /**
     * 二级链路名称
     */
    private String secondLinkName;

    /**
     * 二级链路检测状态(扩展字段)
     */
    private String secondLinkCheckStatus;

    /**
     * 业务链路集合
     */
    private List<TLinkMnt> basicLinkList = Lists.newArrayList();

    public TSecondBasicLink() {
    }

    /**
     * Gets the value of secondLinkId.
     *
     * @return the value of secondLinkId
     * @author shulie
     * @version 1.0
     */
    public long getSecondLinkId() {
        return secondLinkId;
    }

    /**
     * Sets the secondLinkId.
     *
     * <p>You can use getSecondLinkId() to get the value of secondLinkId</p>
     *
     * @param secondLinkId secondLinkId
     * @author shulie
     * @version 1.0
     */
    public void setSecondLinkId(long secondLinkId) {
        this.secondLinkId = secondLinkId;
    }

    /**
     * Gets the value of secondLinkName.
     *
     * @return the value of secondLinkName
     * @author shulie
     * @version 1.0
     */
    public String getSecondLinkName() {
        return secondLinkName;
    }

    /**
     * Sets the secondLinkName.
     *
     * <p>You can use getSecondLinkName() to get the value of secondLinkName</p>
     *
     * @param secondLinkName secondLinkName
     * @author shulie
     * @version 1.0
     */
    public void setSecondLinkName(String secondLinkName) {
        this.secondLinkName = secondLinkName;
    }

    /**
     * Gets the value of secondLinkCheckStatus.
     *
     * @return the value of secondLinkCheckStatus
     * @author shulie
     * @version 1.0
     */
    public String getSecondLinkCheckStatus() {
        return secondLinkCheckStatus;
    }

    /**
     * Sets the secondLinkCheckStatus.
     *
     * <p>You can use getSecondLinkCheckStatus() to get the value of secondLinkCheckStatus</p>
     *
     * @param secondLinkCheckStatus secondLinkCheckStatus
     * @author shulie
     * @version 1.0
     */
    public void setSecondLinkCheckStatus(String secondLinkCheckStatus) {
        this.secondLinkCheckStatus = secondLinkCheckStatus;
    }

    /**
     * Gets the value of basicLinkList.
     *
     * @return the value of basicLinkList
     * @author shulie
     * @version 1.0
     */
    public List<TLinkMnt> getBasicLinkList() {
        return basicLinkList;
    }

    /**
     * Sets the basicLinkList.
     *
     * <p>You can use getBasicLinkList() to get the value of basicLinkList</p>
     *
     * @param basicLinkList basicLinkList
     * @author shulie
     * @version 1.0
     */
    public void setBasicLinkList(List<TLinkMnt> basicLinkList) {
        this.basicLinkList = basicLinkList;
    }

    @Override
    public String toString() {
        return "TSecondBasicLink{" +
            "secondLinkId=" + secondLinkId +
            ", secondLinkName='" + secondLinkName + '\'' +
            ", secondLinkCheckStatus='" + secondLinkCheckStatus + '\'' +
            ", basicLinkList=" + basicLinkList +
            '}';
    }
}
