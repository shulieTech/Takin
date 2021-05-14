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

package com.pamirs.tro.entity.domain.vo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

/**
 * @Auther: vernon
 * @Date: 2019/11/1 00:33
 * @Description:
 */
public class LinkManageExcelVo extends BaseRowModel {
    @ExcelProperty(value = "阿斯旺场景id", index = 0)
    private String aswanId;
    @ExcelProperty(value = "接口描述", index = 1)
    private String interfaceDesc0;
    @ExcelProperty(value = "接口名称", index = 2)
    private String interfaceName0;
    @ExcelProperty(value = "链路描述", index = 3)
    private String linkDesc;
    @ExcelProperty(value = "链路入口", index = 4)
    private String linkEntrence;
    @ExcelProperty(value = "链路模块", index = 5)
    private String linkModule;
    @ExcelProperty(value = "链路名称", index = 6)
    private String linkName;
    @ExcelProperty(value = "链路等级", index = 7)
    private String linkRank;
    @ExcelProperty(value = "链路类型", index = 8)
    private String linkType;
    @ExcelProperty(value = "链路负责人工号", index = 9)
    private String principalNo;
    @ExcelProperty(value = "目标RT", index = 10)
    private String rt;
    @ExcelProperty(value = "目标RT-SA", index = 11)
    private String rtSa;
    @ExcelProperty(value = "选择二级链路", index = 12)
    private String secondLinkId;
    @ExcelProperty(value = "目标成功率", index = 13)
    private String targetSuccessRate;
    @ExcelProperty(value = "目标TPS", index = 14)
    private String tps;
    @ExcelProperty(value = "是否可用", index = 15)
    private String useYn;
    @ExcelProperty(value = "计算单量方式", index = 16)
    private String volumeCalcStatus;

    public String getAswanId() {
        return aswanId;
    }

    public void setAswanId(String aswanId) {
        this.aswanId = aswanId;
    }

    public String getInterfaceDesc0() {
        return interfaceDesc0;
    }

    public void setInterfaceDesc0(String interfaceDesc0) {
        this.interfaceDesc0 = interfaceDesc0;
    }

    public String getInterfaceName0() {
        return interfaceName0;
    }

    public void setInterfaceName0(String interfaceName0) {
        this.interfaceName0 = interfaceName0;
    }

    public String getLinkDesc() {
        return linkDesc;
    }

    public void setLinkDesc(String linkDesc) {
        this.linkDesc = linkDesc;
    }

    public String getLinkEntrence() {
        return linkEntrence;
    }

    public void setLinkEntrence(String linkEntrence) {
        this.linkEntrence = linkEntrence;
    }

    public String getLinkModule() {
        return linkModule;
    }

    public void setLinkModule(String linkModule) {
        this.linkModule = linkModule;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkRank() {
        return linkRank;
    }

    public void setLinkRank(String linkRank) {
        this.linkRank = linkRank;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getPrincipalNo() {
        return principalNo;
    }

    public void setPrincipalNo(String principalNo) {
        this.principalNo = principalNo;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public String getRtSa() {
        return rtSa;
    }

    public void setRtSa(String rtSa) {
        this.rtSa = rtSa;
    }

    public String getSecondLinkId() {
        return secondLinkId;
    }

    public void setSecondLinkId(String secondLinkId) {
        this.secondLinkId = secondLinkId;
    }

    public String getTargetSuccessRate() {
        return targetSuccessRate;
    }

    public void setTargetSuccessRate(String targetSuccessRate) {
        this.targetSuccessRate = targetSuccessRate;
    }

    public String getTps() {
        return tps;
    }

    public void setTps(String tps) {
        this.tps = tps;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public String getVolumeCalcStatus() {
        return volumeCalcStatus;
    }

    public void setVolumeCalcStatus(String volumeCalcStatus) {
        this.volumeCalcStatus = volumeCalcStatus;
    }
}
