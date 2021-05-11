package io.shulie.amdb.request.query;

import io.shulie.amdb.common.request.PagingRequest;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TAmdbAppInstanceBatchAppQueryRequest extends PagingRequest {
    /**
     * 应用名称
     */
    private List<String> appNames;

    /**
     * 应用ID
     */
    private List<Integer> appIds;

    /**
     * 应用ID
     */
    private List<String> agentIds;

    /**
     * 应用ID
     */
    private List<String> ipAddress;

    /**
     * 客户Id
     */
    private String tenantKey;

    /**
     * 查询内容
     */
    private List<String> fields;

    public List<String> getFields() {
        if(fields==null){
            fields = new ArrayList<>();
        }
        return fields;
    }
}
