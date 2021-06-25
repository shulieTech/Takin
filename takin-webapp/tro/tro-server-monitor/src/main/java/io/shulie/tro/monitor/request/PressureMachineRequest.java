package io.shulie.tro.monitor.request;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author: HengYu
 * @Date: 2020-11-13 09:03
 * @Description:
 */

@Data
public class PressureMachineRequest implements Serializable {

    /**
     * 压力机名称
     */
    private String name;

    /**
     * 压力机IP
     */
    private String ip;

    /**
     * 标签
     */
    private String flag;

    /**
     * cpu核数
     */
    private Integer cpu;

    /**
     * 内存，单位字节
     */
    private Long memory;

    /**
     * 磁盘，单位字节
     */
    private Long disk;

    /**
     * cpu利用率
     */
    private BigDecimal cpuUsage;

    /**
     * cpu load
     */
    private BigDecimal cpuLoad;

    /**
     * 内存利用率
     */
    private BigDecimal memoryUsed;

    /**
     * 磁盘 IO 等待率
     */
    private BigDecimal diskIoWait;

    /**
     * 网络带宽入大小
     */
    private Long transmittedIn;

    /**
     * 网络带宽入利用率
     */
    private BigDecimal transmittedInUsage;

    /**
     * 网络带宽出大小
     */
    private Long transmittedOut;

    /**
     * 网络带宽出利用率
     */
    private BigDecimal transmittedOutUsage;

    /**
     * 网络带宽利用率
     */
    private BigDecimal transmittedUsage;

    /**
     * 压测场景id
     */
    private List<Long> sceneId;

    /**
     * 状态 0：空闲 ；1：压测中  -1:离线
     */
    private Integer status;

    /**
     * 状态 0: 正常 1： 删除
     */
    private Boolean isDeleted;


}