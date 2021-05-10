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
package com.pamirs.attach.plugin.es.common;

import com.pamirs.pradar.Pradar;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/7/13 6:08 下午
 */
public abstract class AbstractRequestIndexRename implements RequestIndexRename {

    /**
     * 是否支持直接修改索引
     *
     * @return
     */
    @Override
    public boolean supportedDirectReindex(Object target) {
        return true;
    }

    /**
     * 间接修改索引,替换原有对象
     *
     * @param target
     * @return
     */
    @Override
    public Object indirectIndex(Object target) {
        return target;
    }

    protected String[] toClusterTestIndex(String[] indexes) {

        String[] newIndexes = new String[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            newIndexes[i] = Pradar.isClusterTestPrefix(indexes[i]) ?
                    indexes[i]
                    : Pradar.addClusterTestPrefixLower(indexes[i]);
        }
        return newIndexes;
    }

    protected String toIndexString(String[] indexes) {
        if (indexes == null || indexes.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String idx : indexes) {
            builder.append(idx).append(',');
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }
}
