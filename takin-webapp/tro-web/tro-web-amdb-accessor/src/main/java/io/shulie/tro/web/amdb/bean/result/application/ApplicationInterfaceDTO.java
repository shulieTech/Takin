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

package io.shulie.tro.web.amdb.bean.result.application;

import java.io.Serializable;

import lombok.Data;

/**
 * @author shiyajian
 * create: 2020-10-12
 */
@Data
public class ApplicationInterfaceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 接口ID
     */
    private String id;

    /**
     * 接口类型
     */
    private String interfaceType;

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 应用名
     */
    private String appName;

    @Override
    public int hashCode() {
        return (appName + "#" + interfaceName + "#" + interfaceType).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        ApplicationInterfaceDTO that = (ApplicationInterfaceDTO)o;
        return interfaceType.equals(that.interfaceType) && interfaceName.equals(that.interfaceName) && appName.equals(that.appName);
    }
}
