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

/**
 * @Auther: vernon
 * @Date: 2019/12/8 17:22
 * @Description:序列化工厂
 */
public class GenericsSerializerFactory<T> {
    public GenericsSerializer<T> serializer;

    public GenericsSerializer getSerializer() {
        if (this.serializer == null) {
            return new DefaultSerializer();
        }
        return this.serializer;
    }

    public void setSerializer(GenericsSerializer serializer) {
        this.serializer = serializer;
    }

}
