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
package com.shulie.instrument.simulator.thread.interceptor;

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.ResultCode;
import com.pamirs.pradar.interceptor.AroundInterceptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.resource.DynamicFieldManager;
import com.shulie.instrument.simulator.thread.ThreadConstants;

import javax.annotation.Resource;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/1/22 8:12 下午
 */
public class CallableCallInterceptor extends AroundInterceptor {
    @Resource
    protected DynamicFieldManager manager;

    @Override
    public void doBefore(Advice advice) throws Throwable {
        Object context = manager.getDynamicField(advice.getTarget(), ThreadConstants.DYNAMIC_FIELD_CONTEXT);
        Object threadId = manager.getDynamicField(advice.getTarget(), ThreadConstants.DYNAMIC_FIELD_THREAD_ID);
        if (threadId == null) {
            return;
        }
        if (!(threadId instanceof Long)) {
            return;
        }
        Long tid = (Long) threadId;
        if (context != null && Thread.currentThread().getId() != tid) {
            Pradar.setInvokeContext(context);
        }
    }

    @Override
    public void doAfter(Advice advice) throws Throwable {
        try {
            Object context = manager.getDynamicField(advice.getTarget(), ThreadConstants.DYNAMIC_FIELD_CONTEXT);
            Object threadId = manager.getDynamicField(advice.getTarget(), ThreadConstants.DYNAMIC_FIELD_THREAD_ID);
            if (threadId == null) {
                return;
            }
            if (!(threadId instanceof Long)) {
                return;
            }
            Long tid = (Long) threadId;
            if (context != null && Thread.currentThread().getId() != tid) {
                Pradar.clearInvokeContext();
            }
        } finally {
            manager.removeAll(advice.getTarget());
        }
        if (Pradar.isThreadCommit()) {
            Pradar.endServerInvoke(ResultCode.INVOKE_RESULT_SUCCESS);
        }
    }

    @Override
    public void doException(Advice advice) throws Throwable {
        try {
            Object context = manager.getDynamicField(advice.getTarget(), ThreadConstants.DYNAMIC_FIELD_CONTEXT);
            Object threadId = manager.getDynamicField(advice.getTarget(), ThreadConstants.DYNAMIC_FIELD_THREAD_ID);
            if (threadId == null) {
                return;
            }
            if (!(threadId instanceof Long)) {
                return;
            }
            Long tid = (Long) threadId;
            if (context != null && Thread.currentThread().getId() != tid) {
                Pradar.clearInvokeContext();
            }
        } finally {
            manager.removeAll(advice.getTarget());
        }
        if (Pradar.isThreadCommit()) {
            Pradar.endServerInvoke(ResultCode.INVOKE_RESULT_FAILED);
        }
    }
}
