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

package com.pamirs.tro.entity.domain.vo;

/**
 * agent获取基础配置
 *
 * @author 298403
 * @date 2019-03-28
 */
public class AgentApplicationConfigVo {

    /**
     * sql检查异常 Y N
     */
    private String sqlCheck;

    /**
     * 防作弊检查异常 Y N
     */
    private String cheatCheck;

    public String getSqlCheck() {
        return sqlCheck;
    }

    public void setSqlCheck(String sqlCheck) {
        this.sqlCheck = sqlCheck;
    }

    public String getCheatCheck() {
        return cheatCheck;
    }

    public void setCheatCheck(String cheatCheck) {
        this.cheatCheck = cheatCheck;
    }

    @Override
    public String toString() {
        return "AgentApplicationConfigVo{" +
            "sqlCheck='" + sqlCheck + '\'' +
            ", cheatCheck='" + cheatCheck + '\'' +
            '}';
    }
}
