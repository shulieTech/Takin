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

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 账户账本表
 */
@Data
@TableName(value = "t_ac_account_book")
public class AccountBookEntity {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID（外键）
     */
    @TableField(value = "uid")
    private Long uid;

    /**
     * 账户ID（外键）
     */
    @TableField(value = "acc_id")
    private Long accId;

    /**
     * 父类ID
     */
    @TableField(value = "parent_book_id")
    private Long parentBookId;

    /**
     * 余额
     */
    @TableField(value = "balance")
    private BigDecimal balance;

    /**
     * 冻结金额
     */
    @TableField(value = "lock_balance")
    private BigDecimal lockBalance;

    /**
     * 总金额
     */
    @TableField(value = "total_balance")
    private BigDecimal totalBalance;

    /**
     * 科目
     */
    @TableField(value = "subject")
    private Integer subject;

    /**
     * 记账方向，借或贷
     */
    @TableField(value = "direct")
    private Integer direct;

    /**
     * 规则
     */
    @TableField(value = "rule")
    private String rule;

    /**
     * 规则余额
     */
    @TableField(value = "rule_balance")
    private BigDecimal ruleBalance;

    /**
     * 生效时间
     */
    @TableField(value = "start_time")
    private LocalDateTime startTime;

    /**
     * 失效时间
     */
    @TableField(value = "end_time")
    private LocalDateTime endTime;

    /**
     * 状态
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 版本
     */
    @TableField(value = "version")
    private Integer version;

    /**
     * 是否删除
     */
    @TableField(value = "is_deleted")
    private Boolean isDeleted;

    /**
     * 标签
     */
    @TableField(value = "tags")
    private Long tags;

    /**
     * 扩展字段
     */
    @TableField(value = "features")
    private String features;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_create")
    private LocalDateTime gmtCreate;

    /**
     * 更新时间
     */
    @TableField(value = "gmt_update")
    private LocalDateTime gmtUpdate;
}
