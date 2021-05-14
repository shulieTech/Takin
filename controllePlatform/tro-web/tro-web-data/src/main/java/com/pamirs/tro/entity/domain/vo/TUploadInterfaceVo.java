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

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 说明: job dubbo 接口抓取 数据上传 防漏
 *
 * @author 298403
 * @version v1.0
 * @Date: Create in 2019/1/14
 */
public class TUploadInterfaceVo {

    /**
     * app name
     */
    @NotBlank
    private String appName;

    /**
     * 详情列表
     */
    @NotNull
    private List<TUploadInterfaceDetailVo> appDetails = new ArrayList<>();

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public List<TUploadInterfaceDetailVo> getAppDetails() {
        return appDetails;
    }

    public void setAppDetails(List<TUploadInterfaceDetailVo> appDetails) {
        this.appDetails = appDetails;
    }

    @Override
    public String toString() {
        return "TUploadInterfaceVo{" +
            "appName='" + appName + '\'' +
            ", appDetails=" + appDetails +
            '}';
    }

}
