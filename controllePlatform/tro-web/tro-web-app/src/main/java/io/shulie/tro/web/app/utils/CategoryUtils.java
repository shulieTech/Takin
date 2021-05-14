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

package io.shulie.tro.web.app.utils;

import com.pamirs.tro.entity.domain.entity.linkmanage.structure.Category;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: mubai
 * @Date: 2020-07-02 14:42
 * @Description:
 */
public class CategoryUtils {

    /**
     * 组装前端需要的展示数据
     */
    public static void assembleVo(Category tree) {
        if (tree == null) {
            return;
        }
        String serviceName = tree.getServiceName();
        if (StringUtils.isNotBlank(serviceName) && serviceName.contains("|")) {
            String[] split = serviceName.split("\\|");
            if (split.length < 3) {
                return;
            }
            tree.setServiceName(split[0]);
            tree.setServiceType(split[1]);
            tree.setServiceDetail(split[2]);
            if (CollectionUtils.isEmpty(tree.getChildren())) {
                tree.setChildren(null);
            }
        }
        if (CollectionUtils.isEmpty(tree.getChildren())) {
            return;
        }
        for (Category child : tree.getChildren()) {
            assembleVo(child);
        }
    }

}
