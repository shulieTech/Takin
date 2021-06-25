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

package io.shulie.tro.web.data.dao.leakcheck;

import java.util.List;

import io.shulie.tro.web.data.param.leakcheck.LeakCheckConfigDetailCreateParam;
import io.shulie.tro.web.data.param.leakcheck.LeakCheckConfigDetailDeleteParam;
import io.shulie.tro.web.data.param.leakcheck.LeakCheckConfigDetailQueryParam;
import io.shulie.tro.web.data.param.leakcheck.LeakCheckConfigDetailUpdateParam;
import io.shulie.tro.web.data.result.leakcheck.LeakCheckConfigBatchDetailResult;
import io.shulie.tro.web.data.result.leakcheck.LeakCheckConfigSingleDetailResult;

/**
 * @Author: fanxx
 * @Date: 2020/12/31 2:38 下午
 * @Description:
 */
public interface LeakCheckConfigDetailDAO {
    /**
     * 查询漏数配置详情列表(单个数据源)
     *
     * @param queryParam
     * @return
     */
    LeakCheckConfigSingleDetailResult selectSingle(LeakCheckConfigDetailQueryParam queryParam);

    /**
     * 查询漏数配置详情列表(多个数据源)
     *
     * @param queryParam
     * @return
     */
    List<LeakCheckConfigBatchDetailResult> selectList(LeakCheckConfigDetailQueryParam queryParam);

    /**
     * 新增漏数配置详情
     *
     * @param createParam
     * @return
     */
    List<Long> insert(LeakCheckConfigDetailCreateParam createParam);

    /**
     * 更新漏数配置详情
     *
     * @param updateParam
     * @return
     */
    int update(LeakCheckConfigDetailUpdateParam updateParam);

    /**
     * 删除漏数配置详情
     *
     * @param deleteParam
     * @return
     */
    int delete(LeakCheckConfigDetailDeleteParam deleteParam);
}
