/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pamirs.pradar.internal;

import java.util.Map;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/3/24 9:06 下午
 */
public interface IPradarInternalService {

    /**
     * 给指定值添加大写的压测前缀
     *
     * @param value 指定值
     * @return 添加大写的压测前缀后的值
     */
    String addClusterTestPrefixUpper(String value);

    /**
     * 给指定值添加小写的压测前缀
     *
     * @param value 指定值
     * @return 添加小写的压测前缀后的值
     */
    String addClusterTestPrefixLower(String value);


    /**
     * 给指定值添加小写的压测前缀
     *
     * @param value 指定值
     * @return 添加小写的压测前缀后的值
     */
    String addClusterTestPrefixRodLower(String value);

    /**
     * 给指定值添加压测前缀
     *
     * @param value 指定值
     * @return 添加压测前缀后的值
     */
    String addClusterTestPrefix(String value);

    /**
     * 给指定值添加大写的压测后缀
     *
     * @param value 指定值
     * @return 添加大写的压测前缀后的值
     */
    String addClusterTestSuffixUpper(String value);

    /**
     * 给指定值添加小写的压测后缀
     *
     * @param value 指定值
     * @return 添加小写的压测前缀后的值
     */
    String addClusterTestSuffixLower(String value);

    /**
     * 给指定值添加压测后缀
     *
     * @param value 指定值
     * @return 添加压测前缀后的值
     */
    String addClusterTestSuffix(String value);

    /**
     * 判断当前值是否包含压测前缀
     *
     * @param value 当前值
     * @return 是否包含压测前缀
     */
    boolean isContainsClusterTest(String value);

    /**
     * 判断当前值是否包含压测前缀，匹配规则为 prefix+压测前缀, prefix 是全局匹配，压测前缀则忽略大小写匹配
     *
     * @param value
     * @param prefix
     * @return
     */
    boolean isContainsClusterTest(String value, String prefix);

    /**
     * 将当前值移除压测前缀
     *
     * @param value 当前值
     * @return 返回移除压测前缀后的值
     */
    String removeClusterTestPrefix(String value);

    /**
     * 判断当前值是否是压测前缀开头,匹配规则为 prefix 全匹配，压测前缀则忽略大小写匹配
     *
     * @param value  当前值
     * @param prefix 需要追加在压测前缀前的前缀
     * @return 是否是压测前缀
     */
    boolean isClusterTestPrefix(String value, String prefix);

    /**
     * 判断当前值是否是压测前缀开头
     *
     * @param value 当前值
     * @return 是否是压测前缀
     */
    boolean isClusterTestPrefix(String value);

    /**
     * 判断当前值是否是压测前缀开头
     *
     * @param value 当前值
     * @return 是否是压测前缀
     */
    boolean isClusterTestPrefixRod(String value);

    /**
     * 判断当前值是否是压测前缀结尾
     *
     * @param value 当前值
     * @return 是否是压测前缀
     */
    boolean isClusterTestSuffix(String value);

    /**
     * 开启新的trace。该接口仅提供给最源头的前中间件或自己启动的定时程序调用，使用该接口时， 必须最后调用endTrace结束。
     *
     * @param traceId     全局唯一的id，如果传入的值为空或者null，系统会自动生成
     * @param serviceName 用户自定义的入口标识值，不能为 <code>null</code>， 建议传入能够唯一标识入口的数据，例如用户访问网络的 http url
     */
    void startTrace(String traceId, String serviceName, String methodName);

    /**
     * 开启新的trace，该接口仅提供给最源头的前中间件或自己启动的定时程序调用， 支持配置 invokeId 来开启一个嵌套的调用链。使用该接口时，必须最后调用endTrace结束。
     *
     * @param traceId     全局唯一的id，如果传入的值为空或者null，系统会自动生成
     * @param invokeId    额外指定 invokeId
     * @param serviceName 用户自定义的入口标识值，不能为 <code>null</code>， 建议传入能够唯一标识入口的数据，例如用户访问网络的 http url
     */
    void startTrace(String traceId, String invokeId, String serviceName, String methodName);

    /**
     * 标记压测流量
     *
     * @param isClusterTest
     */
    void setClusterTest(boolean isClusterTest);

    /**
     * 判断当前流量是否为压测流量
     *
     * @return
     */
    boolean isClusterTest();

    /**
     * 设置 debug 流量
     *
     * @param b
     */
    void setDebug(boolean b);

    /**
     * 判断当前流量是否为调试压测流量
     *
     * @return
     */
    boolean isDebug();

    /**
     * 结束一次跟踪，Threadlocal 变量会被清空，调用了startTrace及startTrace4Top的必须在finally或者最后调用该接口。
     */
    void endTrace();

