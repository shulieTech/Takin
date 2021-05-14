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

package io.shulie.tro.web.data.dao.fastdebug;

import java.util.List;

import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.common.vo.fastdebug.FastDebugExceptionVO;
import io.shulie.tro.web.data.param.fastdebug.FastDebugConfigCreateParam;
import io.shulie.tro.web.data.param.fastdebug.FastDebugConfigSearchParam;
import io.shulie.tro.web.data.param.fastdebug.FastDebugConfigUpdateParam;
import io.shulie.tro.web.data.param.fastdebug.FastDebugExceptionParam;
import io.shulie.tro.web.data.param.fastdebug.FastDebugExceptionSearchParam;
import io.shulie.tro.web.data.param.fastdebug.FastDebugResultParam;
import io.shulie.tro.web.data.param.fastdebug.FastDebugResultSearchParam;
import io.shulie.tro.web.data.param.fastdebug.FastDebugResultUpdateParam;
import io.shulie.tro.web.data.result.fastdebug.FastDebugConfigResult;
import io.shulie.tro.web.data.result.fastdebug.FastDebugResult;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.data.dao.fastdebug
 * @date 2020/12/28 9:49 上午
 */
public interface FastDebugDao {
    /**
     * 创建
     * @param param
     */
     Long createFastDebugConfig(FastDebugConfigCreateParam param);

    /**
     * 删除
     * @param id
     */
     void deleteFastDebugConfig(Long id);

    /**
     * 更新
     * @param param
     */
    void updateFastDebugConfig(FastDebugConfigUpdateParam param);

    /**
     * 查询
     * @param id
     * @return
     */
    FastDebugConfigResult selectById(Long id);

    /**
     * 查询
     * @param name
     * @return
     */
    FastDebugConfigResult selectByName(String name);

    /**
     * 查询
     * @param param
     * @return
     */
    PagingList<FastDebugConfigResult> selectList(FastDebugConfigSearchParam param);

    /**
     * 通过ids获取
     * @param ids
     * @return
     */
    List<FastDebugConfigResult> getConfigByIds(List<Long> ids);

    /**
     * 查询debugList
     * @param param
     * @return
     */
    PagingList<FastDebugResult> selectDebugList(FastDebugResultSearchParam param);

    /**
     * 插入debugResult
     * @param param
     * @return
     */
    FastDebugResult insertDebugResult(FastDebugResultParam param);

    /**
     * 批量插入配置异常
     * @param params
     */
    void batchInsertException(List<FastDebugExceptionParam> params);

    /**
     * 插入配置异常
     * @param param
     */
    void insertException(FastDebugExceptionParam param);

    /**
     * 获取配置异常信息
     * @param traceId
     * @param rpcId
     * @param appName
     * @param description
     * @return
     */
    List<FastDebugExceptionVO> getException(String traceId,String rpcId,String appName,String description);

    /**
     * 获取配置异常
     * @param param
     * @return
     */
    PagingList<FastDebugExceptionVO> getExceptionPage(FastDebugExceptionSearchParam param);

    /**
     * 获取配置异常信息
     * @param traceId
     * @param appName
     * @param customerId
     * @return
     */
    Long getExceptionCount(String traceId,Long customerId,String appName);

    /**
     * 更新调试结果
     * @param param
     */
    void updateFastDebugResult(FastDebugResultUpdateParam param);

    /**
     * 查询debug详细
     * @param id
     * @return
     */
    FastDebugResult selectDebugResultById(Long id);

    /**
     * 删除调试记录
     * @param id
     */
    void  deleteFastDebugResultById(Long id);
}
