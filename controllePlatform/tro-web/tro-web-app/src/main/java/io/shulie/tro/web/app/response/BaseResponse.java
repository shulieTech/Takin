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

package io.shulie.tro.web.app.response;

import java.util.List;
import io.shulie.tro.web.app.common.RestContext;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @author HengYu
 * @className BaseResponse
 * @date 2021/4/13 8:34 上午
 * @description 基础响应类封装一些通用字段
 */
@Data
public class BaseResponse {

    /**
     * 权限所属用户Id
     */
    public Long permissionUserId;

    /**
     * 是否可编辑
     */
    public Boolean canEdit = true;

    /**
     * 是否可删除
     */
    public Boolean canRemove = true;

    /**
     * 是否可启用禁用
     */
    public Boolean canEnableDisable = true;


    /**
     * 控制访问权限方法
     */
    public void permissionControl(){
        List<Long> allowUpdateUserIdList = RestContext.getUpdateAllowUserIdList();
        if (CollectionUtils.isNotEmpty(allowUpdateUserIdList)) {
            this.setCanEdit(allowUpdateUserIdList.contains(this.permissionUserId));
        }
        List<Long> allowDeleteUserIdList = RestContext.getDeleteAllowUserIdList();
        if (CollectionUtils.isNotEmpty(allowDeleteUserIdList)) {
            this.setCanRemove(allowDeleteUserIdList.contains(this.permissionUserId));
        }
        List<Long> allowEnableDisableUserIdList = RestContext.getEnableDisableAllowUserIdList();
        if (CollectionUtils.isNotEmpty(allowEnableDisableUserIdList)) {
            this.setCanEnableDisable(allowEnableDisableUserIdList.contains(this.permissionUserId));
        }
    }

}
