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

package io.shulie.tro.cloud.web.entrypoint.controller.user;

import java.util.List;

import com.pamirs.tro.entity.domain.vo.user.UserQueryParam;
import com.pamirs.tro.entity.domain.vo.user.UserVo;
import io.shulie.tro.cloud.biz.service.user.TroCloudUserService;
import io.shulie.tro.cloud.common.constants.APIUrls;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: fanxx
 * @Date: 2020/3/24 下午4:42
 * @Description:
 */
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
@Api(tags = "客户管理")
public class TroWebUserController {

    @Autowired
    private TroCloudUserService troCloudUserService;

    /**
     * 添加用户
     *
     * @param userVo
     * @return
     */
    @ApiOperation("添加用户")
    @PostMapping("user/add")
    public ResponseResult addUser(@RequestBody UserVo userVo) {
        return troCloudUserService.addUser(userVo);
    }

    /**
     * 更新用户
     *
     * @param userVo
     * @return
     */
    @ApiOperation("更新用户")
    @PutMapping("user/update")
    public ResponseResult updateUser(@RequestBody UserVo userVo) {
        return troCloudUserService.updateUser(userVo);
    }

    /**
     * 用户详情
     *
     * @param id
     * @return
     */
    @ApiOperation("用户详情")
    @GetMapping("user/detail")
    public ResponseResult getUserDetail(@ApiParam(name = "id", value = "用户id", required = true) Long id) {
        return troCloudUserService.queryUserDetail(id);
    }

    /**
     * 用户列表
     *
     * @param nick
     * @return
     */
    @ApiOperation("客户列表")
    @GetMapping("user/list")
    public ResponseResult<List<UserVo>> listUser(@ApiParam(name = "nick", value = "客户名称") String nick,
        @ApiParam(name = "id", value = "客户ID") Long id,
        Integer current,
        Integer pageSize) {
        UserQueryParam queryParam = new UserQueryParam();
        queryParam.setId(id);
        queryParam.setNick(nick);
        queryParam.setCurrentPage(current);
        queryParam.setPageSize(pageSize);
        return troCloudUserService.selectByExample(queryParam);
    }
}
