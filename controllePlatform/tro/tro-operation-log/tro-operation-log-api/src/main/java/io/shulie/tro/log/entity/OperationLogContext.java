package io.shulie.tro.log.entity;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * @author shiyajian
 * create: 2020-09-16
 */
@Data
public class OperationLogContext {

    private String operationType;

    private Long startTime;

    private Long endTime;

    private Long costTime;

    private Boolean success;

    /**
     * 本次是否不记录日志
     */
    private Boolean ignore = false;

    private Map<String, String> vars = new HashMap<>();

}
