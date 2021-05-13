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

package io.shulie.tro.web.common.vo.fastdebug;

import java.util.List;

import io.shulie.tro.web.common.enums.fastdebug.CallStackExceptionEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.common.vo.fastdebug
 * @date 2021/3/1 10:16 上午
 */
@Data
@NoArgsConstructor
public class FastDebugCallStackExceptionVO {
    @ApiModelProperty("异常信息")
    private String exception;
    @ApiModelProperty("异常信息详情")
    private String exceptionDetail;
    @ApiModelProperty("异常节点个数")
    private Integer count;
    @ApiModelProperty("异常处理建议")
    private List<String> suggestion;
    @ApiModelProperty("异常节点明细")
    private List<FastDebugNodeExceptionVO> nodes;
    public FastDebugCallStackExceptionVO(CallStackExceptionEnum exceptionEnum,List<FastDebugNodeExceptionVO> vos) {
        this.exception =  exceptionEnum.getException();
        this.exceptionDetail = exceptionEnum.getDetail();
        this.suggestion = exceptionEnum.getSuggestion();
        this.count = vos.size();
        this.nodes = vos;
    }

}
