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

package io.shulie.tro.web.data.constant;

/**
 * 常量池
 *
 * @author liuchuan
 */
public interface DataConstants {

    /**
     * mybatis-plus 查询一条语句的时候, 加一个 limit 1
     * 防止查出多条
     */
    String LIMIT_ONE = "limit 1";

}