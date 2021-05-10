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
package com.pamirs.attach.plugin.common.datasource.trace;


import com.pamirs.attach.plugin.common.datasource.trace.impl.JdbcPradarImpl;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PradarHelper {
    private final static Logger LOGGER = LoggerFactory.getLogger(PradarHelper.class.getName());

    static JdbcPradar pradar = new JdbcPradarImpl();


    /**
     * execute之前写日志
     */
    public static boolean startRpc(SqlTraceMetaData metaData) {
        return startRpc(metaData, null);
    }

    /**
     * execute之前写日志
     */
    public static boolean startRpc(SqlTraceMetaData metaData, String sql) {
        try {
            if (sql != null && metaData != null) {
                metaData.setSql(sql);
            }
            if (metaData != null && metaData.getSql() != null) {
                String lowerCaseSql = StringUtils.trim(StringUtils.lowerCase(metaData.getSql()));
                if ("select 1".equals(lowerCaseSql) || "select 'x'".equals(lowerCaseSql)) {
                    return true;
                }
                if ("select 1 from dual".equals(lowerCaseSql) || "select 'x' from dual".equals(lowerCaseSql)) {
                    return true;
                }
            }

            pradar.startRpc(metaData, sql);
            return true;
        } catch (Throwable e) {
            LOGGER.error("jdbc start rpc err!SqlMetadata:{} sql:{}", metaData, sql, e);
        }
        return false;
    }

    /**
     * @param result
     */
    public static boolean endRpc(SqlTraceMetaData sqlMetaData, Object result) {
        try {
            if (sqlMetaData != null && sqlMetaData.getSql() != null) {
                String lowerCaseSql = StringUtils.trim(StringUtils.lowerCase(sqlMetaData.getSql()));
                if ("select 1".equals(lowerCaseSql) || "select 'x'".equals(lowerCaseSql)) {
                    return true;
                }
                if ("select 1 from dual".equals(lowerCaseSql) || "select 'x' from dual".equals(lowerCaseSql)) {
                    return true;
                }
            }
            pradar.endRpc(sqlMetaData, result);
            return true;
        } catch (Throwable e) {
            LOGGER.error("jdbc end rpc err!SqlMetadata:{} result:{}", sqlMetaData, result, e);
        }
        return false;
    }

}

