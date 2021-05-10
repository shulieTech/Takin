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
import com.pamirs.pradar.exception.PradarException;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对实例方法参数包装拦截修改
 */
public abstract class ParametersWrapperInterceptorAdaptor extends ParametersWrapperInterceptor {
    protected final static Logger LOGGER = LoggerFactory.getLogger(ParametersWrapperInterceptorAdaptor.class.getName());

    /**
     * 拦截修改参数
     */
    @Override
    public final Object[] getParameter(Advice advice) throws Throwable {
        try {
            return getParameter0(advice);
        } catch (PradarException e) {
            LOGGER.error("", e);
            if (Pradar.isClusterTest()) {
                throw e;
            }
        } catch (PressureMeasureError e) {
            LOGGER.error("", e);
            if (Pradar.isClusterTest()) {
                throw e;
            }
        } catch (Throwable t) {
            LOGGER.error("", t);
            if (Pradar.isClusterTest()) {
                throw new PressureMeasureError(t);
            }
        }
        return advice.getParameterArray();
    }

    protected Object[] getParameter0(Advice advice) throws Throwable {
        return advice.getParameterArray();
    }
}
