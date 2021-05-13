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

package io.shulie.tro.web.data.model.mysql;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 链路管理表
 */
@Data
@TableName(value = "t_link_manage_table")
public class LinkManageTableEntity {
    /**
     * 主键
     */
    @TableId(value = "LINK_ID", type = IdType.AUTO)
    private Long linkId;

    /**
     * 链路名称
     */
    @TableField(value = "LINK_NAME")
    private String linkName;

    /**
     * 链路入口
     */
    @TableField(value = "ENTRACE")
    private String entrace;

    /**
     * 影子入口
     */
    @TableField(value = "PT_ENTRACE")
    private String ptEntrace;

    /**
     * 技术链路变更前
     */
    @TableField(value = "CHANGE_BEFORE")
    private String changeBefore;

    /**
     * 技术链路变更后
     */
    @TableField(value = "CHANGE_AFTER")
    private String changeAfter;

    /**
     * 变化差异
     */
    @TableField(value = "CHANGE_REMARK")
    private String changeRemark;

    /**
     * 是否有变更 0:正常；1:已变更
     */
    @TableField(value = "IS_CHANGE")
    private Integer isChange;

    /**
     * 任务类型 0:普通入口；1:定时任务
     */
    @TableField(value = "IS_JOB")
    private Integer isJob;

    /**
     * 租户id
     */
    @TableField(value = "CUSTOMER_ID")
    private Long customerId;

    /**
     * 用户id
     */
    @TableField(value = "USER_ID")
    private Long userId;

    /**
     * 是否有效 0:有效;1:无效
     */
    @TableField(value = "IS_DELETED")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME")
    private Date updateTime;

    /**
     * 应用名
     */
    @TableField(value = "APPLICATION_NAME")
    private String applicationName;

    /**
     * 变更类型: 1:无流量调用通知;2:添加调用关系通知
     */
    @TableField(value = "CHANGE_TYPE")
    private Integer changeType;

    /**
     * 是否可以删除 0:可以删除;1:不可以删除
     */
    @TableField(value = "CAN_DELETE")
    private Integer canDelete;

    /**
     * 拓展字段
     */
    @TableField(value = "features")
    private String features;

    public static final String COL_LINK_ID = "LINK_ID";

    public static final String COL_LINK_NAME = "LINK_NAME";

    public static final String COL_ENTRACE = "ENTRACE";

    public static final String COL_PT_ENTRACE = "PT_ENTRACE";

    public static final String COL_CHANGE_BEFORE = "CHANGE_BEFORE";

    public static final String COL_CHANGE_AFTER = "CHANGE_AFTER";

    public static final String COL_CHANGE_REMARK = "CHANGE_REMARK";

    public static final String COL_IS_CHANGE = "IS_CHANGE";

    public static final String COL_IS_JOB = "IS_JOB";

    public static final String COL_CUSTOMER_ID = "CUSTOMER_ID";

    public static final String COL_USER_ID = "USER_ID";

    public static final String COL_IS_DELETED = "IS_DELETED";

    public static final String COL_CREATE_TIME = "CREATE_TIME";

    public static final String COL_UPDATE_TIME = "UPDATE_TIME";

    public static final String COL_APPLICATION_NAME = "APPLICATION_NAME";

    public static final String COL_CHANGE_TYPE = "CHANGE_TYPE";

    public static final String COL_CAN_DELETE = "CAN_DELETE";

    public static final String COL_FEATURES = "features";
}
