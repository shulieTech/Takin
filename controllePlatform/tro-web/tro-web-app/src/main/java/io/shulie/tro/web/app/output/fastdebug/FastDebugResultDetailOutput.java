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

package io.shulie.tro.web.app.output.fastdebug;

import java.util.List;
import java.util.Map;

import io.shulie.tro.web.common.vo.fastdebug.DebugResponseVO;
import io.shulie.tro.web.common.vo.fastdebug.FastDebugCallstackAnalysisVO;
import io.shulie.tro.web.common.vo.fastdebug.TraceNodeVO;
import lombok.Data;
import org.springframework.http.HttpEntity;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.output.output
 * @date 2020/12/28 5:34 下午
 */
@Data
public class FastDebugResultDetailOutput {
    private String name;
    private Long id;
    /**
     * 业务活动名称
     */
    private String businessLinkName;
    private String responseCode;
    private Long callTime;
    private String requestTime;
    private String requestUrl;
    private HttpEntity<String> request;
    private DebugResponseVO response;
    private String traceId;
    /**
     * 是否漏数异常
     */
    private Boolean isLeakException;
    private String errorMessage;
    /**
     * 调用栈分析结果
     */
    private List<FastDebugCallstackAnalysisVO> callStackMessage;
    //private Map<String, List<TraceNodeVO>> callStackMessage;
    /**
     * 配置异常数
     */
    private Long exceptionCount;

    /**
     * 调用栈异常数
     */
    private Long callStackExceptionCount;

    /**
     * 数据验证异常数
     */
    private Integer checkDataCount;

    /**
     * 调试人
     */
    private String creatorName;

    /**
     * 调试状态
     */
    private String status;
}
