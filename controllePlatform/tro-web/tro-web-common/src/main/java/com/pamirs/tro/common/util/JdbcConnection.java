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

package com.pamirs.tro.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import com.pamirs.tro.common.constant.DataSourceVerifyTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: fanxx
 * @Date: 2020/12/31 9:52 上午
 * @Description:
 */
public class JdbcConnection {
    private static final Logger logger = LoggerFactory.getLogger(JdbcConnection.class);

    public static Long fetchCurrentTime(String url, String username, String password, DataSourceVerifyTypeEnum dbType) {
        Connection conn = null;
        String noPwUrl = url.trim().startsWith("jdbc") ? url.split("\\?")[0] : url;
        if (Objects.isNull(dbType)) {
            throw new RuntimeException("不支持的检查数据源[" + noPwUrl + "][" + dbType + "]");
        }
        try {
            switch (dbType) {
                case MYSQL:
                    conn = generateConnection(url, username, password);
                    Statement statement = conn.createStatement();
                    statement.setQueryTimeout(30);
                    try (ResultSet resultSet = statement.executeQuery("SELECT now()")) {
                        Long currentTime = resultSet.next() ? resultSet.getTimestamp(1).getTime() : null;
                        resultSet.close();
                        statement.close();
                        return currentTime;
                    }
                default:
                    throw new RuntimeException("不支持的检查数据源[" + noPwUrl + "][" + dbType + "]");
            }
        } catch (Exception ex1) {
            throw new RuntimeException("连接数据库[" + noPwUrl + "]失败，" + ex1.getMessage(), ex1);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("error:", e);
                }
            }
        }
    }

    public static String findClassNameForCheckDb(String jdbcUrl) {
        String trim = jdbcUrl.trim();
        if (trim.startsWith("jdbc:mysql")) {
            return "com.mysql.jdbc.Driver";
        } else if (trim.startsWith("jdbc:oracle")) {
            return "oracle.jdbc.OracleDriver";
        } else if (trim.startsWith("jdbc:sqlserver")) {
            return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        } else if (trim.startsWith("jdbc:postgresql")) {
            return "org.postgresql.Driver";
        } else {
            return "";
        }
    }

    public static Connection generateConnection(String jdbcUrl, String username, String password) throws ClassNotFoundException, SQLException {
        if (!jdbcUrl.contains("mysql")) {
            throw new RuntimeException("不支持的 JDBC:" + jdbcUrl);
        }
        JdbcConnection.class.getClassLoader().loadClass(findClassNameForCheckDb(jdbcUrl));
        DriverManager.setLoginTimeout(20);
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    public static void main(String[] args) {
        System.out.println(fetchCurrentTime("jdbc:mysql://114.55.42.181:3306/taco_app", "canal1", "canal", DataSourceVerifyTypeEnum.MYSQL));
    }
}
