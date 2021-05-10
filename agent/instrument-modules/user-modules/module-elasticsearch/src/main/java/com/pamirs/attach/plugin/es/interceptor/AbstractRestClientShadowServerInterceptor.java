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
package com.pamirs.attach.plugin.es.interceptor;

import java.io.IOException;

import com.pamirs.attach.plugin.es.common.RequestIndexRename;
import com.pamirs.attach.plugin.es.common.RequestIndexRenameProvider;
import com.pamirs.attach.plugin.es.shadowserver.ShadowEsClientHolder;
import com.pamirs.pradar.CutOffResult;
import com.pamirs.pradar.ErrorTypeEnum;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.interceptor.CutoffInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.pamirs.pradar.pressurement.agent.shared.service.ErrorReporter;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.elasticsearch.client.RestClient;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/04/12 11:51 上午
 */
public abstract class AbstractRestClientShadowServerInterceptor extends CutoffInterceptorAdaptor {

    @Override
    public CutOffResult cutoff0(Advice advice) throws Throwable {
        ClusterTestUtils.validateClusterTest();
        Object target = advice.getTarget();
        if (!(target instanceof RestClient)) {
            return CutOffResult.PASSED;
        }
        if (!Pradar.isClusterTest()) {
            return CutOffResult.PASSED;
        }
        if (GlobalConfig.getInstance().isShadowEsServer()) {
            return doShadowServerInterceptor(advice);
        } else {
            return doShadowIndexInterceptor(advice);
        }
    }

    protected CutOffResult doShadowIndexInterceptor(Advice advice) {
        Object[] args = advice.getParameterArray();
        RequestIndexRename requestIndexRename = RequestIndexRenameProvider.get(args[0]);
        if (requestIndexRename == null) {
            throw new PressureMeasureError("elasticsearch " + args[0].getClass().getName() + " is not supported!");
        }
        if (requestIndexRename.supportedDirectReindex(args[0])) {
            requestIndexRename.reindex(args[0]);
        } else {
            Object index = requestIndexRename.indirectIndex(args[0]);
            advice.changeParameter(0, index);
        }
        return CutOffResult.PASSED;
    }

    protected CutOffResult doShadowServerInterceptor(Advice advice) {
        Object target = advice.getTarget();
        String methodName = advice.getBehavior().getName();
        Object[] args = advice.getParameterArray();
        if (!doCheck(target, methodName, args)) {
            return CutOffResult.PASSED;
        }
        try {
            RestClient restClient = ShadowEsClientHolder.getShadowRestClient((RestClient)target);
            return CutOffResult.cutoff(doCutoff(restClient, methodName, args));
        } catch (PressureMeasureError e) {
            LOGGER.error(e.getMessage(), e);
            ErrorReporter.buildError()
                .setErrorType(ErrorTypeEnum.ShadowEsServer)
                .setErrorCode("redisServer-0001")
                .setMessage("获取影子数据源失败！")
                .setDetail(ExceptionUtils.getStackTrace(e))
                .report();
            throw e;
        } catch (IOException e) {
            throw new PressureMeasureError(e);
        }
    }

    protected abstract Object doCutoff(RestClient restClient, String methodName, Object[] args) throws IOException;

    protected abstract boolean doCheck(Object target, String methodName, Object[] args);
}
