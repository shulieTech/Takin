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

package io.shulie.tro.web.app.response.fastdebug;


import java.util.List;

import io.shulie.tro.web.common.vo.fastdebug.DebugResponseVO;
import io.shulie.tro.web.common.vo.fastdebug.FastDebugCallstackAnalysisVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.http.HttpEntity;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.output.output
 * @date 2020/12/28 5:34 下午
 */
@Data
public class FastDebugResultDetailResponse {
    /**
     * 调用name
     */
    @ApiModelProperty("调用name")
    private String name;

    /**
     * 业务活动名称
     */
    private String businessLinkName;
    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;
    /**
     * 响应码
     */
    @ApiModelProperty("响应码")
    private String responseCode;
    /**
     * 调用时长
     */
    @ApiModelProperty("调用时长")
    private Long callTime;
    /**
     * 请求时间
     */
    @ApiModelProperty("请求时间")
    private String requestTime;
    /**
     * 请求地址
     */
    @ApiModelProperty("请求地址")
    private String requestUrl;
    /**
     * 请求
     */
    @ApiModelProperty("请求")
    private HttpEntity<String> request;
    /**
     * 响应
     */
    @ApiModelProperty("响应")
    private DebugResponseVO response;
    /**
     * traceId
     */
    @ApiModelProperty("traceId")
    private String traceId;
    /**
     * 是否漏数异常
     */
    @ApiModelProperty("是否漏数，true:漏数，false:正常")
    private Boolean isLeakException;

    @ApiModelProperty("错误信息")
    private String errorMessage;

    ///**
    // *调用栈错误信息
    // */
    //@ApiModelProperty("调用栈错误信息")
    //private String callStackErrorMessage;
    //Map<String, List<TraceNodeVO>> callStackMessage;

    /**
     * 调用栈分析结果
     */
    @ApiModelProperty("调用栈分析信息")
    private List<FastDebugCallstackAnalysisVO> callStackMessage;

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
    @ApiModelProperty("调试人")
    private String creatorName;

    /**
     * 调试状态
     */
    @ApiModelProperty("状态")
    private String status;
}
