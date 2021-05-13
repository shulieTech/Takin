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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;

import com.google.common.base.Joiner;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.pamirs.tro.common.constant.LinkLevelEnum;
import com.pamirs.tro.common.constant.TROErrorEnum;
import com.pamirs.tro.common.constant.TimeUnits;
import com.pamirs.tro.common.exception.TROModuleException;
import com.pamirs.tro.common.util.DateUtils;
import com.pamirs.tro.common.util.HmacSha1Signature;
import com.pamirs.tro.common.util.NumberUtil;
import com.pamirs.tro.entity.domain.entity.TAlarm;
import com.pamirs.tro.entity.domain.entity.TApplicationIp;
import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import com.pamirs.tro.entity.domain.entity.TReport;
import com.pamirs.tro.entity.domain.query.Result;
import com.pamirs.tro.entity.domain.query.ResultList;
import com.pamirs.tro.entity.domain.query.TAlarmQuery;
import com.pamirs.tro.entity.domain.query.TReportQuery;
import com.pamirs.tro.entity.domain.vo.TLinkBasicVO;
import com.pamirs.tro.entity.domain.vo.TLinkServiceMntVo;
import com.pamirs.tro.entity.domain.vo.TReportAppIpDetail;
import com.pamirs.tro.entity.domain.vo.TReportDetail;
import com.pamirs.tro.entity.domain.vo.TReportResult;
import com.pamirs.tro.entity.domain.vo.TScenario;
import io.shulie.tro.web.app.common.CommonService;
import io.shulie.tro.web.app.service.TReportService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

/**
 * 说明: 压测报告相关服务实接口实现
 *
 * @author shulie
 * @version v1.0
 * @date 2018年5月17日
 */
@Service
public class TReportServiceImpl extends CommonService implements TReportService {

    /**
     * 添加压测报告
     *
     * @param tReport 压测报告
     * @return 添加结果
     */
    @Override
    public Result<Void> add(TReport tReport) {
        Result<Void> result = new Result<>();
        try {
            int count = tReportDao.insert(tReport);
            if (count != 1) {
                result.setSuccess(Boolean.FALSE);
                result.setErrorMessage(TROErrorEnum.MONITOR_DB_ADD_EXCEPTION.getErrorMessage());
            }
        } catch (Exception e) {
            LOGGER.error(MessageFormat.format("error:{0},model:{1}",
                TROErrorEnum.MONITOR_DB_ADD_EXCEPTION.getErrorMessage(),
                JSON.toJSONString(tReport)), e);
            result.setSuccess(Boolean.FALSE);
            result.setErrorMessage(TROErrorEnum.MONITOR_DB_ADD_EXCEPTION.getErrorMessage());
        }
        return result;
    }

