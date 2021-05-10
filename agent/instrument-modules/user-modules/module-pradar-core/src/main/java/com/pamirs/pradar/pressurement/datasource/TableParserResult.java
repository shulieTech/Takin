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
package com.pamirs.pradar.pressurement.datasource;

import java.util.Collections;
import java.util.List;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/4/25 1:55 下午
 */
public class TableParserResult {
    public static final TableParserResult EMPTY = new TableParserResult(Collections.EMPTY_LIST, true);

    private List<String> tables;
    private boolean isQuery;

    public TableParserResult(List<String> tables, boolean isQuery) {
        this.tables = tables;
        this.isQuery = isQuery;
    }

    public List<String> getTables() {
        return tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    public boolean isQuery() {
        return isQuery;
    }

    public void setQuery(boolean query) {
        isQuery = query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TableParserResult sqlResult = (TableParserResult) o;

        if (isQuery != sqlResult.isQuery) {
            return false;
        }
        return tables != null ? tables.equals(sqlResult.tables) : sqlResult.tables == null;
    }

    @Override
    public int hashCode() {
        int result = tables != null ? tables.hashCode() : 0;
        result = 31 * result + (isQuery ? 1 : 0);
        return result;
    }
}
