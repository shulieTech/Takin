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

import io.shulie.tro.web.data.param.perfomanceanaly.PerformanceThreadQueryParam;
import io.shulie.tro.web.data.result.perfomanceanaly.PerformanceThreadCountResult;
import io.shulie.tro.web.data.result.perfomanceanaly.PerformanceThreadDataResult;

/**
 * @ClassName PerformanceThreadDataDAO
 * @Description
 * @Author qianshui
 * @Date 2020/11/4 下午2:39
 */
public interface PerformanceThreadDataDAO {

    /**
     * 图表：时间 统计线程数
     * @param baseIds
     * @return
     */
    List<PerformanceThreadCountResult> getPerformanceThreadCountList(List<String> baseIds);

    /**
     * 线程列表
     * @param param
     * @return
     */
    List<PerformanceThreadDataResult> getPerformanceThreadDataList(PerformanceThreadQueryParam param);

    /**
     * 获取线程栈数据
     * @param link
     * @return
     */
    String getThreadStackInfo(String link);

    void clearData(String time);
}
