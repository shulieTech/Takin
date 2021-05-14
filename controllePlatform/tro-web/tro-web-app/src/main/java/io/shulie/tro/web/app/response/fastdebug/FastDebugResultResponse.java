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

package io.shulie.tro.web.app.response.fastdebug;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.response.fastdebug
 * @date 2020/12/28 11:13 上午
 */
@Data
public class FastDebugResultResponse {
    @ApiModelProperty("id")
    private Long id;

    /**
     * 调试名称
     */
    @ApiModelProperty("调试名称")
    private String name;

    /**
     * 业务活动name,组装体
     */
    @ApiModelProperty("业务活动组装体")
    private String businessLinkName;

    /**
     * 请求url
     */
    @ApiModelProperty("请求url")
    private String requestUrl;

    /**
     * 请求方式
     */
    @ApiModelProperty("请求方式")
    private String httpMethod;

    /**
     * 创建时间
     */
    @ApiModelProperty("调试时间")
    private String gmtCreate;
    /**
     * 操作人
     */
    @ApiModelProperty("调试人")
    private String creatorName;

    /**
     * 0:失败；1：成功；调试中根据config中status判断
     */
    @ApiModelProperty("调试结果")
    private String debugResult;

}
