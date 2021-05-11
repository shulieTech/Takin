package io.shulie.amdb.request.query;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class MetricsQueryRequest {
    /**
     * 表名
     */
    String measurementName;
    /**
     * tags
     */
    List<LinkedHashMap<String, String>> tagMapList;
    /**
     * filelds
     */
    Map<String, String> fieldMap;
    /**
     * Group
     */
    String groups;
    /**
     * startTime
     */
    long startTime;
    /**
     * endTime
     */
    long endTime;
}
