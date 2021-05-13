package io.shulie.tro.monitor.content;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhaoyong
 */
@Data
public class ServerFileDTO implements Serializable {
    private static final long serialVersionUID = 8344432439254074229L;

    /**
     * 盘符路径
     */
    private String dirName;


    /**
     * 总大小
     */
    private long total;

    /**
     * 剩余大小
     */
    private long free;

    /**
     * 已经使用量
     */
    private long used;

    /**
     * 资源的使用率
     */
    private double usage;

    /**
     * 磁盘io利用率
     */
    private double ioUsage;

    /**
     * 各个磁盘的情况
     */
    private List<ServerFileDTO> children;
}
