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

package com.pamirs.tro.entity.domain.dto.config;

import java.io.Serializable;

import lombok.Data;

/**
 * @ClassName WhiteListSwitchDTO
 * @Description 白名单开启状态
 * @Author qianshui
 * @Date 2020/8/18 上午11:50
 */
@Data
public class WhiteListSwitchDTO implements Serializable {

    private static final long serialVersionUID = -578646403705050337L;

    /**
     * 配置编码
     */
    private String configCode;

    /**
     * 开关状态
     * 0-关闭
     * 1-开启
     */
    private Integer switchFlag = 1;

    /**
     * OPENED("已开启",0),
     * OPENING("开启中",1),
     * OPEN_FAILING("开启异常",2),
     * CLOSED("已关闭",3),
     * CLOSING("关闭中",4),
     * CLOSE_FAILING("关闭异常",5)
     */
    private String switchStatus;

    @Deprecated
    public void setSwitchFlag(Integer switchFlag) {
        this.switchFlag = switchFlag;
        if (switchFlag != null && switchFlag == 1) {
            this.setSwitchStatus("OPENED");
        } else {
            this.setSwitchStatus("CLOSED");
        }
    }

    public void setSwitchFlagFix(Boolean value) {
        this.switchFlag = value ? 1 : 0;
        this.setSwitchStatus(value ? "OPENED" : "CLOSED");
    }
}
