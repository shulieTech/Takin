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

package io.shulie.tro.web.app.response.application;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.shulie.tro.web.app.response.BaseResponse;
import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/11/27 9:44 上午
 * @Description:
 */
@Data
public class ApplicationDsResponse extends BaseResponse {
    /**
     * 配置id
     */
    private Long id;

    /**
     * 应用id
     */
    private Long applicationId;

    /**
     * 存储类型，0：数据库 1：缓存
     */
    private DbTypeResponse dbType;

    /**
     * 方案类型，0：影子库 1：影子表 2:影子server
     */
    private DsTypeResponse dsType;

    /**
     * 服务器地址
     */
    private String url;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
