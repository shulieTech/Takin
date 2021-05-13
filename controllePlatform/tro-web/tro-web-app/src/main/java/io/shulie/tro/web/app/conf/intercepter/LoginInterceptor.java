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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pamirs.tro.entity.domain.entity.user.User;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.service.auth.TroAuthService;
import io.shulie.tro.web.data.mapper.mysql.TroUserMapper;
import io.shulie.tro.web.data.model.mysql.TroUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @Author: fanxx
 * @Date: 2020/3/24 下午9:08
 * @Description:
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private TroAuthService troAuthService;
    @Resource
    private TroUserMapper troUserMapper;

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        User user = setUserLoginMessage(request, response);
        //        User user = RestContext.getUser();
        if (user != null) {
            return true;
        }
        log.debug("用户未登录");
        log.debug("客户端IP：[{}]", getIpAddress(request));
        log.debug("请求路径：[{}]", request.getRequestURI());
        troAuthService.checkFail(response);
        return false;
    }

    /**
     * 设置用户登录信息
     *
     * @param request
     * @param response
     */
    private User setUserLoginMessage(HttpServletRequest request, HttpServletResponse response) {
        try {
            User user = troAuthService.check(request, response);
            if (user != null) {
                return getAdminUser(user);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 查询系统管理员并设置
     *
     * @return
     */
    private User getAdminUser(User user) {
        TroUserEntity admin = troUserMapper.selectById(user.getCustomerId());
        if (admin != null) {
            user.setCustomerId(admin.getId());
            user.setCustomerKey(admin.getKey());
        } else {
            log.error("未查询到租户信息");
        }
        RestContext.clear();
        RestContext.setUser(user);
        return user;
    }
}
