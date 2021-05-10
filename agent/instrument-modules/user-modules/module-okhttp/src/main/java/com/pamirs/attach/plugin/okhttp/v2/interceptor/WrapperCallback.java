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
package com.pamirs.attach.plugin.okhttp.v2.interceptor;

import com.pamirs.pradar.MiddlewareType;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.PradarSwitcher;
import com.pamirs.pradar.ResultCode;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/6/30 11:13 上午
 */
public class WrapperCallback implements Callback {
    private Callback callback;

    public WrapperCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onFailure(Request request, IOException e) {
        try {
            this.callback.onFailure(request, e);
            doException(request, e);
        } catch (Throwable exception) {
            doException(request, exception);
        }

    }

    private void doException(Request request, Throwable e) {
        if (!PradarSwitcher.isTraceEnabled()) {
            return;
        }
        if (Pradar.isResponseOn()) {
            Pradar.response(e);
        }
        Pradar.endClientInvoke(ResultCode.INVOKE_RESULT_FAILED, MiddlewareType.TYPE_RPC);
    }

    @Override
    public void onResponse(Response resp) throws IOException {
        try {
            this.callback.onResponse(resp);
            doAfter(resp.request(), resp);
        } catch (IOException e) {
            doException(resp.request(), e);
            throw e;
        } catch (Throwable e) {
            doException(resp.request(), e);
            throw new IOException(e);
        }
    }

    private void doAfter(Request request, Response response) throws IOException {
        if (!PradarSwitcher.isTraceEnabled()) {
            return;
        }

        if (Pradar.isResponseOn()) {
            Pradar.response(response.code());
        }
        final long length = response.body().contentLength();
        if (length >= 0) {
            Pradar.responseSize(length);
        }
        Pradar.endClientInvoke(ResultCode.INVOKE_RESULT_SUCCESS, MiddlewareType.TYPE_RPC);
    }
}
