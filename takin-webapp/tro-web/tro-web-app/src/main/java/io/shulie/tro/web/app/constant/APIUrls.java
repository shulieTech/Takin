/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.tro.web.app.constant;

/**
 * 说明: 接口定义类
 *
 * @author shulie
 * @version v1.0
 * @2018年4月13日
 */
public class APIUrls {

    /***
     * root level URI **
     */
    public static final String TRO_API_URL = "/api/";

    public static final String TRO_OPEN_API_URL = "/open-api/v01/";

    /**
     * 配置中心模块
     */
    public static final String API_TRO_CONFCENTER_URI = "confcenter/";
    /**
     * 01 应用管理子模块
     */
    // API.01.01.001 添加应用接口
    public static final String API_TRO_CONFCENTER_ADD_APPLICATION_URI = API_TRO_CONFCENTER_URI
        + "applicationmnt/add/application";
    // API.01.01.002 查询应用信息列表接口
    public static final String API_TRO_CONFCENTER_QUERY_APPLICATIONINFO_URI = API_TRO_CONFCENTER_URI
        + "applicationmnt/query/applicationlist";
    // API.01.01.003 根据应用id查询应用信息详情接口
    public static final String API_TRO_CONFCENTER_MODIFY_APPLICATIONINFO_URI = API_TRO_CONFCENTER_URI
        + "applicationmnt/query/applicationinfo";
    // API.01.01.004 删除应用信息接口
    public static final String API_TRO_CONFCENTER_DELETE_APPLICATIONINFO_URI = API_TRO_CONFCENTER_URI
        + "applicationmnt/delete/applicationinfo";
    // API.01.01.005 查询应用下拉框数据接口
    public static final String API_TRO_CONFCENTER_QUERY_APPLICATIONDATA_URI = API_TRO_CONFCENTER_URI
        + "applicationmnt/dic/query/applicationdata";
    // API.01.01.006 根据应用id更新应用信息
    public static final String API_TRO_CONFCENTER_UPDATE_APPLICATIONINFO_URI = API_TRO_CONFCENTER_URI
        + "applicationmnt/update/applicationinfo";
    // API.01.01.007  查询应用信息列表(同步表查数据)
    public static final String API_TRO_CONFCENTER_QUERY_APPNAMELIST_URI = API_TRO_CONFCENTER_URI
        + "applicationmnt/query/appnamebyprada";
    // API.01.01.008  查询应用信息列表(同步表查数据)
    public static final String API_TRO_UPDATE_APP_AGENT_VERSION_URI = API_TRO_CONFCENTER_URI
        + "applicationmnt/update/applicationAgent";

    /**
     * 02 黑白名单管理子模块
     */
    // API.01.02.001 添加白名单接口
    public static final String API_TRO_CONFCENTER_ADD_WLIST_URI = API_TRO_CONFCENTER_URI + "wbmnt/add/wlist";
    //白名单上传接口
    public static final String API_TRO_CONFCENTER_UPLOAD_WLIST_URI = API_TRO_CONFCENTER_URI + "wbmnt/upload/wlist";
    //白名单导出接口
    public static final String API_TRO_CONFCENTER_EXCEL_DOWNLOAD_URI = API_TRO_CONFCENTER_URI + "wbmnt/download/wlist";
    // API.01.02.002 查询白名单列表
    public static final String API_TRO_CONFCENTER_QUERY_WLIST_URI = API_TRO_CONFCENTER_URI + "wbmnt/query/wlist";

    public static final String API_TRO_CONFCENTER_WHITELIST_FILE_URI = API_TRO_CONFCENTER_URI + "wbmnt/query/{userAppKey}";


