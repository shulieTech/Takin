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

package io.shulie.tro.cloud.common.enums;

/**
 * @author 何仲奇
 * @Package com.pamirs.tro.common.enums
 * @date 2020/9/25 12:56 下午
 */
public enum SceneManageErrorEnum {
    PRESSURE_MEASUREMENT_FAILED("压测失败"),
    SCENEMANAGE__NOT_FIND_SCENE("更新场景生命周期失败，场景不存在"),
    SCENEMANAGE_UPDATE_LIFECYCLE_NOT_FIND_SCENE("更新场景生命周期失败，场景不存在"),
    SCENEMANAGE_UPDATE_LIFECYCLE_UNKNOWN_STATE("更新场景生命周期失败，未知状态"),
    SCENEMANAGE_UPDATE_LIFECYCLE_CHECK_FAILED("更新场景生命周期失败，check状态错误"),
    SCENEMANAGE_UPDATE_LIFECYCLE_FAIL("更新场景生命周期失败，系统运行错误");

    /**
     * 错误信息
     */
    private String errorMessage;

    SceneManageErrorEnum(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
