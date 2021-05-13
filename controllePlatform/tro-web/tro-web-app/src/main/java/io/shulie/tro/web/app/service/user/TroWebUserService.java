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

package io.shulie.tro.web.app.service.user;

import javax.servlet.http.HttpServletResponse;

import com.pamirs.tro.entity.domain.entity.user.User;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.request.user.UserAllocationUpdateRequest;
import io.shulie.tro.web.app.request.user.UserCreateRequest;
import io.shulie.tro.web.app.request.user.UserDeleteRequest;
import io.shulie.tro.web.app.request.user.UserDetailQueryRequest;
import io.shulie.tro.web.app.request.user.UserPasswordUpdateRequest;
import io.shulie.tro.web.app.request.user.UserUpdateRequest;
import io.shulie.tro.web.app.response.user.UserDetailResponse;
import io.shulie.tro.web.app.response.user.UserImportResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: fanxx
 * @Date: 2020/3/24 下午4:44
 * @Description:
 */
public interface TroWebUserService {
    void createUser(UserCreateRequest createRequest);

    void updateUser(UserUpdateRequest updateRequest);

    void updatePassword(UserPasswordUpdateRequest updateRequest);

    void deleteUser(UserDeleteRequest deleteRequest);

    UserDetailResponse queryUser(UserDetailQueryRequest queryRequest);

    UserImportResponse importUser(MultipartFile file);

    void download(HttpServletResponse response, String path);

    void downloadExample(HttpServletResponse response);

    User queryUserByKey(String key);

    /**
     * 指定责任人
     *
     * @param input
     * @return
     */
    Response allocationUser(UserAllocationUpdateRequest input);
}