    public static final String API_TRO_CONFCENTER_QUERY_WLIST_4AGENT_URI = API_TRO_CONFCENTER_URI
        + "wbmnt/query/wlist/agent";
    // API.01.02.003 根据id查询白名单详情接口
    public static final String API_TRO_CONFCENTER_QUERY_WLISTBYID_URI = API_TRO_CONFCENTER_URI
        + "wbmnt/query/wlistinfo";
    // API.01.02.004 根据id更新白名单接口
    public static final String API_TRO_CONFCENTER_UPDATE_WLIST_URI = API_TRO_CONFCENTER_URI + "wbmnt/update/wlist";
    // API.01.02.005 删除白名单接口
    public static final String API_TRO_CONFCENTER_DELETE_WLIST_URI = API_TRO_CONFCENTER_URI + "wbmnt/delete/wlist";
    // API.01.02.006 查询白名单字典列表接口
    public static final String API_TRO_CONFCENTER_DIC_QUERY_WLIST_URI = API_TRO_CONFCENTER_URI
        + "wbmnt/dic/query/wlist";
    // API.01.02.007 根据appname查询该应用下的白名单（查询pradar接口）
    public static final String API_TRO_CONFCENTER_QUERY_WLISTBYAPPNAME_URI = API_TRO_CONFCENTER_URI
        + "wbmnt/query/wlistbyappname";
    // API.01.02.008 根据applicationId查询该应用下的白名单列表
    public static final String API_TRO_CONFCENTER_QUERY_WLISTBYAPPID_URI = API_TRO_CONFCENTER_URI + "wbmnt/query/list";

    // API.01.02.007 添加黑名单接口
    public static final String API_TRO_CONFCENTER_ADD_BLIST_URI = API_TRO_CONFCENTER_URI + "add/blist";
    // API.01.02.008 查询黑名单列表
    public static final String API_TRO_CONFCENTER_QUERY_BLIST_URI = API_TRO_CONFCENTER_URI + "query/blist";
    // API.01.02.009 根据id查询黑名单详情接口
    public static final String API_TRO_CONFCENTER_QUERY_BLISTBYID_URI = API_TRO_CONFCENTER_URI
        + "query/blistbyid";
    // API.01.02.010 根据id更新黑名单接口
    public static final String API_TRO_CONFCENTER_UPDATE_BLIST_URI = API_TRO_CONFCENTER_URI + "update/blist";
    //API.01.02.010 根据id启用禁用黑名单接口
    public static final String API_TRO_CONFCENTER_USEYN_BLIST_URI = API_TRO_CONFCENTER_URI + "useyn/blist";
    // API.01.02.011 删除黑名单接口
    public static final String API_TRO_CONFCENTER_DELETE_BLIST_URI = API_TRO_CONFCENTER_URI + "delete/blist";

