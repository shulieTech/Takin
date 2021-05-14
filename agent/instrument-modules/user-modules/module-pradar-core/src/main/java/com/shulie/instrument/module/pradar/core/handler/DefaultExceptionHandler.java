/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shulie.instrument.module.pradar.core.handler;

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.shulie.instrument.simulator.message.exception.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/3/11 6:58 下午
 */
public class DefaultExceptionHandler implements ExceptionHandler {
    private final static Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @Override
    public void handleException(Throwable throwable, String message, Object listener) throws Throwable {
        if (throwable instanceof PressureMeasureError) {
            throw throwable;
        }
        if (Pradar.isClusterTest()) {
            throw new PressureMeasureError(throwable, Pradar.isClusterTest());
        } else {
            logger.warn("SIMULATOR: {} listener:{}",
                    message,
                    listener == null ? "" : listener.getClass().getName(),
                    throwable
            );
        }
    }
}
