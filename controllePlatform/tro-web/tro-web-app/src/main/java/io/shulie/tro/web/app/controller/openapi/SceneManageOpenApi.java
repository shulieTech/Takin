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

package io.shulie.tro.web.app.controller.openapi;

import java.util.List;

import com.pamirs.tro.entity.domain.vo.scenemanage.SceneManageQueryVO;
import io.shulie.tro.cloud.open.resp.scenemanage.SceneManageListResp;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.utils.json.JsonHelper;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.controller.openapi.response.scenemanage.SceneManageListOpenApiResp;
import io.shulie.tro.web.app.controller.openapi.response.scenemanage.SceneManageOpenApiResp;
import io.shulie.tro.web.app.service.scenemanage.SceneManageService;
import io.shulie.tro.web.common.domain.WebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName SceneManageController
 * @Description
 * @Author qianshui
 * @Date 2020/4/17 下午2:31
 */
@RestController
@RequestMapping(APIUrls.TRO_OPEN_API_URL+"/scenemanage")
@Api(tags = "压测场景管理")
public class SceneManageOpenApi {

    @Autowired
    private SceneManageService sceneManageService;


    @GetMapping("/detail")
    @ApiOperation(value = "压测场景详情")
    public Response<SceneManageOpenApiResp> getDetail(@ApiParam(name = "id", value = "ID", required = true) Long id) {
        ResponseResult responseResult = sceneManageService.detailScene(id);
        return Response.success(ofSceneManageOpenApiResp(responseResult.getData()));
    }

    @GetMapping("/list")
    @ApiOperation(value = "压测场景列表")
    public Response<List<SceneManageListOpenApiResp>> getList(@ApiParam(name = "current", value = "页码", required = true) Integer current,
                                                              @ApiParam(name = "pageSize", value = "页大小", required = true) Integer pageSize,
                                                              @ApiParam(name = "customName", value = "客户名称") String customName,
                                                              @ApiParam(name = "customId", value = "客户ID") Long customId,
                                                              @ApiParam(name = "sceneId", value = "压测场景ID") Long sceneId,
                                                              @ApiParam(name = "sceneName", value = "压测场景名称") String sceneName,
                                                              @ApiParam(name = "status", value = "压测状态") Integer status) {
        SceneManageQueryVO queryVO = new SceneManageQueryVO();
        queryVO.setCurrent(current);
        queryVO.setCurrentPage(current);
        queryVO.setPageSize(pageSize);
        queryVO.setCustomName(customName);
        queryVO.setCustomId(customId);
        queryVO.setSceneId(sceneId);
        queryVO.setSceneName(sceneName);
        queryVO.setStatus(status);
        WebResponse<List<SceneManageListResp>> pageList = sceneManageService.getPageList(queryVO);
        return Response.success(ofListSceneManageListOpenApiResp(pageList.getData()));
    }

    private SceneManageOpenApiResp ofSceneManageOpenApiResp(Object data){
        if (data == null){
            return null;
        }
        String s = JsonHelper.bean2Json(data);
        return JsonHelper.json2Bean(s, SceneManageOpenApiResp.class);
    }

    private List<SceneManageListOpenApiResp> ofListSceneManageListOpenApiResp(Object data){
        if (data == null){
            return null;
        }
        String s = JsonHelper.bean2Json(data);
        return JsonHelper.json2List(s, SceneManageListOpenApiResp.class);
    }
}