    /**
     * 03  链路管理子模块
     */
    // API.01.03.001 添加链路信息接口
    public static final String API_TRO_CONFCENTER_ADD_LINK_URI = API_TRO_CONFCENTER_URI + "linkmnt/add/link";
    // API.01.03.002 查询链路信息列表接口
    public static final String API_TRO_CONFCENTER_QUERY_LINKLIST_URI = API_TRO_CONFCENTER_URI
        + "linkmnt/query/linklist";
    //链路管理导出
    public static final String API_TRO_CONFCENTER_LINKLIST_DOWNLOAD = API_TRO_CONFCENTER_URI
        + "linkmnt/download/linklist";
    //链路管理上传
    public static final String API_TRO_CONFCENTER_LINKLIST_UPLOAD = API_TRO_CONFCENTER_URI + "linkmnt/upload/linklist";
    // API.01.03.003 根据链路id查询链路信息详情接口
    public static final String API_TRO_CONFCENTER_QUERY_LINKINFO_URI = API_TRO_CONFCENTER_URI
        + "linkmnt/query/linkinfo";
    // API.01.03.004 删除链路信息接口
    public static final String API_TRO_CONFCENTER_DELETE_LINKINFO_URI = API_TRO_CONFCENTER_URI + "linkmnt/delete/link";
    // API.01.03.005 查询链路等级字典列表
    public static final String API_TRO_CONFCENTER_QUERY_LINKRANK_URI = API_TRO_CONFCENTER_URI
        + "linkmnt/query/linkrank";
    // API.01.03.006 根据id更新链路信息接口
    public static final String API_TRO_CONFCENTER_UPDATE_LINKINFO_URI = API_TRO_CONFCENTER_URI
        + "linkmnt/update/linkinfo";
    // API.01.03.007 删除链路服务接口
    public static final String API_TRO_CONFCENTER_DELETE_LINKINTERFACE_URI = API_TRO_CONFCENTER_URI
        + "linkmnt/delete/linkinterface";
    // API.01.03.008 查询链路类型字典列表
    public static final String API_TRO_CONFCENTER_QUERY_LINKTYPE_URI = API_TRO_CONFCENTER_URI
        + "linkmnt/query/linktype";
    // API.01.03.009 通过链路类型查链路
    public static final String API_TRO_CONFCENTER_QUERY_LINKBYLINKTYPE_URI = API_TRO_CONFCENTER_URI
        + "linkmnt/query/linkByLinkType";
    // API.01.03.010 链路id和链路名称 下拉框使用
    public static final String API_TRO_CONFCENTER_QUERY_LINKIDNAME_URI = API_TRO_CONFCENTER_URI
        + "linkmnt/query/linkIdName";
    // API.01.03.011 查询链路模块字典列表
    public static final String API_TRO_CONFCENTER_QUERY_LINKMODULE_URI = API_TRO_CONFCENTER_URI
        + "linkmnt/query/linkmodule";
    // API.01.03.012 查询白名单列表
    public static final String API_TRO_CONFCENTER_QUERY_WHITELIST_URI = API_TRO_CONFCENTER_URI
        + "linkmnt/query/whiteListForLink";
    // API.01.03.013 查询计算单量方式字典列表
    public static final String API_TRO_CONFCENTER_QUERY_CALC_VOLUME_URI = API_TRO_CONFCENTER_URI
        + "linkmnt/query/calcVolumeList";
    // API.01.03.014 查询查询链路头信息, 包含：链路所属模块、数量、计算单量列表
    public static final String API_TRO_CONFCENTER_QUERY_LINK_HEADER_URI = API_TRO_CONFCENTER_URI
        + "linkmnt/query/linkHead";
    // API.01.03.015 查询链路模块下有哪些二级链路
    public static final String API_TRO_CONFCENTER_QUERY_SECONDLINK_BY_MODULE_URI = API_TRO_CONFCENTER_URI
        + "linkmnt/query/secondLinkByModule";
    /**
     * 04  二级链路管理子模块
     */
    // API.01.04.001 添加二级链路信息接口
    public static final String API_TRO_CONFCENTER_ADD_SECOND_LINK_URI = API_TRO_CONFCENTER_URI
        + "secondlinkmnt/add/link";
    // API.01.04.002 查询二级链路信息列表接口
    public static final String API_TRO_CONFCENTER_QUERY_SECOND_LINKLIST_URI = API_TRO_CONFCENTER_URI
        + "secondlinkmnt/query/linklist";
    // API.01.04.003 根据链路id查询链路信息详情接口
    public static final String API_TRO_CONFCENTER_QUERY_SECOND_LINKINFO_URI = API_TRO_CONFCENTER_URI
        + "secondlinkmnt/query/linkinfo";
    // API.01.04.004 删除链路信息接口
    public static final String API_TRO_CONFCENTER_DELETE_SECOND_LINKINFO_URI = API_TRO_CONFCENTER_URI
        + "secondlinkmnt/delete/link";
    // API.01.04.005 根据id更新链路信息接口
    public static final String API_TRO_CONFCENTER_UPDATE_SECOND_LINKINFO_URI = API_TRO_CONFCENTER_URI
        + "secondlinkmnt/update/linkinfo";

