//package io.shulie.tro.web.data.dao.middleware;
//
//import java.util.List;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
////import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.google.common.collect.Lists;
//import io.shulie.tro.common.beans.page.PagingList;
//import io.shulie.tro.web.data.convert.middleware.AppMiddlewareScanConvert;
////import io.shulie.tro.web.data.mapper.mysql.AppMiddlewareInfoMapper;
////import io.shulie.tro.web.data.mapper.mysql.AppMiddlewareScanMapper;
////import io.shulie.tro.web.data.model.mysql.AppMiddlewareScanEntity;
//import io.shulie.tro.web.data.param.middleware.AppMiddlewareScanSearchParam;
//import io.shulie.tro.web.data.result.middleware.AppMiddlewareScanResult;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * @author 无涯
// * @Package io.shulie.tro.web.data.dao.middleware
// * @date 2021/2/24 5:18 下午
// */
//@Component
//public class AppMiddlewareScanDaoImpl implements AppMiddlewareScanDao{
//
//    @Autowired
//    private AppMiddlewareScanMapper appMiddlewareScanMapper;
//    @Override
//    public PagingList<AppMiddlewareScanResult> getPageList(AppMiddlewareScanSearchParam param) {
//        Page<AppMiddlewareScanEntity> page  = new Page<>(param.getCurrent() + 1,param.getPageSize());
//        IPage<AppMiddlewareScanEntity> scanEntityIPage = appMiddlewareScanMapper.selectPage(page,getWrapper(param));
//        if(CollectionUtils.isEmpty(scanEntityIPage.getRecords())) {
//            return PagingList.empty();
//        }
//        return PagingList.of(AppMiddlewareScanConvert.INSTANCE.ofList(scanEntityIPage.getRecords()),
//            scanEntityIPage.getTotal());
//    }
//
//    @Override
//    public List<AppMiddlewareScanResult> getList(AppMiddlewareScanSearchParam param) {
//        LambdaQueryWrapper<AppMiddlewareScanEntity> wrapper = getWrapper(param);
//        List<AppMiddlewareScanEntity> entities = appMiddlewareScanMapper.selectList(wrapper);
//        if(CollectionUtils.isEmpty(entities)) {
//            return Lists.newArrayList();
//        }
//        return AppMiddlewareScanConvert.INSTANCE.ofList(entities);
//    }
//
//    private LambdaQueryWrapper<AppMiddlewareScanEntity> getWrapper(
//        AppMiddlewareScanSearchParam param) {
//        LambdaQueryWrapper<AppMiddlewareScanEntity> wrapper = new LambdaQueryWrapper<>();
//        if(param.getStatus() != null) {
//            wrapper.eq(AppMiddlewareScanEntity::getStatus, param.getStatus());
//        }
//        if(StringUtils.isNotBlank(param.getAppName())) {
//            wrapper.eq(AppMiddlewareScanEntity::getAppName, param.getAppName());
//        }
//        if(StringUtils.isNotBlank(param.getType())) {
//            wrapper.eq(AppMiddlewareScanEntity::getMiddlewareType, param.getType());
//        }
//        wrapper.eq(AppMiddlewareScanEntity::getIsDeleted,0);
//        return wrapper;
//    }
//
//
//}
