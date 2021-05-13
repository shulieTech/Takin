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

package io.shulie.tro.web.data.dao.linkmanage;

import java.util.List;

import io.shulie.tro.web.data.param.linkmanage.BusinessLinkManageCreateParam;
import io.shulie.tro.web.data.param.linkmanage.BusinessLinkManageQueryParam;
import io.shulie.tro.web.data.param.linkmanage.BusinessLinkManageUpdateParam;
import io.shulie.tro.web.data.result.linkmange.BusinessLinkResult;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: fanxx
 * @Date: 2020/10/16 5:21 下午
 * @Description: 业务活动dao
 */
public interface BusinessLinkManageDAO {

    BusinessLinkResult selectBussinessLinkById(@Param("id") Long id);

    List<BusinessLinkResult> selectBussinessLinkByIdList(@Param("ids") List<Long> ids);

    List<BusinessLinkResult> selectList(BusinessLinkManageQueryParam queryParam);

    int insert(BusinessLinkManageCreateParam param);

    /**
     * 指定责任人-业务活动
     *
     * @param param
     * @return
     */
    int allocationUser(BusinessLinkManageUpdateParam param);

    /**
     * 获取列表
     * @return
     */
    List<BusinessLinkResult> getList();

    /**
     * 获取列表根据ids
     * @return
     */
    List<BusinessLinkResult> getListByIds(List<Long> ids);
}
