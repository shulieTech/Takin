//package io.shulie.tro.web.app.service.middleware;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import com.google.common.collect.Lists;
//import com.pamirs.tro.common.util.DateUtils;
//import io.shulie.tro.common.beans.page.PagingList;
//import io.shulie.tro.web.app.input.middleware.AppMiddlewareScanSearchInput;
//import io.shulie.tro.web.app.output.middleware.AppMiddlewareScanOutput;
//import io.shulie.tro.web.app.service.risk.util.DateUtil;
//import io.shulie.tro.web.common.enums.middleware.AppMiddlewareScanStatusEnum;
//import io.shulie.tro.web.data.dao.middleware.AppMiddlewareScanDao;
//import io.shulie.tro.web.data.param.middleware.AppMiddlewareScanSearchParam;
//import io.shulie.tro.web.data.result.middleware.AppMiddlewareScanResult;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
///**
// * @author 无涯
// * @Package io.shulie.tro.web.app.service.middleware
// * @date 2021/2/24 5:50 下午
// */
//@Service
//public class AppMiddlewareScanServiceImpl implements AppMiddlewareScanService{
//    @Autowired
//    private AppMiddlewareScanDao appMiddlewareScanDao;
//    @Override
//    public PagingList<AppMiddlewareScanOutput> getPageList(AppMiddlewareScanSearchInput input) {
//        AppMiddlewareScanSearchParam param = new AppMiddlewareScanSearchParam();
//        BeanUtils.copyProperties(input,param);
//        PagingList<AppMiddlewareScanResult> pagingList = appMiddlewareScanDao.getPageList(param);
//        if (CollectionUtils.isEmpty(pagingList.getList())) {
//            return PagingList.empty();
//        }
//        return PagingList.of(getOutputs(pagingList.getList()), pagingList.getTotal());
//    }
//
//    private List<AppMiddlewareScanOutput> getOutputs(List<AppMiddlewareScanResult> results) {
//        return results.stream().map(result -> {
//            AppMiddlewareScanOutput output = new AppMiddlewareScanOutput();
//            output.setGmtModified(DateUtils.dateToString(result.getGmtModified(),DateUtils.FORMATE_YMDHMS));
//            if(StringUtils.isNotBlank(result.getMiddlewareVersion())) {
//                output.setMiddlewareVersions(Lists.newArrayList(result.getMiddlewareVersion().split(",")));
//            }
//            output.setStatusDesc(AppMiddlewareScanStatusEnum.getDescByStatus(result.getStatus()));
//            return output;
//        }).collect(Collectors.toList());
//    }
//
//    @Override
//    public void exportExcel(AppMiddlewareScanSearchInput input) {
//        AppMiddlewareScanSearchParam param = new AppMiddlewareScanSearchParam();
//        BeanUtils.copyProperties(input,param);
//        List<AppMiddlewareScanResult> results = appMiddlewareScanDao.getList(param);
//        List<AppMiddlewareScanOutput> outputs =  getOutputs(results);
//        // todo 导出excel
//    }
//}
