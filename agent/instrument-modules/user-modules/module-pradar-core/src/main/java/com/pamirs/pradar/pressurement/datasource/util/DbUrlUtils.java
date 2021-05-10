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
package com.pamirs.pradar.pressurement.datasource.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Create by xuyh at 2020/3/25 14:01.
 */
public class DbUrlUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(DbUrlUtils.class.getName());

    public static final String JNDI_MODE_PREFIX = "jndi://";

    public static String getKey(String url, String username) {
        int index = StringUtils.indexOf(url, '?');
        if (index != -1) {
            url = url.substring(0, url.indexOf('?'));
        }

        if (StringUtils.isNotBlank(username)) {
            url = url + '|' + username;
        }
        return url;
    }

    public static String parseDataSourceKey(String url) {
        if (url.startsWith(JNDI_MODE_PREFIX)) {
            return url;
        }
        DbType dbType = DbType.guessDbType(url);
        String host, port, dbUpper = null;
        if (dbType != null) {
            try {
                SqlMetaData sqlMetaData = dbType.sqlMetaData(url);
                host = sqlMetaData.getHost();
                port = sqlMetaData.getPort();
                if (StringUtils.isNotBlank(sqlMetaData.getDbName())) {
                    dbUpper = sqlMetaData.getDbName().toUpperCase();
                }
            } catch (Throwable e) {
                throw new RuntimeException("Db url parse failed.");
            }
        } else {
            host = getHostFromDbUrl(url);
            port = getPortFromDbUrl(url);
            dbUpper = getDbNameUpperFromDbUrl(url);
        }
        if (StringUtils.isNotBlank(dbUpper)) {
            return host + ":" + port + "|" + dbUpper;
        } else {
            return host + ":" + port;
        }
    }


    @SuppressWarnings("unchecked")
    public static void parseDataSourceSchemaPair(Map<String, Object> dataItem, Map<String, String> schemaMap) {
        Map<String, Object> dataSourceBusiness = null, dataSourcePerformanceTest = null;
        try {
            Map<String, Object> shadowDbConfig = (Map<String, Object>) dataItem.get("shadowDbConfig");
            Map<String, Object> datasourceMediator = (Map<String, Object>) shadowDbConfig.get("datasourceMediator");
            List<Map<String, Object>> dataSources = (List<Map<String, Object>>) shadowDbConfig.get("dataSources");
            String dataSourceBusinessId = String.valueOf(datasourceMediator.get("dataSourceBusiness"));
            String dataSourcePerformanceTestId = String.valueOf(datasourceMediator.get("dataSourcePerformanceTest"));
            for (Map<String, Object> dataSource : dataSources) {
                String id = String.valueOf(dataSource.get("id"));
                if (dataSourceBusinessId.equalsIgnoreCase(id)) {
                    dataSourceBusiness = dataSource;
                }
                if (dataSourcePerformanceTestId.equalsIgnoreCase(id)) {
                    dataSourcePerformanceTest = dataSource;
                }
            }
        } catch (Throwable e) {
            LOGGER.error("", e);
        }
        if (dataSourceBusiness != null && dataSourcePerformanceTest != null) {
            String schemaBusiness = String.valueOf(dataSourceBusiness.get("schema"));
            String schemaPerformanceTest = String.valueOf(dataSourcePerformanceTest.get("schema"));
            if (schemaBusiness == null || schemaBusiness.isEmpty() || schemaBusiness.equalsIgnoreCase("null")
                    || schemaPerformanceTest == null || schemaPerformanceTest.isEmpty() || schemaPerformanceTest.equalsIgnoreCase("null")) {
                String urlBusiness = String.valueOf(dataSourceBusiness.get("url"));
                String urlPerformanceTest = String.valueOf(dataSourcePerformanceTest.get("url"));
                if (urlBusiness == null || urlBusiness.isEmpty() || urlBusiness.equalsIgnoreCase("null")
                        || urlPerformanceTest == null || urlPerformanceTest.isEmpty() || urlPerformanceTest.equalsIgnoreCase("null")) {
                    return;
                }
                DbType dbTypeBusiness = DbType.guessDbType(urlBusiness);
                DbType dbTypePerformanceTest = DbType.guessDbType(urlPerformanceTest);
                if (dbTypeBusiness == null || dbTypePerformanceTest == null) {
                    return;
                }
                SqlMetaData sqlMetaDataBusiness = dbTypeBusiness.sqlMetaData(urlBusiness);
                SqlMetaData sqlMetaDataPerformanceTest = dbTypePerformanceTest.sqlMetaData(urlPerformanceTest);
                if (sqlMetaDataBusiness == null || sqlMetaDataPerformanceTest == null) {
                    return;
                }
                schemaBusiness = sqlMetaDataBusiness.getDbName();
                schemaPerformanceTest = sqlMetaDataPerformanceTest.getDbName();
            }
            if (schemaBusiness != null && !schemaBusiness.isEmpty() && !schemaBusiness.equalsIgnoreCase("null")
                    && schemaPerformanceTest != null && !schemaPerformanceTest.isEmpty() && !schemaPerformanceTest.equalsIgnoreCase("null")) {
                schemaMap.put(schemaBusiness, schemaPerformanceTest);
            }
        }
    }

    //jdbc:mysql://mysql67.akctest1.com/mshop_member
    //jdbc:oracle:thin:@114.55.42.181:1521/esshop
    public static String getHostFromDbUrl(String url) {
        //TODO 对不同的jdbc Url格式进行host截取
        if (url.contains("//")) {
            url = url.substring(url.indexOf("//") + 2, url.length());
            if (url.contains(":")) {
                url = url.substring(0, url.lastIndexOf(":"));
            } else {
                url = url.substring(0, url.indexOf("/"));
            }
            return url;
        } else if (url.contains("@")) {
            url = url.substring(url.indexOf("@") + 1, url.length());
            if (url.contains(":")) {
                url = url.substring(0, url.lastIndexOf(":"));
            } else {
                url = url.substring(0, url.indexOf("/"));
            }
            return url;
        } else {
            return url.substring(url.indexOf("//") + 2, url.lastIndexOf(":"));
        }
    }

    public static String getPortFromDbUrl(String url) {
        if (url.contains("//")) {
            url = url.substring(url.indexOf("//") + 2, url.length());
        } else if (url.contains("@")) {
            url = url.substring(url.indexOf("@") + 1, url.length());
        }
        if (url.contains(":")) {
            String port = url.substring(url.lastIndexOf(":") + 1, url.length());
            if (port.contains("/")) {
                port = port.substring(0, port.indexOf("/"));
            }
            return port;
        } else {
            return "UNKNOWN";
        }
    }

    public static String getDbNameUpperFromDbUrl(String url) {
        String db = url.substring(url.lastIndexOf("/") + 1, url.length());
        if (db.contains("?")) {
            db = db.substring(0, db.indexOf("?"));
        }
        return db.toUpperCase();
    }
}
