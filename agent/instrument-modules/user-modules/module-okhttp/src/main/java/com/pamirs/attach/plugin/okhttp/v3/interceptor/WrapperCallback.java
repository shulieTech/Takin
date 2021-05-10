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
package com.pamirs.attach.plugin.okhttp.v3.interceptor;

import com.pamirs.pradar.MiddlewareType;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.PradarSwitcher;
import com.pamirs.pradar.ResultCode;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
    public void onFailure(Call call, IOException e) {
        try {
            this.callback.onFailure(call, e);
            doException(call, e);
        } catch (Throwable exception) {
            doException(call, exception);
        }

    }

    private void doException(Call call, Throwable e) {
        if (!PradarSwitcher.isTraceEnabled()) {
            return;
        }
        if (Pradar.isResponseOn()) {
            Pradar.response(e);
        }
        Pradar.endClientInvoke(ResultCode.INVOKE_RESULT_FAILED, MiddlewareType.TYPE_RPC);
    }

    @Override
    public void onResponse(Call call, Response resp) throws IOException {
        try {
            this.callback.onResponse(call, resp);
            doAfter(call, resp);
        } catch (IOException e) {
            doException(call, e);
            throw e;
        } catch (Throwable e) {
            doException(call, e);
            throw new IOException(e);
        }
    }

    private void doAfter(Call call, Response resp) {
        if (!PradarSwitcher.isTraceEnabled()) {
            return;
        }

        if (Pradar.isResponseOn()) {
            Pradar.response(resp.code());
        }
        long length = resp.body().contentLength();
        if (length >= 0) {
            Pradar.responseSize(length);
        }
        Pradar.endClientInvoke(ResultCode.INVOKE_RESULT_SUCCESS, MiddlewareType.TYPE_RPC);
    }
}