    /**
     * 结束一次跟踪，Threadlocal 变量会被清空，调用了 startTrace 及 startTrace4Top 的必须在finally或者最后调用该接口。
     */
    void endTrace(String resultCode, int type);

    /**
     * 判断是否有上下文
     *
     * @return
     */
    boolean hasInvokeContext();

    /**
     * 判断指定的 Map 中是否包含上下文
     *
     * @return
     */
    boolean hasInvokeContext(Map<String, String> ctx);

    /**
     * 将 Map 解析成一个上下文对象
     *
     * @param ctx
     * @return
     */
    Object parseInvokeContext(Map<String, String> ctx);

    /**
     * 获取当前的调用方法
     *
     * @return
     */
    String getMethod();

    /**
     * 获取当前的调用服务名
     *
     * @return
     */
    String getService();

    /**
     * 设置rpc上下文，如果当前上下文中有父上下文则保持父上下文
     *
     * @param rpcCtx
     */
    void setInvokeContextWithParent(Object rpcCtx);

    /**
     * 切换当前线程关联的RPC调用上下文。上下文对象可以是 {@link #getInvokeContext()} 的返回值
     *
     * @param invokeCtx 调用上下文，可以为null，表示清空当前Threadlocal变量， 该接口不允许业务方调用，只允许 rpc 层调用。
     * @see #getInvokeContext() 直接获取 InvokeContext 对象，不做 Map 转换
     */
    @SuppressWarnings("unchecked")
    void setInvokeContext(Object invokeCtx);

    /**
     * 清理全部调用上下文信息
     */
    void clearInvokeContext();

    /**
     * 从栈上弹出一层 InvokeContext，用于客户端 Send/Recv 异步时主逻辑 需要把 send 的子 InvokeContext 弹出的场景
     *
     * @return 弹出的当前子 InvokeContext
     * @see #endClientInvoke(String, int) 类似用法，但不记日志
     */
    Object popInvokeContext();

    /**
     * 提交上下文
     *
     * @param ctx
     */
    void commitInvokeContext(Object ctx);


    void upAppName(String upAppName);

    void setLogType(int logType);

    void middlewareName(String middlewareName);

    /**
     * 获取当前调用的中间件名称
     *
     * @return
     */
    String getMiddlewareName();

    /**
     * 开始客户端调用
     *
     * @param serviceName 服务名称
     * @param methodName  方法名称
     */
    void startClientInvoke(String serviceName, String methodName);

    /**
     * 记录客户端收到RPC响应的事件
     *
     * @param resultCode 参考 RPC_RESULT_开头的结果码。有响应表示RPC成功，但业务上有可能还是失败，因此有不同的结果码。
     * @param type       类型为：TYPE_TRACE,TYPE_DUBBO_CLIENT,TYPE_DB,TYPE_METAQ,TYPE_SEARCH
     */
    void endClientInvoke(String resultCode, int type);

    /**
     * 服务端收到请求，兼容多个插件埋点重复的问题，兼容服务端与客户端均为本地的问题
     *
     * @param service
     * @param method
     * @param remoteAppName
     * @param ctxObj
     */
    void startServerInvoke(String service, String method, String remoteAppName, Object ctxObj);

    /**
     * 服务端收到请求，兼容多个插件埋点重复的问题，兼容服务端与客户端均为本地的问题
     *
     * @param service
     * @param method
     * @param ctxObj
     */
    void startServerInvoke(String service, String method, Object ctxObj);

    /**
     * 服务端收到RPC请求
     */
    void startServerInvoke(String service, String method, String remoteAppName);

    /**
     * 服务端返回RPC响应，指定 invoke 类型，Threadlocal变量会被清空
     *
     * @param type 类型为：   TYPE_DUBBO_SERVER,TYPE_METAQ
     */
    void endServerInvoke(int type);

    /**
     * 服务端返回RPC响应，指定 invoke 类型，Threadlocal变量会被清空
     *
     * @param resultCode 参考 RPC_RESULT_开头的结果码。有响应表示RPC成功，但业务上有可能还是失败，因此有不同的结果码。
     * @param type       类型为：   TYPE_DUBBO_SERVER,TYPE_METAQ
     */
    void endServerInvoke(String resultCode, int type);

    /**
     * 获取入口应用名
     */
    String getTraceAppName();

    /**
     * 获取上一个应用名
     *
     * @return
     */
    String getUpAppName();

    /**
     * 获取 remote 应用名
     *
     * @return
     */
    String getRemoteAppName();

    /**
     * 获取 remoteIP
     *
     * @return
     */
    String getRemoteIp();


    /**
     * 获取全局唯一的Traceid
     */
    String getTraceId();

