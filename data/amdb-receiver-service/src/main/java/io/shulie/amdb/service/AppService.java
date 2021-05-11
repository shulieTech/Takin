package io.shulie.amdb.service;

import com.github.pagehelper.PageInfo;
import io.shulie.amdb.common.Response;
import io.shulie.amdb.entity.AppDO;
import io.shulie.amdb.request.query.TAmdbAppBatchAppQueryRequest;
import io.shulie.amdb.response.app.AmdbAppResponse;

import java.util.List;

public interface AppService {
    Response insert(AppDO tAmdbApp);

    void insertAsync(AppDO tAmdbApp);

    int insertBatch(List<AppDO> tAmdbApps);

    int update(AppDO tAmdbApp);

    int updateBatch(List<AppDO> tAmdbApps);

    int delete(AppDO tAmdbApp);

    AppDO selectByPrimaryKey(AppDO tAmdbApp);

    AppDO selectOneByParam(AppDO tAmdbApp);

    List<AppDO> selectByFilter(String filter);

    PageInfo<AmdbAppResponse> selectByBatchAppParams(TAmdbAppBatchAppQueryRequest param);

    List<String> selectAllAppName(TAmdbAppBatchAppQueryRequest param);
}
