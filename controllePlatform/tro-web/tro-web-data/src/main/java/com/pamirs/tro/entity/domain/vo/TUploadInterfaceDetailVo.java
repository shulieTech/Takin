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
 * 说明: job dubbo 接口抓取 数据上传 详情
 *
 * @author 298403
 * @version v1.0
 * @Date: Create in 2019/1/14
 */
public class TUploadInterfaceDetailVo {

    /**
     * 类型 dubbo / job
     */
    private String type;

    /**
     * 接口名称
     */
    private String interfaceName;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    @Override
    public String toString() {
        return "TUploadInterfaceDetailVo{" +
            "type='" + type + '\'' +
            ", interfaceName='" + interfaceName + '\'' +
            '}';
    }
}
