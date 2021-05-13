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

package com.pamirs.tro.entity.domain.vo;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.Gson;
import io.shulie.tro.cloud.common.serialize.DateToStringFormatSerialize;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 说明：数据字典模型类
 *
 * @author shulie
 * @version 1.0
 * @create 2018/11/1 0001 17:32
 */
public class TDictionaryVo {
    /**
     * 字典ID
     */
    private String typeId;
    /**
     * 字典名称
     */
    private String typeName;
    /**
     * 字典别名
     */
    private String typeAlias;
    /**
     * 字典可维护标志
     */
    private String typeActive;
    /**
     * 是否是叶子
     */
    private String isLeaf;

    /**
     * 数据字典值ID
     */
    private String valueId;
    /**
     * 值顺序
     */
    private String valueOrder;
    /**
     * 值名称
     */
    private String valueName;
    /**
     * 值编码
     */
    private String valueCode;
    /**
     * 值是否可用
     */
    private String valueActive;
    /**
     * 值语言，默认中文
     */
    private String language = "ZH_CN";
    /**
     * 值创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = DateToStringFormatSerialize.class)
    private Date createTime;
    /**
     * 值修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = DateToStringFormatSerialize.class)
    private Date modifyTime;

    public static void main(String[] args) {
        TDictionaryVo tDictionaryVo = new TDictionaryVo();
        tDictionaryVo.setTypeName("性别");
        tDictionaryVo.setTypeAlias("GENDER");
        tDictionaryVo.setTypeActive("Y");
        tDictionaryVo.setCreateTime(new Date());
        tDictionaryVo.setIsLeaf("1");
        tDictionaryVo.setModifyTime(new Date());
        tDictionaryVo.setValueActive("Y");
        tDictionaryVo.setValueCode("Male");
        tDictionaryVo.setValueOrder("1");
        tDictionaryVo.setValueName("男");
        String record = new Gson().toJson(tDictionaryVo);
        System.out.println(record);

    }

    /**
     * Gets the value of typeName.
     *
     * @return the value of typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Sets the typeName.
     *
     * <p>You can use getTypeName() to get the value of typeName</p>
     *
     * @param typeName typeName
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * Gets the value of typeAlias.
     *
     * @return the value of typeAlias
     */
    public String getTypeAlias() {
        return typeAlias;
    }

    /**
     * Sets the typeAlias.
     *
     * <p>You can use getTypeAlias() to get the value of typeAlias</p>
     *
     * @param typeAlias typeAlias
     */
    public void setTypeAlias(String typeAlias) {
        this.typeAlias = typeAlias;
    }

    /**
     * Gets the value of typeActive.
     *
     * @return the value of typeActive
     */
    public String getTypeActive() {
        return typeActive;
    }

    /**
     * Sets the typeActive.
     *
     * <p>You can use getTypeActive() to get the value of typeActive</p>
     *
     * @param typeActive typeActive
     */
    public void setTypeActive(String typeActive) {
        this.typeActive = typeActive;
    }

    /**
     * Gets the value of isLeaf.
     *
     * @return the value of isLeaf
     */
    public String getIsLeaf() {
        return isLeaf;
    }

    /**
     * Sets the isLeaf.
     *
     * <p>You can use getIsLeaf() to get the value of isLeaf</p>
     *
     * @param isLeaf isLeaf
     */
    public void setIsLeaf(String isLeaf) {
        this.isLeaf = isLeaf;
    }

    /**
     * Gets the value of valueOrder.
     *
     * @return the value of valueOrder
     */
    public String getValueOrder() {
        return valueOrder;
    }

    /**
     * Sets the valueOrder.
     *
     * <p>You can use getValueOrder() to get the value of valueOrder</p>
     *
     * @param valueOrder valueOrder
     */
    public void setValueOrder(String valueOrder) {
        this.valueOrder = valueOrder;
    }

    /**
     * Gets the value of valueName.
     *
     * @return the value of valueName
     */
    public String getValueName() {
        return valueName;
    }

    /**
     * Sets the valueName.
     *
     * <p>You can use getValueName() to get the value of valueName</p>
     *
     * @param valueName valueName
     */
    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    /**
     * Gets the value of vavlueCode.
     *
     * @return the value of vavlueCode
     */
    public String getValueCode() {
        return valueCode;
    }

    /**
     * Sets the vavlueCode.
     *
     * <p>You can use getVavlueCode() to get the value of vavlueCode</p>
     *
     * @param vavlueCode vavlueCode
     */
    public void setValueCode(String vavlueCode) {
        this.valueCode = vavlueCode;
    }

    /**
     * Gets the value of valueActive.
     *
     * @return the value of valueActive
     */
    public String getValueActive() {
        return valueActive;
    }

    /**
     * Sets the valueActive.
     *
     * <p>You can use getValueActive() to get the value of valueActive</p>
     *
     * @param valueActive valueActive
     */
    public void setValueActive(String valueActive) {
        this.valueActive = valueActive;
    }

    /**
     * Gets the value of leaguage.
     *
     * @return the value of leaguage
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the leaguage.
     *
     * <p>You can use getLeaguage() to get the value of leaguage</p>
     *
     * @param language language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Gets the value of typeId.
     *
     * @return the value of typeId
     */
    public String getTypeId() {
        return typeId;
    }

    /**
     * Sets the typeId.
     *
     * <p>You can use getTypeId() to get the value of typeId</p>
     *
     * @param typeId typeId
     */
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    /**
     * Gets the value of valueId.
     *
     * @return the value of valueId
     */
    public String getValueId() {
        return valueId;
    }

    /**
     * Sets the valueId.
     *
     * <p>You can use getValueId() to get the value of valueId</p>
     *
     * @param valueId valueId
     */
    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    /**
     * Gets the value of createTime.
     *
     * @return the value of createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * Sets the createTime.
     *
     * <p>You can use getCreateTime() to get the value of createTime</p>
     *
     * @param createTime createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * Gets the value of modifyTime.
     *
     * @return the value of modifyTime
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * Sets the modifyTime.
     *
     * <p>You can use getModifyTime() to get the value of modifyTime</p>
     *
     * @param modifyTime modifyTime
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Override
    public String toString() {
        return "TDictionaryVo{" +
            "typeId='" + typeId + '\'' +
            ", typeName='" + typeName + '\'' +
            ", typeAlias='" + typeAlias + '\'' +
            ", typeActive='" + typeActive + '\'' +
            ", isLeaf='" + isLeaf + '\'' +
            ", valueId='" + valueId + '\'' +
            ", valueOrder='" + valueOrder + '\'' +
            ", valueName='" + valueName + '\'' +
            ", vavlueCode='" + valueCode + '\'' +
            ", valueActive='" + valueActive + '\'' +
            ", leaguage='" + language + '\'' +
            ", createTime=" + createTime +
            ", modifyTime=" + modifyTime +
            '}';
    }
}
