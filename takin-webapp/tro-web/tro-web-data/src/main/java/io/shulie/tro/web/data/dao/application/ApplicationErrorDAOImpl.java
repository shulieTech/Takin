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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import io.shulie.tro.web.amdb.api.ApplicationClient;
import io.shulie.tro.web.amdb.bean.query.application.ApplicationErrorQueryDTO;
import io.shulie.tro.web.amdb.bean.result.application.ApplicationErrorDTO;
import io.shulie.tro.web.data.param.application.ApplicationErrorQueryParam;
import io.shulie.tro.web.data.result.application.ApplicationErrorResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: fanxx
 * @Date: 2020/10/16 11:11 上午
 * @Description:
 */
@Service
public class ApplicationErrorDAOImpl implements ApplicationErrorDAO {

    @Autowired
    private ApplicationClient applicationClient;

    @Override
    public List<ApplicationErrorResult> selectErrorList(ApplicationErrorQueryParam param) {
        List<ApplicationErrorResult> applicationErrorResultList = Lists.newArrayList();
        ApplicationErrorQueryDTO queryDTO = new ApplicationErrorQueryDTO();
        queryDTO.setAppName(param.getApplicationName());
        List<ApplicationErrorDTO> applicationErrorDTOList = applicationClient.listErrors(queryDTO);
        if (CollectionUtils.isEmpty(applicationErrorDTOList)) {
            return Collections.emptyList();
        }
        applicationErrorResultList = applicationErrorDTOList.stream().map(applicationErrorDTO -> {
            ApplicationErrorResult applicationErrorResult = new ApplicationErrorResult();
            applicationErrorResult.setExceptionId(applicationErrorDTO.getId());
            applicationErrorResult.setAgentId(applicationErrorDTO.getAgentIds());
            applicationErrorResult.setDescription(applicationErrorDTO.getDescription());
            applicationErrorResult.setCreateTime(applicationErrorDTO.getTime());
            return applicationErrorResult;
        }).collect(Collectors.toList());
        return applicationErrorResultList;
    }
}
