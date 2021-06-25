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

package io.shulie.amdb.adaptors;


import io.shulie.amdb.adaptors.common.Closeable;
import io.shulie.amdb.adaptors.connector.DataContext;

/**
 * 适配器上下文
 *
 * @author vincent
 */
public class AdaptorContext extends DataContext implements Closeable {

    private AdaptorModel model;

    /**
     * 获取model
     *
     * @return
     */
    public AdaptorModel model() {
        return model;
    }

    @Override
    public boolean close() {
        return true;
    }
}
