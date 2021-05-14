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

package io.shulie.tro.cloud.common.page;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: vernon
 * @Date: 2020/1/8 17:22
 * @Description:
 */
public class PageUtils {
    public static <T> List<T> getPage(Boolean needPage, Integer current, Integer pageSize, List<T> filteredSources) {
        if (filteredSources == null || filteredSources.isEmpty()) {
            return new ArrayList<>();
        }
        List<T> pagedTargets = new ArrayList<>();
        if (needPage) {
            Integer page = current;
            if (page < 0) {
                page = 0;
            }
            Integer offset = page * pageSize;
            if (offset <= filteredSources.size() - 1) {
                for (int index = offset, count = 0; index < filteredSources.size() && count < pageSize;
                     index++, count++) {
                    pagedTargets.add(filteredSources.get(index));
                }
            }
        } else {
            pagedTargets.addAll(filteredSources);
        }
        return pagedTargets;
    }
}
