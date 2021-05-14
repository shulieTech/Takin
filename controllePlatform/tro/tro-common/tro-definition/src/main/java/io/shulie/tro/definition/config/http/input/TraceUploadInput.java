package io.shulie.tro.definition.config.http.input;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author shiyajian
 * create: 2020-12-09
 */
public class TraceUploadInput implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 追踪实例对象
     */
    private String traceDeployObject;

    /**
     * 追踪凭证
     */
    private String sampleId;

    /**
     * 状态0:待采集;1:采集中;2:采集结束
     */
    private Integer status;

    /**
     * 行号
     */
    private Integer lineNum;

    /**
     * 评价耗时
     */
    private BigDecimal avgTime;

    /**
     * 追踪实例下一级的信息
     */
    private List<TraceUploadInput> children;

}
