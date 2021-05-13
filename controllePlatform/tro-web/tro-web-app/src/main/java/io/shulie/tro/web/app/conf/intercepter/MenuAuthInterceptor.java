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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import com.google.common.collect.Maps;
import com.pamirs.tro.entity.domain.entity.user.User;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.service.auth.TroAuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @Author: fanxx
 * @Date: 2020/9/8 8:28 PM
 * @Description:
 */
@Component
@Slf4j
public class MenuAuthInterceptor implements HandlerInterceptor {

    @Autowired
    TroAuthService troAuthService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //验证菜单权限
        User user = RestContext.getUser();
        if (user == null) {
            if (StringUtils.isNotBlank(RestContext.getTenantUserKey())) {
                return true;
            }
            return false;
        } else {
            if (user.getUserType() == 0) {
                //系统管理员
                return true;
            } else {
                //普通用户
                String url = request.getRequestURI();
                //是否有菜单权限
                boolean hasPermission = troAuthService.hasPermissionUrl(user, url);
                if (!hasPermission) {
                    String msg = "权限不足，用户无菜单权限！";
                    log.error("用户无菜单权限:{},{}", user.getName(), url);
                    println(response, msg);
                }
                return hasPermission;
            }
        }
    }

    private void println(HttpServletResponse response, String message) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        PrintWriter printWriter;
        try {
            printWriter = response.getWriter();
            Map<String, Object> map = Maps.newHashMap();
            map.put("msg", message);
            map.put("status", HttpStatus.UNAUTHORIZED.value());
            String s = JSON.toJSONString(map);
            printWriter.write(s);
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
