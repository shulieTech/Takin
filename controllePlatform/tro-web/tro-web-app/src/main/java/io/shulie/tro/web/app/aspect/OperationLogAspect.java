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
import java.util.Date;

import io.shulie.tro.common.beans.annotation.ModuleDef;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.context.OperationLogContextHolder;
import io.shulie.tro.web.app.holder.OperationMessageHolder;
import io.shulie.tro.web.app.request.log.OperationLogCreateRequest;
import io.shulie.tro.web.app.service.log.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shiyajian
 * create: 2020-09-15
 */
@Aspect
@Component
@Slf4j
public class OperationLogAspect {

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 指定切入点
     */
    @Pointcut("@annotation(io.shulie.tro.common.beans.annotation.ModuleDef)")
    private void controllerAspect() { /* no context */}

    /**
     * 方法开始执行
     */
    @Before("controllerAspect()")
    public void doBefore() {
        OperationLogContextHolder.reset();
        OperationLogContextHolder.start();
    }

    /**
     * 方法结束执行后的操作
     */
    @AfterReturning("controllerAspect()")
    public void doAfter(JoinPoint jp) {
        OperationLogContextHolder.end(true);
        record(jp);
    }

    /**
     * 方法有异常时的操作
     */
    @AfterThrowing("controllerAspect()")
    public void doAfterThrow(JoinPoint jp) {
        OperationLogContextHolder.end(false);
        //        record(jp);
    }

    /**
     * 记录日志
     *
     * @param jp 切点
     */
    private void record(JoinPoint jp) {
        // 如果设置了ignore，则不保存日志
        if (OperationLogContextHolder.get().getIgnore()) {
            return;
        }
        Signature signature = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method targetMethod = methodSignature.getMethod();
        ModuleDef annotation = targetMethod.getAnnotation(ModuleDef.class);
        //操作内容描述
        String message = OperationMessageHolder.formatMessage(annotation.logMsgKey(),
            OperationLogContextHolder.get().getVars());
        //操作模块  主模块
        String moduleName = annotation.moduleName();
        //操作模块  子模块
        String subModuleName = annotation.subModuleName();
        //操作类型
        String operType = OperationLogContextHolder.get().getOperationType();
        if (operType == null) {
            operType = "-";
        }
        //操作状态
        Boolean status = OperationLogContextHolder.get().getSuccess();
        //开始时间
        long startTimeMills = OperationLogContextHolder.get().getStartTime();
        //结束时间
        long stopTimeMills = OperationLogContextHolder.get().getEndTime();
        //操作人id
        Long userId = RestContext.getUser().getId();
        //操作人名称
        String userName = RestContext.getUser().getName();

        OperationLogCreateRequest createRequest = new OperationLogCreateRequest();
        createRequest.setModule(moduleName);
        createRequest.setSubModule(subModuleName);
        createRequest.setType(operType);
        createRequest.setStatus(status ? "成功" : "失败");
        createRequest.setContent(message);
        createRequest.setUserId(userId);
        createRequest.setUserName(userName);
        createRequest.setStartTime(new Date(startTimeMills));
        createRequest.setEndTime(new Date(stopTimeMills));
        operationLogService.record(createRequest);
    }
}
