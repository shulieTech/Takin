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

package io.shulie.tro.web.app.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pamirs.tro.common.constant.InterfaceLevelEnum;
import com.pamirs.tro.common.constant.LinkBottleneckLevelEnum;
import com.pamirs.tro.common.constant.LinkBottleneckTypeEnum;
import com.pamirs.tro.common.constant.PageLevelEnum;
import com.pamirs.tro.common.util.DateUtils;
import com.pamirs.tro.common.util.HttpUtils;
import com.pamirs.tro.common.util.http.DateUtil;
import com.pamirs.tro.entity.dao.bottleneck.TLinkBottleneckDao;
import com.pamirs.tro.entity.dao.confcenter.TApplicationMntDao;
import com.pamirs.tro.entity.dao.confcenter.TLinkTopologyInfoDao;
import com.pamirs.tro.entity.dao.confcenter.TWListMntDao;
import com.pamirs.tro.entity.dao.transparentflow.TPressureTimeRecordDao;
import com.pamirs.tro.entity.domain.entity.LinkBottleneck;
import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import com.pamirs.tro.entity.domain.entity.TLinkTopologyInfo;
import com.pamirs.tro.entity.domain.entity.TPressureTimeRecord;
import com.pamirs.tro.entity.domain.entity.TWList;
import io.shulie.tro.web.app.service.LinkBottleneckService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author: xingchen
 * @ClassName: LinkBottleneckServiceImpl
 * @package: com.pamirs.tro.web.api.service.impl
 * @Date: 2019/6/10下午9:43
 * @Description:
 */
@Service
public class LinkBottleneckServiceImpl implements LinkBottleneckService {
    private static Logger logger = LoggerFactory.getLogger(LinkBottleneckServiceImpl.class);
    /**
     * 链路瓶颈
     */
    @Autowired
    private TLinkBottleneckDao TLinkBottleneckDao;
    /**
     * 应用管理
     */
    @Autowired
    private TApplicationMntDao tApplicationMntDao;
    /**
     * 链路拓扑
     */
    @Autowired
    private TLinkTopologyInfoDao tLinkTopologyInfoDao;
    /**
     * 压测管理
     */
    @Autowired
    private TPressureTimeRecordDao tPressureTimeRecordDao;
    /**
     * redis
     */
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 白名单管理
     */
    @Autowired
    private TWListMntDao twListMntDao;

    /**
     * 链路URL
     */
    @Value("${aops.ip}")
    private String AOPS_IP;

    @Value("${aops.host}")
    private String BOTTLENECK_URL;

    @Value("${pradar.open.web.ip}")
    private String PRADAR_IP;

    @Value("${stp.web.ip}")
    private String STP_WEB_IP;

    @Value("${stp.web.job}")
    private String STP_WEB_JOB_URL;

    /**
     * RT_TPS_URL
     */
    @Value("${pradar.open.web.avgTpsRt}")
    private String RT_TPS_URL;
    /**
     * RT_URL
     */
    @Value("${pradar.open.web.rtCount}")
    private String RT_COUNT_URL;

    /**
     * 前几秒
     */
    private int preNSecond = 60;

    /**
     * 每次查询多少个应用
     */
    private int pageSize = 20;

    /**
     * mq堆积严重量
     */
    private int mqSerious = 1000000;

    /**
     * mq堆积普通量
     */
    private int mqGeneral = 500000;

