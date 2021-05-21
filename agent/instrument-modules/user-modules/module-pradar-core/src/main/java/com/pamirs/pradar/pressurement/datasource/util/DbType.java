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

import com.pamirs.pradar.PradarCoreUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fabing.zhaofb
 */
public enum DbType {
    MYSQL("jdbc:mysql://", "jdbc:mysql:loadbalance://", "jdbc:mysql:replication://") {
        @Override
        public SqlMetaData readMetaData(String url) {
            String mainUrl = getMainUrl(url);
            String dbName = mainUrl.substring(mainUrl.lastIndexOf('/') + 1);
            mainUrl = mainUrl.substring(0, mainUrl.lastIndexOf('/'));
            try {
                String host = null;
                if (mainUrl.indexOf(':') != -1) {
                    host = mainUrl.substring(0, mainUrl.indexOf(':'));
                    mainUrl = mainUrl.substring(mainUrl.indexOf(':') + 1);
                } else {
                    host = mainUrl;
                }
                int port = 3306;
                if (null != mainUrl && !"".equals(mainUrl)) {
                    try {
                        port = Integer.valueOf(mainUrl);
                    } catch (NumberFormatException e) {
                    }
                }
                SqlMetaData sqlMetaData = new SqlMetaData();
                sqlMetaData.setDbType(DbType.MYSQL);
                sqlMetaData.setHost(host);
                sqlMetaData.setPort(String.valueOf(port));
                sqlMetaData.setDbName(StringUtils.lowerCase(dbName));
                sqlMetaData.setUrl(url);
                return sqlMetaData;
            } catch (Throwable e) {
                return null;
            }
        }
    },
    ORACLE("jdbc:oracle:thin:@", "jdbc:oracle:thin:@//") {
        @Override
        public SqlMetaData readMetaData(String url) {
            String mainUrl = getMainUrl(url);
            SqlMetaData sqlMetaData = null;
            if (mainUrl.charAt(0) == '(') {
                sqlMetaData = parseServiceConfigOracleUrl(mainUrl);
            } else {
                sqlMetaData = parseCommonOracleUrl(mainUrl);
            }
            if (sqlMetaData != null) {
                sqlMetaData.setUrl(url);
            }
            return sqlMetaData;
        }

        private SqlMetaData parseServiceConfigOracleUrl(String mainUrl) {
            Map<String, String> keyValues = parseKeyValues(mainUrl);
            SqlMetaData sqlMetaData = new SqlMetaData();
            sqlMetaData.setDbType(DbType.ORACLE);
            sqlMetaData.setHost(StringUtils.trim(keyValues.get("host")));
            sqlMetaData.setPort(StringUtils.trim(keyValues.get("port")));
            sqlMetaData.setDbName(StringUtils.trim(keyValues.get("service_name")));
            return sqlMetaData;
        }

        private Map<String, String> parseKeyValues(String str) {
            Map<String, String> keyValues = new HashMap<String, String>();
            int lastLeft = -1;
            for (int i = 0, len = str.length(); i < len; i++) {
                if (str.charAt(i) == '(') {
                    lastLeft = i;
                } else if (str.charAt(i) == ')' && lastLeft != -1) {
                    String keyValueStr = str.substring(lastLeft + 1, i);
                    if (keyValueStr.indexOf('=') != -1) {
                        String[] pair = StringUtils.split(keyValueStr, '=');
                        if (pair.length == 2) {
                            keyValues.put(StringUtils.trim(StringUtils.lowerCase(pair[0])), StringUtils.trim(pair[1]));
                        }
                        lastLeft = -1;
                    }
                }
            }
            return keyValues;
        }

        private SqlMetaData parseCommonOracleUrl(String mainUrl) {
            int last1 = mainUrl.lastIndexOf('/');
            int last2 = mainUrl.lastIndexOf(':');
            int lastIndex = Math.max(last1, last2);
            String dbName = mainUrl.substring(lastIndex + 1);
            mainUrl = mainUrl.substring(0, lastIndex);
            try {
                String host = null;
                if (mainUrl.indexOf(":") != -1) {
                    host = mainUrl.substring(0, mainUrl.indexOf(':'));
                    mainUrl = mainUrl.substring(mainUrl.indexOf(':') + 1);
                } else if (mainUrl.indexOf("/") != -1) {
                    host = mainUrl.substring(0, mainUrl.indexOf('/'));
                    mainUrl = mainUrl.substring(mainUrl.indexOf('/') + 1);
                } else if (mainUrl.indexOf('\\') != -1) {
                    host = mainUrl.substring(0, mainUrl.indexOf('\\'));
                    mainUrl = mainUrl.substring(mainUrl.indexOf('\\') + 1);
                } else {
                    host = mainUrl;
                }
                while (host.startsWith("/")) {
                    host = host.substring(1);
                }

                int port = 1421;
                try {
                    port = Integer.valueOf(mainUrl);
                } catch (NumberFormatException e) {
                }
                SqlMetaData sqlMetaData = new SqlMetaData();
                sqlMetaData.setDbType(DbType.ORACLE);
                sqlMetaData.setHost(host);
                sqlMetaData.setPort(String.valueOf(port));
                sqlMetaData.setDbName(StringUtils.lowerCase(dbName));
                return sqlMetaData;
            } catch (Throwable e) {
                return null;
            }
        }
    },
    SQLSERVER("jdbc:microsoft:sqlserver://", "jdbc:sqlserver://", "jdbc:jtds:sqlserver://") {
        @Override
        public SqlMetaData readMetaData(String url) {
            String mainUrl = getMainUrl(url);
            try {
                String host = mainUrl.substring(0, mainUrl.indexOf(':'));
                mainUrl = mainUrl.substring(mainUrl.indexOf(':') + 1);
                String port = "1433";
                if (mainUrl.charAt(0) != ';' && mainUrl.indexOf(';') != -1) {
                    port = mainUrl.substring(0, mainUrl.indexOf(';'));
                    mainUrl = mainUrl.substring(mainUrl.indexOf(';') + 1);
                } else if (mainUrl.contains("/")) {
                    port = mainUrl.substring(0, mainUrl.indexOf("/"));
                    mainUrl = mainUrl.substring(mainUrl.indexOf("/") + 1);
                }

                String[] parameters = StringUtils.split(mainUrl, ';');
                if (ArrayUtils.isEmpty(parameters)) {
                    return null;
                }
                Map<String, String> parameterMap = new HashMap<String, String>();
                for (String parameter : parameters) {
                    if (StringUtils.isBlank(parameter) || parameter.indexOf('=') == -1) {
                        continue;
                    }
                    String[] pair = StringUtils.split(parameter, '=');
                    if (pair.length != 2) {
                        continue;
                    }
                    parameterMap.put(StringUtils.trim(pair[0]), StringUtils.trim(pair[1]));
                }
                String dbName = null;
                if (parameterMap.size() > 0 && parameterMap.containsKey("DatabaseName")) {
                    dbName = parameterMap.get("DatabaseName");
                } else if (parameterMap.containsKey("instanceName")) {
                    dbName = parameterMap.get("instanceName");
                } else if (!parameterMap.containsKey("instanceName")) {
                    dbName = parameters[0];
                }

                SqlMetaData sqlMetaData = new SqlMetaData();
                sqlMetaData.setDbType(DbType.SQLSERVER);
                sqlMetaData.setHost(host);
                sqlMetaData.setPort(port);
                sqlMetaData.setDbName(org.apache.commons.lang.StringUtils.lowerCase(dbName));
                sqlMetaData.setUrl(url);
                return sqlMetaData;
            } catch (Throwable e) {
                return null;
            }
        }
    },
    DB2("jdbc:db2://") {
        @Override
        public SqlMetaData readMetaData(String url) {
            String mainUrl = getMainUrl(url);
            try {
                String host = null;
                String port = "50000";
                if (mainUrl.indexOf(':') != -1) {
                    host = mainUrl.substring(0, mainUrl.indexOf(':'));
                    mainUrl = mainUrl.substring(mainUrl.indexOf(':') + 1);
                } else {
                    host = "";// PradarCoreUtils.getLocalAddress();
                    String dbName = mainUrl;
                    SqlMetaData sqlMetaData = new SqlMetaData();
                    sqlMetaData.setDbType(DbType.DB2);
                    sqlMetaData.setHost(host);
                    sqlMetaData.setPort(port);
                    sqlMetaData.setDbName(StringUtils.lowerCase(dbName));
                    return sqlMetaData;
                }

                port = mainUrl.substring(0, mainUrl.indexOf('/'));
                String dbName = mainUrl.substring(mainUrl.indexOf('/') + 1);
                SqlMetaData sqlMetaData = new SqlMetaData();
                sqlMetaData.setDbType(DbType.DB2);
                sqlMetaData.setHost(host);
                sqlMetaData.setPort(port);
                sqlMetaData.setDbName(StringUtils.lowerCase(dbName));
                sqlMetaData.setUrl(url);
                return sqlMetaData;
            } catch (Throwable e) {
                return null;
            }
        }
    },
    POSTGRESQL("jdbc:postgresql://") {
        @Override
        SqlMetaData readMetaData(String url) {
            String mainUrl = getMainUrl(url);
            try {
                String host = null;
                String port = "5432";
                if (mainUrl.indexOf(':') != -1) {
                    host = mainUrl.substring(0, mainUrl.indexOf(':'));
                    mainUrl = mainUrl.substring(mainUrl.indexOf(':') + 1);
                } else {
                    host = PradarCoreUtils.getLocalAddress();
                    String dbName = mainUrl;
                    SqlMetaData sqlMetaData = new SqlMetaData();
                    sqlMetaData.setDbType(DbType.POSTGRESQL);
                    sqlMetaData.setHost(host);
                    sqlMetaData.setPort(port);
                    sqlMetaData.setDbName(org.apache.commons.lang.StringUtils.lowerCase(dbName));
                    return sqlMetaData;
                }

                port = mainUrl.substring(0, mainUrl.indexOf('/'));
                String dbName = mainUrl.substring(mainUrl.indexOf('/') + 1);
                SqlMetaData sqlMetaData = new SqlMetaData();
                sqlMetaData.setDbType(DbType.POSTGRESQL);
                sqlMetaData.setHost(host);
                sqlMetaData.setPort(port);
                sqlMetaData.setDbName(org.apache.commons.lang.StringUtils.lowerCase(dbName));
                sqlMetaData.setUrl(url);
                return sqlMetaData;
            } catch (Throwable e) {
                return null;
            }
        }
    },
    HIVE("jdbc:hive2://") {
        @Override
        SqlMetaData readMetaData(String url) {
            String mainUrl = getMainUrl(url);
            try {
                String host = null;
                String port = "10000";
                if (mainUrl.indexOf(':') != -1) {
                    host = mainUrl.substring(0, mainUrl.indexOf(':'));
                    mainUrl = mainUrl.substring(mainUrl.indexOf(':') + 1);
                } else {
                    host = "127.0.0.1";
                    String dbName = mainUrl;
                    SqlMetaData sqlMetaData = new SqlMetaData();
                    sqlMetaData.setDbType(DbType.HIVE);
                    sqlMetaData.setHost(host);
                    sqlMetaData.setPort(port);
                    sqlMetaData.setDbName(org.apache.commons.lang.StringUtils.lowerCase(dbName));
                    return sqlMetaData;
                }

                port = mainUrl.substring(0, mainUrl.indexOf('/'));
                String dbName = mainUrl.substring(mainUrl.indexOf('/') + 1);
                SqlMetaData sqlMetaData = new SqlMetaData();
                sqlMetaData.setDbType(DbType.HIVE);
                sqlMetaData.setHost(host);
                sqlMetaData.setPort(port);
                sqlMetaData.setDbName(org.apache.commons.lang.StringUtils.lowerCase(dbName));
                sqlMetaData.setUrl(url);
                return sqlMetaData;
            } catch (Throwable e) {
                return null;
            }
        }
    },
    NEO4J("http://") {
        @Override
        public SqlMetaData readMetaData(String url) {
            try {
                String ip = url.substring(url.indexOf(":") + 3, url.lastIndexOf(":"));
                String port = url.substring(url.lastIndexOf(":") + 1);
                if (StringUtils.isNotBlank(port) && port.contains("/")) {
                    port = port.substring(0, port.indexOf("/"));
                }
                SqlMetaData sqlMetaData = new SqlMetaData();
                sqlMetaData.setDbType(DbType.NEO4J);
                sqlMetaData.setHost(ip);
                sqlMetaData.setPort(port);
                sqlMetaData.setDbName(DbType.NEO4J.name());
                sqlMetaData.setUrl(url);
                return sqlMetaData;
            } catch (Throwable e) {
                return null;
            }
        }
    },
    MONGODB("mongodb:") {
        @Override
        public SqlMetaData readMetaData(String url) {
            String s[] = StringUtils.split(url.split("//")[1], '/');
            try {
                SqlMetaData sqlMetaData = new SqlMetaData();


                String[] connectionInfo = StringUtils.split(s[0], '@');
                String[] addressInfo = StringUtils.split(connectionInfo[1], ',');

                sqlMetaData.setDbType(DbType.MONGODB);

                String[] arr = StringUtils.split(addressInfo[0], ':');
                sqlMetaData.setHost(arr[0]);
                sqlMetaData.setPort(arr[1]);
                sqlMetaData.setDbName(null);
                sqlMetaData.setUrl(addressInfo[0]);
                return sqlMetaData;
            } catch (Throwable e) {
                return null;
            }
        }
    };