    /**
     * 05  一级链路管理子模块
     */
    // API.01.05.001 添加二级链路信息接口
    public static final String API_TRO_CONFCENTER_ADD_FIRST_LINK_URI = API_TRO_CONFCENTER_URI + "firstlinkmnt/add/link";
    // API.01.05.002 查询二级链路信息列表接口
    public static final String API_TRO_CONFCENTER_QUERY_FIRST_LINKLIST_URI = API_TRO_CONFCENTER_URI
        + "firstlinkmnt/query/linklist";
    // API.01.05.003 根据链路id查询链路信息详情接口
    public static final String API_TRO_CONFCENTER_QUERY_FIRST_LINKINFO_URI = API_TRO_CONFCENTER_URI
        + "firstlinkmnt/query/linkinfo";
    // API.01.05.004 删除链路信息接口
    public static final String API_TRO_CONFCENTER_DELETE_FIRST_LINKINFO_URI = API_TRO_CONFCENTER_URI
        + "firstlinkmnt/delete/link";
    // API.01.05.005 根据id更新链路信息接口
    public static final String API_TRO_CONFCENTER_UPDATE_FIRST_LINKINFO_URI = API_TRO_CONFCENTER_URI
        + "firstlinkmnt/update/linkinfo";
    // API.01.05.006 根据id查询链路拓扑
    public static final String API_TRO_CONFCENTER_QUERY_LINK_TOPOLOGY_URI = API_TRO_CONFCENTER_URI
        + "firstlinkmnt/query/linkTopology";
    // API.01.05.007 根据二级链路id查询是否存在一级链路
    public static final String API_TRO_CONFCENTER_EXIST_FIRST_LINK_URI = API_TRO_CONFCENTER_URI
        + "firstlinkmnt/query/existFirstLink";

    /**
     * 06 数据字典子模块
     */
    // API.01.06.001 保存数据字典
    public static final String API_TRO_CONFCENTER_SAVE_DICTIONARY_URL = API_TRO_CONFCENTER_URI + "dictionary/save";
    // API.01.06.002 修改数据字典
    public static final String API_TRO_CONFCENTER_UPDATE_DICTIONARY_URL = API_TRO_CONFCENTER_URI + "dictionary/update";
    // API.01.06.003 删除数据字典
    public static final String API_TRO_CONFCENTER_DELETE_DICTIONARY_URL = API_TRO_CONFCENTER_URI + "dictionary/delete";
    // API.01.06.004 查询数据字典详情
    public static final String API_TRO_CONFCENTER_QUERY_DICTIONARY_DETAIL_URL = API_TRO_CONFCENTER_URI
        + "dictionary/query/detail";
    // API.01.06.005 查询数据字典列表
    public static final String API_TRO_CONFCENTER_QUERY_DICTIONARY_LIST_URL = API_TRO_CONFCENTER_URI
        + "dictionary/query/list";

    // API.01.06.006 查询数据字典key_value值
    public static final String API_TRO_CONFCENTER_KV_DICTIONARY_URL = API_TRO_CONFCENTER_URI
        + "dictionary/queryDictValue";

