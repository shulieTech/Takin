package io.shulie.tro.monitor.content;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhaoyong
 */
@Data
public class ServerDTO implements Serializable {
    private static final long serialVersionUID = 9195269804501192667L;

    /**
     * 服务器名称
     */
    private String computerName;

    /**
     * 服务器Ip
     */
    private String computerIp;


    /**
     * 操作系统
     */
    private String osName;

    /**
     * 当前收包大小
     */
    private Long receive;

    /**
     * 当前发包大小
     */
    private Long transmitted;

    /**
     * 总收包大小
     */
    private Long totalReceive;

    /**
     * 总发包大小
     */
    private Long totalTransmitted;
}
