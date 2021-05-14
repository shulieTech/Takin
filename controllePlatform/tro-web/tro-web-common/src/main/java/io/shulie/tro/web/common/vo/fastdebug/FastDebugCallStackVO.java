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

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.output.fastdebug
 * @date 2020/12/30 4:14 下午
 */
@Data
public class FastDebugCallStackVO {

    private Integer id;
    @ApiModelProperty("true:压测标；false:业务标")
    private Boolean clusterTest;
    /**
     * 接口（服务/方法）
     */
    @ApiModelProperty("服务/方法")
    private String interfaceName;

    private String appName;

    private String agentId;

    /**
     * 层级
     */
    @ApiModelProperty("层级")
    private Integer level;

    /**
     * 应用,中间件
     */
    @ApiModelProperty("应用与中间件")
    private List<String> appAndMiddleware;

    /**
     * 服务名
     */
    @ApiModelProperty("服务名")
    private String serviceName;


    /**
     * 状态
     */
    @ApiModelProperty("true:正常；false:异常")
    private Boolean success;

    /**
     * 结果编码
     */
    @ApiModelProperty("状态码")
    private String resultCode;

    @ApiModelProperty("true:下游节点正常；false:下游节点异常")
    private Boolean nodeSuccess;

    private String traceId;

    private String rpcId;

    /**
     * 区分服务端
     */
    @ApiModelProperty("0:客户端；1:服务端")
    private Integer logType;

    /**
     * 是否下游有未知节点
     */
    @ApiModelProperty("true:下游有未知节点；false:下游无未知节点")
    private Boolean isUpperUnknownNode;

    /**
     * 下级节点
     */
    @ApiModelProperty("递归下级数据")
    private List<FastDebugCallStackVO> nextNodes;

}
