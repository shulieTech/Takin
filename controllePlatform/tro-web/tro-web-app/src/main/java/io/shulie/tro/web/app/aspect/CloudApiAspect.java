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

package io.shulie.tro.web.app.aspect;

import io.shulie.tro.cloud.open.req.HttpCloudRequest;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.common.constant.RemoteConstant;
import io.shulie.tro.web.common.util.FilterSqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @ClassName CloudApiAspect
 * @Description
 * @Author qianshui
 * @Date 2020/5/14 下午8:47
 */
@Aspect
@Component
@Slf4j
public class CloudApiAspect {

    @Pointcut("execution(public * io.shulie.tro.web.diff.cloud.impl..*.*(..))")
    public void myAspect() {

    }

    @Before("myAspect()")
    public void doBefore(JoinPoint joinPoint) {
        Object[] params = joinPoint.getArgs();
        if (params != null && params.length == 1) {
            if (params[0] instanceof HttpCloudRequest) {
                HttpCloudRequest inParam = (HttpCloudRequest)params[0];
                inParam.setLicense(RemoteConstant.LICENSE_VALUE);
                inParam.setUid(RestContext.getUser() != null ? RestContext.getUser().getId() : null);
                String filterSql = FilterSqlUtil.buildFilterSql(RestContext.getQueryAllowUserIdList());
                if (filterSql != null) {
                    inParam.setFilterSql(filterSql);
                }
            }
        }
    }
}