    /**
     * 修改压测报告
     *
     * @param tReport 压测报告
     * @return 修改结果
     */
    @SuppressWarnings("unchecked")
    @Override
    public Result<Void> modify(TReport tReport) {
        Result<Void> result = new Result<>();
        if (null == tReport || null == tReport.getId()) {
            result.setSuccess(Boolean.FALSE);
            return result;
        }
        try {
            int count = tReportDao.update(tReport);

            //add cpu information

            TReport tReportfromDb = tReportDao.selectOneById(tReport.getId());
            Date startTime = tReportfromDb.getStartTime();
            Date endTime = tReportfromDb.getEndTime();
            String secondLinkId = tReportfromDb.getSecondLinkId();
            if (StringUtils.isNotEmpty(secondLinkId) && startTime != null && endTime != null) {
                //xuefu provider query war name from 2nd link
                Map<String, List<TApplicationMnt>> baseLinkIdToApplication = tSecondLinkMntService.
                    queryApplicationListByLinkInfo(secondLinkId, LinkLevelEnum.SECOND_LINK_LEVEL.getName());

                String linkBasic = tReportfromDb.getLinkBasic();
                List<TLinkBasicVO> tLinkBasicVOS = Collections.EMPTY_LIST;
                if (StringUtils.isNotEmpty(linkBasic)) {
                    tLinkBasicVOS = JSON.parseArray(linkBasic, TLinkBasicVO.class);
                }
                if (baseLinkIdToApplication != null && baseLinkIdToApplication.size() > 0) {
                    for (Map.Entry<String, List<TApplicationMnt>> baseLinkIdToApplicationEntry : baseLinkIdToApplication
                        .entrySet()) {
                        if (tLinkBasicVOS.stream().noneMatch(
                            tLinkBasicVO -> tLinkBasicVO.getLinkId().equals(baseLinkIdToApplicationEntry.getKey()))) {
                            continue;
                        }
                        List<TApplicationMnt> applicationList = baseLinkIdToApplicationEntry.getValue();
                        if (CollectionUtils.isEmpty(applicationList)) {
                            continue;
                        }

                        for (TApplicationMnt app : applicationList) {
                            List<TApplicationIp> appIpList = tApplicationIpDao.queryApplicationIpByNameList(
                                app.getApplicationName());
                            if (appIpList != null) {
                                for (TApplicationIp appIpObj : appIpList) {
                                    String type = appIpObj.getType();
                                    String ip = appIpObj.getIp();
                                    if (StringUtils.isEmpty(ip)) {
                                        continue;
                                    }
                                    String startMin = HmacSha1Signature.getDateMinuteModified(new Date(), startTime);
                                    String endMin = HmacSha1Signature.getDateMinuteModified(new Date(), endTime);

                                    Map<String, Object> map = aopsService.getAopsData(ip, startMin, endMin,
                                        StringUtils.substringBefore((System.currentTimeMillis() / 1000 + ""), "."));

                                    if (map != null && map.size() > 0) {
                                        String averageMemoryPercent = (String)map.get("averageMemoryPercent");
                                        String averageCpuPercent = (String)map.get("averageCpugpuPercent");
                                        String averagetotalIoWriterSpeed = (String)map.get("averagetotalIoWriterSpeed");
                                        String averagetotalIoReaderSpeed = (String)map.get("averagetotalIoReaderSpeed");
                                        String averagetotalIoSpeed = (String)map.get("averagetotalIoSpeed");

                                        List<TReportAppIpDetail> tReportAppIpDetails = tReportAppIpDetailDao
                                            .countMechine(String.valueOf(tReport.getId()), app.getApplicationName(),
                                                baseLinkIdToApplicationEntry.getKey(), ip);
                                        if (tReportAppIpDetails != null && tReportAppIpDetails.size() > 0) {
                                            //这里虽然用了for循环，但是理论上上面的4个条件可以唯一确认一条记录
                                            tReportAppIpDetails.forEach(tReportAppIpDetail -> {
                                                tReportAppIpDetail.setCpu(averageCpuPercent);
                                                tReportAppIpDetail.setIoall(averagetotalIoSpeed);
                                                tReportAppIpDetail.setIoread(averagetotalIoReaderSpeed);
                                                tReportAppIpDetail.setIowrite(averagetotalIoWriterSpeed);
                                                tReportAppIpDetail.setMemory(averageMemoryPercent);
                                                tReportAppIpDetail.setType(type);
                                                tReportAppIpDetail.setSystemName(appIpObj.getSystemName());
                                                tReportAppIpDetailDao.update(tReportAppIpDetail);
                                            });
                                        } else {
                                            TReportAppIpDetail appIpDetail = new TReportAppIpDetail();
                                            appIpDetail.setId(UUID.randomUUID().toString());
                                            appIpDetail.setLinkId(baseLinkIdToApplicationEntry.getKey());
                                            appIpDetail.setReportId(tReport.getId() + "");
                                            appIpDetail.setApplicationName(app.getApplicationName());
                                            appIpDetail.setCpu(averageCpuPercent);
                                            appIpDetail.setIoall(averagetotalIoSpeed);
                                            appIpDetail.setIoread(averagetotalIoReaderSpeed);
                                            appIpDetail.setIowrite(averagetotalIoWriterSpeed);
                                            appIpDetail.setMemory(averageMemoryPercent);
                                            appIpDetail.setIp(ip);
                                            appIpDetail.setType(type);
                                            appIpDetail.setSystemName(appIpObj.getSystemName());
                                            tReportAppIpDetailDao.insert(appIpDetail);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //
            if (count != 1) {
                result.setSuccess(Boolean.FALSE);
                result.setErrorMessage(TROErrorEnum.MONITOR_DB_UPDATE_EXCEPTION.getErrorMessage());
            }
        } catch (TROModuleException e) {
            LOGGER.error(MessageFormat.format("error:{0},model:{1}",
                TROErrorEnum.MONITOR_DB_UPDATE_EXCEPTION.getErrorMessage(),
                JSON.toJSONString(tReport)), e);
            result.setSuccess(Boolean.FALSE);
            result.setErrorMessage(TROErrorEnum.MONITOR_DB_UPDATE_EXCEPTION.getErrorMessage());
        }
        return result;
    }

    /**
     * 按id删除压测报告
     *
     * @param id 报告id
     * @return 删除结果
     */
    @Override
    public Result<Void> deleteById(Long id) {
        Result<Void> result = new Result<>();
        if (null == id) {
            result.setSuccess(Boolean.FALSE);
            return result;
        }
        try {
            int count = tReportDao.delete(id);
            if (count != 1) {
                result.setSuccess(Boolean.FALSE);
                result.setErrorMessage(TROErrorEnum.MONITOR_DB_DELETE_EXCEPTION.getErrorMessage());
            }
        } catch (Exception e) {
            LOGGER.error(MessageFormat.format("error:{0},id:{1}",
                TROErrorEnum.MONITOR_DB_DELETE_EXCEPTION.getErrorMessage(), id), e);
            result.setSuccess(Boolean.FALSE);
            result.setErrorMessage(TROErrorEnum.MONITOR_DB_DELETE_EXCEPTION.getErrorMessage());
        }
        return result;
    }

    /**
     * 查询压测报告列表
     *
     * @param query 报告查询实体
     * @return 查询的报告列表
     */
    @Override
    public ResultList<TReport> queryListByQuery(TReportQuery query) {
        ResultList<TReport> resultList = new ResultList<>();
        try {
            query.setOrderBy("start_time desc");
            List<TReport> tReportList = tReportDao.selectList(query);
            long count = tReportDao.selectListCount(query);
            resultList = new ResultList<>(query.getStart(), count, query.getPageSize(), tReportList);

            return resultList;
        } catch (Exception e) {
            LOGGER.error(MessageFormat.format("error:{0},query:{1}",
                TROErrorEnum.MONITOR_DB_QUERYLIST_EXCEPTION.getErrorMessage(), JSON.toJSONString(query)), e);
            resultList.setSuccess(Boolean.FALSE);
            resultList.setErrorMessage(TROErrorEnum.MONITOR_DB_QUERYLIST_EXCEPTION.getErrorMessage());

        }
        return resultList;
    }

    /**
     * 按id查询压测报告
     *
     * @param id 报告id
     * @return 压测报告
     */
    @Override
    public Result<TReport> queryOneById(Long id) {
        Result<TReport> result = new Result<>();
        if (null == id) {
            result.setSuccess(Boolean.FALSE);
            return result;
        }
        try {
            TReport tReport = tReportDao.selectOneById(id);
            result.setData(tReport);
        } catch (Exception e) {
            LOGGER.error(MessageFormat.format("error:{0},id:{1}",
                TROErrorEnum.MONITOR_DB_QUERY_EXCEPTION.getErrorMessage(), id), e);
            result.setSuccess(Boolean.FALSE);
            result.setErrorMessage(TROErrorEnum.MONITOR_DB_QUERY_EXCEPTION.getErrorMessage());
        }
        return result;
    }

    @Override
    public Result<TReport> queryOneByScenarioId(Long id, Integer status) {
        Result<TReport> result = new Result<>();
        if (null == id || null == status) {
            result.setSuccess(Boolean.FALSE);
            return result;
        }
        try {
            TReport tReport = tReportDao.selectScenarioId(id, status);
            result.setData(tReport);
        } catch (Exception e) {
            LOGGER.error(MessageFormat.format("error:{0},id:{1}",
                TROErrorEnum.MONITOR_DB_QUERY_EXCEPTION.getErrorMessage(), id), e);
            result.setSuccess(Boolean.FALSE);
            result.setErrorMessage(TROErrorEnum.MONITOR_DB_QUERY_EXCEPTION.getErrorMessage());
        }
        return result;
    }

    @Override
    public List<TReportAppIpDetail> queryMachineDetail(TReport tReport) {
        List<TReportAppIpDetail> list = tReportAppIpDetailDao.queryMachineDetail(tReport.getId() + "");
        return list;
    }

    @Override
    public List<TReport> queryBySecondLinkIdAndStatus(String secondLinkId, String status) {
        return tReportDao.queryBySecondLinkIdAndStatus(secondLinkId, status);
    }

    @Override
    public List<TReport> queryStatus(String status) {
        return tReportDao.queryByStatus(status);
    }

    @Override
    public TReportDetail queryReportDetail(Long reportId) {

        Result<TReport> reportResult = queryOneById(reportId);
        TReportDetail tReportDetail = new TReportDetail();

        if (reportResult.isSuccess() && null != reportResult.getData()) {
            TReport tReport = reportResult.getData();
            //将报告名称去重并逗号分隔,
            List<List<Map<String, Object>>> list = new Gson().fromJson(tReport.getLinkBasicName().trim(),
                new TypeToken<List<List<Map<String, Object>>>>() {
                }.getType());
            String basicLinkName = Joiner.on(",").join(list.stream().flatMap(mapList -> mapList.stream()).map(
                map -> MapUtils.getString(map, "label")).distinct().collect(Collectors.toList()));
            tReport.setLinkBasicName(basicLinkName);

            tReportDetail.setPass(true);
            tReportDetail.settReport(tReport);

            List<TReportResult> tReportResults = new ArrayList<>();
            String linkBasic = tReport.getLinkBasic();
            List<TLinkBasicVO> tLinkBasicVOS = JSON.parseArray(linkBasic, TLinkBasicVO.class);
            if (CollectionUtils.isEmpty(tLinkBasicVOS)) {
                tReportDetail.setPass(false);
            }

            for (TLinkBasicVO tLinkBasicVO : tLinkBasicVOS) {
                TScenario statistics = tLinkBasicVO.getStatistics();
                TReportResult tReportResult = new TReportResult();
                tReportResult.setPass(true);

                TLinkServiceMntVo tLinkServiceMntVo = new TLinkServiceMntVo();
                //复制部分属性值给前端使用, 目标值
                tLinkServiceMntVo.setLinkId(Long.valueOf(tLinkBasicVO.getLinkId()));
                tLinkServiceMntVo.setLinkName(tLinkBasicVO.getLinkName());
                tLinkServiceMntVo.setRtSa(tLinkBasicVO.getRtSa());
                tLinkServiceMntVo.setRt(tLinkBasicVO.getRt());
                tLinkServiceMntVo.setTps(tLinkBasicVO.getTps());
                tLinkServiceMntVo.setTargetSuccessRate(tLinkBasicVO.getTargetSuccessRate());
                tLinkServiceMntVo.setAswanId(tLinkBasicVO.getAswanId());
                tReportResult.settLinkServiceMntVo(tLinkServiceMntVo);
                //通过统计的压测信息和基础链路的目标值比对，如果小于目标就将测试报告状态设置为false
                if (statistics != null) {
                    tReportResult.settScenario(statistics);

                    Integer actualTps = Objects.isNull(statistics.getTps()) ? 0 : statistics.getTps();
                    Integer targetTps = NumberUtils.createInteger(
                        StringUtils.isEmpty(tLinkBasicVO.getTps()) ? "0" : tLinkBasicVO.getTps());

                    BigDecimal actualRt = Objects.isNull(statistics.getRt()) ? BigDecimal.ZERO : statistics.getRt();
                    BigDecimal targetRt = NumberUtils.createBigDecimal(
                        StringUtils.isEmpty(tLinkBasicVO.getRt()) ? "0" : tLinkBasicVO.getRt());

                    BigDecimal actualSuccessRate = Objects.isNull(statistics.getSuccessRate()) ? BigDecimal.ZERO
                        : statistics.getSuccessRate();
                    BigDecimal targetSuccessRate = NumberUtils.createBigDecimal(
                        StringUtils.isEmpty(tLinkBasicVO.getTargetSuccessRate()) ? "0"
                            : tLinkBasicVO.getTargetSuccessRate());

                    BigDecimal actualRtRate = Objects.isNull(statistics.getRtRate()) ? BigDecimal.ZERO
                        : statistics.getRtRate();
                    BigDecimal targetRtSa = NumberUtils.createBigDecimal(
                        StringUtils.isEmpty(tLinkBasicVO.getRtSa()) ? "0" : tLinkBasicVO.getRtSa());

                    boolean flag = (actualTps < targetTps ||
                        actualRt.compareTo(targetRt) > 0 ||
                        actualSuccessRate.compareTo(targetSuccessRate) < 0) ||
                        actualRtRate.compareTo(targetRtSa) < 0;
                    if (flag) {
                        tReportDetail.setPass(false);
                        tReportResult.setPass(false);
                    }
                } else {
                    tReportDetail.setPass(false);
                    tReportResult.setPass(false);
                }

                tReportResult.setDuration(
                    DateUtils.gapTime(tLinkBasicVO.getStartTime(), tLinkBasicVO.getEndTime(), TimeUnits.MINUTES));

                //查询应用ip(服务器)资源详情
                List<TReportAppIpDetail> tReportAppIpDetails = queryApplicationIpByIpList(String.valueOf(reportId),
                    String.valueOf(tLinkBasicVO.getLinkId()));
                tReportResult.settReportAppIpDetails(tReportAppIpDetails);

                //对服务器信息进行分租，应用服务器为web，数据库服务器为db，其他为中间件服务器
                Map<String, List<TReportAppIpDetail>> serverGroup = new HashMap<>(10);
                for (TReportAppIpDetail tReportAppIpDetail : tReportAppIpDetails) {
                    String type = tReportAppIpDetail.getType();
                    if ("web".equals(type)) {
                        List<TReportAppIpDetail> webServerArray = serverGroup.get("web");
                        if (CollectionUtils.isEmpty(webServerArray)) {
                            webServerArray = new ArrayList<>();
                        }
                        webServerArray.add(tReportAppIpDetail);
                        serverGroup.put("web", webServerArray);
                    } else if ("db".equals(type)) {
                        List<TReportAppIpDetail> dbServerArray = serverGroup.get("db");
                        if (CollectionUtils.isEmpty(dbServerArray)) {
                            dbServerArray = new ArrayList<>();
                        }
                        dbServerArray.add(tReportAppIpDetail);
                        serverGroup.put("db", dbServerArray);
                    } else {
                        List<TReportAppIpDetail> middlewareServerArray = serverGroup.get("other");
                        if (CollectionUtils.isEmpty(middlewareServerArray)) {
                            middlewareServerArray = new ArrayList<>();
                        }
                        middlewareServerArray.add(tReportAppIpDetail);
                        serverGroup.put("other", middlewareServerArray);
                    }
                }

                if (serverGroup.size() > 0) {
                    List<TReportAppIpDetail> webServer = serverGroup.get("web");
                    Map<String, Float> webServerInfo = new HashMap<>();
                    webServerInfo.put("maxCpuUsageRate", webServer == null ? 0f : webServer.stream()
                        .map(tReportAppIpDetail -> NumberUtil.getFloat(tReportAppIpDetail.getCpu()))
                        .max(Comparator.comparing(u -> u)).orElse(0f));
                    webServerInfo.put("maxMemoryUsageRate", webServer == null ? 0f : webServer.stream()
                        .map(tReportAppIpDetail -> NumberUtil.getFloat(tReportAppIpDetail.getMemory()))
                        .max(Comparator.comparing(u -> u)).orElse(0F));
                    tReportResult.setWebServer(webServerInfo);

                    List<TReportAppIpDetail> dbServerInfo = serverGroup.get("db");
                    Map<String, Float> dbServer = new HashMap<>();
                    dbServer.put("maxCpuUsageRate", dbServerInfo == null ? 0f : dbServerInfo.stream()
                        .map(tReportAppIpDetail -> NumberUtil.getFloat(tReportAppIpDetail.getCpu()))
                        .max(Comparator.comparing(u -> u)).orElse(0f));
                    dbServer.put("maxMemoryUsageRate", dbServerInfo == null ? 0f : dbServerInfo.stream()
                        .map(tReportAppIpDetail -> NumberUtil.getFloat(tReportAppIpDetail.getMemory()))
                        .max(Comparator.comparing(u -> u)).orElse(0F));
                    tReportResult.setDbServer(dbServer);

                    List<TReportAppIpDetail> otherServerInfo = serverGroup.get("other");

                    Map<String, Float> otherServer = new HashMap<>();
                    otherServer.put("maxCpuUsageRate", dbServerInfo == null ? 0f : otherServerInfo.stream()
                        .map(tReportAppIpDetail -> NumberUtil.getFloat(tReportAppIpDetail.getCpu()))
                        .max(Comparator.comparing(u -> u)).orElse(0f));
                    otherServer.put("maxMemoryUsageRate", dbServerInfo == null ? 0f : otherServerInfo.stream()
                        .map(tReportAppIpDetail -> NumberUtil.getFloat(tReportAppIpDetail.getMemory()))
                        .max(Comparator.comparing(u -> u)).orElse(0F));
                    tReportResult.setMiddlewareServer(otherServer);
                }
                tReportResult.settScenarioList(tLinkBasicVO.getStatisticsPerMinute());
                tReportResults.add(tReportResult);
            }

            tReportDetail.settReportResults(tReportResults);

            //查询报告中压测的基础链路相关资源的告警
            List<TReportAppIpDetail> reportAppIpDetailList = queryMachineDetail(tReport);

            TAlarmQuery query = new TAlarmQuery();
            TAlarm tAlarm = new TAlarm();
            query.setQuery(tAlarm);
            query.setBeginAlarmDate(DateUtils.dateToString(tReport.getStartTime(), DateUtils.FORMATE_YMDHMS));
            query.setEndAlarmDate(DateUtils.dateToString(tReport.getEndTime(), DateUtils.FORMATE_YMDHMS));
            query.setWarNames(
                reportAppIpDetailList.stream().map(reportAppIpDetail -> reportAppIpDetail.getApplicationName())
                    .collect(Collectors.toList()));
            //            ResultList<TAlarm> alarmResultList = tAlarmService.queryListByQuery(query);
            //            tReportDetail.settAlarms((List<TAlarm>) alarmResultList.getDatalist());
            //            if (CollectionUtils.isNotEmpty(tReportDetail.gettAlarms())) {
            //                tReportDetail.setPass(false);
            //            }
            tReportDetail.setPass(true);

        }
        return tReportDetail;
    }

    @Override
    public List<TReportAppIpDetail> queryApplicationIpByIpList(String reportId, String linkId) {
        return tReportAppIpDetailDao.queryReportAppIpListByReportIdAndLinkId(reportId, linkId);
    }
}
