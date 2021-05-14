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

package com.pamirs.tro.entity.domain.entity.auth;

import org.apache.commons.lang.StringUtils;

/**
 * @Author: fanxx
 * @Date: 2020/9/7 7:31 PM
 * @Description:
 */
public class AuthorityAssembly {
    private String resourceId;

    private String action;

    private Integer objectType;

    private String objectId;

    private String value;

    private String name;

    private String code;

    private String[] userIds;

    private String useIdConcat;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getObjectType() {
        return objectType;
    }

    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String[] getUserIds() {
        if (StringUtils.isBlank(this.useIdConcat)) {
            return new String[0];
        } else {
            String[] actionArr = this.useIdConcat.split(",");
            return actionArr;
        }
    }

    public void setUserIds(String[] userIds) {
        this.userIds = userIds;
    }

    public String getUseIdConcat() {
        return useIdConcat;
    }

    public void setUseIdConcat(String useIdConcat) {
        this.useIdConcat = useIdConcat;
    }
}

