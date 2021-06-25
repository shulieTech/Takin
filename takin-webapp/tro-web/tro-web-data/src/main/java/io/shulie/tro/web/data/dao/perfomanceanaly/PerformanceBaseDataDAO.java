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

package io.shulie.tro.web.data.dao.perfomanceanaly;

import java.util.List;

import io.shulie.tro.web.data.param.perfomanceanaly.PerformanceBaseDataParam;
import io.shulie.tro.web.data.param.perfomanceanaly.PerformanceBaseQueryParam;
import io.shulie.tro.web.data.result.perfomanceanaly.PerformanceBaseDataResult;

/**
 * @ClassName PerformanceBaseDataDAO
 * @Description
 * @Author qianshui
 * @Date 2020/11/4 下午2:39
 */
public interface PerformanceBaseDataDAO {

    /**
     * 插入db base and thread
     * @param param
     */
    void insert(PerformanceBaseDataParam param);

    /**
     * 根据appName 获取进程名称列表
     * @param param
     * @return
     */
    List<String> getProcessNameList(PerformanceBaseQueryParam param);

    /**
     * base基础信息 单条数据
     * @param param
     * @return
     */
    PerformanceBaseDataResult getOnePerformanceBaseData(PerformanceBaseQueryParam param);

    /**
     * base基础信息 列表
     * @param param
     * @return
     */
    List<PerformanceBaseDataResult> getPerformanceBaseDataList(PerformanceBaseQueryParam param);

    void clearData(String time);

}
