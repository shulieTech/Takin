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

package com.pamirs.tro.entity.domain.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 说明：黑名单实体类
 *
 * @author shulie
 * @version V1.0
 * @date: 2018年3月1日 下午12:49:55
 */
@Deprecated
@ApiModel(value = "TBList", description = "黑名单实体类")
public class TBList extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "blistId", value = "黑名单编号")
    private long blistId;

    @ApiModelProperty(name = "redisKey", value = "redis key名称")
    private String redisKey;

    @ApiModelProperty(name = "useYn", value = "是否启用：1启用 0禁用")
    private String useYn;

    private Long customerId;

    private Long userId;

    private Boolean canEdit = true;

    private Boolean canRemove = true;

    private Boolean canEnableDisable = true;

    public TBList() {
        super();
    }

    /**
     * 2018年5月17日
     *
     * @return the blistId
     * @author shulie
     * @version 1.0
     */
    public long getBlistId() {
        return blistId;
    }

    /**
     * 2018年5月17日
     *
     * @param blistId the blistId to set
     * @author shulie
     * @version 1.0
     */
    public void setBlistId(long blistId) {
        this.blistId = blistId;
    }

    /**
     * 2018年5月17日
     *
     * @return the redisKey
     * @author shulie
     * @version 1.0
     */
    public String getRedisKey() {
        return redisKey;
    }

    /**
     * 2018年5月17日
     *
     * @param redisKey the redisKey to set
     * @author shulie
     * @version 1.0
     */
    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    /**
     * 2018年5月17日
     *
     * @return the useYn
     * @author shulie
     * @version 1.0
     */
    public String getUseYn() {
        return useYn;
    }

    /**
     * 2018年5月17日
     *
     * @param useYn the useYn to set
     * @author shulie
     * @version 1.0
     */
    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(Boolean canEdit) {
        this.canEdit = canEdit;
    }

    public Boolean getCanRemove() {
        return canRemove;
    }

    public void setCanRemove(Boolean canRemove) {
        this.canRemove = canRemove;
    }

    public Boolean getCanEnableDisable() {
        return canEnableDisable;
    }

    public void setCanEnableDisable(Boolean canEnableDisable) {
        this.canEnableDisable = canEnableDisable;
    }

    /**
     * 2018年5月17日
     *
     * @return 实体字符串
     * @author shulie
     * @version 1.0
     */
    @Override
    public String toString() {
        return "TBList [blistId=" + blistId + ", redisKey=" + redisKey + ", useYn="
            + useYn + "]";
    }
}
