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
package com.pamirs.attach.plugin.alibaba.druid.util;

import com.alibaba.druid.pool.DruidConnectionHolder;
import com.pamirs.attach.plugin.alibaba.druid.obj.DruidConnectionHolderBuilder;
import com.pamirs.attach.plugin.alibaba.druid.obj.impl.DruidConnectionHolder106Newer;
import com.pamirs.attach.plugin.alibaba.druid.obj.impl.DruidConnectionHolder106Older;
import com.alibaba.druid.pool.DruidAbstractDataSource;

import java.lang.reflect.Constructor;
import java.sql.Connection;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/3/8 8:42 下午
 */
public class DruidVersion {
    private static boolean isBiggerThan106x;
    private static DruidConnectionHolderBuilder builder;

    static {
        isBiggerThan106x = getLowerConstructor() != null;
        if (isBiggerThan106x) {
            builder = new DruidConnectionHolder106Newer();
        } else {
            builder = new DruidConnectionHolder106Older();
        }
    }

    private static Constructor getLowerConstructor() {
        try {
            Class clazz = DruidConnectionHolder.class;
            return clazz.getDeclaredConstructor(DruidAbstractDataSource.class, Connection.class, long.class, java.util.Map.class, java.util.Map.class);
        } catch (Throwable e) {
            return null;
        }
    }

    public static DruidConnectionHolderBuilder newBuilder() {
        return builder;
    }
}
