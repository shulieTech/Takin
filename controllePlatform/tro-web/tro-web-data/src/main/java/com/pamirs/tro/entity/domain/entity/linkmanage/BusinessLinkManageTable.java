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
 * t_business_link_manage_table
 *
 * @author 业务链路实体
 */
@Data
public class BusinessLinkManageTable implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long linkId;
    /**
     * 链路名称
     */
    private String linkName;
    /**
     * 链路入口
     */
    private String entrace;
    /**
     * 业务链路绑定的技术链路
     */
    private String relatedTechLink;
    /**
     * 业务链路级别: p0/p1/p2/p3
     */
    private String linkLevel;
    /**
     * 业务链路的上级业务链路名
     */
    private String parentBusinessId;
    /**
     * 是否有变更 0:正常；1:已变更
     */
    private Integer isChange;
    /**
     * 业务链路是否否核心链路 0:不是;1:是
     */
    private Integer isCore;
    /**
     * 是否有效 0:有效;1:无效
     */
    private Integer isDeleted;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 业务域
     */
    private String businessDomain;
    /**
     * 是否能删除
     */
    private Integer canDelete;
}
