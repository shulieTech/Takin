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
package com.pamirs.attach.plugin.common.datasource.trace.impl;

import com.pamirs.attach.plugin.common.datasource.trace.JdbcPradar;
import com.pamirs.attach.plugin.common.datasource.trace.SqlTraceMetaData;
import com.pamirs.pradar.MiddlewareType;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.ResultCode;

/**
 * @author fabing.zhaofb
 */
public class JdbcPradarImpl implements JdbcPradar {

    @Override
    public void startRpc(SqlTraceMetaData sqlMetaData, String sql) {
        if (sqlMetaData == null) {
            return;
        }
        try {
            if (sql != null) {
                sqlMetaData.setSql(sql);
            }
            startRpc(sqlMetaData);
        } catch (Throwable e) {
            //ignore
        }
    }

    private void startRpc(SqlTraceMetaData sqlMetaData) {
        Pradar.startClientInvoke(sqlMetaData.getUrl(), sqlMetaData.getTableNames());
        Pradar.middlewareName(sqlMetaData.getDbType());
        Pradar.remoteIp(sqlMetaData.getHost());
        Object request = sqlMetaData.getParameters();
        if (!Pradar.isRequestOn()) {
            request = null;
        }
        Pradar.request(request);
        try {
            Pradar.remotePort(Integer.valueOf(sqlMetaData.getPort()));
        } catch (NumberFormatException e) {
        }
    }


    @Override
    public void endRpc(SqlTraceMetaData sqlMetaData, Object result) {
        try {
            if (!Pradar.isResponseOn()) {
                result = null;
            }
            Pradar.response(result);
            Pradar.callBack(sqlMetaData.getSql());
            if (result != null && result instanceof Exception) {
                Pradar.endClientInvoke(ResultCode.INVOKE_RESULT_FAILED,
                        MiddlewareType.TYPE_DB);
            } else {
                Pradar.endClientInvoke(ResultCode.INVOKE_RESULT_SUCCESS,
                        MiddlewareType.TYPE_DB);
            }
        } catch (Throwable e1) {
            //ignore
        }
    }
}
