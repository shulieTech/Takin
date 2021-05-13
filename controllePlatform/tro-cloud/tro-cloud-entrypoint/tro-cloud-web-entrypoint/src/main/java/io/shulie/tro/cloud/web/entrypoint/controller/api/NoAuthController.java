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

package io.shulie.tro.cloud.web.entrypoint.controller.api;

import java.util.Map;

import javax.annotation.Resource;

import com.pamirs.tro.entity.dao.report.TReportMapper;
import com.pamirs.tro.entity.dao.scenemanage.TSceneManageMapper;
import io.shulie.tro.common.beans.response.ResponseResult;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName NoAuthController
 * @Description
 * @Author qianshui
 * @Date 2020/8/30 下午2:08
 */
@RestController
@RequestMapping("/api/noauth")
public class NoAuthController {

    @Resource
    private TSceneManageMapper TSceneManageMapper;

    @Resource
    private TReportMapper TReportMapper;

    /**
     * 恢复压测中的场景状态
     * update t_scene_manage set `status`=0 where id=？;
     * update t_report set `status`=2 where scene_id=？;
     *
     * @param paramMap
     * @return
     */
    @PutMapping("/resume/scenetask")
    public ResponseResult resumeSceneTask(@RequestBody Map<String, Object> paramMap) {
        Long sceneId = Long.parseLong(String.valueOf(paramMap.get("sceneId")));
        if (sceneId == null) {
            ResponseResult.fail("sceneId cannot be null,", "");
        }
        TReportMapper.resumeStatus(sceneId);
        TSceneManageMapper.resumeStatus(sceneId);
        return ResponseResult.success("resume success");
    }
}