    /**
     * 08 影子表配置 接口
     */
    // API.01.08.000 java agent 获取 影子表配置
    public static final String API_TRO_CONFCENTER_SHADOWCONFIG_AGENT_GET_SHADOWCONFIG_URL = API_TRO_CONFCENTER_URI
        + "shadowTableConfig/queryAppShadowTableConfig";
    // API.01.08.001 从pradar获取 应用的影子表配置
    public static final String API_TRO_CONFCENTER_SHADOWCONFIG_GET_CONFIG_FROM_PRADAR_URL = API_TRO_CONFCENTER_URI
        + "shadowTableConfig/getTableConfigFromPradar";
    // API.01.08.002 获取影子表配置分页
    public static final String API_TRO_CONFCENTER_SHADOWCONFIG_GET_CONFIG_PAGE_URL = API_TRO_CONFCENTER_URI
        + "shadowTableConfig/queryConfigPage";
    // API.01.08.003 添加影子表配置分页
    public static final String API_TRO_CONFCENTER_SHADOWCONFIG_ADD_CONFIG_URL = API_TRO_CONFCENTER_URI
        + "shadowTableConfig/save";
    // API.01.08.004 更新影子表配置分页
    public static final String API_TRO_CONFCENTER_SHADOWCONFIG_UPDATE_CONFIG_URL = API_TRO_CONFCENTER_URI
        + "shadowTableConfig/update";
    // API.01.08.005 删除影子表配置分页
    public static final String API_TRO_CONFCENTER_SHADOWCONFIG_DELETE_CONFIG_URL = API_TRO_CONFCENTER_URI
        + "shadowTableConfig/delete";
    // API.01.08.006 通过应用id 获取 该应用对应的 数据库ip端口与库名
    public static final String API_TRO_CONFCENTER_SHADOWCONFIG_QUERYIPPORTNAME_CONFIG_URL = API_TRO_CONFCENTER_URI
        + "shadowTableConfig/queryIpPortNameByApplictionId";
    // API.01.08.007 通过应用id 获取 该应用对应的数据库ip端口列表
    public static final String API_TRO_CONFCENTER_SHADOWCONFIG_QUERY_IPPORT_CONFIG_URL = API_TRO_CONFCENTER_URI
        + "shadowTableConfig/queryIpPortList";
    // API.01.08.008 通过应用id 获取 该应用对应的数据库名称列表
    public static final String API_TRO_CONFCENTER_SHADOWCONFIG_QUERY_DBNAME_CONFIG_URL = API_TRO_CONFCENTER_URI
        + "shadowTableConfig/queryDbNameList";
    // API.01.08.009 使用 获取影子库的应用列表氪模糊搜索
    public static final String API_TRO_CONFCENTER_SHADOWCONFIG_QUERY_SHADOWDB_URL = API_TRO_CONFCENTER_URI
        + "shadowTableConfig/queryShadowDbPage";
    // API.01.08.010 添加影子库数据源
    public static final String API_TRO_CONFCENTER_SHADOW_DATASOURCE_SAVE_URL = API_TRO_CONFCENTER_URI
        + "shadowTableConfig/saveShadowTableDataSource";
    // API.01.08.011 修改影子库数据源
    public static final String API_TRO_CONFCENTER_SHADOW_DATASOURCE_UPDATE_URL = API_TRO_CONFCENTER_URI
        + "shadowTableConfig/updateShadowTableDataSource";
    // API.01.08.012 获取数据源 所对应的应用
    public static final String API_TRO_CONFCENTER_SHADOW_DATASOURCE_GET_APPLICATION_URL = API_TRO_CONFCENTER_URI
        + "shadowTableConfig/getDatasourceAppliction";

    /**
     * 9 TRO 全局配置
     */
    //  API.01.09.000 新增全局配置
    public static final String API_TRO_CONFCENTER_GLOBAL_CONFIG_ADD = API_TRO_CONFCENTER_URI
        + "troConfig/addGlobalConfig";
    // API.01.09.001 查询全局配置
    public static final String API_TRO_CONFCENTER_GLOBAL_CONFIG_QUERY = API_TRO_CONFCENTER_URI
        + "troConfig/queryGlobalConfig";
    // API.01.09.002 更新全局配置
    public static final String API_TRO_CONFCENTER_GLOBAL_CONFIG_UPDATE = API_TRO_CONFCENTER_URI
        + "troConfig/updateGlobalConfig";

    /**
     * 10 链路拓扑图配置
     */
    // API.10.01.001 链路拓扑图Excel上传
    public static final String API_TRO_LINKTOPOLOGY_IMPORT_EXCEL_URI = API_TRO_CONFCENTER_URI
        + "linkTopology/importExcel";

    // API.10.01.002  通过链路分组查询 链路拓扑图
    public static final String API_TRO_LINKTOPOLOGY_QUERY_LINK_GROUP_URI = API_TRO_CONFCENTER_URI
        + "linkTopology/queryLinkGroup";
    // API.10.01.003 查询应用瓶颈数量
    public static final String API_TRO_LINKTOPOLOGY_QUERY_BOTTLENECK_COUNT_URI = API_TRO_CONFCENTER_URI
        + "linkTopology/query/linkBottleSummary";
    //API.10.01.004 查询链路节点瓶颈详情
    public static final String API_TRO_LINKTOPOLOGY_QUERY_BOTTLENECK_DETAIL_URI = API_TRO_CONFCENTER_URI
        + "linkTopology/query/linkBottleDetail";

