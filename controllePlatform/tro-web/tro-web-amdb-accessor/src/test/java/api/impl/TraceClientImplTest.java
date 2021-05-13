/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package api.impl;

import com.pamirs.pradar.log.parser.trace.RpcStack;
import io.shulie.tro.utils.json.JsonHelper;
import io.shulie.tro.web.amdb.api.impl.TraceClientImpl;

/**
 * @author 无涯
 * @Package api.impl
 * @date 2020/12/29 2:27 下午
 */
public class TraceClientImplTest {

    public void test() {
        TraceClientImpl impl = new TraceClientImpl();
        RpcStack rpcStack = impl.getTraceDetailById("1d56256516092160826037680d0001");

        //System.out.println(JsonHelper.bean2Json(rpcStack));
        String s = JsonHelper.bean2Json(rpcStack);

        long start = System.currentTimeMillis();
        RpcStack rpcStack1 = JsonHelper.json2Bean(s, RpcStack.class);
        long end = System.currentTimeMillis();

        System.out.println(end - start);
        System.out.println(JsonHelper.bean2Json(rpcStack).length());
    }

}
