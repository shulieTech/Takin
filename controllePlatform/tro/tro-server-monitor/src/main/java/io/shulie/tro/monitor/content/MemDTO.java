package io.shulie.tro.monitor.content;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhaoyong
 */
@Data
public class MemDTO implements Serializable {
    private static final long serialVersionUID = 5834422711509585282L;

    /**
     * 内存总量
     */
    private Long total;

    /**
     * 已用内存
     */
    private Long used;

    /**
     * 剩余内存
     */
    private Long free;

    private Long swapTotal;

    private Long swapUsed;
}