    /**
     * 11 压测时间记录
     */
    //API.01.11.001 保存开始压测时间
    public static final String TRO_CONFCENTER_ADD_PRESSURETIME = API_TRO_CONFCENTER_URI + "pressureTime/add";
    public static final String TRO_CONFCENTER_UPDATE_PRESSURETIME = API_TRO_CONFCENTER_URI + "pressureTime/update";
    public static final String TRO_CONFCENTER_QUERY_LATEST_PRESSURETIME = API_TRO_CONFCENTER_URI + "pressureTime/query";

    /**
     * 压测控制模块
     */
    public static final String API_TRO_PRESSURECONTROL_URI = "/pressurecontrol/";

    /**
     * 压测前后置准备模块
     */
    public static final String API_TRO_PRESSUREREADY_URI = "/pressureready/";

    /**
     * 01 数据构建子模块
     */
    // API.02.01.001 根据条件查询构建信息
    public static final String API_TRO_PRESSUREREADY_BUILDDATA_QUERY_BUILDINFO_URI = API_TRO_PRESSUREREADY_URI
        + "builddata/query/buildinfo";
    // API.02.01.002 新增脚本执行状态接口
    public static final String API_TRO_PRESSUREREADY_BUILDDATA_UPDATE_SCRIPTSTATUS_URI = API_TRO_PRESSUREREADY_URI
        + "builddata/update/scriptstatus";
    // API.02.01.003 查询脚本构建状态接口
    public static final String API_TRO_PRESSUREREADY_BUILDDATA_QUERY_SCRIPTBUILDSTATUS_URI = API_TRO_PRESSUREREADY_URI
        + "builddata/query/scriptbuildstatus";
    // API.02.01.004 批量清理接口
    public static final String API_TRO_PRESSUREREADY_BUILDDATA_QUERY_BATCHCLEAN_URI = API_TRO_PRESSUREREADY_URI
        + "builddata/execute/batchclean";
    // API.02.01.005 构建执行脚本接口
    public static final String API_TRO_PRESSUREREADY_BUILDDATA_EXECUTE_SCRIPT_URI = API_TRO_PRESSUREREADY_URI
        + "builddata/execute/script";
    // API.02.01.006 数据构建调试开关接口
    public static final String API_TRO_PRESSUREREADY_BUILDDATA_DEBUG_SWITCH_URI = API_TRO_PRESSUREREADY_URI
        + "builddata/debug/switch";

    /**
     * 02 压测检测子模块
     */
    // API.02.02.001 查询压测检测接口(包含异常检测)
    public static final String API_TRO_PRESSUREREADY_PMCHECK_QUERY_CHECKLIST_URI = API_TRO_PRESSUREREADY_URI
        + "pmcheck/query/checklist";
    // API.02.02.002 影子库整体同步检测 接口(查询构建表)
    public static final String API_TRO_PRESSUREREADY_PMCHECK_CHECK_BASICDATA_URI = API_TRO_PRESSUREREADY_URI
        + "pmcheck/check/shadowlib";
    // API.02.02.003 白名单检测实时接口(包含dubbo和http)
    public static final String API_TRO_PRESSUREREADY_PMCHECK_CHECK_WLIST_URI = API_TRO_PRESSUREREADY_URI
        + "pmcheck/check/wlist";
    // API.02.02.004 缓存预热检测
    public static final String API_TRO_PRESSUREREADY_CACHE_CHECK_CACHE_URI = API_TRO_PRESSUREREADY_URI
        + "pmcheck/check/cache";
    // API.02.02.005 批量检测接口
    public static final String API_TRO_PRESSUREREADY_PMCHECK_CHECK_BATCHCHECK_URI = API_TRO_PRESSUREREADY_URI
        + "pmcheck/check/batchcheck";
    // API.02.02.006 压测检测调试开关接口
    public static final String API_TRO_PRESSUREREADY_PMCHECK_DEBUG_SWITCH_URI = API_TRO_PRESSUREREADY_URI
        + "pmcheck/debug/switch";

    /**
     * 权限模块
     */
    public static final String API_TRO_AUTHORITY_URI = "/authority/";
    // API.04.01.001 按钮权限控制
    public static final String API_TRO_BUTTON_AUTHORITY_URI = API_TRO_AUTHORITY_URI + "check";

