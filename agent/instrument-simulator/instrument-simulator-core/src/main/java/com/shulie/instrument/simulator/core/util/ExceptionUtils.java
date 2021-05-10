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
package com.shulie.instrument.simulator.core.util;

import java.io.IOException;

/**
 * 异常工具类
 */
public class ExceptionUtils {

    public static boolean isCauseByIOException(final Throwable cause) {
        return isCauseByTargetException(cause, IOException.class);
    }

    public static boolean isCauseByTargetException(final Throwable cause, final Class<? extends Throwable> targetCauseType) {
        if (targetCauseType.isAssignableFrom(cause.getClass())) {
            return true;
        }

        return null != cause.getCause() && isCauseByTargetException(cause.getCause(), targetCauseType);

    }

}
