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

package io.shulie.tro.web.app.common;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.DataSource;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import com.github.pagehelper.Page;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pamirs.tro.common.constant.TROConstantEnum;
import com.pamirs.tro.common.constant.TRODictTypeEnum;
import com.pamirs.tro.common.exception.TROModuleException;
import com.pamirs.tro.common.redis.RedisManager;
import com.pamirs.tro.common.util.DateUtils;
import com.pamirs.tro.common.util.PageInfo;
import com.pamirs.tro.common.util.Snowflake;
import com.pamirs.tro.entity.dao.assist.loaddata.TLoadDataDao;
import com.pamirs.tro.entity.dao.assist.loaddata.TReturnDataDao;
import com.pamirs.tro.entity.dao.common.CommonDao;
import com.pamirs.tro.entity.dao.confcenter.TApplicationIpDao;
import com.pamirs.tro.entity.dao.confcenter.TApplicationMntDao;
import com.pamirs.tro.entity.dao.confcenter.TBListMntDao;
import com.pamirs.tro.entity.dao.confcenter.TBaseConfigDao;
import com.pamirs.tro.entity.dao.confcenter.TFirstLinkMntDao;
import com.pamirs.tro.entity.dao.confcenter.TLinkMntDao;
import com.pamirs.tro.entity.dao.confcenter.TLinkTopologyInfoDao;
import com.pamirs.tro.entity.dao.confcenter.TSecondBasicDao;
import com.pamirs.tro.entity.dao.confcenter.TSecondLinkMntDao;
import com.pamirs.tro.entity.dao.confcenter.TShadowTableConfigDao;
import com.pamirs.tro.entity.dao.confcenter.TShadowTableDataSourceDao;
import com.pamirs.tro.entity.dao.confcenter.TWListMntDao;
import com.pamirs.tro.entity.dao.dict.TDictDao;
import com.pamirs.tro.entity.dao.dict.TDictionaryTypeMapper;
import com.pamirs.tro.entity.dao.monitor.TAlarmDao;
import com.pamirs.tro.entity.dao.monitor.TAlarmMonitorDao;
import com.pamirs.tro.entity.dao.monitor.TReportAppIpDetailDao;
import com.pamirs.tro.entity.dao.monitor.TReportDao;
import com.pamirs.tro.entity.dao.pressureready.TDataBuildDao;
import com.pamirs.tro.entity.dao.pressureready.TLinkDetectionDao;
import com.pamirs.tro.entity.dao.preventcheat.TApplicationInfoUploadDao;
import com.pamirs.tro.entity.dao.preventcheat.TApplicationMntConfigDao;
import com.pamirs.tro.entity.dao.user.TUserMapper;
import com.pamirs.tro.entity.domain.entity.BTRelationLink;
import com.pamirs.tro.entity.domain.entity.RelationLinkModel;
import com.pamirs.tro.entity.domain.query.Conf;
import com.pamirs.tro.entity.domain.vo.TLinkApplicationInterfaceVo;
import io.shulie.tro.web.app.init.sync.ConfigSyncService;
import io.shulie.tro.web.app.service.AopsService;
import io.shulie.tro.web.app.service.TAlarmService;
import io.shulie.tro.web.app.service.TFirstLinkMntService;
import io.shulie.tro.web.app.service.TReportService;
import io.shulie.tro.web.app.service.TSecondLinkMntService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 说明：公共抽取类
 *
 * @author shulie
 * @version V1.0
 * @date: 2018年3月15日 下午2:06:52
 * @see CommonServiceTest
 */
@Service
@SuppressWarnings("all")
public class CommonService {

    private static AtomicInteger cursor = new AtomicInteger(1);

    public final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected TApplicationMntDao tApplicationMntDao;

    @Autowired
    protected TBListMntDao tBListMntDao;

    @Autowired
    protected TLinkMntDao tLinkMnDao;

    @Autowired
    protected Snowflake snowflake;

    @Autowired
    protected TAlarmDao tAlarmDao;

    @Autowired
    protected TAlarmMonitorDao tAlarmMonitorDao;

    @Autowired
    protected TFirstLinkMntDao firstLinkMntDao;

    @Autowired
    protected TSecondBasicDao secondBasicDao;

    @Autowired
    protected TSecondLinkMntDao secondLinkDao;

    @Autowired
    protected TDictDao tDicDao;

    @Autowired
    protected CommonDao commonDao;

    @Autowired
    protected TApplicationIpDao tApplicationIpDao;

    @Autowired
    protected TDataBuildDao TDataBuildDao;

    @Autowired
    protected TWListMntDao tWListMntDao;

