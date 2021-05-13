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

package com.pamirs.tro.entity.domain.entity.linkmanage.structure;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.pamirs.tro.entity.domain.entity.linkmanage.figure.LinkEdge;
import com.pamirs.tro.entity.domain.entity.linkmanage.figure.LinkVertex;
import com.pamirs.tro.entity.domain.entity.linkmanage.figure.RpcType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @Auther: vernon
 * @Date: 2019/12/10 10:23
 * @Description:
 */
@Data
public class Category implements Serializable {

    /**
     * 内部id
     */
    public String innerId;
    /**
     * 唯一标识 ,前端使用
     */
    public String id;

    /**
     * 子节点集合
     */
    @ApiModelProperty(name = "children", value = "子节点集合")
    public List<Category> children;
    private static final String MYSQL = "MYSQL";
    private static final String ORACLE = "ORACLE";
    private static final String SQL_SERVER = "SQL_SERVER";
    /**
     * 应用名
     */
    @ApiModelProperty(name = "applicationName", value = "应用名")
    public String applicationName;
    /**
     * 用于前端展示
     */
    @ApiModelProperty(name = "serviceName", value = "service名称")
    private String serviceName;

    /**
     * 已知节点
     */
    @ApiModelProperty(name = "nodeList", value = "已知节点信息")
    private Set<String> nodeList;

    private Set<String> dataList;

    /**
     * 未知节点
     */
    @ApiModelProperty(name = "unKnowNodeList", value = "未知节点信息")
    private Set<String> unKnowNodeList;

    private String inVid;
    private String outVid;
    /**
     * 0: 老节点 ； 1： 新增节点 ； -1：删除节点
     */
    @ApiModelProperty(name = "nodeStatus", value = "节点新增状态")
    private Integer nodeStatus = 0;

    @ApiModelProperty(name = "serviceType", value = "service类型")
    private String serviceType;

    @ApiModelProperty(name = "serviceDetail", value = "service详情")
    private String serviceDetail;

    @ApiModelProperty(name = "event", value = "链路梳理与实时分析适配")
    private String event;
    private static final String DB2 = "DB2";
    /**
     * 父级rpcId，没有默认为0
     */
    @ApiModelProperty(name = "parentId", value = "父级rpcId,没有默认为0")
    public String parentId;
    /**
     * 深度
     */
    @ApiModelProperty(name = "dept", value = "dept")
    private Integer dept;
    /**
     * 当前的rpcId
     */
    @ApiModelProperty(name = "currentId", value = "当前节点的rpcId")
    private String currentId;

    public Category() {
    }

    public Category(LinkEdge edge, LinkVertex linkVertex) {
        this.innerId = edge.getEdgeId();
        this.currentId = edge.getRpcId();
        this.inVid = ObjectUtils.toString(edge.getInVid());
        this.outVid = ObjectUtils.toString(edge.getOutVid());

        this.applicationName = edge.getApplicationName();
        this.event = edge.getEvent();
        this.serviceType = rpcType(RpcType.getByValue(edge.getRpcType(), RpcType.UNKNOWN));
        StringBuilder serviceNameBuilder = new StringBuilder();
        serviceNameBuilder
            .append(applicationName)
            .append("|")
            .append(rpcType(RpcType.getByValue(edge.getRpcType(), null)))
            .append("|")
            .append(edge.getServiceName());
        this.serviceName = serviceNameBuilder.toString();
        this.serviceDetail = edge.getServiceName();

        if (currentId.contains(".")) {
            this.dept = currentId.split("\\.").length;
            this.parentId = currentId.substring(0, currentId.lastIndexOf("."));
        } else {
            this.dept = 0;
            this.parentId = "0";
        }

        if (linkVertex != null && linkVertex.getVertexOpData() != null) {
            this.setNodeList(linkVertex.getVertexOpData().getIpList());
            this.setDataList(linkVertex.getVertexOpData().getDataList());
            this.setUnKnowNodeList(linkVertex.getVertexOpData().getUnKnowIpList());
        }

    }

    public static <T extends Category> Comparator<T> getComparator() {
        return new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                int rc = rpcIdCompare(o1.getRpcIdArray(o1.getCurrentId()), o2.getRpcIdArray(o2.getCurrentId()));
                return rc;
            }
        };
    }

    public static int rpcIdCompare(int[] thisRpcIdArray, int[] thatRpcIdArray) {
        int size = thisRpcIdArray.length < thatRpcIdArray.length ? thisRpcIdArray.length : thatRpcIdArray.length;
        for (int i = 0; i < size; i++) {
            int result = thisRpcIdArray[i] - thatRpcIdArray[i];
            if (result != 0) {
                return result;
            }
        }
        return thisRpcIdArray.length - thatRpcIdArray.length;
    }

    /**
     * 将“.” 分隔的版本号、rpcId 切分成整数数组
     */
    public static int[] parseVersion(String str) {
        if (str != null) {
            String[] strs = StringUtils.split(str, '.');
            int[] ints = new int[strs.length];
            for (int i = 0; i < strs.length; ++i) {
                ints[i] = parseIntQuietly(strs[i], 0);
            }
            return ints;
        }
        return new int[] {0};
    }

    public static int parseIntQuietly(String value, int defaultValue) {
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 将rpcid化为数组
     *
     * @param rpcId
     * @return
     */
    public int[] getRpcIdArray(String rpcId) {
        int[] values = null;

        if (values == null) {
            values = parseVersion(rpcId);
            if (values.length >= 1) {
                values[0] = 0; // 修正部分 trace 不是以 0 作根的问题
            }
        }
        return values;
    }

    public static String rpcType(RpcType rpcType) {
        if (rpcType == null) {
            return null;
        }
        if (rpcType.getText().contains("DUBBO")) {
            return "DUBBO";
        } else if (rpcType.getText().contains("HTTP")) {
            return "HTTP";
        }
        return rpcType.getText();
    }
}
