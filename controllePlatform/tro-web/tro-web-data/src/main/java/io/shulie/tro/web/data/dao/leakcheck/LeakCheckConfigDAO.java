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

import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.data.param.leakcheck.LeakCheckConfigCreateParam;
import io.shulie.tro.web.data.param.leakcheck.LeakCheckConfigDeleteParam;
import io.shulie.tro.web.data.param.leakcheck.LeakCheckConfigQueryParam;
import io.shulie.tro.web.data.param.leakcheck.LeakCheckConfigSingleQueryParam;
import io.shulie.tro.web.data.param.leakcheck.LeakCheckConfigUpdateParam;
import io.shulie.tro.web.data.result.leakcheck.LeakCheckConfigResult;

/**
 * @Author: fanxx
 * @Date: 2020/12/31 2:29 下午
 * @Description:
 */
public interface LeakCheckConfigDAO {
    /**
     * 分页查询漏数配置
     *
     * @param queryParam
     * @return
     */
    PagingList<LeakCheckConfigResult> selectPage(LeakCheckConfigQueryParam queryParam);

    /**
     * 新增漏数配置
     *
     * @param createParam
     * @return
     */
    int insert(LeakCheckConfigCreateParam createParam);

    /**
     * 更新漏数配置
     *
     * @param updateParam
     * @return
     */
    int update(LeakCheckConfigUpdateParam updateParam);

    /**
     * 删除漏数配置
     *
     * @param deleteParam
     * @return
     */
    int delete(LeakCheckConfigDeleteParam deleteParam);

    /**
     * 查询单个配置
     *
     * @param singleQueryParam
     * @return
     */
    LeakCheckConfigResult selectSingle(LeakCheckConfigSingleQueryParam singleQueryParam);

    /**
     * 批量查询配置
     *
     * @param queryParam
     * @return
     */
    List<LeakCheckConfigResult> selectList(LeakCheckConfigQueryParam queryParam);
}
