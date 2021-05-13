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

package com.pamirs.tro.entity.domain.entity.linkmanage;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * t_scene_link_relate
 *
 * @author
 */
@Data
public class SceneLinkRelate implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    private Long id;
    /**
     * 场景id
     */
    private String sceneId;
    /**
     * 链路入口
     */
    private String entrance;
    /**
     * 是否有效 0:有效;1:无效
     */
    private Integer isDeleted;
    /**
     * 插入时间
     */
    private Date createTime;
    /**
     * 变更时间
     */
    private Date updateTime;
    /**
     * 上级业务链路ID
     */
    private String parentBusinessLinkId;
    /**
     * 业务链路ID
     */
    private String businessLinkId;
    /**
     * 技术链路ID
     */
    private String techLinkId;
    /**
     * 前端树结构的key--前端产生
     */
    private String frontUUIDKey;

}
