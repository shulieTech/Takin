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

package io.shulie.tro.cloud.web.entrypoint.controller.cloud;

import java.util.Map;

import com.pamirs.tro.entity.domain.entity.cloudserver.LicenseGenerateParam;
import io.shulie.tro.common.beans.response.ResponseResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.shulie.tro.cloud.common.utils.LicenceUtils;

/**
 * @Author: mubai
 * @Date: 2020-05-22 18:17
 * @Description:
 */

@RestController
@RequestMapping("/api/license/")
public class LicenseController {

    @PostMapping("generate")
    public ResponseResult generateLicense(@RequestBody LicenseGenerateParam param) {
        if (param == null || StringUtils.isBlank(param.getExpireTime()) || StringUtils.isBlank(param.getUserAppKey())) {
            return ResponseResult.fail("0", "userAppKey | expireTime 不能为空", "");
        }
        String license = LicenceUtils.generateLicense(param.getUserAppKey(), param.getExpireTime());
        return ResponseResult.success(license);
    }

    @PostMapping("analysis")
    public ResponseResult analysisLicense(@RequestBody LicenseGenerateParam param) {
        if (param == null || StringUtils.isBlank(param.getLicense())) {
            return ResponseResult.fail("0", "license 不能为空", "");
        }
        Map<String, Object> map = LicenceUtils.analysisLicense(param.getLicense());
        return ResponseResult.success(map);
    }

}
