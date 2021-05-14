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

package io.shulie.tro.web.app.service.mq.receive;

import java.nio.charset.Charset;

/**
 * @Auther: vernon
 * @Date: 2019/12/8 17:23
 * @Description:
 */
public class DefaultSerializer implements GenericsSerializer {

    private static final String UTF_8 = "UTF-8";

    @Override
    public byte[] serialize(Object var1) {
        return var1.toString().getBytes(Charset.forName(UTF_8));
    }

    @Override
    public Object unserialize(byte[] var1) {
        return new String(var1, Charset.forName(UTF_8));
    }
}
