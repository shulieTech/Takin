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

package io.shulie.tro.web.data.param.linkmanage;

import java.util.Date;

import lombok.Data;

/**
 * @Author ZhangXT
 * @Description 业务流程
 * @Date 2020/11/5 17:54
 */
@Data
public class SceneUpdateParam {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 场景名
     */
    private String sceneName;
    /**
     * 场景所绑定的业务链路名集合
     */
    private String businessLink;
    /**
     * 场景等级 :p0/p1/02/03
     */
    private String sceneLevel;
    /**
     * 是否核心场景 0:不是;1:是
     */
    private Integer isCore;
    /**
     * 是否有变更 0:没有变更，1有变更
     */
    private Integer isChanged;
    /**
     * 是否有效 0:有效;1:无效
     */
    private Integer isDeleted;
    /**
     * 变更时间
     */
    private Date updateTime;

    /**
     * 租户id
     */
    private Long customerId;

    /**
     * 用户id
     */
    private Long userId;

    public SceneUpdateParam() {
    }

    public SceneUpdateParam(Long id, Long userId) {
        this.id = id;
        this.userId = userId;
    }
}
