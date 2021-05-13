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

package io.shulie.tro.cloud.open.entrypoint.controller.file;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.google.common.collect.Maps;
import io.shulie.tro.cloud.common.constants.APIUrls;
import io.shulie.tro.cloud.open.req.filemanager.FileContentParamReq;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.utils.file.FileManagerHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName FileController
 * @Description
 * @Author qianshui
 * @Date 2020/4/17 下午5:50
 */
@RestController
@RequestMapping(APIUrls.TRO_OPEN_API_URL + "file")
@Api(tags = "文件管理")
@Slf4j
public class FileOpenController {


    @Value("${script.path}")
    private String scriptPath;

    @ApiOperation("文件内容获取")
    @PostMapping(value = "/getFileContentByPaths")
    public ResponseResult<Map<String, Object>> getFileContentByPaths(@RequestBody FileContentParamReq req) {
        Map<String, Object> result = Maps.newHashMap();
        try {
            for(String filePath :req.getPaths()) {
                if (new File(filePath).exists()) {
                    result.put(filePath, FileManagerHelper.readFileToString(new File(filePath),"UTF-8"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseResult.success(result);
    }

}
