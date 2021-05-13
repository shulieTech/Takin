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

package io.shulie.tro.web.common.vo.whitelist;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "WhiteListVO", description = "白名单列表接口返参")
public class WhiteListVO {

    @ApiModelProperty(name = "wlistId", value = "白名单ID")
    private String wlistId;

    @ApiModelProperty(name = "interfaceType", value = "接口类型")
    private Integer interfaceType;

    @ApiModelProperty(name = "interfaceName", value = "接口名称")
    private String interfaceName;

    @ApiModelProperty(name = "useYn", value = "是否已加入")
    private Integer useYn;


    private Long customerId;

    private String applicationId;

    private String appName;

    private Long userId;

    private Date gmtCreate;

    @ApiModelProperty(name = "gmtModified", value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    //todo private Date gmtModified; 兼容原版前端
    private Date gmtUpdate;

    @ApiModelProperty(name = "gmtModified", value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtModified;

    /**
     * 全局
     */
    @ApiModelProperty(name = "isGlobal", value = "是否全局")
    private Boolean isGlobal;

    /**
     * 是否手工添加
     */
    @ApiModelProperty(name = "isHandwork", value = "是否手工添加")
    private Boolean isHandwork;

    /**
     * 标签
     */
    @ApiModelProperty(name = "tags", value = "标签")
    private List<String> tags;

    @ApiModelProperty(name = "effectiveAppNames", value = "生效范围")
    private List<String> effectiveAppNames;

    /**
     * 已经加入的，有数据库的dbId;
     */
    private String dbId;

    private Boolean canEdit = true;

    private Boolean canRemove = true;

    private Boolean canEnableDisable = true;

    /**
     * 数据库中存在的而且状态为启用
     * 前端页面根据这个字段判断是否显示【编辑+删除】按钮
     */
    private Boolean isDbValue = false;

    public static WhiteListVO build(String id, Integer interfaceType, String interfaceName, Integer useYn,
        Date gmtUpdate) {
        WhiteListVO whiteListVO = new WhiteListVO();
        whiteListVO.setWlistId(id);
        whiteListVO.setInterfaceType(interfaceType);
        whiteListVO.setInterfaceName(interfaceName);
        whiteListVO.setUseYn(useYn);
        whiteListVO.setGmtUpdate(gmtUpdate);
        return whiteListVO;
    }
}
