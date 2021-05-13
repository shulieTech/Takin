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

import java.io.StringReader;
import java.util.List;
import java.util.Objects;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: fanxx
 * @Date: 2021/1/4 2:28 下午
 * @Description:
 */
public class SqlParseUtil {
    private static final Logger logger = LoggerFactory.getLogger(SqlParseUtil.class);

    public static Boolean checkIsSelect(String sql) {
        CCJSqlParserManager pm = new CCJSqlParserManager();
        try {
            Statement statement = pm.parse(new StringReader(sql));
            if (statement instanceof Select) {
                return Boolean.TRUE;
            }
        } catch (JSQLParserException e) {
            logger.error("SqlParseUtil Error:", e);
        }
        return Boolean.FALSE;
    }

    public static Boolean checkIsPtTable(String sql) {
        CCJSqlParserManager pm = new CCJSqlParserManager();
        try {
            Statement statement = pm.parse(new StringReader(sql));
            if (statement instanceof Select) {
                Select selectStatement = (Select)statement;
                TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
                List tableList = tablesNamesFinder.getTableList(selectStatement);
                if (CollectionUtils.isNotEmpty(tableList)) {
                    String table = (String)tableList.get(0);
                    table = table.toUpperCase();
                    if (table.startsWith("PT_")) {
                        return Boolean.TRUE;
                    }
                }
            }
        } catch (JSQLParserException e) {
            logger.error("SqlParseUtil Error:", e);
        }
        return Boolean.FALSE;
    }

    public static Boolean checkHasLimit(String sql) {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        try {
            Select select = (Select)parserManager.parse(new StringReader(sql));
            PlainSelect plain = (PlainSelect)select.getSelectBody();
            Limit limit = plain.getLimit();
            if (!Objects.isNull(limit) && limit.getRowCount() instanceof LongValue) {
                LongValue longValue = (LongValue)limit.getRowCount();
                if (Long.valueOf(1L).equals(longValue.getValue())) {
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            }
        } catch (JSQLParserException e) {
            logger.error("SqlParseUtil Error:", e);
        }
        return Boolean.FALSE;
    }

    public static String parseAndGetTableName(String sql) {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        try {
            Select select = (Select)parserManager.parse(new StringReader(sql));
            PlainSelect plain = (PlainSelect)select.getSelectBody();
            Table table = (Table)plain.getFromItem();
            return table.getName();
        } catch (JSQLParserException e) {
            logger.error("SqlParseUtil Error:", e);
        }
        return "";
    }
}
