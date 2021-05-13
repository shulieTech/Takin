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
 * @ClassName: ChaosHostQuery
 * @package: com.pamirs.tro.entity.domain.query
 * @Date: 2019/5/9 0009 17:36
 * @Description: 节点查询
 */
public class ChaosHostQuery extends QueryPage {

    private String name;

    private String ip;

    private Long appCode;

    private String port;

    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = StringUtils.trimToNull(name);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = StringUtils.trimToNull(ip);
    }

    public Long getAppCode() {
        return appCode;
    }

    public void setAppCode(Long appCode) {
        if (appCode != null && appCode > 0) {
            this.appCode = appCode;
        }
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = StringUtils.trimToNull(port);
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
