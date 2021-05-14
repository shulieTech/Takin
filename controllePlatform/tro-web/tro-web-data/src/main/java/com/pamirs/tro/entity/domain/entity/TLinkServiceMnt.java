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

package com.pamirs.tro.entity.domain.entity;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pamirs.tro.common.util.LongToStringFormatSerialize;
import com.pamirs.tro.entity.domain.annotation.ExcelTag;

/**
 * 说明：基础链路服务实体类
 *
 * @author shulie
 * @version V1.0
 * @date: 2018年3月1日 下午12:49:55
 */
@JsonIgnoreProperties(value = {"handler"})
public class TLinkServiceMnt extends BaseEntity {

    private static final long serialVersionUID = 1L;

    // @Field id : 链路服务id
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    @ExcelTag(name = "链路服务id", type = String.class)
    private long linkServiceId;

    // @Field linkId :链路id
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    private long linkId;

    // @Field interfaceName : 用户名
    @NotBlank(message = "接口名称不能为空")
    @ExcelTag(name = "接口名称", type = String.class)
    private String interfaceName;

    // @Field interfaceDesc : 接口说明
    @NotBlank(message = "接口说明不能为空")
    @ExcelTag(name = "接口说明", type = String.class)
    private String interfaceDesc;

    public TLinkServiceMnt() {
        super();
    }

    /**
     * 2018年5月17日
     *
     * @return the linkServiceId
     * @author shulie
     * @version 1.0
     */
    public long getLinkServiceId() {
        return linkServiceId;
    }

    /**
     * 2018年5月17日
     *
     * @param linkServiceId the linkServiceId to set
     * @author shulie
     * @version 1.0
     */
    public void setLinkServiceId(long linkServiceId) {
        this.linkServiceId = linkServiceId;
    }

    /**
     * 2018年5月17日
     *
     * @return the linkId
     * @author shulie
     * @version 1.0
     */
    public long getLinkId() {
        return linkId;
    }

    /**
     * 2018年5月17日
     *
     * @param linkId the linkId to set
     * @author shulie
     * @version 1.0
     */
    public void setLinkId(long linkId) {
        this.linkId = linkId;
    }

    /**
     * 2018年5月17日
     *
     * @return the interfaceName
     * @author shulie
     * @version 1.0
     */
    public String getInterfaceName() {
        return interfaceName;
    }

    /**
     * 2018年5月17日
     *
     * @param interfaceName the interfaceName to set
     * @author shulie
     * @version 1.0
     */
    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    /**
     * 2018年5月17日
     *
     * @return the interfaceDesc
     * @author shulie
     * @version 1.0
     */
    public String getInterfaceDesc() {
        return interfaceDesc;
    }

    /**
     * 2018年5月17日
     *
     * @param interfaceDesc the interfaceDesc to set
     * @author shulie
     * @version 1.0
     */
    public void setInterfaceDesc(String interfaceDesc) {
        this.interfaceDesc = interfaceDesc;
    }

    /**
     * 2018年5月17日
     *
     * @return 实体字符串
     * @author shulie
     * @version 1.0
     */
    @Override
    public String toString() {
        return "TLinkServiceMnt [linkServiceId=" + linkServiceId + ", linkId=" + linkId + ", interfaceName="
            + interfaceName + ", interfaceDesc=" + interfaceDesc + "]";
    }

}
