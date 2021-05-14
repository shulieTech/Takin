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

package io.shulie.tro.web.amdb.bean.query.application;

import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.amdb.bean.query.application
 * @date 2021/4/20 2:02 下午
 */
@Data
public class ApplicationInterfaceQueryDTO {
    private String appName;
    private String rpcType;
    private String serviceName;
    private String methodName;
    private String middlewareName;
    private String fieldNames;
    private Integer pageSize;
    private Integer currentPage;

}
