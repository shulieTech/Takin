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

package io.shulie.tro.web.app.service.fastdebug;

import java.util.List;

import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.input.fastdebug.FastDebugCallStackDetailSearchInput;
import io.shulie.tro.web.app.input.fastdebug.FastDebugConfigCreateInput;
import io.shulie.tro.web.app.input.fastdebug.FastDebugConfigSearchInput;
import io.shulie.tro.web.app.input.fastdebug.FastDebugConfigUpdateInput;
import io.shulie.tro.web.app.input.fastdebug.FastDebugExceptionSearchInput;
import io.shulie.tro.web.app.input.fastdebug.FastDebugResultSearchInput;
import io.shulie.tro.web.app.output.fastdebug.FastDebugCallStackDetailOutput;
import io.shulie.tro.web.app.output.fastdebug.FastDebugConfigDetailOutput;
import io.shulie.tro.web.app.output.fastdebug.FastDebugConfigLeakSqlOutput;
import io.shulie.tro.web.app.output.fastdebug.FastDebugExceptionOutput;
import io.shulie.tro.web.app.output.fastdebug.FastDebugLocateCallstackOutput;
import io.shulie.tro.web.app.output.fastdebug.FastDebugResultDetailOutput;
import io.shulie.tro.web.app.output.fastdebug.FastDebugResultLeakSqlOutput;
import io.shulie.tro.web.app.output.fastdebug.FastDebugResultOutput;
import io.shulie.tro.web.common.vo.component.CascaderVO;
import io.shulie.tro.web.common.vo.fastdebug.FastDebugCallStackExceptionVO;
import io.shulie.tro.web.common.vo.fastdebug.FastDebugCallStackVO;
import io.shulie.tro.web.common.vo.fastdebug.FastDebugExceptionVO;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.service.fastdebug
 * @date 2020/12/28 9:45 上午
 */
public interface FastDebugService {

    /**
     * 创建
     *
     * @param input
     * @return
     */
    Long createFastDebugConfig(FastDebugConfigCreateInput input);

    /**
     * 删除
     *
     * @param id
     */
    void deleteFastDebugConfig(Long id);

    /**
     * 更新
     *
     * @param input
     */
    void updateFastDebugConfig(FastDebugConfigUpdateInput input);

    /**
     * 查询
     *
     * @param id
     * @return
     */
    FastDebugConfigDetailOutput selectById(Long id);

    /**
     * 根据业务活动获取
     *
     * @param businessLinkId
     * @return
     */
    List<FastDebugConfigLeakSqlOutput> getLeakageCheckData(Long businessLinkId);

    /**
     * 查询
     *
     * @param input
     * @return
     */
    PagingList<FastDebugConfigDetailOutput> selectList(FastDebugConfigSearchInput input);

    /**
     * 查询debugList
     *
     * @param input
     * @return
     */
    PagingList<FastDebugResultOutput> selectDebugList(FastDebugResultSearchInput input);

    /**
     * 获取debug详情
     *
     * @param id
     * @return
     */
    FastDebugResultDetailOutput selectDebugById(Long id);

    /**
     * 根据id 删除
     *
     * @param id
     */
    void deleteFastDebugResult(Long id);

    /**
     * 获取配置异常
     *
     * @param input
     * @return
     */
    PagingList<FastDebugExceptionVO> getExceptionPage(FastDebugExceptionSearchInput input);

    /**
     * 获取配置异常 下拉内容
     *
     * @param input
     * @return
     */
    FastDebugExceptionOutput getExceptionSearch(FastDebugExceptionSearchInput input);

    /**
     * 获取应用实例
     * @param traceId
     * @return
     */
    List<CascaderVO> getExamples(String traceId);

    /**
     * 开始调试
     *
     * @param id
     */
    Long debug(Long id);

    /**
     * 调试并保存
     *
     * @param input
     * @return
     */
    Long debugAndSave(FastDebugConfigCreateInput input);

    /**
     * 调试并更新
     *
     * @param input
     * @return
     */
    Long debugAndUpdate(FastDebugConfigUpdateInput input);

    /**
     * 获取调用栈信息
     *
     * @param traceId
     * @param id
     * @return
     */
    List<FastDebugCallStackVO> getCallStack(String traceId, Integer id);

    /**
     *  获取调用栈信息异常
     * @param resultId
     * @param traceId
     * @param type
     * @return
     */
    List<FastDebugCallStackExceptionVO> getCallStackException(Long resultId,String traceId, Integer type);

    /**
     * 节点定位
     * @param nodeId
     * @return
     */
    FastDebugLocateCallstackOutput locateCallstackNode(Long nodeId);

    /**
     * 调用栈详情
     *
     * @param input
     * @return
     */
    FastDebugCallStackDetailOutput getCallStackDetail(FastDebugCallStackDetailSearchInput input);

    /**
     * 获取漏数结果
     *
     * @param resultId
     * @return
     */
    List<FastDebugResultLeakSqlOutput> getLeakageCheckDataResult(Long resultId);

    /**
     * 导入
     *
     * @param path
     */
    void importExcel(String path);

}