    /**
     * 压测辅助模块
     */
    public static final String API_TRO_PRESSUREMEASUREMENT_ASSIST_URI = "/assist/";
    // API.05.02.008 抽数sql上传接口
    public static final String API_TRO_SQL_UPLOAD_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI + "dbconf/sql/upload";
    // API.05.02.009 抽数sql批量上传接口
    public static final String API_TRO_SQL_BATCH_UPLOAD_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI
        + "dbconf/sql/batch/upload";
    // API.05.02.010 抽数sql下载接口
    public static final String API_TRO_SQL_DOWNLOAD_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI
        + "dbconf/sql/download";
    /**
     * 01 压测辅助虚拟消费子模块
     */
    // API.05.01.001 启动消费脚本
    public static final String API_TRO_ASSIST_START_CONSUMER_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI
        + "startconsumer";
    // API.05.01.002 停止消费脚本
    public static final String API_TRO_ASSIST_STOP_CONSUMER_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI
        + "stopconsumer";
    // API.05.01.003 保存MQ消息
    public static final String API_TRO_MQMSG_ADD_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI + "mqmsg/add";
    // API.05.01.004 修改MQ消息
    public static final String API_TRO_MQMSG_UPDATE_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI + "mqmsg/update";
    // API.05.01.005 删除MQ消息
    public static final String API_TRO_MQMSG_DELETE_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI + "mqmsg/delete";
    // API.05.01.006 查询MQ消息列表
    public static final String API_TRO_MQMSG_QUERY_LIST_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI
        + "mqmsg/query/list";
    // API.05.01.007 查询MQ消息详情
    public static final String API_TRO_MQMSG_QUERY_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI + "mqmsg/query";
    // API.05.01.008 查询MQ消息类型
    public static final String API_TRO_MQMSG_QUERY_TYPE_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI
        + "mqmsg/query/type";

    /**
     * 03 MQ虚拟生产消息
     */
    // API.05.03.001 ESB/IBM 虚拟发送消息
    public static final String API_TRO_MQPRODUCER_EBM_SENDMSG_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI
        + "produce/ebm/mqmsg";
    // API.05.03.002 ROCKETMQ 虚拟发送消息
    public static final String API_TRO_MQPRODUCER_ROCKETMQ_SENDMSG_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI
        + "produce/rocketmq/mqmsg";
    // API.05.03.003 ESB/IBM/ROCKETMQ停止虚拟发送消息
    public static final String API_TRO_MQPRODUCER_STOP_SENDMSG_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI
        + "produce/mqmsg/stop";

    // API.05.03.004 新增ESB/IBM虚拟发送消息
    public static final String API_TRO_MQPRODUCER_EBM_ADD_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI
        + "produce/mqmsg/ebm/add";
    // API.05.03.005 删除ESB/IBM虚拟发送消息
    public static final String API_TRO_MQPRODUCER_EBM_DEL_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI
        + "produce/mqmsg/ebm/del";
    // API.05.03.006 修改ESB/IBM虚拟发送消息
    public static final String API_TRO_MQPRODUCER_EBM_UPDATE_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI
        + "produce/mqmsg/ebm/update";
    // API.05.03.007 查询ESB/IBM虚拟发送消息列表
    public static final String API_TRO_MQPRODUCER_EBM_QUERYLIST_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI
        + "produce/mqmsg/ebm/querylist";
    // API.05.03.008 根据id查询ESB/IBM虚拟发送消息详情
    public static final String API_TRO_MQPRODUCER_EBM_QUERYBYID_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI
        + "produce/mqmsg/ebm/querybyid";

