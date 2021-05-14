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

package com.pamirs.tro.entity.domain.query;

import com.pamirs.tro.common.constant.Constants;
import org.apache.commons.lang.StringUtils;

/**
 * @Author: 710524
 * @ClassName: ChaosCmdTemplateQuery
 * @package: com.pamirs.tro.entity.domain.query
 * @Date: 2019/5/9 0009 17:46
 * @Description: 模板命令查询
 */
public class ChaosCmdTemplateQuery extends QueryPage {

    private String name;

    private String keyword;

    private String commandTemplate;

    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = StringUtils.trimToNull(name);
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = StringUtils.trimToNull(keyword);
    }

    public String getCommandTemplate() {
        return commandTemplate;
    }

    public void setCommandTemplate(String commandTemplate) {
        this.commandTemplate = StringUtils.trimToNull(commandTemplate);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (Constants.Y.equalsIgnoreCase(status) || Constants.N.equalsIgnoreCase(status)) {
            this.status = status.toUpperCase();
        }
    }
}
