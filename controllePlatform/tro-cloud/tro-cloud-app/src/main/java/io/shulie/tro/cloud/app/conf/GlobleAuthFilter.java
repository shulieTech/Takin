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

package io.shulie.tro.cloud.app.conf;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pamirs.tro.entity.domain.entity.user.User;
import io.shulie.tro.cloud.biz.service.user.TroCloudUserService;
import io.shulie.tro.cloud.biz.service.user.TroUserService;
import io.shulie.tro.cloud.common.constants.LoginConstant;
import io.shulie.tro.cloud.common.context.LoginUser;
import io.shulie.tro.cloud.common.context.RestContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @ClassName GlobleAuthFilter
 * @Description 全局认证filter
 * 认证成功后，将用户信息存放RestContext
 * @Author qianshui
 * @Date 2020/11/3 下午5:45
 */
@Component
public class GlobleAuthFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(GlobleAuthFilter.class);

    private static final String LICENSE_KEY = "licenseKey";

    private static final String FILTER_SQL = "filterSql";

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TroCloudUserService troCloudUserService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        //clear 本地线程
        RestContext.clear();
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        RestContext.setHttpServletRequest(httpServletRequest);
        RestContext.setHttpServletResponse(httpServletResponse);

        //过滤控制台请求数据
        try {
            Long userId = (Long) httpServletRequest.getSession().getAttribute(LoginConstant.SESSION_KEY);
            if (userId != null) {
                LoginUser user = (LoginUser) redisTemplate.opsForValue().get(LoginConstant.REDIS_KEY_PREFIX + userId);
                if (user != null) {
                    RestContext.setUser(user);
                    RestContext.getUser().setLoginChannel(0);
                    httpServletRequest.getSession().setMaxInactiveInterval(TroUserService.maxWait);
                    redisTemplate.opsForValue().set(LoginConstant.REDIS_KEY_PREFIX + user.getId(), user, TroUserService.maxWait, TimeUnit.SECONDS);
                }
            }
        } catch (Exception e) {
            logger.error("GlobleAuthFilter Console：" + e.getMessage(), e);
        }

        //过滤license请求数据
        try {
            String license = httpServletRequest.getHeader(LICENSE_KEY);
            if (StringUtils.isNoneBlank(license)) {
                String filterSql = httpServletRequest.getHeader(FILTER_SQL);
                RestContext.setFilterSql(filterSql);
                User user = troCloudUserService.queryUserByKey(license);
                if(user != null) {
                    RestContext.setUser(convert(user));
                    RestContext.getUser().setLoginChannel(1);
                }
            }
        } catch (Exception e) {
            logger.error("GlobleAuthFilter License：" + e.getMessage(), e);
        }
        //继续执行其他filter
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    private LoginUser convert(User user) {
        if(user == null) {
            return null;
        }
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(user, loginUser);
        return loginUser;
    }
}
