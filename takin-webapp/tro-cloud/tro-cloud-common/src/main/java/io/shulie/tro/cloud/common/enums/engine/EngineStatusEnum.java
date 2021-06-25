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

package io.shulie.tro.cloud.common.enums.engine;

/**
 * @author 何仲奇
 * @Package com.pamirs.tro.entity.domain.vo.jmeter
 * @date 2020/9/23 3:01 下午
 */
public enum EngineStatusEnum {
    STARTED("启动成功","started"),
    START_FAILED( "启动失败","startFail"),
    INTERRUPT( "中断","interrupt"),
    INTERRUPT_SUCCESSED("中断成功","interruptSuccess"),
    INTERRUPT_FAILED("中断失败","interruptFail");


    private String status;
    private String message;

    EngineStatusEnum(String message,String status) {
        this.message = message;
        this.status = status;
    }

    public  String getStatus() {
        return status;
    }


    public String getMessage() {
        return message;
    }

    public static EngineStatusEnum getJmeterStatusEnum(String status) {
        for(EngineStatusEnum statusEnum:values()) {
            if(status.equals(statusEnum.getStatus())) {
                return statusEnum;
            }
        }
        return null;
    }

}
