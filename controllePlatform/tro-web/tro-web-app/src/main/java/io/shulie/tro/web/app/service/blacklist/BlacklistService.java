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

package io.shulie.tro.web.app.service.blacklist;

import java.util.List;

import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.input.blacklist.BlacklistCreateInput;
import io.shulie.tro.web.app.input.blacklist.BlacklistSearchInput;
import io.shulie.tro.web.app.input.blacklist.BlacklistUpdateInput;
import io.shulie.tro.web.app.output.blacklist.BlacklistOutput;
import io.shulie.tro.web.common.vo.blacklist.BlacklistVO;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.service.blacklist
 * @date 2021/4/6 2:15 下午
 */
public interface BlacklistService {
    /**
     * 新增
     * @param input
     */
    void insert(BlacklistCreateInput input);

    /**
     * 更新
     * @param input
     */
    void update(BlacklistUpdateInput input);

    /**
     * 启动禁用
     * @param input
     */
    void enable(BlacklistUpdateInput input);

    /**
     * 批量启动
     * @param ids
     * @param useYn
     */
    void batchEnable(List<Long> ids, Integer useYn);

    /**
     * 删除
     * @param id
     */
    void delete(Long id);

    /**
     * 删除
     * @param id
     */
    void batchDelete(List<Long> id);

    /**
     * 分页查询
     * @return
     */
    PagingList<BlacklistVO> pageList(BlacklistSearchInput input);

    /**
     * 获取所有
     * @return
     */
    List<BlacklistOutput> selectList(BlacklistSearchInput input);

    /**
     * 根据id查询
     * @return
     */
    BlacklistOutput selectById(Long id);


}