    /**
     * 获取当前方法invoke调用层次
     */
    String getInvokeId();

    /**
     * 获取当前rpc调用type
     */
    Integer getLogType();

    /**
     * 设置请求内容
     *
     * @param request
     */
    void request(Object request);

    /**
     * 设置响应内容
     *
     * @param response
     */
    void response(Object response);


    /**
     * invoke 上追加的key value信息，会打印到当前 invoke 日志中。 与添加业务信息的 {@link #putUserData(String, String)} 不同，
     * attribute 不会跟随 invoke 调用传递，只对本地当前的这一次 invoke 有效
     *
     * @see #putUserData(String, String)
     */
    void attribute(String key, String value);

    /**
     * 获取随 Pradar 通过 DUBBO、MetaQ 等中间件传递的业务信息
     *
     * @param key 不能为空
     */
    String getUserData(String key);

    /**
     * 放置 key 对应的业务信息，这个信息会打印到当前 invoke 的日志之中。 信息会随 Pradar 通过 DUBBO、MetaQ 等中间件传递。
     * 数据在调用链里面的兄弟间、父子间传递，但不会往回传。 如果仅仅希望添加业务信息，不需要信息被传递，可以使用 {@link #attribute(String, String)}
     *
     * @param key   不能为空
     * @param value 值，不能有回车、换行、“|” 等符号
     * @return 返回成功还是失败
     * @see #attribute(String, String)
     */
    boolean putUserData(String key, String value);

    /**
     * 判断是否具有用户属性
     *
     * @param key 用户属性 key
     * @return true|false
     */
    boolean hasUserData(String key);

    /**
     * 清除 key 对应的业务信息
     *
     * @param key 不能为空
     * @return 原来的值
     */
    String removeUserData(String key);

    /**
     * 获取随 Pradar 通过 DUBBO、METAQ 等中间件传递的业务信息。 供内部使用，业务应该使用 {@link #getUserData(String)}
     */
    Map<String, String> getUserDataMap();

    /**
     * 导出业务信息，供中间件传输 Pradar 上下文时使用。
     */
    String exportUserData();

    /**
     * 用于业务方希望追加相关数据到rpc调用链中，比如想把业务的方法中的某个参数值打印出来，放到rpc的日志中。
     *
     * @param msg 用户希望追加的内容，不能有回车、换行、“|” 等符号
     */
    void callBack(String msg);

    /**
     * rpc请求大小
     */
    void requestSize(long size);

    /**
     * 追加远程服务地址
     *
     * @param remoteIp 远程机器ip地址
     */
    void remoteIp(String remoteIp);

    /**
     * 追加远程端口
     *
     * @param port 远程机器端口
     */
    void remotePort(String port);

    /**
     * 追加远程端口
     *
     * @param port 远程机器端口
     */
    void remotePort(int port);

    /**
     * rpc响应的大小
     */
    void responseSize(long size);

    /**
     * 获取服务配置
     *
     * @param key 配置key
     * @return 返回配置值
     */
    String getProperty(String key);

    /**
     * 获取服务配置
     *
     * @param key          配置key
     * @param defaultValue 默认值
     * @return 返回配置值
     */
    String getProperty(String key, String defaultValue);

    /**
     * 返回request是否打开
     *
     * @return true/false
     */
    boolean isRequestOn();

    /**
     * 返回exception是否打开
     *
     * @return true/false
     */
    boolean isResponseOn();

    /**
     * 返回response是否打开
     *
     * @return true/false
     */
    boolean isExceptionOn();

    /**
     * 返回插件请求最大长度
     *
     * @return
     */
    Integer getPluginRequestSize();

    /**
     * 返回插件响应最大长度
     *
     * @return
     */
    Integer getPluginResponseSize();

    /**
     * 获取int配置
     *
     * @param key          配置key
     * @param defaultValue 默认值
     * @return 返回配置值
     */
    Integer getIntProperty(String key, Integer defaultValue);

    /**
     * 获取int配置
     *
     * @param key 配置key
     * @return 返回配置值
     */
    Integer getIntProperty(String key);

    /**
     * 获取long配置
     *
     * @param key          配置key
     * @param defaultValue 默认值
     * @return 返回配置值
     */
    Long getLongProperty(String key, Long defaultValue);

    /**
     * 获取int配置
     *
     * @param key 配置key
     * @return 返回配置值
     */
    Long getLongProperty(String key);

    /**
     * 获取long配置
     *
     * @param key          配置key
     * @param defaultValue 默认值
     * @return 返回配置值
     */
    Boolean getBooleanProperty(String key, Boolean defaultValue);

    /**
     * 获取int配置
     *
     * @param key 配置key
     * @return 返回配置值
     */
    Boolean getBooleanProperty(String key);


}
