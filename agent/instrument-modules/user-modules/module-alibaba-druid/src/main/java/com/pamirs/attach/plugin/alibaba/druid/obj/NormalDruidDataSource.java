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

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.exception.PressureMeasureError;

import java.sql.SQLException;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/8/13 8:39 下午
 */
public class NormalDruidDataSource extends DruidDataSource {
    @Override
    public DruidPooledConnection getConnection(long maxWaitMillis) throws SQLException {
        if (Pradar.isClusterTest()) {
            throw new PressureMeasureError("DruidDataSource business datasource get a pressurement request.");
        }
        return super.getConnection(maxWaitMillis);
    }

    @Override
    public DruidPooledConnection getConnectionDirect(long maxWaitMillis) throws SQLException {
        if (Pradar.isClusterTest()) {
            throw new PressureMeasureError("DruidDataSource business datasource get a pressurement request.");
        }
        return super.getConnectionDirect(maxWaitMillis);
    }
}
