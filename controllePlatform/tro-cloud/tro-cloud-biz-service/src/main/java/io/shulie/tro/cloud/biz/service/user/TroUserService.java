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

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pamirs.tro.entity.domain.entity.user.User;
import com.pamirs.tro.entity.domain.vo.user.UserLoginParam;
import com.pamirs.tro.entity.domain.vo.user.UserQueryParam;
import io.shulie.tro.cloud.biz.service.user.Impl.BCrypt;
import io.shulie.tro.cloud.common.constants.LoginConstant;
import io.shulie.tro.cloud.common.context.LoginUser;
import io.shulie.tro.cloud.common.context.RestContext;
import io.shulie.tro.common.beans.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/3/24 下午8:45
 * @Description:
 */
@Component
@Slf4j
public class TroUserService {

    public static int maxWait = 120 * 60; //单位秒

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TroCloudUserService troCloudUserService;

    public ResponseResult login(UserLoginParam userLoginParam, HttpServletRequest request,
        HttpServletResponse response) {
        UserQueryParam param = new UserQueryParam();
        String username = userLoginParam.getUsername();
        String password = userLoginParam.getPassword();
        param.setName(username);
        User user = troCloudUserService.queryUser(param);
        if (user == null) {
            log.error("用户不存在:{}", username);
            return ResponseResult.fail("300", "用户不存在", "");
        }

        String salt = user.getSalt();
        String pwd = BCrypt.hashpw(password, salt);
        if (!pwd.equals(user.getPassword())) {
            log.error("用户密码错误:{}", username);
            return ResponseResult.fail("300", "用户密码错误", "");
        }

        if (user.getStatus() == 1) {
            log.error("用户已禁用:{}", username);
            return ResponseResult.fail("300", "用户已禁用", "");
        }

        LoginUser responseUser = new LoginUser();
        responseUser.setId(user.getId());
        responseUser.setName(user.getName());
        responseUser.setPassword(user.getPassword());
        responseUser.setRole(user.getRole());
        responseUser.setKey(user.getKey());
        responseUser.setVersion(user.getVersion());
        request.getSession().setAttribute(LoginConstant.SESSION_KEY, responseUser.getId());
        request.getSession().setMaxInactiveInterval(maxWait);
        redisTemplate.opsForValue().set(LoginConstant.REDIS_KEY_PREFIX + user.getId(), responseUser, maxWait,
            TimeUnit.SECONDS);
        RestContext.setUser(responseUser);
        return ResponseResult.success(responseUser);
    }

    public ResponseResult logout(HttpServletRequest request) {
        User user = (User)request.getSession().getAttribute(LoginConstant.SESSION_KEY);
        if (user != null) {
            HttpSession session = request.getSession();
            session.removeAttribute(LoginConstant.SESSION_KEY);
        }
        return ResponseResult.success("成功退出");
    }
}
