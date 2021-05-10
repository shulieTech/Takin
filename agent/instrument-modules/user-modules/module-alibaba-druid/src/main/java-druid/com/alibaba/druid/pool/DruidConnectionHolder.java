/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.druid.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/3/8 8:54 下午
 */
public class DruidConnectionHolder {
    public DruidConnectionHolder(DruidAbstractDataSource dataSource, Connection conn) throws SQLException {

    }

    public DruidConnectionHolder(DruidAbstractDataSource dataSource, Connection conn, long connectNanoSpan) throws SQLException {

    }

    public DruidConnectionHolder(DruidAbstractDataSource dataSource, Connection conn, long connectNanoSpan, Map<String, Object> variables, Map<String, Object> globalVariables) throws SQLException {

    }

    public long getCreateNanoSpan() {
        return 0;
    }
}