    /**
     * 处理链路瓶颈
     */
    @Override
    public void handleLinkBottleneck() {
        try {
            //logger.error("链路瓶颈开始时间" + DateUtil.getYYYYMMDDHHMMSS(new Date()));
            // 查询所有应用
            List<Map<String, Object>> apps = Optional.ofNullable(tApplicationMntDao.queryApplicationdata()).orElse(
                Lists.newArrayList());
            if (CollectionUtils.isEmpty(apps)) {
                return;
            }
            Set<String> appNames = apps.stream().map(app -> ObjectUtils.toString(app.get("applicationName"))).collect(
                Collectors.toSet());
            /**
             * 分页获取应用信息
             */
            JSONObject dataObject = Optional.ofNullable(getLinkBottleneck(new ArrayList<>(appNames))).orElse(
                new JSONObject());

            /**
             * 循环处理
             */
            List<LinkBottleneck> insertList = Lists.newArrayList();
            List<String> noAppNames = Lists.newArrayList();

            String mqValue = ObjectUtils.toString(redisTemplate.opsForValue().get("calMq"));
            for (String appName : dataObject.keySet()) {
                JSONArray valueArray = dataObject.getJSONArray(appName);
                if (CollectionUtils.isEmpty(valueArray)) {
                    noAppNames.add(appName);
                    continue;
                }
                // 基础资源负载及异常,严重
                List<JSONObject> loadSeriousList = Lists.newArrayList();
                Map<String, StringBuffer> loadSeriousTextMap = Maps.newHashMap();
                loadSeriousTextMap.put("text", new StringBuffer());

                // 值
                for (int k = 0; k < valueArray.size(); k++) {
                    //
                    JSONObject valueObject = valueArray.getJSONObject(k);
                    /**
                     * 1、计算基础资源及其负载异常
                     */
                    int loadLevel = calLoadExeceptionLevel(valueObject, loadSeriousTextMap);
                    // 只存储严重的
                    if (loadLevel == LinkBottleneckLevelEnum.BOTTLENECK_LEVEL_SERIOUS.getCode()) {
                        loadSeriousList.add(valueObject);
                    }

                    /**
                     * 2、计算异步处理
                     */
                    /**
                     * 五分钟处理一次,判断当前时间是否超过上次处理时间,数据存储到redis
                     */
                    // 还不到五分钟,数据还未失效
                    if (StringUtils.isNotBlank(mqValue)) {
                        continue;
                    }
                    List<LinkBottleneck> links = Lists.newArrayList();
                    calAsynLevel(valueObject, links, appName);

                    if (CollectionUtils.isNotEmpty(links)) {
                        insertList.addAll(links);
                    }
                }
                // 封装插入数据，基础资源及异常
                if (CollectionUtils.isNotEmpty(loadSeriousList)) {
                    buildBottleneck(insertList, appName, loadSeriousList,
                        LinkBottleneckTypeEnum.BOTTLENECK_LOAD_EXECEPTION.getCode(),
                        LinkBottleneckLevelEnum.BOTTLENECK_LEVEL_SERIOUS.getCode(),
                        loadSeriousTextMap.getOrDefault("text",
                            new StringBuffer()).toString());
                }
            }

            if (StringUtils.isBlank(mqValue)) {
                redisTemplate.opsForValue().set("calMq", "calMq", 5, TimeUnit.MINUTES);
            }

            /**
             * TPS/RT稳定性 ，RT响应时间
             */
            calTpsRt(insertList);

            /**
             * 计算job堆积量
             */
            //calJob(insertList);
            /**
             * 批量插入
             */
            if (CollectionUtils.isNotEmpty(insertList)) {
                batchInsert(insertList);
            }

            if (CollectionUtils.isNotEmpty(noAppNames)) {
                logger.error("未采集到数据应用:" + JSONObject.toJSONString(noAppNames));
            }

            //            logger.error("链路瓶颈结束时间" + DateUtil.getYYYYMMDDHHMMSS(new Date()));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 查询job堆积量
     *
     * @param insertList
     */
    private void calJob(List<LinkBottleneck> insertList) {
        // 1、获取链路拓扑图里面的http接口类型
        TLinkTopologyInfo record = new TLinkTopologyInfo();
        List<String> entranceTypes = Lists.newArrayList();
        entranceTypes.add("JOB");
        record.setEntranceTypes(entranceTypes);
        List<TLinkTopologyInfo> list = tLinkTopologyInfoDao.list(record);
        // 去重Map
        Map<String, Object> duplicateMap = Maps.newHashMap();
        // 调用接口计算堆积量
        for (int i = 0; i < list.size(); i++) {
            TLinkTopologyInfo info = list.get(i);
            // 应用名称
            String appName = info.getApplicationName();
            // 链路url
            String linkEntrance = info.getLinkEntrance();
            String key = appName + linkEntrance;
            if (duplicateMap.containsKey(key)) {
                continue;
            }
            duplicateMap.put(key, key);
            Date endTime = new Date();

            Map<String, Object> queryMap = Maps.newHashMap();
            queryMap.put("appName", appName);
            queryMap.put("targetCode", linkEntrance);
            queryMap.put("startTime", DateUtil.getYYYYMMDDHHMMSS(endTime));
            queryMap.put("endTime", DateUtil.getYYYYMMDDHHMMSS(endTime));
            // 获取job的堆积量
            JSONObject presObject = getPostData(STP_WEB_IP + STP_WEB_JOB_URL, queryMap);
            Double jobSum = Double.parseDouble(ObjectUtils.toString(presObject.getOrDefault("sum", "0")));

            // 查询白名单,查询
            TApplicationMnt app = Optional.ofNullable(tApplicationMntDao.queryApplicationinfoByName(appName)).orElse(
                new TApplicationMnt());
            if (StringUtils.isBlank(app.getApplicationId() + "")) {
                continue;
            }
            Map<String, String> querytwMap = Maps.newHashMap();
            querytwMap.put("appId", "" + app.getApplicationId());
            querytwMap.put("url", linkEntrance);
            TWList twList = Optional.ofNullable(twListMntDao.getWListByParam(querytwMap)).orElse(new TWList());

            // job频率
            String interval = twList.getJobInterval();

            int times = getTimesByInterval(interval);
            if (times == 0) {
                continue;
            }
            // 获取最高的tps TODO 根据event 获取最高的tps
            Double topTps = getTopTps(queryMap);

            Double dataSum = topTps * times * 60;

            if (BigDecimal.valueOf(jobSum).compareTo(BigDecimal.valueOf(dataSum)) < 0) {
                continue;
            }
            String text = linkEntrance + " 积压数据量超过" + jobSum;
            // RTEntity
            LinkBottleneck jobEntity = new LinkBottleneck();
            jobEntity.setAppName(appName);
            jobEntity.setBottleneckType(LinkBottleneckTypeEnum.BOTTLENECK_ASYN.getCode());
            jobEntity.setBottleneckLevel(LinkBottleneckLevelEnum.BOTTLENECK_LEVEL_SERIOUS.getCode());
            jobEntity.setText(text);
            jobEntity.setKeyWords(linkEntrance);
            // 将查询参数放到Context
            jobEntity.setContent(JSONObject.toJSONString(queryMap));
            insertList.add(jobEntity);
        }
    }

    /**
     * 根据参数获取最高的tps
     *
     * @param queryMap
     * @return
     */
    private Double getTopTps(Map<String, Object> queryMap) {
        return Double.valueOf(0);
    }

    /**
     * 根据调度频率获取计算倍数
     * y)	(1)调度间隔≤1分钟，积压数据处理时间≤10分钟或积压数据量超过高峰tps*10*60
     * z)	(2)调度间隔≤5分钟，积压数据处理时间≤30分钟或积压数据量超过高峰tps*30*60
     * aa)	(3)调度间隔≤15分钟，积压数据处理时间≤60分钟或积压数据量超过高峰tps*60*60
     * bb)	(4)调度间隔≤60分钟，积压数据处理时间≤120分钟或积压数据量超过高峰tps*120*60
     *
     * @param interval
     * @return
     */
    private int getTimesByInterval(String interval) {
        int defaultTimes = 0;
        if (StringUtils.isBlank(interval)) {
            return defaultTimes;
        }
        int inter = Integer.parseInt(interval);
        if (inter > 60) {
            return defaultTimes;
        }

        if (inter <= 1) {
            return 10;
        }
        if (inter <= 5) {
            return 30;
        }
        if (inter <= 15) {
            return 60;
        }
        if (inter <= 60) {
            return 120;
        }
        return defaultTimes;
    }

    /**
     * 批量插入
     *
     * @param insertList
     */
    private void batchInsert(List<LinkBottleneck> insertList) {
        // 插入数据库
        insertList.stream().forEach(linkBottleneck -> {
            try {
                // 插入
                TLinkBottleneckDao.insertSelective(linkBottleneck);
            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        });
    }

    /**
     * 计算TPS/RT稳定性
     *
     * @param insertList
     */
    private void calTpsRt(List<LinkBottleneck> insertList) {
        // 查询压测开始时间
        TPressureTimeRecord timeRecord = Optional.ofNullable(tPressureTimeRecordDao.queryLatestPressureTime()).orElse(
            new TPressureTimeRecord());
        if (StringUtils.isNotBlank(timeRecord.getStartTime())) {
            /**
             * 五分钟处理一次,判断当前时间是否超过上次处理时间,数据存储到redis
             */
            String value = ObjectUtils.toString(redisTemplate.opsForValue().get("calTpsRt"));
            // 还不到五分钟
            if (StringUtils.isNotBlank(value)) {
                return;
            }

            Date pressureTime = DateUtil.getDate(timeRecord.getStartTime());
            // 是否需要查询，压测开始时间过30分钟以后开始查询,每五分钟查询一次
            Date currentTime = new Date();
            // 判断两个时间是否是否超过30分钟,五分钟取一次，35分钟时，计算无意义，下个1分钟计算 也就是36分钟以后
            Long second = DateUtil.getUntilSecond(pressureTime, currentTime);
            if (second > (30 + 5 + 1) * 60) {
                // 1、获取链路拓扑图里面的http接口类型
                TLinkTopologyInfo record = new TLinkTopologyInfo();
                List<String> entranceTypes = Lists.newArrayList();
                entranceTypes.add("http");
                entranceTypes.add("DUBBO");
                record.setEntranceTypes(entranceTypes);
                List<TLinkTopologyInfo> list = tLinkTopologyInfoDao.list(record);
                // 去重Map
                Map<String, Object> duplicateMap = Maps.newHashMap();
                // 调用接口返回RT和TPS
                for (int i = 0; i < list.size(); i++) {
                    TLinkTopologyInfo info = list.get(i);
                    // 应用名称
                    String appName = info.getApplicationName();
                    // 链路url
                    String url = info.getLinkEntrance();
                    String key = appName + url;
                    if (duplicateMap.containsKey(key)) {
                        continue;
                    }
                    duplicateMap.put(key, key);
                    // 查询压测平均RT和TPS, 开始时间为压测开始时间之后的半个小时，结束时间为当前时间
                    Date startPreTime = DateUtil.afterXMinuteDate(pressureTime, 30);
                    Date endTime = new Date();

                    Map<String, Object> queryMap = Maps.newHashMap();
                    queryMap.put("appName", appName);
                    queryMap.put("event", url);
                    queryMap.put("ptFlag", false);
                    queryMap.put("startTime", DateUtil.getYYYYMMDDHHMMSS(startPreTime));
                    queryMap.put("endTime", DateUtil.getYYYYMMDDHHMMSS(endTime));
                    // 获取链路TPS和RT
                    JSONObject presObject = getPostData(PRADAR_IP + RT_TPS_URL, queryMap);
                    Double presRT = Double.parseDouble(ObjectUtils.toString(presObject.getOrDefault("rt", "0")));
                    Double presTPS = Double.parseDouble(ObjectUtils.toString(presObject.getOrDefault("tps", "0")));

                    // 获取当前时间前5分钟到当前时间的TPS和RT
                    Date preXDate = DateUtil.preXMinuteDate(endTime, 5);
                    queryMap.put("startTime", DateUtil.getYYYYMMDDHHMMSS(preXDate));
                    JSONObject currentObj = getPostData(PRADAR_IP + RT_TPS_URL, queryMap);
                    Double currRT = Double.parseDouble(ObjectUtils.toString(currentObj.getOrDefault("rt", "0")));
                    Double currTPS = Double.parseDouble(ObjectUtils.toString(currentObj.getOrDefault("tps", "0")));

                    // RT/TPS 波动
                    BigDecimal diffRT;
                    if (presRT == 0) {
                        diffRT = BigDecimal.valueOf(currRT).multiply(BigDecimal.valueOf(100));
                    } else {
                        diffRT = BigDecimal.valueOf(currRT - presRT).divide(BigDecimal.valueOf(presRT), 2,
                            RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).abs();
                    }
                    BigDecimal diffTPS;
                    if (presTPS == 0) {
                        diffTPS = BigDecimal.valueOf(currTPS).multiply(BigDecimal.valueOf(100));
                    } else {
                        diffTPS = BigDecimal.valueOf(currTPS - presTPS).divide(BigDecimal.valueOf(presRT), 2,
                            RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).abs();
                    }

                    // 默认为正常
                    int rtLevel = LinkBottleneckLevelEnum.BOTTLENECK_LEVEL_NORMAL.getCode();
                    int tpsLevel = LinkBottleneckLevelEnum.BOTTLENECK_LEVEL_NORMAL.getCode();
                    String rtText = "";
                    String tpsText = "";
                    if (diffRT.compareTo(BigDecimal.valueOf(20)) > 0) {
                        if (diffRT.compareTo(BigDecimal.valueOf(50)) > 0) {
                            // 严重
                            rtLevel = LinkBottleneckLevelEnum.BOTTLENECK_LEVEL_SERIOUS.getCode();
                        } else {
                            // 普通
                            rtLevel = LinkBottleneckLevelEnum.BOTTLENECK_LEVEL_GENERAl.getCode();
                        }
                        rtText = "入口RT波动:" + diffRT + "%";
                    }

                    if (diffTPS.compareTo(BigDecimal.valueOf(20)) > 0) {
                        if (diffTPS.compareTo(BigDecimal.valueOf(50)) > 0) {
                            // 严重
                            tpsLevel = LinkBottleneckLevelEnum.BOTTLENECK_LEVEL_SERIOUS.getCode();
                        } else {
                            // 普通
                            tpsLevel = LinkBottleneckLevelEnum.BOTTLENECK_LEVEL_GENERAl.getCode();
                        }
                        tpsText = "入口TPS波动:" + diffTPS + "%";
                    }

                    // 都是正常的
                    if (!(rtLevel == tpsLevel && rtLevel == LinkBottleneckLevelEnum.BOTTLENECK_LEVEL_NORMAL
                        .getCode())) {
                        // 都是严重的或者都是普通的
                        if (rtLevel == tpsLevel &&
                            (rtLevel == LinkBottleneckLevelEnum.BOTTLENECK_LEVEL_SERIOUS.getCode()
                                || rtLevel == LinkBottleneckLevelEnum.BOTTLENECK_LEVEL_GENERAl.getCode())) {
                            LinkBottleneck rtTpsEntity = new LinkBottleneck();
                            rtTpsEntity.setAppName(appName);
                            rtTpsEntity.setKeyWords(url);
                            rtTpsEntity.setBottleneckType(LinkBottleneckTypeEnum.BOTTLENECK_TPS_RT.getCode());
                            String text = rtText + ";" + tpsText;
                            rtTpsEntity.setText(text);
                            rtTpsEntity.setBottleneckLevel(rtLevel);
                            rtTpsEntity.setContent(JSONObject.toJSONString(queryMap));

                            insertList.add(rtTpsEntity);
                        } else {
                            // 两者不同的
                            // 先存储RT
                            if (rtLevel != LinkBottleneckLevelEnum.BOTTLENECK_LEVEL_NORMAL.getCode()) {
                                LinkBottleneck rtTpsEntity = new LinkBottleneck();
                                rtTpsEntity.setAppName(appName);
                                rtTpsEntity.setKeyWords(url);
                                rtTpsEntity.setBottleneckType(LinkBottleneckTypeEnum.BOTTLENECK_TPS_RT.getCode());
                                rtTpsEntity.setBottleneckLevel(rtLevel);
                                rtTpsEntity.setContent(JSONObject.toJSONString(queryMap));
                                rtTpsEntity.setText(rtText);
                                insertList.add(rtTpsEntity);
                            }

                            // 存储TPS
                            if (tpsLevel != LinkBottleneckLevelEnum.BOTTLENECK_LEVEL_NORMAL.getCode()) {
                                LinkBottleneck rtTpsEntity2 = new LinkBottleneck();
                                rtTpsEntity2.setAppName(appName);
                                rtTpsEntity2.setKeyWords(url);
                                rtTpsEntity2.setBottleneckType(LinkBottleneckTypeEnum.BOTTLENECK_TPS_RT.getCode());
                                rtTpsEntity2.setText(tpsText);
                                rtTpsEntity2.setBottleneckLevel(tpsLevel);
                                rtTpsEntity2.setContent(JSONObject.toJSONString(queryMap));
                                insertList.add(rtTpsEntity2);
                            }
                        }
                    }

                    /**
                     * 计算RT响应时间
                     * 1、获取Appname
                     * 2、event 类型
                     * 3、压测开始时间超过35分钟以后，每五分钟查询一次,超过对应时间的次数
                     */
                    // 查询白名单,查询
                    TApplicationMnt app = Optional.ofNullable(tApplicationMntDao.queryApplicationinfoByName(appName))
                        .orElse(new TApplicationMnt());
                    if (StringUtils.isBlank(app.getApplicationId() + "")) {
                        continue;
                    }
                    Map<String, String> querytwMap = Maps.newHashMap();
                    querytwMap.put("appId", "" + app.getApplicationId());
                    querytwMap.put("url", url);
                    TWList twList = Optional.ofNullable(twListMntDao.getWListByParam(querytwMap)).orElse(new TWList());

                    // 页面还是接口 HTTP类型：1页面 2接口
                    String httpType = twList.getHttpType();
                    if (StringUtils.isBlank(httpType)) {
                        continue;
                    }
                    //  页面分类：1普通页面加载 2简单查询页面/复杂界面 3复杂查询页面
                    String pageLevel = twList.getPageLevel();
                    //  接口类型：1简单操作/查询 2一般操作/查询 3复杂操作 4涉及级联嵌套调用多服务操作 5调用外网操作
                    String interfaceLevel = twList.getInterfaceLevel();
                    Map<String, Object> queryRt = Maps.newHashMap();
                    queryRt.put("appName", appName);
                    queryRt.put("event", url);
                    queryRt.put("startTime", preXDate);
                    queryRt.put("endTime", endTime);
                    // 页面分类枚举
                    PageLevelEnum pageLevelEnum = PageLevelEnum.getPageLevelEnum(pageLevel);
                    if (pageLevelEnum == null) {
                        logger.error("pageLevel->" + pageLevel);
                        continue;
                    }
                    // 接口分类枚举
                    InterfaceLevelEnum interfaceLevelEnum = InterfaceLevelEnum.getInterfaceLevelEnum(interfaceLevel);
                    if (interfaceLevelEnum == null) {
                        logger.error("interfaceLevel->" + interfaceLevel);
                        continue;
                    }
                    // 响应时间，毫秒
                    int rtTime;
                    if ("1".equals(httpType)) {
                        rtTime = pageLevelEnum.getTime();
                    } else if ("2".equals(httpType)) {
                        rtTime = interfaceLevelEnum.getTime();
                    } else {
                        continue;
                    }
                    queryRt.put("rtTime", rtTime);
                    // 调用接口查询该时间段内，超过对应接口的次数
                    JSONObject rtObject = getPostData(RT_COUNT_URL, queryRt);
                    Double rtCount = Double.parseDouble(ObjectUtils.toString(rtObject.getOrDefault("rtCount", "0")));

                    if (rtCount == 0) {
                        continue;
                    }
                    StringBuffer text = new StringBuffer();
                    if ("1".equals(httpType)) {
                        text = text.append(pageLevelEnum.getName());
                    } else if ("2".equals(httpType)) {
                        text = text.append(interfaceLevelEnum.getName());
                    }
                    text = text.append("次数").append(rtCount);
                    // RTEntity
                    LinkBottleneck rtEntity = new LinkBottleneck();
                    rtEntity.setAppName(appName);
                    rtEntity.setBottleneckType(LinkBottleneckTypeEnum.BOTTLENECK_OTHER.getCode());
                    rtEntity.setText(text.toString());
                    rtEntity.setKeyWords(url);
                    // 将查询参数放到Context
                    rtEntity.setContent(JSONObject.toJSONString(queryRt));
                    insertList.add(rtEntity);
                }
                // 设置标记位，未超过5分钟，下次不处理
                redisTemplate.opsForValue().set("calTpsRt", "calTpsRt", 5, TimeUnit.MINUTES);
            }
        }

    }

    /**
     * 获取RT/TPS
     *
     * @param queryMap
     */
    private JSONObject getPostData(String url, Map<String, Object> queryMap) {
        JSONObject dataObject = new JSONObject();
        try {
            String pres = HttpUtils.post(url, JSONObject.toJSONString(queryMap));
            dataObject = JSONObject.parseObject(pres, JSONObject.class);
        } catch (Exception e) {
            //logger.error(ExceptionUtils.getStackTrace(e));
        }
        return dataObject;
    }

    /**
     * 计算异步处理等级
     *
     * @param valueObject
     * @param links       插入List
     * @param appName     应用名
     */
    private void calAsynLevel(JSONObject valueObject, List<LinkBottleneck> links, String appName) {
        JSONArray jsonArray = valueObject.getJSONArray("rktmqDepth");
        if (CollectionUtils.isEmpty(jsonArray)) {
            return;
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject dataObject = jsonArray.getJSONObject(i);
            String topic = ObjectUtils.toString(dataObject.getOrDefault("topic", ""));
            if (StringUtils.isBlank(topic)) {
                continue;
            }
            Double depth = Double.parseDouble(ObjectUtils.toString(dataObject.getOrDefault("depth", "0")));

            LinkBottleneck linkBottleneck = new LinkBottleneck();
            // 小于普通的，不处理
            if (depth <= mqGeneral) {
                continue;
            }

            if (depth > mqSerious) {
                linkBottleneck.setBottleneckLevel(LinkBottleneckLevelEnum.BOTTLENECK_LEVEL_SERIOUS.getCode());
            } else if (depth > mqGeneral) {
                linkBottleneck.setBottleneckLevel(LinkBottleneckLevelEnum.BOTTLENECK_LEVEL_GENERAl.getCode());
            }
            linkBottleneck.setAppName(appName);
            linkBottleneck.setKeyWords(topic);
            linkBottleneck.setContent(JSONObject.toJSONString(dataObject));
            String text = "topic:" + depth.intValue() / 10000 + "w";
            linkBottleneck.setText(text);

            links.add(linkBottleneck);
        }
    }

    /**
     * 计算基础资源及其负载异常等级
     *
     * @param valueObject
     */
    private int calLoadExeceptionLevel(JSONObject valueObject, Map<String, StringBuffer> textMap) {
        /**
         * cpu百分比 90%
         * 内存总使用率 90%
         * io使用率 90%
         * 磁盘分区使用率 100%
         *       fullgc 时间 大于5秒
         *       fullgc 次数 10次
         */
        StringBuffer text = textMap.getOrDefault("text", new StringBuffer());
        StringBuffer newText = new StringBuffer();
        // io
        double io = Double.parseDouble(ObjectUtils.toString(valueObject.getOrDefault("io", "0")));
        if (io > 90) {
            newText = newText.append("io使用率大于90%,");
        }
        // mem
        double mem = Double.parseDouble(ObjectUtils.toString(valueObject.getOrDefault("mem", "0")));
        if (mem > 90) {
            newText = newText.append("内存总使用率大于90%,");
        }
        // cpu
        double cpu = Double.parseDouble(ObjectUtils.toString(valueObject.getOrDefault("cpu", "0")));
        if (cpu > 90) {
            newText = newText.append("cpu百分比使用率90%,");
        }
        // disk
        double disk = Double.parseDouble(ObjectUtils.toString(valueObject.getOrDefault("disk", "0")));
        if (disk > 100) {
            newText = newText.append("磁盘分区使用率大于100%,");
        }
        // fullgcCount
        double fullgcCount = Double.parseDouble(ObjectUtils.toString(valueObject.getOrDefault("fullgcCount", "0")));
        // fullgcTime
        double fullgcTime = Double.parseDouble(ObjectUtils.toString(valueObject.getOrDefault("fullgcTime", "0")));
        if (fullgcTime > 5 && fullgcCount > 10) {
            newText = newText.append("fullgc次数10次,时间大于5秒,");
        }
        // 判断条件
        if (cpu > 90 || mem > 90 || io > 90 || disk > 100 || (fullgcCount > 10 && fullgcTime > 5)) {
            // 基础资源负载及其异常
            text = text.append("ip=" + valueObject.getOrDefault("ip", "") + ",");
            text = text.append(newText).append(";");
            textMap.put("text", text);
            return LinkBottleneckLevelEnum.BOTTLENECK_LEVEL_SERIOUS.getCode();
        }
        // 默认返回正常
        return LinkBottleneckLevelEnum.BOTTLENECK_LEVEL_NORMAL.getCode();
    }

    /**
     * 封装数据插入数据
     *
     * @param insertList
     * @param appName
     * @param contextList
     * @param type
     * @param level
     */
    private void buildBottleneck(List<LinkBottleneck> insertList, String appName,
        List<JSONObject> contextList,
        int type, int level, String text) {
        LinkBottleneck insertEntity = new LinkBottleneck();
        // 设置值
        insertEntity.setAppName(appName);

        // 资源负载及其异常
        insertEntity.setBottleneckType(type);
        insertEntity.setBottleneckLevel(level);
        insertEntity.setContent(JSONObject.toJSONString(contextList));
        insertEntity.setText(text);
        insertList.add(insertEntity);
    }

    private JSONObject getLinkBottleneck(List<String> appNames) {
        JSONObject dataObject = new JSONObject();
        int total = appNames.size();
        for (int i = 0; i < total; i += pageSize) {
            if (i + pageSize > total) {
                pageSize = total - i;
            }
            List<String> subList = appNames.subList(i, i + pageSize);
            JSONObject jsonObject = queryBottleneck(subList);

            if (jsonObject == null || jsonObject.size() == 0) {
                continue;
            }
            dataObject.putAll(jsonObject);
        }
        return dataObject;
    }

    /**
     * 查询应用信息
     *
     * @param appNames
     * @return
     */
    private JSONObject queryBottleneck(List<String> appNames) {
        JSONObject dataObject = new JSONObject();
        try {
            Map<String, Object> param = Maps.newHashMap();
            // 当前时间的前60秒
            param.put("time", DateUtils.dateToString(DateUtils.getPreviousNSecond(preNSecond), "yyyy-MM-dd HH:mm:ss"));
            param.put("applist", appNames);
            String dataStr = HttpUtils.post(AOPS_IP + BOTTLENECK_URL, JSONObject.toJSONString(param));
            if ("error".equals(dataStr)) {
                return null;
            }
            JSONObject rtObject = JSONObject.parseObject(dataStr, JSONObject.class);
            dataObject = rtObject.getJSONObject("data");
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return dataObject;
    }
}
