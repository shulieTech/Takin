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

package io.shulie.tro.cloud.open.resp.scenemanage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import io.shulie.tro.cloud.common.bean.RuleBean;
import io.shulie.tro.cloud.common.bean.TimeBean;
import io.shulie.tro.cloud.common.bean.scenemanage.SceneBusinessActivityRefBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName SceneManageWrapperDTO
 * @Description
 * @Author qianshui
 * @Date 2020/4/17 下午5:55
 */
@Data
@ApiModel(description = "场景详情出参")
public class SceneManageWrapperResp implements Serializable {

    private static final long serialVersionUID = 7324148443733465383L;

    @ApiModelProperty(value = "压测场景ID")
    private Long id;

    @ApiModelProperty(value = "客户ID")
    private Long customId;

    @ApiModelProperty(value = "压测场景名称")
    private String pressureTestSceneName;

    @ApiModelProperty(value = "业务活动配置")
    private List<SceneBusinessActivityRefResp> businessActivityConfig;

    @ApiModelProperty(value = "施压类型,0:并发,1:tps,2:自定义;不填默认为0")
    private Integer pressureType;

    @ApiModelProperty(value = "并发数量")
    private Integer concurrenceNum;

    @ApiModelProperty(value = "指定IP数")
    private Integer ipNum;

    @ApiModelProperty(value = "压测时长(秒)")
    private Long pressureTestSecond;

    @ApiModelProperty(value = "压测时长")
    private TimeBean pressureTestTime;

    @ApiModelProperty(value = "施压模式")
    @NotNull(message = "施压模式不能为空")
    private Integer pressureMode;

    @ApiModelProperty(value = "递增时长(秒)")
    private Long increasingSecond;

    @ApiModelProperty(value = "递增时长")
    private TimeBean increasingTime;

    @ApiModelProperty(value = "阶梯层数")
    private Integer step;

    @ApiModelProperty(value = "预计消耗流量")
    private BigDecimal estimateFlow;

    @ApiModelProperty(name = "scriptType", value = "脚本类型")
    private Integer scriptType;

    @ApiModelProperty(name = "uploadFile", value = "压测脚本/文件")
    private List<SceneScriptRefResp> uploadFile;

    @ApiModelProperty(name = "stopCondition", value = "SLA终止配置")
    private List<SceneSlaRefResp> stopCondition;

    @ApiModelProperty(name = "warningCondition", value = "SLA警告配置")
    private List<SceneSlaRefResp> warningCondition;

    @ApiModelProperty(name = "status", value = "压测状态")
    private Integer status;

    @ApiModelProperty(value = "总测试时长(压测时长+预热时长)")
    private transient Long totalTestTime;

    private transient String updateTime;

    private transient String lastPtTime;

    private String features;

    private Integer configType;

    private Long scriptId;

    private String BusinessFlowId;

    @ApiModelProperty(name = "executeTime",value = "定时执行时间")
    private String executeTime ;

    @ApiModelProperty(name = "tag",value = "场景标签")
    private List<String> tag ;

    @ApiModelProperty(name = "isScheduler",value = "是否为定时启动场景")
    private Boolean isScheduler ;

    @ApiModelProperty(name = "scheduleInterval", value = "漏数时间间隔")
    private Integer scheduleInterval;

    @Data
    public static class SceneBusinessActivityRefResp extends SceneBusinessActivityRefBean {

        private static final long serialVersionUID = -6384484202725660595L;

        @ApiModelProperty(value = "ID")
        private Long id;

        @ApiModelProperty(value = "绑定关系")
        private String bindRef;

        @ApiModelProperty(value = "应用IDS")
        private String applicationIds;

        private Long scriptId;

    }

    @Data
    public static class SceneSlaRefResp implements Serializable {

        private static final long serialVersionUID = 5117439939447730586L;

        @ApiModelProperty(value = "ID")
        private Long id;

        @ApiModelProperty(value = "规则名称")
        private String ruleName;

        @ApiModelProperty(value = "适用对象")
        private String[] businessActivity;

        @ApiModelProperty(value = "规则")
        private RuleBean rule;

        @ApiModelProperty(value = "状态")
        private Integer status;

        @ApiModelProperty(value = "触发事件")
        private String event;
    }

    @Data
    public static class SceneScriptRefResp implements Serializable {

        private static final long serialVersionUID = -1038145286303661484L;

        @ApiModelProperty(value = "ID")
        private Long id;

        @ApiModelProperty(value = "文件名称")
        private String fileName;

        @ApiModelProperty(value = "文件类型")
        private Integer fileType;

        @ApiModelProperty(value = "文件大小")
        private String fileSize;

        @ApiModelProperty(value = "上传时间")
        private String uploadTime;

        @ApiModelProperty(value = "上传路径")
        private String uploadPath;

        @ApiModelProperty(value = "是否删除")
        private Integer isDeleted;

        @ApiModelProperty(value = "上传数据量")
        private Long uploadedData;

        @ApiModelProperty(value = "是否拆分")
        private Integer isSplit;

        @ApiModelProperty(value = "Topic")
        private String topic;
    }

}
