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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pamirs.tro.common.util.LongToStringFormatSerialize;

/**
 * 说明: 二级链路基础链路关联关系表实体类
 *
 * @author shulie
 * @version v1.0
 * @Date: Create in 2018/6/18 10:30
 */
public class TSecondBasic extends BaseEntity {

    // @Field secondLinkId : 二级链路id
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    private long secondLinkId;

    // @Field basicLinkId : 基础链路id
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    private long basicLinkId;

    // @Field blinkOrder : 基础链路顺序
    private int blinkOrder;

    // @Field blinkBank : 基础链路等级(二级链路下有基础链路1，基础链路2等)
    private int blinkBank;

    public TSecondBasic() {
    }

    public long getSecondLinkId() {
        return secondLinkId;
    }

    public void setSecondLinkId(long secondLinkId) {
        this.secondLinkId = secondLinkId;
    }

    public long getBasicLinkId() {
        return basicLinkId;
    }

    public void setBasicLinkId(long basicLinkId) {
        this.basicLinkId = basicLinkId;
    }

    public int getBlinkOrder() {
        return blinkOrder;
    }

    public void setBlinkOrder(int blinkOrder) {
        this.blinkOrder = blinkOrder;
    }

    public int getBlinkBank() {
        return blinkBank;
    }

    public void setBlinkBank(int blinkBank) {
        this.blinkBank = blinkBank;
    }

    @Override
    public String toString() {
        return "TSecondBasic{" +
            "secondLinkId=" + secondLinkId +
            ", basicLinkId=" + basicLinkId +
            ", blinkOrder=" + blinkOrder +
            ", blinkBank=" + blinkBank +
            '}';
    }
}
