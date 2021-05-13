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

package io.shulie.tro.web.app.controller.user;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.request.user.UserAllocationUpdateRequest;
import io.shulie.tro.web.app.request.user.UserCreateRequest;
import io.shulie.tro.web.app.request.user.UserDeleteRequest;
import io.shulie.tro.web.app.request.user.UserDetailQueryRequest;
import io.shulie.tro.web.app.request.user.UserPasswordUpdateRequest;
import io.shulie.tro.web.app.request.user.UserUpdateRequest;
import io.shulie.tro.web.app.response.user.UserDetailResponse;
import io.shulie.tro.web.app.response.user.UserImportResponse;
import io.shulie.tro.web.app.service.user.TroWebUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: fanxx
 * @Date: 2020/3/24 下午4:42
 * @Description:
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户管理")
public class TroWebUserController {

    @Autowired
    private TroWebUserService troWebUserService;

    @PostMapping("/create")
    @ApiOperation("添加用户")
    public void createUser(@Validated @RequestBody UserCreateRequest createRequest) {
        troWebUserService.createUser(createRequest);
    }

    @PutMapping("/update")
    @ApiOperation("更新用户")
    public void updateUser(@Validated @RequestBody UserUpdateRequest updateRequest) {
        troWebUserService.updateUser(updateRequest);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除用户")
    public void deleteUser(@Validated @RequestBody UserDeleteRequest deleteRequest) {
        troWebUserService.deleteUser(deleteRequest);
    }

    @PutMapping("/pwd/update")
    @ApiOperation("更新密码")
    public void updatePassword(@Validated @RequestBody UserPasswordUpdateRequest updateRequest) {
        troWebUserService.updatePassword(updateRequest);
    }

    @GetMapping("/detail")
    @ApiOperation("查询用户")
    public UserDetailResponse queryUser(@Validated @NotNull @RequestParam("id") Long id) {
        UserDetailQueryRequest queryRequest = new UserDetailQueryRequest();
        queryRequest.setId(id);
        return troWebUserService.queryUser(queryRequest);
    }

    @PostMapping("/import")
    @ApiOperation("导入用户")
    public UserImportResponse importUser(@NotNull @RequestBody MultipartFile file) {
        return troWebUserService.importUser(file);
    }

    @GetMapping("/download")
    @ApiOperation("下载异常用户数据")
    public void download(HttpServletResponse response, @NotEmpty String path) {
        troWebUserService.download(response, path);
    }

    @GetMapping("/example/download")
    @ApiOperation("下载导入模板")
    public void downloadExample(HttpServletResponse response) {
        troWebUserService.downloadExample(response);
    }

    @PostMapping("/allocation")
    @ApiOperation("指定负责人")
    public Response allocationUser(@RequestBody UserAllocationUpdateRequest request) {
        return troWebUserService.allocationUser(request);
    }
}