    protected String[] prefixs;

    DbType(String... prefixs) {
        if (ArrayUtils.isEmpty(prefixs)) {
            this.prefixs = new String[0];
        } else {
            this.prefixs = prefixs;
        }
    }

    abstract SqlMetaData readMetaData(String url);

    public SqlMetaData sqlMetaData(String url) {
        SqlMetaData sqlMetaData = null;
        try {
            sqlMetaData = readMetaData(url);
            if (sqlMetaData != null) {
                sqlMetaData.setDbType(this);
            }
            if (sqlMetaData == null) {
                System.out.println("sqlmetadata is null null null:" + url);
            } else {
                sqlMetaData.setUrl(url);
            }
        } catch (Throwable e) {
            //ignore;
        }
        return sqlMetaData;
    }

    public boolean isThis(String url) {
        for (String prefix : prefixs) {
            if (StringUtils.startsWith(url, prefix)) {
                return true;
            }
        }
        return false;
    }

    public String getMainUrl(String url) {
        String simpleURL = getSimpleURL(url);
        return cutParameters(simpleURL);
    }

    private String cutParameters(String url) {
        if (url.indexOf('?') != -1) {
            return url.substring(0, url.indexOf('?'));
        }
        return url;
    }

    public String getSimpleURL(String url) {
        for (String prefix : prefixs) {
            if (StringUtils.startsWith(url, prefix)) {
                url = url.substring(prefix.length());
            }
        }
        return url;
    }

    public static DbType guessDbType(String url) {
        for (DbType dbType : DbType.values()) {
            if (dbType.isThis(url)) {
                return dbType;
            }
        }
        return null;
    }

    public static DbType nameOf(String name) {
        for (DbType dbType : DbType.values()) {
            if (StringUtils.equalsIgnoreCase(dbType.name(), name)) {
                return dbType;
            }
        }
        return null;
    }
}

