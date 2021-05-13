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

package io.shulie.tro.web.app.service.linkManage;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.pamirs.tro.entity.domain.dto.linkmanage.InterfaceVo;
import com.pamirs.tro.entity.domain.entity.TWList;
import com.pamirs.tro.entity.domain.query.whitelist.WhiteListCreateListVO;
import com.pamirs.tro.entity.domain.query.whitelist.WhiteListOperateVO;
import com.pamirs.tro.entity.domain.query.whitelist.WhiteListQueryVO;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.input.whitelist.WhitelistImportFromExcelInput;
import io.shulie.tro.web.app.input.whitelist.WhitelistSearchInput;
import io.shulie.tro.web.app.input.whitelist.WhitelistUpdatePartAppNameInput;
import io.shulie.tro.web.app.request.WhiteListDeleteRequest;
import io.shulie.tro.web.app.request.whitelist.WhiteListUpdateRequest;
import io.shulie.tro.web.common.vo.whitelist.WhiteListVO;
import io.shulie.tro.web.common.vo.whitelist.WhitelistPartVO;
import io.shulie.tro.web.data.result.application.ApplicationDetailResult;

/**
 * @Author: mubai
 * @Date: 2020-04-20 19:12
 * @Description:
 */
public interface WhiteListService {

    /**
     * excel导入
     * @param inputs
     */
    void importWhiteListFromExcel(List<WhitelistImportFromExcelInput> inputs);

    void saveWhitelist(WhiteListCreateListVO vo);

    void operateWhitelist(WhiteListOperateVO vo);

    /**
     * 全局白名单
     * @return
     */
    List<String> getExistWhite(List<String> interfaceNames,List<ApplicationDetailResult> appDetailResults);

    PageInfo<WhiteListVO> queryWhitelist(WhiteListQueryVO vo);

    List<TWList> getAllEnableWhitelists(String applicationId);

    void updateWhitelist(WhiteListUpdateRequest request);

    void deleteWhitelist(WhiteListDeleteRequest request);

    /**
     * 应用名
     * @param wlistId
     * @return
     */
    WhitelistPartVO getPart(Long wlistId);

    /**
     * 局部，生效应用
     * @param input
     */
    void part(WhitelistUpdatePartAppNameInput input);

    /**
     * 全局
     * @param wlistId
     */
    void global(Long wlistId);


    /*******************************白名单列表*********************************/
    /**
     *
     * @param input
     * @return
     */
    PagingList<WhiteListVO> pagingList(WhitelistSearchInput input);
    /*******************************白名单列表*********************************/


    /**
     * 所有的 agent 上报的白名单
     *
     * @param appName 应用名称
     * @return 白名单
     */
    List<InterfaceVo> getAllInterface(String appName);
}
