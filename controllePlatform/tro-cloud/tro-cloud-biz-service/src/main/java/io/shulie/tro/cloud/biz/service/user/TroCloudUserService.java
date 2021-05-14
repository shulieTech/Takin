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

package io.shulie.tro.cloud.biz.service.user;

import java.util.List;

import com.pamirs.tro.entity.domain.entity.user.User;
import com.pamirs.tro.entity.domain.vo.user.UserQueryParam;
import com.pamirs.tro.entity.domain.vo.user.UserVo;
import io.shulie.tro.cloud.biz.output.user.UserOutput;
import io.shulie.tro.common.beans.response.ResponseResult;

/**
 * @Author: fanxx
 * @Date: 2020/3/24 下午4:44
 * @Description:
 */
public interface TroCloudUserService {
    User queryUser(UserQueryParam param);

    ResponseResult addUser(UserVo user);

    ResponseResult updateUser(UserVo user);

    ResponseResult queryUserDetail(Long userId);

    ResponseResult<List<UserVo>> selectByExample(UserQueryParam param);

    User queryUserByKey(String key);

    /**
     * 根据userID 获取
     * @param userId
     * @return
     */
    UserOutput selectById(Long userId);




}
