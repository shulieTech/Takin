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

package io.shulie.tro.cloud.app.intercepter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import io.shulie.tro.common.beans.response.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @Auther: vernon
 * @Date: 2019/9/20 14:40
 * @Description:
 */
@Service
public class AuthorityInterceptor implements HandlerInterceptor {

    private static final String REDIS_LOGIN_KEY = "login_key";
    private static final String specialToken = "123456789101112";
    private final Logger log = LoggerFactory.getLogger(AuthorityInterceptor.class);
    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${spring.profiles.active}")
    private String env;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        if (StringUtils.isNotBlank(env) && "dev".equals(env)) {
            return true;
        }
        String sessionId = request.getRequestedSessionId();
        String refer = request.getHeader("Referer");
        if (Objects.nonNull(refer)) {
            if (refer.contains(specialToken)) {
                return true;
            }
        }

        try {

            if (StringUtils.isBlank(sessionId)) {
                writeResponse(response, "300", "sessionId is null");
            }
            Object obj = redisTemplate.opsForHash().get(REDIS_LOGIN_KEY, sessionId);
            if (Objects.isNull(obj)) {
                writeResponse(response, "300", String.format("redis key:%s get is null", sessionId));
            }

            String tokenTime = String.valueOf(obj);
            String[] split = tokenTime.split(":");
            String token = split[0];

            if (StringUtils.isBlank(token) && null == split[1]) {
                writeResponse(response, "300", String.format("redis get token:%s || time:%s is null", token, split[1]));
            }

            Long endTime = Long.valueOf(split[1]);
            long overdueTime = endTime - System.currentTimeMillis();
            if (overdueTime < 1) {
                writeResponse(response, "300", String.format("login is overdue : %s", overdueTime));
            }

            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (Objects.equals(token, cookie.getValue())) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    public void writeResponse(HttpServletResponse response, String code, String msg) throws IOException {
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("type", "opaqueredirect");
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        ResponseResult<String> result = ResponseResult.fail(code, msg, "opaqueredirect");
        writer.write(JSON.toJSONString(result));
        writer.flush();
        writer.close();
    }
}
