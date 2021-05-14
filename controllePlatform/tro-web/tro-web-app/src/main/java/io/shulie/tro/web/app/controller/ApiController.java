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

package io.shulie.tro.web.app.controller;

import io.shulie.tro.web.app.controller.linkmanage.DictionaryCache;
import io.shulie.tro.web.common.domain.WebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ApiController
 * @Description
 * @Author qianshui
 * @Date 2020/5/14 下午7:34
 */
@RestController
@RequestMapping("/api")
@Api(tags = "公共API")
public class ApiController {

    @Autowired
    private DictionaryCache dictionaryCache;

    @GetMapping("/link/dictionary")
    @ApiOperation(value = "数据字典")
    public WebResponse dictionary() {
        return WebResponse.success(dictionaryCache.getDicMap());
    }
}
