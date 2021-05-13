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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import com.google.common.collect.Maps;
import io.shulie.tro.cloud.common.context.LoginUser;
import io.shulie.tro.cloud.common.context.RestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (checkEnvTest(request)) {
            return true;
        }
        LoginUser user = RestContext.getUser();
        if (user != null) {
            return true;
        }
        log.error("用户未登录");
        printNotLogin(response);
        return false;
    }

    private void printNotLogin(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
            Map<String, Object> map = Maps.newHashMap();
            map.put("msg", "未登陆，请重新登陆");
            map.put("status", HttpStatus.UNAUTHORIZED.value());
            String s = JSON.toJSONString(map);
            printWriter.write(s);
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Boolean checkEnvTest(HttpServletRequest request) {
        String env = request.getHeader("Env");
        if (StringUtils.equals("localTest", env)) {
            return true;
        }
        return false;
    }
}
