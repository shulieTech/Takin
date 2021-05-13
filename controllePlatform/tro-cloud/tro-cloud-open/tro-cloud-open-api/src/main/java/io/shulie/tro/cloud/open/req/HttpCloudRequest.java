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

package io.shulie.tro.cloud.open.req;

import java.io.Serializable;

import io.shulie.tro.common.beans.page.PagingDevice;
import lombok.Data;

/**
 * 放在这里的原因，PagingDevice需要支持
 *
 * @author hezhongqi
 */
@Data
public class HttpCloudRequest extends PagingDevice implements Serializable {

    private static final long serialVersionUID = -1529428936481160409L;

    private transient String license;

    //当前用户id
    private Long uid;

    //查询过滤sql
    private transient String filterSql;


}
