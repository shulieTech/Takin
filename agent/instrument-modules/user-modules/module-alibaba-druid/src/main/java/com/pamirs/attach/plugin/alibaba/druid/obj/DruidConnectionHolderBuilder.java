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
package com.pamirs.attach.plugin.alibaba.druid.obj;

import com.alibaba.druid.pool.DruidAbstractDataSource;
import com.alibaba.druid.pool.DruidConnectionHolder;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/3/8 8:47 下午
 */
public interface DruidConnectionHolderBuilder {

    /**
     * 获取连接的时间
     *
     * @return
     */
    long getConnectNanoSpan(DruidConnectionHolder holder);

    /**
     * 构建
     *
     * @param dataSource
     * @param conn
     * @param connectNanoSpan
     * @return
     * @throws SQLException
     */
    DruidConnectionHolder build(DruidAbstractDataSource dataSource, Connection conn, long connectNanoSpan) throws SQLException;
}
