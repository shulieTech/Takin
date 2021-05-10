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
package com.pamirs.attach.plugin.alibaba.druid.obj.impl;

import com.alibaba.druid.pool.DruidAbstractDataSource;
import com.alibaba.druid.pool.DruidConnectionHolder;
import com.pamirs.attach.plugin.alibaba.druid.obj.DruidConnectionHolderBuilder;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/3/8 8:49 下午
 */
public class DruidConnectionHolder106Newer implements DruidConnectionHolderBuilder {

    @Override
    public long getConnectNanoSpan(DruidConnectionHolder holder) {
        return holder.getCreateNanoSpan();
    }

    @Override
    public DruidConnectionHolder build(DruidAbstractDataSource dataSource, Connection conn, long connectNanoSpan) throws SQLException {
        return new DruidConnectionHolder(dataSource, conn, connectNanoSpan);
    }
}