    // API.05.03.009 新增ROCKETMQ虚拟发送消息
    public static final String API_TRO_MQPRODUCER_ROCKETMQ_ADD_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI
        + "produce/mqmsg/rocketmq/add";
    // API.05.03.010 删除ROCKETMQ虚拟发送消息
    public static final String API_TRO_MQPRODUCER_ROCKETMQ_DEL_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI
        + "produce/mqmsg/rocketmq/del";
    // API.05.03.011 修改ROCKETMQ虚拟发送消息
    public static final String API_TRO_MQPRODUCER_ROCKETMQ_UPDATE_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI
        + "produce/mqmsg/rocketmq/update";
    // API.05.03.012 查询ROCKETMQ虚拟发送消息列表
    public static final String API_TRO_MQPRODUCER_ROCKETMQ_QUERYLIST_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI
        + "produce/mqmsg/rocketmq/querylist";
    // API.05.03.013  根据id查询ROCKETMQ虚拟发送消息详情
    public static final String API_TRO_MQPRODUCER_ROCKETMQ_QUERYBYID_URI = API_TRO_PRESSUREMEASUREMENT_ASSIST_URI
        + "produce/mqmsg/rocketmq/querybyid";

    /**
     * 防作弊模块
     */
    public static final String API_TRO_PREVENT_CHEAT_URI = "/preventcheat/";

    /**
     * 01 应用信息配置管理
     */
    // API.06.01.001 查询应用各种开关
    public static final String API_TRO_CONFCENTER_APPLICATION_CONFIG_PAGE = API_TRO_PREVENT_CHEAT_URI
        + "applicationConfig/queryApplicationConfigPage";
    // API.06.01.002 查询某个应用的开关与全局开关
    public static final String API_TRO_CONFCENTER_APPLICATION_CONFIG_QUERY = API_TRO_PREVENT_CHEAT_URI
        + "applicationConfig/queryConfig";
    // API.06.01.003 批量更新应用
    public static final String API_TRO_CONFCENTER_APPLICATION_CONFIG_BATCH_UPDATE = API_TRO_PREVENT_CHEAT_URI
        + "applicationConfig/updateApplicationConfigBatch";

    /**
     * 02 应用信息上传
     */
    // API.06.02.001 应用信息上传 上传可能作弊 与 SQL 解析异常 以后还可以上传其他的
    public static final String API_TRO_APPLICATION_INFO_UPLOAD_UPLOAD = API_TRO_PREVENT_CHEAT_URI
        + "applicationInfo/uploadInfo";
    // API.06.02.002 上传信息查询分页
    public static final String API_TRO_APPLICATION_INFO_UPLOAD_QUERY_PAGE = API_TRO_PREVENT_CHEAT_URI
        + "applicationInfo/queryInfoPage";
    // API.06.02.003 上传信息类型字典
    public static final String API_TRO_APPLICATION_INFO_UPLOAD_INFO_TYPE = API_TRO_PREVENT_CHEAT_URI
        + "applicationInfo/queryInfoType";



    /* 注册中心增删改查 */

    public static final String API_TRO_ISOLATION_UPDATE_REG_CONFIG = "updateRegConfig";


    /* 引用增删改查 */

    public static final String API_TRO_ISOLATION_UPDATE_APP_CONFIG = "updateAppConfig";

    /* **********影子JOB********** */
    public static final String API_TRO_SIMPLIFY_SHADOW_JOB_CONFIGS = "/shadow/job/";
    public static final String API_TRO_SIMPLIFY_SHADOW_QUERY_CONFIGS = API_TRO_SIMPLIFY_SHADOW_JOB_CONFIGS + "query";
    public static final String API_TRO_SIMPLIFY_SHADOW_INSERT_CONFIGS = API_TRO_SIMPLIFY_SHADOW_JOB_CONFIGS + "insert";
    public static final String API_TRO_SIMPLIFY_SHADOW_UPDATE_CONFIGS = API_TRO_SIMPLIFY_SHADOW_JOB_CONFIGS + "update";
    public static final String API_TRO_SIMPLIFY_SHADOW_UPDATE_STATUS_CONFIGS = API_TRO_SIMPLIFY_SHADOW_JOB_CONFIGS
        + "updateStatus";
    public static final String API_TRO_SIMPLIFY_SHADOW_DELETE_CONFIGS = API_TRO_SIMPLIFY_SHADOW_JOB_CONFIGS + "delete";
    public static final String API_TRO_SIMPLIFY_SHADOW_QUERY_DETAIL_CONFIGS = API_TRO_SIMPLIFY_SHADOW_JOB_CONFIGS
        + "query/detail";

    /* **********影子JOB********** */
}



