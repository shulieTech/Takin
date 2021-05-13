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

package io.shulie.tro.cloud.common.exception;

import io.shulie.tro.exception.entity.ExceptionReadable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shiyajian
 * create: 2020-09-04
 */
@Getter
@AllArgsConstructor
public enum TroCloudExceptionEnum implements ExceptionReadable {
    /**
     * ID不能为空
     */
    ID_NULL("", "ID不能为空"),
    /**
     * SceneManage 0001
     */
    SCENEMANAGE_ADD_ERROR("0000-0000-0001-0001", "新增场景失败"),
    SCENEMANAGE_UPDATE_ERROR("0000-0000-0001-0002", "更新场景失败"),
    SCENEMANAGE_GET_ERROR("0000-0000-0001-0003", "获取场景失败"),
    SCENEMANAGE_BULID_PARAM_ERROR("0000-0000-0001-0004", "场景构建参数失败"),
    SCENE_MANAGE_UPDATE_FILE_ERROR("0000-0000-0001-0005", "场景管理之更新相关文件失败!"),

    SCENE_TASK_ERROR("0000-0000-0002-0001", "场景任务错误"),
    /**
     *
     */
    REPORT_GET_ERROR("0000-0000-0002-0002",""),

    /**
     * k8s的节点
     */
    K8S_NODE_EMPTY("0000-0000-0003-0001",""),
    /**
     * pod数量计算
     */
    POD_NUM_EMPTY("0000-0000-0004-0001",""),
    /**
     * pod数量计算
     */
    ENGINE_PACK_VERSION_EXCEPTION("0000-0000-0005-0001","")
    ;

    private final String errorCode;

    private final String defaultValue;

}
