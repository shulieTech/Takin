package io.shulie.amdb.response.metrics;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class MetricsResponse implements Serializable {
    /**
     * tags
     */
    Map<String, String> tags;
    /**
     * value
     */
    List<Map<String, Object>> value;
    /**
     * 指标时间范围
     */
    long times;
}
