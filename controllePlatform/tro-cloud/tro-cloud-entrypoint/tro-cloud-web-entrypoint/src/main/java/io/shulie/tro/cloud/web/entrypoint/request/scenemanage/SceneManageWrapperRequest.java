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

package io.shulie.tro.cloud.web.entrypoint.request.scenemanage;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.shulie.tro.cloud.common.bean.RuleBean;
import io.shulie.tro.cloud.common.bean.TimeBean;
import io.shulie.tro.cloud.web.entrypoint.request.webauth.WebAuthRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName SceneManageWrapperRequest
 * @Description
 * @Author qianshui
 * @Date 2020/4/17 下午5:55
 */
@Data
@ApiModel(description = "场景保存入参")
public class SceneManageWrapperRequest extends WebAuthRequest {

    private static final long serialVersionUID = -7653146473491831687L;

    @ApiModelProperty(name = "id", value = "压测场景ID")
    private Long id;

    @ApiModelProperty(value = "客户Id")
    private Long customId;

    @ApiModelProperty(value = "压测场景名称")
    @NotNull(message = "压测场景名称不能为空")
    private String pressureTestSceneName;

    @ApiModelProperty(value = "施压类型,0:并发,1:tps,2:自定义;不填默认为0")
    private Integer pressureType;

    @ApiModelProperty(value = "场景类型")
    private Integer configType;  //1、业务活动格式；2、业务流程格式

    @ApiModelProperty(value = "脚本id")
    private Long scriptId;

    @ApiModelProperty(value = "业务活动配置")
    @NotEmpty(message = "业务活动配置不能为空")
    private List<SceneBusinessActivityRefRequest> businessActivityConfig;

    @ApiModelProperty(value = "并发数量")
    @NotNull(message = "并发数量不能为空")
    private Integer concurrenceNum;

    @ApiModelProperty(value = "指定IP数")
    @NotNull(message = "指定IP数不能为空")
    private Integer ipNum;

    @ApiModelProperty(value = "压测时长")
    @NotNull(message = "压测时长不能为空")
    private TimeBean pressureTestTime;

    @ApiModelProperty(value = "施压模式")
    @NotNull(message = "施压模式不能为空")
    private Integer pressureMode;

    @ApiModelProperty(value = "递增时长")
    private TimeBean increasingTime;

    @ApiModelProperty(value = "阶梯层数")
    private Integer step;

    @ApiModelProperty(value = "脚本类型")
    @NotNull(message = "脚本类型不能为空")
    private Integer scriptType; //压测引擎类型，jmx活着其他

    @ApiModelProperty(name = "uploadFile", value = "压测脚本/文件")
    @NotEmpty(message = "压测脚本/文件不能为空")
    private List<SceneScriptRefRequest> uploadFile;

    @ApiModelProperty(name = "stopCondition", value = "SLA终止配置")
    @NotEmpty(message = "SLA终止配置不能为空")
    private List<SceneSlaRefRequest> stopCondition;

    @ApiModelProperty(name = "warningCondition", value = "SLA警告配置")
    private List<SceneSlaRefRequest> warningCondition;

    @Data
    public static class SceneScriptRefRequest implements Serializable {

        private static final long serialVersionUID = -2991318843153108331L;

        @ApiModelProperty(value = "ID")
        private Long id;

        @ApiModelProperty(value = "上传ID")
        private String uploadId;

        @ApiModelProperty(value = "文件名称")
        private String fileName;

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

        @ApiModelProperty(value = "文件类型")
        private Integer fileType;
    }

    @Data
    public static class SceneSlaRefRequest implements Serializable {

        private static final long serialVersionUID = 4747478435828708203L;

        @ApiModelProperty(value = "规则名称")
        private String ruleName;

        @ApiModelProperty(value = "适用对象")
        private String[] businessActivity;

        @ApiModelProperty(value = "规则")
        private RuleBean rule;

        @ApiModelProperty(value = "状态")
        private Integer status = 0;
    }

}
