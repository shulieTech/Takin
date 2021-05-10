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
package com.pamirs.pradar.pressurement.base.custominterface;

import java.io.Serializable;

public class AppInterfaceDetailDomain implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -177594545451097L;

    /**
     * type dubbo / job
     */
    private String type;

    private String interfaceName;

    public final static String DUBBO = "dubbo";
    public final static String JOB = "job";
    public final static String TRACE = "trace";
    public final static String SQL = "sql";


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


}
