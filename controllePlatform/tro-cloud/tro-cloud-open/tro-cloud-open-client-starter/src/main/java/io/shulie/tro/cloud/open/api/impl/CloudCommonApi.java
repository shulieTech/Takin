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

package io.shulie.tro.cloud.open.api.impl;

import java.util.Map;

import com.google.common.collect.Maps;
import io.shulie.tro.cloud.common.utils.AppBusinessUtil;
import io.shulie.tro.cloud.open.constant.CloudApiConstant;
import org.springframework.stereotype.Component;

/**
 * @ClassName CloudCommonApi
 * @Description
 * @Author qianshui
 * @Date 2020/11/16 下午4:21
 */
@Component
public class CloudCommonApi {

    protected Map<String, String> getHeaders(String license) {
        Map<String, String> map = Maps.newHashMap();
        map.put(CloudApiConstant.LICENSE_REQUIRED, "true");
        map.put(CloudApiConstant.LICENSE_KEY, this.getDevLicense(license));
        return map;
    }

    protected Map<String, String> getHeaders(String license, String filterSql) {
        Map<String, String> map = Maps.newHashMap();
        map.put(CloudApiConstant.LICENSE_REQUIRED, "true");
        map.put(CloudApiConstant.LICENSE_KEY, this.getDevLicense(license));
        map.put(CloudApiConstant.FILTER_SQL, filterSql);
        return map;
    }

    /**
     * 本地环境 license, 供测试用
     *
     * @return license
     */
    private String getDevLicense(String license) {
        return AppBusinessUtil.isLocal() ? "5b06060a-17cb-4588-bb71-edd7f65035af" : license;
    }
}
