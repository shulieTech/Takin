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
package com.pamirs.attach.plugin.common.datasource.utils;

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.pressurement.datasource.SqlParser;
import com.pamirs.pradar.pressurement.datasource.TableParserResult;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/9/11 7:25 下午
 */
public class SqlUtils {

    /**
     * 检查普通的连接访问是否正常
     * 如果当前是压测流量，则必须访问的所有的表是压测表
     * 如果当前是非压测流量，则必须访问的所有的表为业务表
     *
     * @param dbType @see com.pamirs.pradar.maxplanck.module.db.shared.datasource.util.DbType
     * @param sql
     */
    public static void checkNormalConnectionAccessValid(String dbType, String sql) {
        if (Pradar.isClusterTest()) {
            String table = getIllegalShadowTable(dbType, sql);
            if (table != null) {
                throw new PressureMeasureError("pressure request can't access business table:" + table);
            }
        } else {
            String table = getIllegalBizTable(dbType, sql);
            if (table != null) {
                throw new PressureMeasureError("business request can't access shadow table:" + table);
            }
        }
    }

    /**
     * 获取非法的影子表
     * 即表名是非压测标记开头的
     *
     * @param dbType
     * @param sql
     * @return
     */
    public static String getIllegalShadowTable(String dbType, String sql) {
        TableParserResult sqlResult = SqlParser.getTables(sql, dbType);
        if (sqlResult.getTables().isEmpty()) {
            return null;
        }

        /**
         * 如果是查询直接放行
         */
        if (sqlResult.isQuery()) {
            return null;
        }

        for (String table : sqlResult.getTables()) {
            if (!Pradar.isClusterTestPrefix(table)) {
                return table;
            }
        }
        return null;
    }

    /**
     * 获取非法的业务表
     * 即表名是压测标记开头的
     *
     * @param dbType
     * @param sql
     * @return
     */
    public static String getIllegalBizTable(String dbType, String sql) {
        TableParserResult sqlResult = SqlParser.getTables(sql, dbType);
        if (sqlResult.getTables().isEmpty()) {
            return null;
        }

        for (String table : sqlResult.getTables()) {
            if (Pradar.isClusterTestPrefix(table)) {
                return table;
            }
        }
        return null;
    }

}
