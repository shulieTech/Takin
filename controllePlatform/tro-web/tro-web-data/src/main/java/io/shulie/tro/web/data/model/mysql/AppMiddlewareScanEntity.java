//package io.shulie.tro.web.data.model.mysql;
//
//import java.util.Date;
//
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
//import lombok.Data;
//
///**
//* @Package io.shulie.tro.web.data.model.mysql
//* @author 何仲奇
//* @date 2021/2/24 下午4:55
//*/
///**
//    * 应用中间件扫描表
//    */
//@Data
//@TableName(value = "t_app_middleware_scan")
//public class AppMiddlewareScanEntity {
//    /**
//     * 主键
//     */
//    @TableId(value = "ID", type = IdType.AUTO)
//    private Long id;
//
//    /**
//     * 应用名
//     */
//    @TableField(value = "app_name")
//    private String appName;
//
//    /**
//     * 中间件类型，1. web容器，2. web服务器，3. 消息队列，4. 远程调用，5. 数据源，6. 连接池，7. ESB
//  ，8. 缓存，9. 缓存中间件，10. NoSql，11. 文件存储，12. job
//     */
//    @TableField(value = "middleware_type")
//    private String middlewareType;
//
//    /**
//     * jar包名称
//     */
//    @TableField(value = "jar_name")
//    private String jarName;
//
//    /**
//     * 1. 未录入，2. 无需支持，3. 未支持，4. 已支持
//     */
//    @TableField(value = "status")
//    private Integer status;
//
//    /**
//     * 中间件名称
//     */
//    @TableField(value = "middleware_name")
//    private String middlewareName;
//
//    /**
//     * 中间件支持版本列表
//     */
//    @TableField(value = "middleware_version")
//    private String middlewareVersion;
//
//    /**
//     * 创建时间
//     */
//    @TableField(value = "gmt_create")
//    private Date gmtCreate;
//
//    /**
//     * 更新时间
//     */
//    @TableField(value = "gmt_modified")
//    private Date gmtModified;
//
//    /**
//     * 软删
//     */
//    @TableField(value = "is_deleted")
//    private Boolean isDeleted;
//
//    /**
//     * 租户id
//     */
//    @TableField(value = "customer_id")
//    private Long customerId;
//
//    /**
//     * 创建人
//     */
//    @TableField(value = "creator_id")
//    private Long creatorId;
//
//    /**
//     * 修改人
//     */
//    @TableField(value = "modifier_id")
//    private Long modifierId;
//}