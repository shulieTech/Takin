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

package com.pamirs.tro.entity.domain.vo.scenemanage;

import lombok.Data;

/**
 * @author 何仲奇
 * TODO 新增一张表，用于记录启动记录
 * @Package com.pamirs.tro.entity.domain.vo.scenemanage
 * @date 2020/9/24 9:57 上午
 */
@Data
public class SceneManageStartRecordVO {
    /**
     * 任务ID
     */
    private Long resultId;

    /**
     * 场景ID
     */
    private Long sceneId;

    /**
     * 客户Id 新增
     */
    private Long customerId;

    private Boolean success;

    private String errorMsg;

    public SceneManageStartRecordVO(Long resultId, Long sceneId, Long customerId, Boolean success,
        String errorMsg) {
        this.resultId = resultId;
        this.sceneId = sceneId;
        this.customerId = customerId;
        this.success = success;
        this.errorMsg = errorMsg;
    }

    /**
     * create Builder method
     **/
    public static SceneManageStartRecordVO.Builder build(Long sceneId, Long resultId, Long customerId) {
        return new SceneManageStartRecordVO.Builder(sceneId, resultId, customerId);
    }

    public static class Builder {
        private Long resultId;
        private Long sceneId;
        private Long customerId;
        private Boolean success;
        private String errorMsg;

        Builder(Long sceneId, Long resultId, Long customerId) {
            this.sceneId = sceneId;
            this.resultId = resultId;
            this.customerId = customerId;
        }

        public SceneManageStartRecordVO.Builder success(Boolean success) {
            this.success = success;
            return this;
        }

        public SceneManageStartRecordVO.Builder errorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
            return this;
        }

        public SceneManageStartRecordVO build() {
            return new SceneManageStartRecordVO(resultId, sceneId, customerId, success, errorMsg);
        }
    }

}
