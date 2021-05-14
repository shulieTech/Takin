package io.shulie.tro.monitor.content;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhaoyong
 */
@Data
public class SystemHardwareInfo implements Serializable {
    private static final long serialVersionUID = -5897089088067997094L;

    /**
     * 服务器状态；0 空闲；1 压测中；2 离线（离线的是没有上报的机器）
     */
    private Integer status;

    /**
     * 压测场景id
     */
    private String sceneId;

    /**
     * CpuDTO相关信息
     */
    private CpuDTO cpuDTO ;

    /**
     * 內存相关信息
     */
    private MemDTO memDTO;


    /**
     * 服务器相关信息
     */
    private ServerDTO serverDTO;

    /**
     * 磁盘相关信息
     */
    private ServerFileDTO serverFileDTO;
}
