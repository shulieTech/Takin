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

/**
 * @Author: xingchen
 * @ClassName: MqIsolateionVo
 * @package: com.pamirs.tro.entity.domain.vo
 * @Date: 2019/5/21下午3:46
 * @Description:
 */
public class MqIsolateionVo {
    /**
     * 可隔离的broker地址,写入gen.conf 的文件
     */
    private String isoBrokerAddr;

    /**
     * geo文件名
     */
    private String geoFileName;
    /**
     * 待切换nameserver文件拼接名称
     */
    private String clusterName;
    /**
     * 写入文件内容
     */
    private String fileContent;

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getIsoBrokerAddr() {
        return isoBrokerAddr;
    }

    public void setIsoBrokerAddr(String isoBrokerAddr) {
        this.isoBrokerAddr = isoBrokerAddr;
    }

    public String getGeoFileName() {
        return geoFileName;
    }

    public void setGeoFileName(String geoFileName) {
        this.geoFileName = geoFileName;
    }
}