    @Autowired
    protected TLinkDetectionDao TLinkDetectionDao;

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    protected RedisManager redisManager;

    @Autowired
    protected TReportDao tReportDao;

    @Autowired
    protected TSecondLinkMntService tSecondLinkMntService;

    @Autowired
    protected AopsService aopsService;

    @Autowired
    protected TReportAppIpDetailDao tReportAppIpDetailDao;

    @Autowired
    protected TAlarmService tAlarmService;

    @Autowired
    protected ConfigSyncService configSyncService;

    @Autowired
    @Qualifier("ScriptThreadPool")
    protected ThreadPoolExecutor runShellTaskExecutor;

    @Autowired
    @Qualifier("loadDataThreadPool")
    protected ThreadPoolExecutor loadDataThreadPool;

    @Autowired
    @Qualifier("schedulerPool")
    protected TaskScheduler taskScheduler;

    @Autowired
    @Qualifier("asynExecuteScriptThreadPool")
    protected Executor asynExecuteScriptThreadPool;

    @Autowired
    protected SqlSessionFactory sqlSessionFactory;
    @Autowired
    protected TDictionaryTypeMapper tDictionaryTypeMapper;
    @Autowired
    protected TSecondLinkMntDao secondLinkMntDao;
    @Autowired
    protected TLinkMntDao tLinkMntDao;
    @Autowired
    protected TReportService tReportService;
    @Autowired
    protected TFirstLinkMntService firstLinkService;
    @Autowired
    protected TSecondBasicDao tSecondBasicDao;
    @Autowired
    protected TShadowTableConfigDao tShadowTableConfigDao;
    @Autowired
    protected TShadowTableDataSourceDao tShadowTableDataSourceDao;
    @Autowired
    protected TLoadDataDao TLoadDataDao;
    @Autowired
    protected TReturnDataDao returnDataDao;
    @Autowired
    protected TApplicationMntConfigDao tApplicationMntConfigDao;
    @Autowired
    protected TBaseConfigDao tbaseConfigDao;
    @Autowired
    protected TApplicationInfoUploadDao tApplicationInfoUploadDao;
    /*

        @Autowired
        protected RedissonManager redissonManager;
    */
    @Autowired
    protected TLinkTopologyInfoDao tLinkTopologyInfoDao;
    @Autowired
    protected TUserMapper TUserMapper;
    @Value("${basePath}")
    private String basePath;
    @Value("${pradar_url}")
    private String pradarUrl;
    @Value("${loadSqlPath}")
    private String loadSqlPath;
    @Value("${pm.prefix}")
    private String pmPrefix;
    @Value("${mq.script.name}")
    private String mqScriptName;
    @Value("${mq.producer.script.name}")
    private String mqProducerScriptName;
    /**
     * 数据回传读取数据大小
     */
    @Value("${read.count}")
    private int readCount;
    /**
     * 数据回传中转落地文件地址
     */
    @Value("${loadDataFile}")
    private String loadDataFile;
    @Value("${jar.upload.path}")
    private String jarUploadPath;

    public String getPradarUrl() {
        return pradarUrl;
    }

    public String getBasePath() {
        return basePath;
    }

    public String getLoadSqlPath() {
        return loadSqlPath;
    }

    public String getPmPrefix() {
        return pmPrefix;
    }

    public String getMqScriptName() {
        return mqScriptName;
    }

    public String getLoadDataFile() {
        return loadDataFile;
    }

    public int getReadCount() {
        return readCount;
    }

    public String getMqProducerScriptName() {
        return mqProducerScriptName;
    }

    public String getJarUploadPath() {
        return jarUploadPath;
    }

    /**
     * 说明: 把map中的key转换成小写
     *
     * @param map
     * @return
     * @author shulie
     * @time：2017年11月24日 下午4:34:53
     */
    protected Map<String, Object> switchCaseFromMap(Map<String, Object> map) {

        Map<String, Object> resultMap = Maps.newHashMap();
        for (Entry<String, Object> entry : map.entrySet()) {
            resultMap.put(entry.getKey().toLowerCase(), entry.getValue());
        }
        return resultMap;
    }

