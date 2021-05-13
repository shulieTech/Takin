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
 * @Date: 2021/1/7 10:59 上午
 * @Description:
 */
public enum VerifyResultStatusEnum {
    /**
     * 是否漏数 0:正常;1:漏数;2:未检测;3:检测失败
     */
    NORMAL(0, "无漏数", 1),

    LEAKED(1, "有漏数", 4),

    UNCHECK(2, "未验证", 2),

    FAILED(3, "验证失败", 3);

    private Integer code;

    private String label;

    private Integer warningLevel;

    VerifyResultStatusEnum(Integer code, String label, Integer warningLevel) {
        this.code = code;
        this.label = label;
        this.warningLevel = warningLevel;
    }

    public static VerifyResultStatusEnum getTypeByCode(Integer code) {
        for (VerifyResultStatusEnum statusEnum : VerifyResultStatusEnum.values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum;
            }
        }
        return null;
    }

    public static String getLabelByCode(Integer code) {
        for (VerifyResultStatusEnum statusEnum : VerifyResultStatusEnum.values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum.getLabel();
            }
        }
        return null;
    }

    public static Integer getWarningLevelByCode(Integer code) {
        for (VerifyResultStatusEnum statusEnum : VerifyResultStatusEnum.values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum.getWarningLevel();
            }
        }
        return null;
    }

    public static Integer getCodeByWarningLevel(Integer warningLevel) {
        for (VerifyResultStatusEnum statusEnum : VerifyResultStatusEnum.values()) {
            if (statusEnum.getWarningLevel().equals(warningLevel)) {
                return statusEnum.getCode();
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getWarningLevel() {
        return warningLevel;
    }

    public void setWarningLevel(Integer warningLevel) {
        this.warningLevel = warningLevel;
    }
}
