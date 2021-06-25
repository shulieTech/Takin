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

package io.shulie.tro.web.app.controller.cloud;

import java.util.HashMap;
import java.util.Map;

import io.shulie.tro.web.app.common.Response;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: mubai
 * @Date: 2020-05-20 15:44
 * @Description:
 */

@RequestMapping("/api/cloud/client/")
@RestController
@Api(tags = "客户端下载地址接口")
public class PradarClientUrlController {

    @Value("${tro.cloud.url}")
    private String cloudDomain;

    @Value("${remote.client.download.uri}")
    private String clientUri;

    /**
     * 获取下载地址
     *
     * @return
     */
    @GetMapping("download")
    public Response generateDownloadClientUrl() {

        if (cloudDomain != null && clientUri != null) {
            StringBuilder builder = new StringBuilder();
            builder.append(cloudDomain);
            builder.append(clientUri);
            Map<String, String> map = new HashMap<>();
            map.put("url", builder.toString());
            return Response.success(map);
        }
        return Response.fail("缺少配置客户端的下载路径");
    }

}
