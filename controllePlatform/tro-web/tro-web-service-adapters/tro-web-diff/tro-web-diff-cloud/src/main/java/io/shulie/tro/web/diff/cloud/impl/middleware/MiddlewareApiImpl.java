//package io.shulie.tro.web.diff.cloud.impl.middleware;
//
//import java.util.List;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.google.common.collect.Lists;
//import io.shulie.tro.cloud.open.api.impl.CloudCommonApi;
//import io.shulie.tro.cloud.open.api.middleware.CloudMiddlewareApi;
//import io.shulie.tro.cloud.open.api.report.CloudReportApi;
//import io.shulie.tro.cloud.open.constant.CloudApiConstant;
//import io.shulie.tro.cloud.open.resp.middleware.MiddlewareLibraryResp;
//import io.shulie.tro.common.beans.response.ResponseResult;
//import io.shulie.tro.utils.http.HttpHelper;
//import io.shulie.tro.utils.http.TroResponseEntity;
//import io.shulie.tro.web.diff.api.middleware.MiddlewareApi;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.tro.properties.TroCloudClientProperties;
//
///**
// * @author 无涯
// * @Package io.shulie.tro.cloud.open.api.middleware
// * @date 2021/2/24 4:03 下午
// */
//public class MiddlewareApiImpl implements MiddlewareApi {
//
//    @Autowired
//    private CloudMiddlewareApi cloudMiddlewareApi;
//    @Override
//    public List<MiddlewareLibraryResp> getList() {
//        ResponseResult<List<MiddlewareLibraryResp>> responseResult = cloudMiddlewareApi.getList();
//        if(responseResult.getSuccess()) {
//            return responseResult.getData();
//        }
//        return Lists.newArrayList();
//    }
//}
