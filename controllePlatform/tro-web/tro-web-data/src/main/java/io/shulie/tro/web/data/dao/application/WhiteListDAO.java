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

import com.baomidou.mybatisplus.extension.service.IService;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.common.vo.whitelist.WhiteListVO;
import io.shulie.tro.web.data.model.mysql.WhiteListEntity;
import io.shulie.tro.web.data.param.whitelist.WhitelistGlobalOrPartParam;
import io.shulie.tro.web.data.param.whitelist.WhitelistSearchParam;
import io.shulie.tro.web.data.param.whitelist.WhitelistSaveOrUpdateParam;
import io.shulie.tro.web.data.result.whitelist.WhitelistResult;

/**
 * 白名单配置 dao 层
 *
 * @author liuchuan
 * @date 2021/4/8 10:50 上午
 */
public interface WhiteListDAO extends IService<WhiteListEntity> {

    /**
     * 批量插入或者更新
     * @param params
     */
    void batchSaveOrUpdate(List<WhitelistSaveOrUpdateParam> params);

    /**
     * 通过应用id, 获得白名单配置
     *
     * @param applicationId 应用id
     * @return 名单列表
     */
    List<WhitelistResult> listByApplicationId(Long applicationId);

    /**
     * 查询所有
     * @param param
     * @return
     */
    List<WhitelistResult> getList(WhitelistSearchParam param);

    /**
     * 根据id查询
     * @param wlistId
     * @return
     */
    WhitelistResult selectById(Long wlistId);

    /**
     * 白名单列表
     * @param param
     * @return
     */
    PagingList<WhiteListVO> pagingList(WhitelistSearchParam param);

    /**
     * 更新全局状态还是局部状态
     */
    void updateWhitelistGlobal(WhitelistGlobalOrPartParam param);
}
