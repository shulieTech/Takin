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

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.pamirs.tro.common.constant.LogLevelEnum;
import io.shulie.tro.web.data.dao.fastdebug.FastDebugStackInfoDao;
import io.shulie.tro.web.data.param.fastdebug.FastDebugStackInfoQueryParam;
import io.shulie.tro.web.data.result.fastdebug.FastDebugStackInfoResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: mubai
 * @Date: 2021-01-04 09:25
 * @Description:
 */

@Service
public class FastDebugStackInfoServiceImpl implements FastDebugStackInfoService {

    @Autowired
    private FastDebugStackInfoDao fastDebugStackInfoDao;

    @Override
    public String getStackLog(String traceId, String rpcId, Integer logType, LogLevelEnum type) {
        FastDebugStackInfoQueryParam param = new FastDebugStackInfoQueryParam();
        param.setTraceId(traceId);
        param.setRpcId(rpcId);
        param.setType(Optional.ofNullable(logType).orElse(null));
        if (null != type) {
            param.setLogLevel(type.getName());
        }
        List<FastDebugStackInfoResult> fastDebugStackInfoResults = fastDebugStackInfoDao.selectByExample(param);
        StringBuilder builder = new StringBuilder();
        if (CollectionUtils.isNotEmpty(fastDebugStackInfoResults)) {
            for (FastDebugStackInfoResult result : fastDebugStackInfoResults) {
                builder.append(result.getContent());
                builder.append(System.lineSeparator());
            }
        }
        return builder.toString();
    }

    @Override
    public void clearHistory(Date beforeDate) {
        fastDebugStackInfoDao.clearHistoryData(beforeDate);
    }

}
