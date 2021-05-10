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
import org.elasticsearch.action.Action;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionType;
import org.elasticsearch.client.transport.TransportClient;

/**
 * @author vincent
 * @version v0.1 2016/12/29 11:10
 */
public class TransportClientExecuteInterceptor extends CutoffInterceptorAdaptor {

    private static boolean usingActionType;

    static {
        try {
            Class.forName("org.elasticsearch.action.ActionType");
            usingActionType = true;
        } catch (ClassNotFoundException e) {
            usingActionType = false;
        }
    }

    @Override
    public CutOffResult cutoff0(Advice advice) throws Throwable {
        ClusterTestUtils.validateClusterTest();
        if (!(advice.getTarget() instanceof TransportClient)) {
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

    private CutOffResult doShadowServerInterceptor(Advice advice) {
        Object[] args = advice.getParameterArray();
        if (args.length != 3) {
            return CutOffResult.PASSED;
        }
        if (!(args[1] instanceof ActionRequest)
            || !(args[2] instanceof ActionListener)) {
            return CutOffResult.PASSED;
        }
        try {
            TransportClient ptTransportClient =
                ShadowEsClientHolder.getShadowTransportClient((TransportClient)advice.getTarget());
            if (usingActionType) {
                ptTransportClient.execute((ActionType)args[0], (ActionRequest)args[1], (ActionListener)args[2]);
            } else {
                ptTransportClient.execute((Action)args[0], (ActionRequest)args[1], (ActionListener)args[2]);
            }
            return CutOffResult.cutoff(null);
        } catch (PressureMeasureError e) {
            LOGGER.error(e.getMessage(), e);
            ErrorReporter.buildError()
                .setErrorType(ErrorTypeEnum.ShadowEsServer)
                .setErrorCode("redisServer-0001")
                .setMessage("获取影子数据源失败！")
                .setDetail(ExceptionUtils.getStackTrace(e))
                .report();
            throw e;
        }
    }

    private CutOffResult doShadowIndexInterceptor(Advice advice) {
        Object[] args = advice.getParameterArray();
        if (args[1] == null) {
            return CutOffResult.PASSED;
        }
        RequestIndexRename requestIndexRename = RequestIndexRenameProvider.get(args[1]);
        if (requestIndexRename == null) {
            throw new PressureMeasureError("elasticsearch " + args[1].getClass().getName() + " is not supported");
        }
        requestIndexRename.reindex(args[1]);
        return CutOffResult.PASSED;
    }

}
