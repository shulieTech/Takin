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
package com.pamirs.attach.plugin.es.common.impl;

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import org.elasticsearch.action.get.MultiGetRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/7/13 6:07 下午
 */
public class MultiGetRequestIndexRename extends AbstractReadRequestIndexRename {
    @Override
    public String getName() {
        return "multiGet";
    }

    @Override
    public List<String> reindex0(Object target) {
        MultiGetRequest mReq = (MultiGetRequest) target;
        List<MultiGetRequest.Item> list = mReq.getItems();
        List<String> indexes = new ArrayList<String>();
        for (MultiGetRequest.Item item : list) {
            String index = item.index();
            /**
             * 如果索引在白名单中，则不需要走
             */
            if (GlobalConfig.getInstance().getSearchWhiteList().contains(index)) {
                //do nothing
            } else {
                if (!Pradar.isClusterTestPrefix(index)) {
                    index = Pradar.addClusterTestPrefixLower(item.index());
                }
            }
            item.index(index);
            indexes.add(index);
        }
        return indexes;
    }

    @Override
    public List<String> getIndex0(Object target) {
        MultiGetRequest mReq = (MultiGetRequest) target;
        List<MultiGetRequest.Item> list = mReq.getItems();
        List<String> indexes = new ArrayList<String>();
        for (MultiGetRequest.Item item : list) {
            indexes.add(item.index());
        }
        return indexes;
    }
}
