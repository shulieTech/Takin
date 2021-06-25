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

package io.shulie.tro.web.app.output.middleware;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.request.middleware
 * @date 2021/2/24 5:08 下午
 */
@Data
public class AppMiddlewareScanOutput {
    /**
     * 主键
     */
    private Long id;

    /**
     * 应用名
     */
    private String appName;

    /**
     * 中间件类型，1. web容器，2. web服务器，3. 消息队列，4. 远程调用，5. 数据源，6. 连接池，7. ESB
     ，8. 缓存，9. 缓存中间件，10. NoSql，11. 文件存储，12. job
     */
    private String middlewareType;

    /**
     * jar包名称
     */
    private String jarName;

    /**
     * 1. 未录入，2. 无需支持，3. 未支持，4. 已支持
     */
    private String statusDesc;

    /**
     * 中间件名称
     */
    private String middlewareName;



    /**
     * 中间件支持版本列表
     */
    private List<String> middlewareVersions;

    /**
     * 创建时间
     */
    private String gmtCreate;

    /**
     * 更新时间
     */
    private String gmtModified;

    /**
     * 软删
     */
    private Boolean isDeleted;

    /**
     * 租户id
     */
    private Long customerId;

    /**
     * 创建人
     */
    private Long creatorId;

    /**
     * 修改人
     */
    private Long modifierId;
}
