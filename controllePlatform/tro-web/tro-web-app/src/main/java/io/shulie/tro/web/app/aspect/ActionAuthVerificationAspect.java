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

import java.lang.reflect.Method;
import java.util.Map;

import com.pamirs.tro.entity.domain.entity.user.User;
import io.shulie.tro.web.app.annotation.AuthVerification;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.constant.BizOpConstants;
import io.shulie.tro.web.app.exception.ExceptionCode;
import io.shulie.tro.web.auth.api.enums.ActionTypeEnum;
import io.shulie.tro.web.auth.api.exception.TroAuthException;
import io.shulie.tro.web.common.constant.TroClientAuthConstant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/11/7 下午9:09
 * @Description: 操作权限鉴权
 */
@Aspect
@Component
@Slf4j
@Order(0)
public class ActionAuthVerificationAspect {
    /**
     * 指定切入点
     */
    @Pointcut("@annotation(io.shulie.tro.web.app.annotation.AuthVerification)")
    private void controllerAspect() { /* no context */}

    /**
     * 方法开始执行
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint jp) throws TroAuthException {
        Signature signature = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method targetMethod = methodSignature.getMethod();
        AuthVerification annotation = targetMethod.getAnnotation(AuthVerification.class);
        String moduleCode = annotation.moduleCode();
        ActionTypeEnum actionTypeEnum = annotation.needAuth();
        if (!actionTypeEnum.equals(ActionTypeEnum.QUERY) && TroClientAuthConstant.isExpire) {
            String msg = "license过期，无该操作权限：" + actionTypeEnum.name();
            throw new TroAuthException(ExceptionCode.LICENSE_PERMISSION_DENY_ERROR, msg);
        }
        User user = RestContext.getUser();
        if (user.getUserType() == 0) {
            return;
        }
        Map<String, Boolean> permisstionActionMap = user.getPermissionAction();
        Map<String, Boolean> permisstionMenuMap = user.getPermissionMenu();

        if (actionTypeEnum.getCode().equals(ActionTypeEnum.QUERY.getCode())) {
            if (!permisstionMenuMap.containsKey(moduleCode)) {
                String msg;
                if (BizOpConstants.modelNameMap.containsKey(moduleCode)) {
                    msg = "菜单权限不足：【" + BizOpConstants.modelNameMap.get(moduleCode) + "】,请联系管理员处理";
                } else {
                    msg = "菜单权限不足：" + moduleCode;
                }
                log.error(msg);
                ExceptionCode.MENU_PERMISSION_DENY_ERROR.setDefaultValue(msg);
                throw new TroAuthException(ExceptionCode.MENU_PERMISSION_DENY_ERROR, msg);
            }
        } else {
            String key = moduleCode + "_" + actionTypeEnum.getCode() + "_" + actionTypeEnum.name().toLowerCase();
            if (!permisstionActionMap.containsKey(key)) {
                String msg = "操作权限不足：" + actionTypeEnum.name();
                log.error(msg);
                ExceptionCode.ACTION_PERMISSION_DENY_ERROR.setDefaultValue(msg);
                throw new TroAuthException(ExceptionCode.ACTION_PERMISSION_DENY_ERROR, msg);
            }
        }
    }
}
