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
package com.pamirs.pradar.interceptor;

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.exception.PressureMeasureError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaobin on 2017/1/19.
 */
public class InterceptorInvokerHelper {
    private static boolean propagateException = false;
    private static final Logger logger = LoggerFactory.getLogger(InterceptorInvokerHelper.class);

    public static void handleException(Throwable t) {
        logger.warn("Exception occurred from interceptor", t);
        if (Pradar.isClusterTest()) {
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            }
            if (t instanceof PressureMeasureError) {
                throw (PressureMeasureError) t;
            }
            throw new PressureMeasureError(t);
        }
        if (propagateException) {
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            }
            throw new RuntimeException(t);
        }
    }

    public static void setPropagateException(boolean propagate) {
        propagateException = propagate;
    }

    public static boolean isPropagateException() {
        return propagateException;
    }
}

