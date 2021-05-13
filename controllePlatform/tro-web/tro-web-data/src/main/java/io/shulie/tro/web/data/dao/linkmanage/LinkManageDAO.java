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

import io.shulie.tro.web.data.param.linkmanage.LinkManageCreateParam;
import io.shulie.tro.web.data.param.linkmanage.LinkManageQueryParam;
import io.shulie.tro.web.data.param.linkmanage.LinkManageUpdateParam;
import io.shulie.tro.web.data.result.linkmange.LinkManageResult;

/**
 * @Author: fanxx
 * @Date: 2020/10/19 7:43 下午
 * @Description:
 */
public interface LinkManageDAO {
    /**
     * 根据系统流程id查看系统流程详情
     *
     * @param id
     * @return
     */
    LinkManageResult selecLinkManageById(Long id);

    /**
     * 根据系条件批量查看系统流程
     *
     * @param queryParam
     * @return
     */
    List<LinkManageResult> selectList(LinkManageQueryParam queryParam);

    /**
     * 新增系统流程
     *
     * @param param
     * @return
     */
    int insert(LinkManageCreateParam param);

    /**
     * 指定责任人-系统流程
     * @param param
     * @return
     */
    int allocationUser(LinkManageUpdateParam param);

}
