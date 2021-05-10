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
 * 修改拦截器，主要进行参数、返回值修改
 *
 * @author xiaobin.zfb
 * @since 2020/7/1 2:04 下午
 */
public abstract class ModificationInterceptorAdaptor extends ModificationInterceptor {
    protected final static Logger LOGGER = LoggerFactory.getLogger(ModificationInterceptorAdaptor.class.getName());


    /**
     * 参数修改拦截
     *
     * @param advice 切点对象
     * @return
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

    public Object[] getParameter0(Advice advice) throws Throwable {
        return advice.getParameterArray();
    }


    /**
     * 返回值修改拦截
     *
     * @param advice 切点对象
     * @return
     */
    @Override
    public final Object getResult(Advice advice) throws Throwable {
        try {
            return getResult0(advice);
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
        return advice.getReturnObj();
    }

    public Object getResult0(Advice advice) throws Throwable {
        if (advice.getThrowable() != null) {
            throw new RuntimeException(advice.getThrowable());
        } else {
            return advice.getReturnObj();
        }
    }

    /**
     * 异常处理
     *
     * @param advice 切点对象
     */
    @Override
    public final Object getExceptionResult(Advice advice) throws Throwable {
        try {
            return getResult0(advice);
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
        if (advice.getThrowable() != null) {
            throw new RuntimeException(advice.getThrowable());
        } else {
            throw new RuntimeException();
        }
    }
}
