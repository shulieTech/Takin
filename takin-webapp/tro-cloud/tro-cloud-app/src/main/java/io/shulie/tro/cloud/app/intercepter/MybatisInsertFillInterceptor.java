//package io.shulie.tro.cloud.app.intercepter;
//
//import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
//import org.apache.ibatis.reflection.MetaObject;
//import org.springframework.stereotype.Component;
//
///**
// * @ClassName MybatisInsertFillInterceptor
// * @Description 实现insert时，自动插入RestContext中的租户id、部门id、创建人
// * @Author qianshui
// * @Date 2020/10/27 下午5:07
// */
//@Component
//public class MybatisInsertFillInterceptor implements MetaObjectHandler {
//
//    @Override
//    public void insertFill(MetaObject metaObject) {
//        this.setFieldValByName("deptId", 1L, metaObject);
//        this.setFieldValByName("createUid", 100L, metaObject);
//    }
//
//    @Override
//    public void updateFill(MetaObject metaObject) {
//        //do nothing
//    }
//}
