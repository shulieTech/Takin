package io.shulie.amdb.service;

import java.util.Map;

import io.shulie.amdb.common.Response;
import io.shulie.amdb.request.query.MetricsQueryRequest;
import io.shulie.amdb.response.metrics.MetricsResponse;

public interface MetricsService {
    Map<String, MetricsResponse> getMetrics(MetricsQueryRequest request);
}
