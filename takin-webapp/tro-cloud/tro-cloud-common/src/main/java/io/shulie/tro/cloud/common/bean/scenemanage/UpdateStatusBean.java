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

package io.shulie.tro.cloud.common.bean.scenemanage;

import java.io.Serializable;

import io.shulie.tro.cloud.common.enums.scenemanage.SceneManageStatusEnum;
import lombok.Data;

/**
 * @ClassName UpdateSceneManageStatusVO
 * @Description
 * @Author qianshui
 * @Date 2020/4/18 下午6:24
 */
@Data
public class UpdateStatusBean implements Serializable {

    /**
     * 任务ID
     */
    private Long resultId;

    /**
     * 用于报告
     */
    private Integer preStatus;
    /**
     * 用于报告
     */
    private Integer afterStatus;

    /**
     * 场景ID
     */
    private Long sceneId;

    /**
     * 客户Id 新增
     */
    private Long customerId;

    /**
     * check 状态
     */
    private SceneManageStatusEnum[] checkEnum;
    /**
     * 更新至此状态
     */
    private SceneManageStatusEnum updateEnum;

    public UpdateStatusBean() {}

    /**
     *
     * @param sceneId
     * @param resultId
     * @param customerId
     * @param updateEnum
     * @param checkEnums 最后一个参数 是check
     */
    public UpdateStatusBean(Long sceneId,Long resultId,Long  customerId,SceneManageStatusEnum updateEnum,SceneManageStatusEnum ...checkEnums) {
        this.sceneId = sceneId;
        this.resultId = resultId;
        this.customerId = customerId;
        this.checkEnum = checkEnums;
        this.updateEnum = updateEnum;
    }

    /**create Builder method**/
    public static UpdateStatusBean.Builder build (Long sceneId,Long resultId,Long customerId) {
        return new Builder(sceneId,resultId,customerId);
    }

    public static class Builder {
        private Long resultId;
        private Long sceneId;
        private Long customerId;
        private SceneManageStatusEnum[] checkEnum;
        private SceneManageStatusEnum updateEnum;


        Builder(Long sceneId,Long resultId,Long customerId){
            this.sceneId = sceneId;
            this.resultId = resultId;
            this.customerId =customerId;
        }

        public Builder checkEnum(SceneManageStatusEnum ...statusEnums) {
            this.checkEnum = statusEnums;
            return this;
        }
        public Builder updateEnum(SceneManageStatusEnum updateEnum) {
            this.updateEnum = updateEnum;
            return this;
        }

        public UpdateStatusBean build() {
            return new UpdateStatusBean(sceneId, resultId, customerId,updateEnum ,checkEnum);
        }
    }
}
