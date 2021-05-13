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

package io.shulie.tro.web.data.param.fastdebug;

import java.util.Date;

import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.data.param.fastdebug
 * @date 2020/12/30 3:41 下午
 */
@Data
public class FastDebugResultUpdateParam {
    private Long id;
    private Long configId;
    private Long businessLinkId;
    private String errorMessage;
    private String leakageCheckData;
    private String traceId;
    /**
     * 请求体，包含url,body,header
     */
    private String request;


    /**
     * 请求返回体
     */
    private String response;
    /**
     * 0:失败；1：成功；调试中根据config中status判断
     */
    private Boolean debugResult;

    /**
     * 调用时长
     */
    private Long callTime;

    /**
     * 请求失败
     */
    private String error;


    /**
     * 更新时间
     */
    private Date gmtModified;

    public FastDebugResultUpdateParam() {
        this.gmtModified = new Date();
    }
}
