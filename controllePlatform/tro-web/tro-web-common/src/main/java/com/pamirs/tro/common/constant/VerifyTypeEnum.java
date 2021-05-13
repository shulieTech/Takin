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

package com.pamirs.tro.common.constant;

/**
 * @Author: fanxx
 * @Date: 2021/1/6 10:32 上午
 * @Description:
 */
public enum VerifyTypeEnum {
    /**
     * 压测场景
     */
    SCENE(0),

    /**
     * 业务流程
     */
    FLOW(1),

    /**
     * 业务活动
     */
    ACTIVITY(2);

    private Integer code;

    VerifyTypeEnum(Integer code) {
        this.code = code;
    }

    public static VerifyTypeEnum getTypeByCode(Integer code) {
        for (VerifyTypeEnum verifyTypeEnum : VerifyTypeEnum.values()) {
            if (verifyTypeEnum.getCode().equals(code)) {
                return verifyTypeEnum;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
