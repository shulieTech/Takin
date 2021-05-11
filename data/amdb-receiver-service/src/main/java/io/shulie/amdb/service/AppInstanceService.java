package io.shulie.amdb.service;

import com.github.pagehelper.PageInfo;
import io.shulie.amdb.common.Response;
import io.shulie.amdb.entity.TAmdbAppInstanceDO;
import io.shulie.amdb.request.query.TAmdbAppInstanceBatchAppQueryRequest;
import io.shulie.amdb.request.query.TAmdbAppInstanceErrorInfoByQueryRequest;
import io.shulie.amdb.request.query.TAmdbAppInstanceQueryRequest;
import io.shulie.amdb.response.instance.AmdbAppInstanceResponse;
import io.shulie.amdb.response.instance.InstanceErrorInfoResponse;

import java.util.List;

public interface AppInstanceService {
    Response insert(TAmdbAppInstanceDO record);

    Response insertBatch(List<TAmdbAppInstanceDO> tAmdbApps);

    TAmdbAppInstanceDO selectOneByParam(TAmdbAppInstanceDO instance);

    int update(TAmdbAppInstanceDO record);

    int updateBatch(List<TAmdbAppInstanceDO> tAmdbApps);

    PageInfo<AmdbAppInstanceResponse> selectByParams(TAmdbAppInstanceQueryRequest param);

    Integer getAllInstanceCount(Long appId);

    /**
     * 获取在线实例列表
     *
     * @param appId
     * @return
     */
    Integer getOnlineInstanceCount(Long appId);

    /**
     * 获取异常实例列表
     *
     * @param appId
     * @return
     */
    Integer getExceptionInstanceCount(Long appId);

    PageInfo<AmdbAppInstanceResponse> selectByBatchAppParams(TAmdbAppInstanceBatchAppQueryRequest param);

    List<InstanceErrorInfoResponse> selectErrorInfoByParams(TAmdbAppInstanceErrorInfoByQueryRequest param);

    void initOnlineStatus();

    void deleteByParams(TAmdbAppInstanceQueryRequest param);
}
