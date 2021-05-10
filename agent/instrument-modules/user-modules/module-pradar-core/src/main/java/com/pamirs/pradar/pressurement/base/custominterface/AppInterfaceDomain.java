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
import java.util.ArrayList;
import java.util.List;

public class AppInterfaceDomain implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1775969610596041097L;

    /**
     * app name
     */
    private String appName;

    /**
     * appDetail
     */
    private List<AppInterfaceDetailDomain> appDetails = new ArrayList<AppInterfaceDetailDomain>();


    /**
     * app name
     */
    public String getAppName() {
        return appName;
    }

    /**
     * app name
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * appDetail
     */
    public List<AppInterfaceDetailDomain> getAppDetails() {
        return appDetails;
    }

    /**
     * appDetail
     */
    public void setAppDetails(List<AppInterfaceDetailDomain> appDetails) {
        this.appDetails = appDetails;
    }


}
