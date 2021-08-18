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

package io.shulie.amdb.global;

import io.shulie.amdb.common.Response;
import io.shulie.amdb.exception.AmdbException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: xingchen
 * @ClassName: GlobalExceptionHandler
 * @Package: io.shulie.amdb.global
 * @Date: 2020/10/3012:14
 * @Description:
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    public static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public Response<String> handlerAmdbExcetionThrowable(Throwable e) {
        logger.error("handlerAmdbExcetionThrowable" + ExceptionUtils.getStackTrace(e));
        return Response.fail(e.getMessage());
    }

    @ExceptionHandler(value = AmdbException.class)
    @ResponseBody
    public Response<String> handlerAmdbExcetion(AmdbException e) {
        logger.error("handlerAmdbExcetion" + ExceptionUtils.getStackTrace(e));
        return Response.fail(e.getMessage());
    }
}
