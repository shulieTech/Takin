package io.shulie.amdb.dto;

import com.pamirs.pradar.log.parser.trace.AttributesBased;
import com.pamirs.pradar.log.parser.trace.FlagBased;
import com.pamirs.pradar.log.parser.trace.LocalAttributesBased;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@ApiModel("日志查询出参")
@Data
public class LogResultDTO {

    /**
     * 应用名称
     */
    @ApiModelProperty("应用名称")
    private String appName;

    /**
     * trace id
     */
    @ApiModelProperty("trace id")
    private String traceId;

    /**
     * 入口节点标识
     */
    @ApiModelProperty("入口节点标识")
    private String entranceNodeId;

    /**
     * 入口编号
     */
    @ApiModelProperty("入口编号")
    private String entranceId;

    /**
     * 层级
     */
    @ApiModelProperty("层级")
    private int level;

    /**
     * 父序号
     */
    @ApiModelProperty("父序号")
    private int parentIndex;

    /**
     * 序号
     */
    @ApiModelProperty("序号")
    private int index;

    /**
     * rpcId
     */
    @ApiModelProperty("rpcId")
    private String rpcId;

    /**
     * rpc类型
     */
    @ApiModelProperty("rpc类型")
    private int rpcType = 0;

    /**
     * 日志类型
     */
    @ApiModelProperty("日志类型")
    private int logType = 0;

    /**
     * 入口的app名称
     */
    @ApiModelProperty("入口的app名称")
    private String traceAppName;

    /**
     * 上游的app名称
     */
    @ApiModelProperty("上游的app名称")
    private String upAppName;

    /**
     * 开始时间的时间戳
     */
    @ApiModelProperty("开始时间的时间戳")
    private long startTime;

    /**
     * 耗时
     */
    @ApiModelProperty("耗时")
    private long cost;

    /**
     * 中间件名称
     */
    @ApiModelProperty("中间件名称")
    private String middlewareName;

    /**
     * 服务名
     */
    @ApiModelProperty("服务名")
    private String serviceName;

    /**
     * 方法名
     */
    @ApiModelProperty("方法名")
    private String methodName;

    /**
     * 远程IP
     */
    @ApiModelProperty("远程IP")
    private String remoteIp;

    /**
     * 端口
     */
    @ApiModelProperty("端口")
    private String port;

    /**
     * 结果编码
     */
    @ApiModelProperty("结果编码")
    private String resultCode;

    /**
     * 请求大小
     */
    @ApiModelProperty("请求大小")
    private long requestSize;

    /**
     * 响应大小
     */
    @ApiModelProperty("响应大小")
    private long responseSize;

    /**
     * 请求内容
     */
    @ApiModelProperty("请求内容")
    private String request;

    /**
     * 响应内容
     */
    @ApiModelProperty("响应内容")
    private String response;

    /**
     * 是否是压测流量
     */
    @ApiModelProperty("是否是压测流量")
    private boolean clusterTest;

    /**
     * 附加信息，如sql
     */
    @ApiModelProperty("附加信息，如sql")
    private String callbackMsg;

    /**
     * 采样值
     */
    private int samplingInterval = 1;

    /**
     * 本地方法追踪时的ID
     */
    @ApiModelProperty("本地方法追踪时的ID")
    private String localId;

    /**
     * 是否是异步
     */
    private boolean async;

    /**1.6日志字段**/
    /**
     * 调用Id
     */
    @ApiModelProperty("调用Id")
    private String invokeId;

    /**
     * 调用类型
     */
    @ApiModelProperty("调用类型")
    private String invokeType;

    /**
     * 异常信息
     */
    @ApiModelProperty("异常信息")
    String exceptionMsg;

    /**
     * 异常类型
     */
    @ApiModelProperty("异常类型")
    String exceptionType;
}
