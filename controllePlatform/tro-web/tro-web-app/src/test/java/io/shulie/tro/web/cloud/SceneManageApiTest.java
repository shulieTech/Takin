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

package io.shulie.tro.web.cloud;

import java.util.Collections;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.hutool.core.date.DateUtil;
import io.shulie.tro.cloud.common.pojo.dto.scenemanage.UploadFileDTO;
import io.shulie.tro.cloud.open.req.scenemanage.UpdateSceneFileRequest;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.web.app.Application;
import io.shulie.tro.web.diff.api.scenemanage.SceneManageApi;

/**
 * @author liuchuan
 * @date 2021/4/26 8:39 下午
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SceneManageApiTest {

    @Autowired
    private SceneManageApi sceneManageApi;

    @Test
    public void testUpdateSceneFileByScriptId() {
        UpdateSceneFileRequest request = new UpdateSceneFileRequest();
        request.setScriptId(3L);
        request.setScriptType(0);
        UploadFileDTO uploadFileDTO = new UploadFileDTO();
        uploadFileDTO.setUploadTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        uploadFileDTO.setId(1L);
        uploadFileDTO.setFileName("测试文件");
        uploadFileDTO.setUploadPath("~/test/tmp");
        uploadFileDTO.setIsDeleted(0);
        uploadFileDTO.setUploadedData(0L);
        uploadFileDTO.setIsSplit(0);
        uploadFileDTO.setFileType(0);
        request.setUploadFiles(Collections.singletonList(uploadFileDTO));

        ResponseResult responseResult = sceneManageApi.updateSceneFileByScriptId(request);
        System.out.println(responseResult);
    }

}
