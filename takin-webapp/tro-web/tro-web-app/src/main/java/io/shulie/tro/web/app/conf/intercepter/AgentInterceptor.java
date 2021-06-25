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

package io.shulie.tro.web.app.conf.intercepter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import com.google.common.collect.Maps;
import com.pamirs.tro.entity.domain.entity.user.User;
import io.shulie.tro.web.app.cache.webimpl.AllUserCache;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.common.context.CurrentUser;
import io.shulie.tro.web.data.result.user.UserCacheResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @Author: fanxx
 * @Date: 2020/3/24 下午9:08
 * @Description:
 */
@Component
@Slf4j
public class AgentInterceptor implements HandlerInterceptor {

    public static final String KEY_USER_MAP_REDIS_KEY = "tro:web:key2user";
    private static final String APP_KEY_HEADER_KEY = "UserAppKey";
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AllUserCache allUserCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        RestContext.clear();
        String userAppKey = request.getHeader(APP_KEY_HEADER_KEY);
        if (StringUtils.isBlank(userAppKey)) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("msg", "Agent未携带userAppKey，禁止访问");
            map.put("status", HttpStatus.FORBIDDEN.value());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            assembleResponse(response, map);
            log.error("Agent未携带userAppKey，禁止访问");
            return false;
        }
        CurrentUser currentUser = (CurrentUser)redisTemplate.opsForHash().get(KEY_USER_MAP_REDIS_KEY, userAppKey);
        if (currentUser == null) {
            UserCacheResult cachedUserByKey = allUserCache.getCachedUserByKey(userAppKey);
            if (cachedUserByKey == null) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("msg", "Agent提供的userAppKey在数据库中未找到记录");
                map.put("status", HttpStatus.UNAUTHORIZED.value());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                assembleResponse(response, map);
                log.error("Agent提供的userAppKey在数据库中未找到记录，UserAppKey:{}", userAppKey);
                return false;
            }
            currentUser = new CurrentUser();
            currentUser.setCustomerId(cachedUserByKey.getCustomerId());
            currentUser.setCustomerKey(cachedUserByKey.getCustomerKey());
            currentUser.setId(cachedUserByKey.getId());
            currentUser.setUserType(cachedUserByKey.getUserType());
            redisTemplate.opsForHash().put(KEY_USER_MAP_REDIS_KEY, userAppKey, currentUser);
            redisTemplate.expire(KEY_USER_MAP_REDIS_KEY, 1, TimeUnit.HOURS);
        }
        User user = new User();
        BeanUtils.copyProperties(currentUser, user);
        RestContext.setUser(user);
        RestContext.setTenantUserKey(userAppKey);
        return true;
    }

    public void assembleResponse(HttpServletResponse response, Map<String, Object> map) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter;
        try {
            printWriter = response.getWriter();
            String s = JSON.toJSONString(map);
            printWriter.write(s);
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            log.error("error:", e);
        }
    }
}
