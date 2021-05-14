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

package io.shulie.tro.web.common.vo.application;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @Auther: vernon
 * @Date: 2020/7/7 10:40
 * @Description:
 */
@Data
public class ApplicationApiManageVO implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * api
     */
    private String api;
    /**
     * 应用主键
     */
    private Long applicationId;
    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;
    /**
     * 是否有效 0:有效;1:无效
     */
    private Byte isDeleted;

    private String requestMethod;

    private Boolean canEdit = true;

    private Boolean canRemove = true;

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }
}
