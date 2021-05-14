//package io.shulie.tro.web.data.param.fastdebug;
//
//import java.util.Date;
//
//import lombok.Data;
//
///**
// * @author 无涯
// * @Package io.shulie.tro.web.data.param.fastdebug
// * @date 2020/12/29 10:46 上午
// */
//@Data
//public class FastDebugCallStackParam {
//
//    /**
//     * 结果id
//     */
//    private Long resultId;
//
//    /**
//     * traceId
//     */
//    private String traceId;
//
//    /**
//     * rpcid
//     */
//    private String rpcId;
//
//    ///**
//    // * 1是压测流量，0是业务流量
//    // */
//    //private Byte isClusterTest;
//
//    ///**
//    // * 应用名
//    // */
//    //private String appName;
//    //
//    ///**
//    // * 方法名
//    // */
//    //private String methodName;
//
//    ///**
//    // * 中间件
//    // */
//    //private String middlewareName;
//    //
//    ///**
//    // * 服务名
//    // */
//    //private String serviceName;
//
//    /**
//     * 0：失败，1：成功
//     */
//    private Boolean status;
//
//    /**
//     * 创建时间
//     */
//    private Date gmtCreate;
//
//    /**
//     * 更新时间
//     */
//    private Date gmtModified;
//
//    public FastDebugCallStackParam() {
//        this.gmtCreate = new Date();
//        this.gmtModified = new Date();
//    }
//}
