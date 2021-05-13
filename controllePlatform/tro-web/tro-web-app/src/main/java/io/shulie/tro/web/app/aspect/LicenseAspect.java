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

import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.common.constant.RemoteConstant;
import io.shulie.tro.web.common.domain.WebRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName LicenseAspect
 * @Description
 * @Author qianshui
 * @Date 2020/5/14 下午8:47
 */
@Aspect
@Component
@Slf4j
public class LicenseAspect {

    @Value("${tro.cloud.url}")
    private String remoteUrl;

    @Pointcut("execution(public * io.shulie.tro.web.common.http.HttpWebClient.request*(..))")
    public void setLicense() {

    }

    @Before("setLicense()")
    public void doBefore(JoinPoint joinPoint) {
        Object[] params = joinPoint.getArgs();
        if (params != null && params.length == 1) {
            if (params[0] instanceof WebRequest) {
                WebRequest inParam = (WebRequest)params[0];
                inParam.setRequestUrl(remoteUrl + inParam.getRequestUrl());
                inParam.setLicense(RemoteConstant.LICENSE_VALUE);
                Long uid = RestContext.getUser() != null ? RestContext.getUser().getId() : null;
                if (uid==null){
                  uid=inParam.getUid();
                }
                inParam.setUid(uid);
                inParam.setFilterUids(RestContext.getQueryAllowUserIdList());
            }
        }
    }
}
