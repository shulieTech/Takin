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

package io.shulie.tro.web.data.dao.fastdebug;

import java.util.Date;
import java.util.List;

import io.shulie.tro.web.data.param.fastdebug.FastDebugStackInfoCreateParam;
import io.shulie.tro.web.data.param.fastdebug.FastDebugStackInfoQueryParam;
import io.shulie.tro.web.data.result.fastdebug.FastDebugStackInfoResult;

/**
 * @Author: mubai
 * @Date: 2020-12-28 11:58
 * @Description:
 */
public interface FastDebugStackInfoDao {

    void insert(FastDebugStackInfoCreateParam createParam);

    void insert(List<FastDebugStackInfoCreateParam> fastDebugStackInfoCreateParamList) ;

    /**
     * 获取agent上传数据
     * @param param
     * @return
     */
    List<FastDebugStackInfoResult> selectByExample(FastDebugStackInfoQueryParam param);

    /**
     * 计算个数
     * @param param
     * @return
     */
    Long selectByExampleCount (FastDebugStackInfoQueryParam param);

    /**
     *清除历史数据
     * @param beforeDate
     */
    void clearHistoryData(Date beforeDate);

    /**
     * 根据id获取
     * @param id
     * @return
     */
    FastDebugStackInfoResult getById(Long id);
}
