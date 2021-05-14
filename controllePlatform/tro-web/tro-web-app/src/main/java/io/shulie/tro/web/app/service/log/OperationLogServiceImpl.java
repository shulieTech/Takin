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

package io.shulie.tro.web.app.service.log;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.request.log.OperationLogCreateRequest;
import io.shulie.tro.web.app.request.log.OperationLogQueryRequest;
import io.shulie.tro.web.app.response.log.OperationLogResponse;
import io.shulie.tro.web.data.dao.log.OperationLogDAO;
import io.shulie.tro.web.data.param.log.OperationLogCreateParam;
import io.shulie.tro.web.data.param.log.OperationLogQueryParam;
import io.shulie.tro.web.data.result.log.OperationLogResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/9/23 8:35 下午
 * @Description:
 */
@Component
@Slf4j
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    OperationLogDAO operationLogDAO;

    @Override
    public PagingList<OperationLogResponse> list(OperationLogQueryRequest queryRequest) {

        OperationLogQueryParam queryParam = new OperationLogQueryParam();
        BeanUtils.copyProperties(queryRequest, queryParam);
        PagingList<OperationLogResult> pagingList = operationLogDAO.selectPage(queryParam);
        List<OperationLogResponse> responseList = pagingList.getList().stream()
            .map(operationLogResult -> {
                    OperationLogResponse operationLogResponse = new OperationLogResponse();
                    operationLogResponse.setModules(
                        Lists.newArrayList(operationLogResult.getModule(), operationLogResult.getSubModule())
                    );
                    operationLogResponse.setContent(operationLogResult.getContent());
                    operationLogResponse.setStartTime(operationLogResult.getStartTime());
                    operationLogResponse.setType(operationLogResult.getType());
                    operationLogResponse.setUserName(operationLogResult.getUserName());
                    return operationLogResponse;
                }
            ).collect(Collectors.toList());

        return PagingList.of(responseList, pagingList.getTotal());
    }

    @Override
    public void record(OperationLogCreateRequest createRequest) {
        OperationLogCreateParam createParam = new OperationLogCreateParam();
        BeanUtils.copyProperties(createRequest, createParam);
        operationLogDAO.insert(createParam);
    }
}
