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

package io.shulie.tro.web.data.dao.application;

import java.util.List;

import io.shulie.tro.web.data.param.whitelist.WhitelistAddPartAppNameParam;
import io.shulie.tro.web.data.param.whitelist.WhitelistEffectiveAppDeleteParam;
import io.shulie.tro.web.data.param.whitelist.WhitelistEffectiveAppSearchParam;
import io.shulie.tro.web.data.param.whitelist.WhitelistUpdatePartAppNameParam;
import io.shulie.tro.web.data.result.whitelist.WhitelistEffectiveAppResult;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.data.dao.application
 * @date 2021/4/14 10:15 上午
 */
public interface WhitelistEffectiveAppDao {

    /**
     * 查询生效应用
     * @param param
     * @return
     */
    List<WhitelistEffectiveAppResult> getList(WhitelistEffectiveAppSearchParam param);

    /**
     * 根据ids查询
     * @param ids
     * @return
     */
    List<WhitelistEffectiveAppResult> getListByWhiteIds(List<Long> ids);

    /**
     * 更新生效应用
     * @param params
     */
    void addPartAppName(List<WhitelistAddPartAppNameParam> params);

    /**
     * 更新生效应用
     * @param params
     */
    void updatePartAppName(List<WhitelistUpdatePartAppNameParam> params);

    /**
     *删除
     */
    void delete(WhitelistEffectiveAppDeleteParam param);

    /**
     * 批量
     * @param param
     */
    void batchDelete(WhitelistEffectiveAppDeleteParam param);
}
