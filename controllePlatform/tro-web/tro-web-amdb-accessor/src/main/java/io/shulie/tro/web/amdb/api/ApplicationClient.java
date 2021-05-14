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

package io.shulie.tro.web.amdb.api;

import java.util.List;

import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.amdb.bean.query.application.ApplicationErrorQueryDTO;
import io.shulie.tro.web.amdb.bean.query.application.ApplicationInterfaceQueryDTO;
import io.shulie.tro.web.amdb.bean.query.application.ApplicationNodeQueryDTO;
import io.shulie.tro.web.amdb.bean.query.application.ApplicationQueryDTO;
import io.shulie.tro.web.amdb.bean.result.application.ApplicationDTO;
import io.shulie.tro.web.amdb.bean.result.application.ApplicationErrorDTO;
import io.shulie.tro.web.amdb.bean.result.application.ApplicationInterfaceDTO;
import io.shulie.tro.web.amdb.bean.result.application.ApplicationNodeDTO;

/**
 * @author shiyajian
 * create: 2020-10-13
 */
public interface ApplicationClient {

    /**
     * 查询一个应用对外提供的接口信息
     * 白名单就是基于梳理的接口信息，进行选择并加入
     *
     */
    List<ApplicationInterfaceDTO> listInterfaces(ApplicationInterfaceQueryDTO query);

    /**
     * 分页
     * 查询一个应用对外提供的接口信息
     * 白名单就是基于梳理的接口信息，进行选择并加入
     * @param query
     * @return
     */
    PagingList<ApplicationInterfaceDTO> pageInterfaces(ApplicationInterfaceQueryDTO query);


    PagingList<ApplicationDTO> pageApplications(ApplicationQueryDTO query);

    PagingList<ApplicationNodeDTO> pageApplicationNodes(ApplicationNodeQueryDTO query);

    List<ApplicationErrorDTO> listErrors(ApplicationErrorQueryDTO query);

    List<String> getAllApplicationName();


}