    /**
     * 说明：转换map中的value为String类型
     * ① 此方法适用于key为String类型
     * ② 此方法适用的数据格式有 Map<String,Object>; Map<String, Map<String, Object>>;
     * Map<String,List<Map<String,Object>>>; List<Map<String, Map<String, Object>>>;
     * List<Map<String,List<Map<String,Object>>>> (目前测试可用), 其他格式需要测试方可使用
     *
     * @param obj 泛型参数
     * @return
     * @author shulie
     * @time：2017年12月1日 下午5:50:15
     */
    public <T> T transferElementToString(T obj) {
        if (obj == null) {
            return obj;
        }

        if (obj instanceof List) {
            List list = (List)obj;
            if (list.isEmpty()) {
                return (T)Lists.newArrayList();
            }
        } else if (obj instanceof Map) {
            Map map = (Map)obj;
            if (map.isEmpty()) {
                return (T)Maps.newHashMap();
            }
        }

        if (obj instanceof Map) {
            Map<String, Object> rMap = (Map<String, Object>)obj;
            Map<String, Object> resultMap = Maps.newHashMap();
            for (Entry<String, Object> entry : rMap.entrySet()) {
                if (entry.getValue() instanceof List) {
                    List<Map<String, Object>> innerList = (List<Map<String, Object>>)entry.getValue();
                    resultMap.put(entry.getKey(), transferElementToString(innerList));
                } else if ((entry.getValue() instanceof Map)) {
                    Map<String, Object> innerMap = (Map<String, Object>)entry.getValue();
                    resultMap.put(entry.getKey(), transferElementToString(innerMap));
                } else if (entry.getValue() instanceof String) {
                    resultMap.put(entry.getKey(), entry.getValue());
                } else if (entry.getValue() instanceof Date) {
                    resultMap.put(entry.getKey(), DateUtils.transferDateToString(entry.getValue()));
                } else {
                    resultMap.put(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            return (T)resultMap;
        }

        if (obj instanceof List) {
            List<Map<String, Object>> rList = (List<Map<String, Object>>)obj;
            List<Map<String, Object>> list = Lists.newArrayList();
            for (Map<String, Object> map : rList) {
                list.add(transferElementToString(map));
            }

            return (T)list;
        }
        return obj;
    }

    /**
     * 说明：线程提交线程池,并执行线程(先完成的线程任务先出)
     *
     * @param executor  执行器
     * @param workTasks 提交线程的任务
     * @throws InterruptedException 异常
     * @throws ExecutionException 异常
     * @author shulie
     * @time：2017年11月27日 下午2:29:03
     */
    public <T> List<T> executeWorktask(Executor executor, Collection<Callable<T>> workTasks)
        throws InterruptedException, ExecutionException {
        CompletionService<T> completionService = new ExecutorCompletionService<T>(executor);
        workTasks.forEach(workTask -> completionService.submit(workTask));
        List<T> newArrayList = Lists.newArrayList();
        for (int i = 0; i < workTasks.size(); i++) {
            newArrayList.add(completionService.take().get());
        }
        return newArrayList;
    }

    /**
     * 说明：监控线程池的情况
     *
     * @param executor
     * @author shulie
     * @time：2017年12月3日 下午6:09:15
     */
    public Map<String, Object> monitorThreadPoolInfo(ThreadPoolExecutor executor, String threadPoolName) {
        // 初始线程数、核心线程数、正在执行的任务数量、已完成任务数量、任务总数、队列里缓存的任务数量、池中存在的最大线程数、最大允许的线程数、线程空闲时间、线程池是否关闭、线程池是否终止
        LOGGER.info(String.format(
            threadPoolName
                + " 初始线程数: %d, 核心线程数: %d, 正在执行的任务数量: %d, 已完成任务数量: %d, 任务总数: %d, 队列里缓存的任务数量: %d, 池中存在的最大线程数: %d, "
                + "最大允许的线程数: %d,  线程空闲时间: %d, 线程池是否关闭: %s, 线程池是否终止: %s",
            executor.getPoolSize(), executor.getCorePoolSize(), executor.getActiveCount(),
            executor.getCompletedTaskCount(), executor.getTaskCount(), executor.getQueue().size(),
            executor.getLargestPoolSize(), executor.getMaximumPoolSize(),
            executor.getKeepAliveTime(TimeUnit.MILLISECONDS), executor.isShutdown(), executor.isTerminated()));

        return new HashMap<String, Object>() {
            {
                put("线程池名称", threadPoolName);
                put("线程池中线程数", executor.getPoolSize());
                put("核心线程数", executor.getCorePoolSize());
                put("正在执行的任务数量", executor.getActiveCount());
                put("已完成任务数量", executor.getCompletedTaskCount());
                put("任务总数", executor.getTaskCount());
                put("队列里缓存的任务数量", executor.getQueue().size());
                put("池中存在的最大线程数", executor.getLargestPoolSize());
                put("最大允许的线程数", executor.getMaximumPoolSize());
                put("线程空闲时间(毫秒)", executor.getKeepAliveTime(TimeUnit.MILLISECONDS));
                put("线程池是否关闭", executor.isShutdown());
                put("线程池是否终止", executor.isTerminated());
            }
        };
    }

    /**
     * 说明：重新设置pageHelp的查询总数
     *
     * @param querySubjectlist
     * @param pageInfo
     * @author shulie
     * @time：2017年12月11日 下午5:41:44
     */
    public void setPageInfo(List<Map<String, Object>> querySubjectlist, PageInfo<Map<String, Object>> pageInfo) {
        if (querySubjectlist instanceof Page) {
            Page page = (Page)querySubjectlist;
            pageInfo.setPageNum(page.getPageNum());
            pageInfo.setTotal(page.getTotal());
        }
    }

    /**
     * 说明: 查询链路应用关系,更新构建表链路信息
     *
     * @param linkId        链路id
     * @param interfaceName 接口名称
     * @return 链路应用列表
     * @author shulie
     */
    @Deprecated
    public List<TLinkApplicationInterfaceVo> selectLinkApplicationInfo(String linkId) {
        return commonDao.selectLinkApplicationInfo(linkId);
    }

    /**
     * 说明：mybatis批量插入/更新数据
     *
     * @param sqlSessionFactory
     * @param mapperClass       要使用的Mapper的Class
     * @param pojoClass         列表中POJO对象的Class
     * @param methodName        要执行的Mapper类中的方法名
     * @param objList           要入库的数据列表
     * @return
     * @author shulie
     * @time：2017年11月28日 下午3:03:00
     */
    public <T> boolean batchInsertByMapper(SqlSessionFactory sqlSessionFactory, Class<?> mapperClass,
        Class pojoClass, String methodName, List<?> objList) {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        boolean addCheck = true;
        Class[] paramTypes = new Class[1];
        T mapper = (T)session.getMapper(mapperClass);
        try {
            paramTypes[0] = Class.forName(pojoClass.getName());
            Method method = mapperClass.getMethod(methodName, paramTypes);
            for (Object obj : objList) {
                //去除null 否则oracle 无法识别类型
                Map<String, Object> objectMap = (Map<String, Object>)obj;
                Map<String, Object> keys = (Map<String, Object>)objectMap.get("map");
                Map<String, Object> notNullValue = Maps.newHashMapWithExpectedSize(10);
                for (Map.Entry<String, Object> entry : keys.entrySet()) {
                    if (entry.getValue() == null) {
                        continue;
                    }
                    notNullValue.put(entry.getKey(), entry.getValue());
                }
                objectMap.put("map", notNullValue);
                method.invoke(mapper, obj);
            }
            session.flushStatements();
            session.commit();
            session.clearCache();
        } catch (Exception ex) {
            LOGGER.error("批量添加/更新失败 methodName:" + methodName, ex);
            addCheck = false;
            session.rollback();
        } finally {
            session.close();
        }
        return addCheck;
    }

    /**
     * 说明: 根据链路id查询链路应用服务信息插入到链路检测表中
     *
     * @param linkId 链路id
     * @return 链路应用服务信息
     * @author shulie
     */
    @Deprecated
    public List<TLinkApplicationInterfaceVo> selectLinkWholeInfo(String linkId) {
        return commonDao.selectLinkWholeInfo(linkId);
    }

    /**
     * 说明: 过滤非法字符
     *
     * @param 传入的字符串数据
     * @return 过滤掉的字符串
     * @author shulie
     * @date 2018/8/6 11:38
     */
    protected String filterIllegalCharacters(String str) {
        List<String> lists = Lists.newArrayList();
        StringBuilder sb = new StringBuilder();
        str = StringUtils.trimToEmpty(str);
        //		boolean flag=
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            // These characters are part of the query syntax and must be escaped
            if (c == '\\' || c == '＋' || c == '－' || c == '(' || c == ')' || c == '!' || c == '^' || c == '['
                || c == ']' || c == '\"'
                || c == '{' || c == '}' || c == '~' || c == '?' || c == '&' || c == '/' || c == '。' || c == '】'
                || c == '【' || c == '！' || c == '；' || c == '？'
                || c == '（' || c == '）' || c == '“' || c == '”' || c == '\'' || c == '＇' || c == '＂' || Character
                .isWhitespace(c)
                //                    c == '+' ||  c == '-' || c == ':' || c == '：' || c == '*' || c == '|' || c ==
                //                    ';' || c == '.'|| c == '_'
            ) {
                sb.append("");
                continue;
            }
            sb.append(c);
        }
        return StringUtils.trimToEmpty(sb.toString()).trim();
    }

    /**
     * 说明: 关闭输入输出流和销毁对象
     *
     * @param reader缓冲输入流,inputStream标准输入流,bytes字节数组输出流,processes多个进程对象
     * @author shulie
     * @date 2018/8/9 14:54
     */
    protected void closeAll(BufferedReader reader, InputStream inputStream, ByteArrayOutputStream bytes,
        Process... processes) {

        if (processes != null) {
            for (Process process : processes) {
                if (process != null) {
                    process.destroy();
                }
            }
        }

        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                LOGGER.info("PressureMeasurementAssistService.closeAll 关闭流异常{}", e);
            }
        }

        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                LOGGER.info("PressureMeasurementAssistService.closeAll 关闭输入流异常{}", e);
            }
        }
        if (bytes != null) {
            try {
                bytes.close();
            } catch (IOException e) {
                LOGGER.info("PressureMeasurementAssistService.closeAll 关闭字节数组输出流异常{}", e);
            }
        }
    }

    /**
     * 说明：关闭资源
     *
     * @param ps   预设语句对象
     * @param conn jdbc连接
     * @param rs   结果集对象
     * @author shulie
     * @date 2018/8/31 19:15
     */
    public void closeAll(PreparedStatement ps, Connection conn, ResultSet rs) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 说明: 根据类型查询字典列表
     *
     * @return 白名单列表或者基础链路等级字典列表
     * @author shulie
     */
    public Map<String, Object> queryDicList(TRODictTypeEnum troDictTypeEnum) {
        List<Map<String, Object>> queryWListDic = tDicDao.queryDicList(troDictTypeEnum.toString());
        if (queryWListDic.isEmpty()) {
            return Maps.newHashMap();
        }

        Map<String, Object> dicList = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        queryWListDic.forEach(map -> {
            resultMap.put(MapUtils.getString(map, TROConstantEnum.VALUE_ORDER.toString()),
                MapUtils.getString(map, TROConstantEnum.VALUE_NAME.toString()));
        });
        dicList.put("dicList", resultMap);
        dicList.put("dictType", MapUtils.getString(queryWListDic.get(0), TROConstantEnum.DICT_TYPE.toString()));

        return dicList;
    }

    /**
     * 说明: 解析字子级链路并插入关联关系链路表
     *
     * @param parentLinkId 父链路id
     * @param childLinks   子链路id
     * @param objTable     目标表
     * @author shulie
     * @date 2018/12/26 18:00
     */
    protected void saveRelationLink(Long parentLinkId, String childLinks, String objTable) {
        if (StringUtils.isEmpty(childLinks)) {
            LOGGER.info("下属链路为空");
            return;
        }
        //解析baseLinks, 然后持久化
        List<List<String>> jsonList = JSON.parseObject(childLinks,new TypeReference<List<List<String>>>(){});

        for (int i = 0; i < jsonList.size(); i++) {
            List<String> childLinkList = jsonList.get(i);
            for (int j = 0; j < childLinkList.size(); j++) {
                String childLinkId = childLinkList.get(j);
                RelationLinkModel relationLinkModel = new BTRelationLink();
                ((BTRelationLink)relationLinkModel).setObjTable(objTable);
                relationLinkModel.setParentLinkId(parentLinkId);
                relationLinkModel.setChildLinkId(Long.parseLong(childLinkId));
                relationLinkModel.setLinkBank(i + 1);
                relationLinkModel.setLinkOrder(j + 1);
                commonDao.saveRelationLink(relationLinkModel);
            }
        }
    }

    /**
     * 说明: 转换子级链路名称和id
     *
     * @param objTable     目标表
     * @param parentLinkId 父级链路id
     * @return 子级链路名称和id集合
     * @author shulie
     * @date 2018/12/27 11:17
     */
    public List<List<Map<String, Object>>> getRelationLinkRelationShip(String objTable, String parentLinkId) {
        //1，根据二级链路找到基础链路列表
        List<Map<String, Object>> relationShipLists = commonDao.queryRelationLinkRelationShip(objTable, parentLinkId);
        if (CollectionUtils.isEmpty(relationShipLists)) {
            return Lists.newArrayList();
        }

        ConcurrentMap<String, List<Map<String, Object>>> groupBasicLink = relationShipLists.stream().collect(Collectors
            .groupingByConcurrent(map -> MapUtils.getString(map, "LINK_BANK")));
        List<List<Map<String, Object>>> baseLinkList = Lists.newArrayListWithCapacity(groupBasicLink.size());
        groupBasicLink.forEach((string, mapList) -> {
            for (Iterator<Map<String, Object>> iterator = mapList.iterator(); iterator.hasNext(); ) {
                iterator.next().remove("LINK_BANK");
            }
            baseLinkList.add(mapList);
        });
        return baseLinkList;
    }

    /**
     * 说明: 指定标识符拆分字符串为集合
     *
     * @param separator the literal, nonempty string to recognize as a separator
     * @param string    split string
     * @return Split collection
     * @author shulie
     * @date 2018/8/31 15:36
     */
    public List<String> splitStrings(String separator, String string) throws TROModuleException {
        if (StringUtils.isAnyEmpty(separator, string)) {
            throw new TROModuleException("分隔符或者带分隔字符串不能为空");
        }
        return Splitter.on(separator)
            .omitEmptyStrings()
            .trimResults()
            .splitToList(string)
            .stream()
            .distinct()
            .collect(Collectors.toList());
    }

    /**
     * 说明：根据不同的数据源动态生成对应数据源的SqlSessionFactory/JdbcTemplate
     *
     * @param dataSourceEnum
     * @param t              录入参数允许为SqlSessionFactory.class,JdbcTemplate.class
     * @return 返回SqlSessionFactory, JdbcTemplate
     * @throws Exception
     * @author shulie
     * @Date: Create in 2018/8/31 15:56
     */
    public <T> T dymanicGenerateDataSource(Class<T> t, Conf conf)
        throws Exception {
        DruidDataSource druidDataSource = createDataSource(conf);
        if (t.isAssignableFrom(SqlSessionFactory.class)) {
            SqlSessionFactory sqlSessionFactory = null;
            sqlSessionFactory = sqlSessionFactoryBean(druidDataSource);
            return (T)sqlSessionFactory;
        } else if (t.isInstance(JdbcTemplate.class)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(druidDataSource);
            return (T)jdbcTemplate;
        } else {
            throw new RuntimeException("创建数据源时,参数类型错误");
        }
    }

    /**
     * 说明：动态创建数据源
     *
     * @param config 数据源配置对象
     * @return
     * @throws SQLException
     * @author shulie
     * @date 2018/8/31 19:50
     */
    public DruidDataSource createDataSource(Conf conf) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();

        dataSource.setInitialSize(5);
        dataSource.setMinIdle(5);
        dataSource.setMaxActive(30);
        dataSource.setConnectionErrorRetryAttempts(2);
        dataSource.setQueryTimeout(30);
        dataSource.setMaxWait(60000);
        dataSource.setTimeBetweenEvictionRunsMillis(6000); // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        dataSource.setMinEvictableIdleTimeMillis(60000); // 配置一个连接在池中最小生存的时间，单位是毫秒
        dataSource.setValidationQuery("SELECT 1 FROM DUAL");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        StatFilter statFilter = new StatFilter();
        statFilter.setSlowSqlMillis(1000);
        statFilter.setLogSlowSql(true);
        dataSource.setProxyFilters(Stream.of(statFilter).collect(Collectors.toList()));
        dataSource.setFilters("stat,slf4j");
        dataSource.setDriverClassName(conf.getDriverClassName());
        dataSource.setUrl(conf.getUrl());
        dataSource.setUsername(conf.getUsername());
        try {
            dataSource.setPassword(ConfigTools.decrypt(conf.getPublicKey(), conf.getPasswd()));
        } catch (Exception e) {
            LOGGER.error("生成dataSource 异常 {}", e);
        }
        return dataSource;
    }

    /**
     * 说明：根据不同的数据源动态生成不同的SqlSessionFactory
     *
     * @param dataSource
     * @return
     * @throws Exception
     * @author shulie
     * @date 2018/8/31 19:50
     */
    public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage("com.pamirs.tro.entity.dao");
        bean.setMapperLocations(new PathMatchingResourcePatternResolver()
            .getResources("classpath*:com/pamirs/tro/entity/mapper/**/*.xml"));
        bean.setConfigLocation(new DefaultResourceLoader().getResource("classpath:ibatis.xml"));
        return bean.getObject();
    }

    /**
     * 说明：获取数据源的连接
     *
     * @param configList 数据源对象集合
     * @return 连接
     * @author shulie
     * @Date: Create in 2018/8/31 15:56
     */
    public Connection getConnection(Conf conf) {
        Connection conn = null;
        try {
            Class.forName(conf.getDriverClassName());
            conn = (Connection)DriverManager.getConnection(conf.getUrl(), conf.getUsername(),
                ConfigTools.decrypt(conf.getPublicKey(), conf.getPasswd()));
        } catch (Exception e) {
            LOGGER.error("getConnection 获取链接失败", e);
        }
        return conn;
    }

    /**
     * 说明： 批量从ResultSet中获取数据,每次获取count数量，然后游标移动count，以此类推(适用于多线程方法)
     *
     * @param sql  要传入的sql语句
     * @param objs sql语句中占位符的数组
     * @param con  连接对象
     * @return 返回的是要查找的数据集合
     * @throws SQLException sql异常
     * @author shulie
     * @date 2018/8/31 18:50
     */
    public List<Map<String, Object>> selectResultSet(String sql, Object[] objs, Connection con, int count)
        throws SQLException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setFetchSize(1000);
            ps.setFetchDirection(ResultSet.FETCH_FORWARD);
            if (objs != null) {
                for (int i = 0; i < objs.length; i++) {
                    ps.setObject(i + 1, objs[i] + "");
                }
            }
            ResultSet rs = ps.executeQuery();
            rs.absolute(cursor.get());
            rs.previous();
            System.out.println("cursor 现在是多少" + cursor.get());
            ResultSetMetaData colinfo = rs.getMetaData();
            // 获取列的总数
            int cols = colinfo.getColumnCount();

            while (rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < cols; i++) {
                    map.put(colinfo.getColumnName(i + 1), rs.getObject(i + 1));
                }

                list.add(map);
                if (count != 0 && list.size() == count) {
                    return list;
                }
            }
        } catch (SQLException e) {
            LOGGER.error("sql异常", e);
            e.printStackTrace();
        }
        return list;
    }

    public Runnable shoe(Connection connection, int count) {
        return () -> {
            String sql = "SELECT id ,name,addr,age from user";
            try {
                List<Map<String, Object>> maps = selectResultSet(sql, null, connection, count);
                System.out.println(JSON.toJSONString(maps));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
    }

    /**
     * 说明：读取结果集多行数据
     *
     * @param resultSet 结果集
     * @param nLines    行数
     * @return 多行数据结果
     * @author shulie
     * @date 2018/8/31 19:50
     */
    public List<Map<String, Object>> readNLine(ResultSet resultSet, int nLines) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            ResultSetMetaData colinfo = resultSet.getMetaData();
            int cols = colinfo.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < cols; i++) {
                    map.put(colinfo.getColumnLabel(i + 1), resultSet.getObject(i + 1));
                }

                list.add(map);
                if (nLines != 0 && list.size() == nLines) {
                    return list;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 说明：根据sql和参数查询数据库返回数据
     *
     * @param sql  要查询的sql
     * @param objs sql语句中占位符的数组
     * @param con  连接对象
     * @return 返回的是要查找的数据集合
     * @throws SQLException 异常
     * @author shulie
     * @date 2018/8/31 20:02
     */
    public List<Map<String, Object>> selectResultSet(String sql, Object[] objs, Connection con) throws SQLException {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setFetchSize(1000);
            ps.setFetchDirection(ResultSet.FETCH_REVERSE);
            if (objs != null) {
                for (int i = 0; i < objs.length; i++) {
                    ps.setObject(i + 1, objs[i] + "");
                }
            }

            ResultSet rs = ps.executeQuery();
            ResultSetMetaData colinfo = rs.getMetaData();
            // 获取列的总数
            int cols = colinfo.getColumnCount();

            while (rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < cols; i++) {
                    map.put(colinfo.getColumnLabel(i + 1), rs.getObject(i + 1));
                }

                list.add(map);
            }
        } catch (SQLException e) {
            LOGGER.error("sql异常", e);
            e.printStackTrace();
        } finally {
            // 关闭所有的对象
            closeAll(ps, con, null);
        }
        return list;
    }

    /**
     * 说明：返回结果集对象
     *
     * @param sql  要传入的sql语句
     * @param objs sql语句中占位符的数组
     * @param con  连接对象
     * @return
     * @author shulie
     * @date 2018/8/31 20:07
     */
    public ResultSet getResultSet(String sql, Object[] objs, Connection con) {

        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setFetchSize(50);
            ps.setFetchDirection(ResultSet.FETCH_REVERSE);

            if (objs != null) {
                for (int i = 0; i < objs.length; i++) {
                    ps.setObject(i + 1, objs[i] + "");
                }
            }

            resultSet = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } /*finally {
			// 关闭所有的对象
			closeAll(ps, con, null);
		}*/
        return resultSet;
    }

    /**
     * 说明: 批量执行
     *
     * @param
     * @return
     * @author shulie
     * @date 2018/8/31 19:51
     */
    public void batchExecute(String sql, List<List<Object>> objs, Connection con) throws Exception {
        con.setAutoCommit(false);
        PreparedStatement ps = null;
        int index = 0;
        try {
            ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setFetchSize(1000);
            ps.setFetchDirection(ResultSet.FETCH_REVERSE);

            if (objs != null) {
                for (int i = 0; i < objs.size(); i++) {
                    List<Object> list = objs.get(i);
                    for (int j = 0; j < list.size(); j++) {
                        ps.setObject(j + 1, list.get(j) + "");
                    }
                    ps.addBatch();
                    index++;
                    if (index % 5000 == 0) {
                        ps.executeBatch();
                        ps.clearBatch();
                        con.commit();
                    }
                }
            }

            if (index % 5000 != 0) {
                ps.executeBatch();
                con.commit();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭所有的对象
            closeAll(ps, con, null);
        }
    }

    /**
     * 说明：批量执行
     *
     * @param sql  待执行的sql
     * @param objs 待执行的sql的参数
     * @param con  连接
     * @throws Exception 异常
     * @author shulie
     * @date 2018/8/31 20:33
     */
    public void batchExecute(String sql, List<List<Object>> objs, Conf conf) throws Exception {
        Connection con = getConnection(conf);
        con.setAutoCommit(false);
        PreparedStatement ps = null;
        int index = 0;
        try {
            ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setFetchSize(1000);
            ps.setFetchDirection(ResultSet.FETCH_REVERSE);

            if (objs != null) {
                for (int i = 0; i < objs.size(); i++) {
                    List<Object> list = objs.get(i);
                    for (int j = 0; j < list.size(); j++) {
                        ps.setObject(j + 1, list.get(j) + "");
                    }
                    ps.addBatch();
                    index++;
                    if (index % 5000 == 0) {
                        ps.executeBatch();
                        ps.clearBatch();
                        con.commit();
                    }
                }
            }

            if (index % 5000 != 0) {
                ps.executeBatch();
                con.commit();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭所有的对象
            closeAll(ps, con, null);
        }
    }

    /**
     * 说明: 执行sql
     *
     * @param sql sql脚本
     * @author shulie
     * @date 2018/9/6 16:32
     */
    public void executeSql(String sql, Conf conf) throws Exception {
        Connection con = getConnection(conf);
        con.setAutoCommit(false);
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setFetchSize(1000);
            ps.setFetchDirection(ResultSet.FETCH_REVERSE);
            ps.execute();
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭所有的对象
            closeAll(ps, con, null);
        }
    }

    /**
     * 说明: 执行sql
     *
     * @param sql sql脚本
     * @author shulie
     * @date 2018/9/6 16:32
     */
    public void executeSql(String sql, Connection con) {
        PreparedStatement ps = null;
        try {
            con.setAutoCommit(false);
            //oracle 数据库需要去除 \n
            sql = sql.replaceAll("\\\\\n", "");
            ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setFetchSize(1000);
            ps.setFetchDirection(ResultSet.FETCH_REVERSE);
            ps.execute();
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("CommonService.executeSql 执行sql语句异常{}", e);
        } finally {
            // 关闭所有的对象
            closeAll(ps, con, null);
        }
    }

    /**
     * 说明：获取查询结果集的数据总数
     *
     * @param sql  要传入的sql语句
     * @param objs sql语句中占位符的数组
     * @param con  连接对象
     * @return
     * @throws SQLException
     * @author shulie
     * @date 2018/8/31 20:02
     */
    public long getResultDataTotal(String sql, Object[] objs, Connection con) throws SQLException {

        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setFetchSize(1000);
            ps.setFetchDirection(ResultSet.FETCH_FORWARD);
            if (objs != null) {
                for (int i = 0; i < objs.length; i++) {
                    ps.setObject(i + 1, objs[i] + "");
                }
            }

            resultSet = ps.executeQuery();
            resultSet.last();

            return resultSet.getRow();
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("CommonService.getResultDataTotal 执行sql语句异常{}", e);
        } /*finally {
			// 关闭所有的对象
			closeAll(ps, null, null);
		}*/
        return -1;
    }

    /**
     * 说明: 生成一串不重复的52位字符串
     * 生成规则: 当前系统时间(到秒)+32为UUID
     * 例:2018-09-05_21-06-20_a2482adf87324c2dbee267feef0ba5d6
     *
     * @param
     * @return
     * @author shulie
     * @date 2018/9/5 21:04
     */
    public String generateUUID() {
        return Joiner.on("_")
            .join(Splitter.on(" ")
                .omitEmptyStrings()
                .splitToList(DateUtils.getServerTime().replaceAll(":", "-"))) + "_" + UUID.randomUUID().toString()
            .replaceAll("-", "");
    }

}
