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

package io.shulie.tro.web.data.dao.blacklist;

import java.util.List;

import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.common.vo.blacklist.BlacklistVO;
import io.shulie.tro.web.data.param.blacklist.BlackListCreateParam;
import io.shulie.tro.web.data.param.blacklist.BlacklistCreateNewParam;
import io.shulie.tro.web.data.param.blacklist.BlacklistSearchParam;
import io.shulie.tro.web.data.param.blacklist.BlacklistUpdateParam;
import io.shulie.tro.web.data.result.blacklist.BlacklistResult;

/**
 * @Author: fanxx
 * @Date: 2020/11/4 8:17 下午
 * @Description:
 */
public interface BlackListDAO {
    @Deprecated
    int insert(BlackListCreateParam param);

    /**
     * 新增
     * @param param
     */
    void newInsert(BlacklistCreateNewParam param);

    /**
     * 批量新增
     * @param params
     */
    void batchInsert(List<BlacklistCreateNewParam> params);

    /**
     * 更新
     * @param param
     */
    void update(BlacklistUpdateParam param);





    /**
     * 删除
     * @param id
     */
    void delete(Long id);

    /**
     * 删除
     * @param ids
     */
    void batchDelete(List<Long> ids);

    /**
     * 逻辑删
     * @param ids
     */
    @Deprecated
    void logicalDelete(List<Long> ids);

    /**
     * 分页查询
     * @return
     */
    PagingList<BlacklistVO> pageList(BlacklistSearchParam param);

    /**
     * 获取所有
     * @return
     */
    List<BlacklistResult> selectList(BlacklistSearchParam param);

    /**
     * 根据id查询
     * @return
     */
    BlacklistResult selectById(Long id);

    /**
     * 批量id查询
     * @return
     */
    List<BlacklistResult> selectByIds(List<Long> ids);

    /**
     * 说明: 查询所有启用的黑名单列表
     *
     * @return 黑名单列表
     * @author shulie
     */
    List<BlacklistResult> getAllEnabledBlockList(List<Long> appIds);



}
