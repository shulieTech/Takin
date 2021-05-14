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

package io.shulie.tro.web.app.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.Lists;
import com.pamirs.tro.common.constant.Constants;
import com.pamirs.tro.common.constant.TROErrorEnum;
import com.pamirs.tro.common.exception.TROModuleException;
import com.pamirs.tro.common.util.DateUtils;
import com.pamirs.tro.entity.domain.entity.LinkBottleneck;
import com.pamirs.tro.entity.domain.entity.TLinkTopologyInfo;
import com.pamirs.tro.entity.domain.entity.TWList;
import com.pamirs.tro.entity.domain.vo.TLinkTopologyInfoVo;
import com.pamirs.tro.entity.domain.vo.TLinkTopologyShowVo;
import com.pamirs.tro.entity.domain.vo.TopologyLink;
import com.pamirs.tro.entity.domain.vo.TopologyNode;
import com.pamirs.tro.entity.domain.vo.bottleneck.AsyncVo;
import com.pamirs.tro.entity.domain.vo.bottleneck.BottleCountVo;
import com.pamirs.tro.entity.domain.vo.bottleneck.BottleLevelCountVo;
import com.pamirs.tro.entity.domain.vo.bottleneck.BottleNeckDetailVo;
import com.pamirs.tro.entity.domain.vo.bottleneck.BottleNeckVo;
import com.pamirs.tro.entity.domain.vo.bottleneck.RtVo;
import com.pamirs.tro.entity.domain.vo.bottleneck.StabilityVo;
import io.shulie.tro.web.app.common.CommonService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 链路拓扑图 接口
 *
 * @author 298403
 */
@Service
public class LinkTopologyInfoService extends CommonService {

    private static final Logger logger = LoggerFactory.getLogger(LinkTopologyInfoService.class);

    /**
     * 节点类型有HTTP、MQ、JOB、DUBBO这4种情况
     * 瓶颈类别：1.基础资源及异常   2.异步处理  3.TPS/RT稳定性  4.RT响应时间
     * HTTP，需要关注 1，3，4
     * MQ、JOB，需要关注 1，2，3
     * DUBBO，需要关注 1，3
     */
    private static final String[] BOTTLE_TYPE_HTTP = {"1", "3", "4"};
    private static final String[] BOTTLE_TYPE_MQ = {"1", "2", "3"};
    private static final String[] BOTTLE_TYPE_JOB = {"1", "2", "3"};
    private static final String[] BOTTLE_TYPE_DEFAULT = {"1", "3"};

    /**
     * 前一分钟
     */
    private static final int PRE_FIVE_MINUTE = -5;
    private static final int PRE_TWENTY_FOUR_HOUR = -24;

    public static void checkFile(MultipartFile file) throws TROModuleException {
        //判断文件是否存在
        if (null == file) {
            logger.warn("文件不存在！");
            throw new TROModuleException(TROErrorEnum.TRANSPARENTFLOW_LINKTOPOLOGY_EXCEL_IS_EMTPT_EXCEPTION);
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if (!fileName.endsWith("xls") && !fileName.endsWith("xlsx")) {
            logger.error(fileName + "不是excel文件");
            throw new TROModuleException(TROErrorEnum.TRANSPARENTFLOW_LINKTOPOLOGY_EXCEL_SUFFIX_ERROR_EXCEPTION);
        }
    }

    public static Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileName.endsWith("xls")) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith("xlsx")) {
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return workbook;
    }

    /**
     * 导入excel 直接解析 插入
     *
     * @param excel
     * @throws TROModuleException
     */
    public void importExcelData(MultipartFile excel) throws TROModuleException {
        checkFile(excel);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(excel);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<TLinkTopologyInfo> topologyInfoList = new ArrayList<>();
        if (workbook != null) {
            convertToTopology(workbook, topologyInfoList);
        }
        if (CollectionUtils.isNotEmpty(topologyInfoList)) {
            tLinkTopologyInfoDao.deleteAllData();
            tLinkTopologyInfoDao.insertList(topologyInfoList);
        }
    }

    private void convertToTopology(Workbook workbook, List<TLinkTopologyInfo> topologyInfoList) {
        //获得当前sheet工作表
        Sheet sheet = workbook.getSheetAt(0);
        //获得当前sheet的开始行
        int firstRowNum = sheet.getFirstRowNum();
        //获得当前sheet的结束行
        int lastRowNum = sheet.getLastRowNum();
        //循环除了第一行的所有行 第一行为表头
        for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
            TLinkTopologyInfo topologyInfo = new TLinkTopologyInfo();
            topologyInfo.setTltiId(snowflake.next());
            //获得当前行
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                continue;
            }
            //获得当前行的开始列
            int firstCellNum = row.getFirstCellNum();
            //获得当前行的列数
            int lastCellNum = row.getPhysicalNumberOfCells();
            //循环当前行
            for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                Cell cell = row.getCell(cellNum);
                cell.setCellType(CellType.STRING);
                switch (cellNum) {
                    case 0:
                        //第一列 链路id
                        if (StringUtils.isNotEmpty(cell.getStringCellValue())) {
                            topologyInfo.setLinkId(Long.parseLong(cell.getStringCellValue()));
                        }
                        ;
                        break;
                    case 1:
                        topologyInfo.setLinkName(cell.getStringCellValue());
                        break;
                    case 2:
                        topologyInfo.setEntranceType(cell.getStringCellValue());
                        break;
                    case 3:
                        topologyInfo.setLinkEntrance(cell.getStringCellValue());
                        break;
                    case 4:
                        topologyInfo.setNameServer(cell.getStringCellValue());
                        break;
                    case 5:
                        topologyInfo.setLinkType(cell.getStringCellValue());
                        break;
                    case 6:
                        topologyInfo.setLinkGroup(cell.getStringCellValue());
                        break;
                    case 7:
                        topologyInfo.setFromLinkIds(cell.getStringCellValue());
                        break;
                    case 8:
                        topologyInfo.setToLinkIds(cell.getStringCellValue());
                        break;
                    case 9:
                        topologyInfo.setShowFromLinkId(cell.getStringCellValue());
                        break;
                    case 10:
                        topologyInfo.setShowToLinkId(cell.getStringCellValue());
                        break;
                    case 11:
                        topologyInfo.setSecondLinkId(cell.getStringCellValue());
                        break;
                    case 12:
                        topologyInfo.setApplicationName(cell.getStringCellValue());
                        break;
                    case 13:
                        topologyInfo.setVolumeCalcStatus(cell.getStringCellValue());
                        break;
                    default:
                        break;
                }
            }
            topologyInfoList.add(topologyInfo);
        }
    }

    /**
     * 更具链路 分组 查出该链路拓扑图
     *
     * @param linkGroup
     * @param secondLinkId
     * @return
     * @throws TROModuleException
     */
    public TLinkTopologyShowVo queryLinkTopologyByLinkGroup(String linkGroup, String secondLinkId)
        throws TROModuleException {
        if (StringUtils.isEmpty(linkGroup)) {
            throw new TROModuleException(TROErrorEnum.TRANSPARENTFLOW_LINKTOPOLOGY_LINK_GROUP_EMPTY_EXCEPTION);
        }
        TLinkTopologyShowVo vo = new TLinkTopologyShowVo();
        List<TLinkTopologyInfoVo> topologyInfoList = tLinkTopologyInfoDao.queryLinkTopologyByLinkGroup(linkGroup,
            secondLinkId);
        if (CollectionUtils.isEmpty(topologyInfoList)) {
            return vo;
        }
        Set<TopologyLink> linkList = new HashSet<>();
        List<TopologyNode> nodeList = new ArrayList<>();
        //循环 进行赋值
        for (TLinkTopologyInfoVo info : topologyInfoList) {
            TopologyNode node = new TopologyNode();
            BeanUtils.copyProperties(info, node);
            String linkId = String.valueOf(info.getLinkId());
            node.setLinkId(linkId);

            String bottleLevel = getBottleLevel(info.getEntranceType(), info.getLinkEntrance(),
                info.getApplicationName(), queryBottleNeckPreOneMinute());
            node.setBottleLevel(bottleLevel);
            nodeList.add(node);
            if (StringUtils.isNotEmpty(info.getFromLinkIds())) {
                String[] fromLinkId = info.getFromLinkIds().split(",");
                for (String fromLink : fromLinkId) {
                    TopologyLink link = new TopologyLink();
                    link.setFrom(fromLink);
                    link.setTo(linkId);
                    linkList.add(link);
                }
            }
            if (StringUtils.isNotEmpty(info.getToLinkIds())) {
                String[] toinkId = info.getToLinkIds().split(",");
                for (String toLink : toinkId) {
                    TopologyLink link = new TopologyLink();
                    link.setFrom(linkId);
                    link.setTo(toLink);
                    linkList.add(link);
                }
            }
        }
        vo.setLinkList(linkList);
        vo.setNodeList(nodeList);
        return vo;
    }

    /**
     * 查询前一分钟所有瓶颈列表
     *
     * @return
     */
    private List<LinkBottleneck> queryBottleNeckPreOneMinute() {
        //1,查询前一分钟时间所有瓶颈
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, PRE_FIVE_MINUTE);
        return queryBottleNeckPreTime(DateUtils.dateToString(calendar.getTime(), "yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 查询之前时间的瓶颈列表
     *
     * @param startTime 查询开始时间
     * @return
     */
    private List<LinkBottleneck> queryBottleNeckPreTime(String startTime) {
        return tLinkTopologyInfoDao.queryBottleNeckPreTime(startTime);
    }

    /**
     * 获取节点瓶颈级别
     *
     * @param type            节点入口类型
     * @param linkEntrance    节点入口地址
     * @param applicationName 应用名称
     * @return
     */
    private String getBottleLevel(String type, String linkEntrance, String applicationName,
        List<LinkBottleneck> preMinuteBottleNeckList) {

        List<LinkBottleneck> bottleNeckList = preMinuteBottleNeckList.stream().filter(
            linkBottleneck -> applicationName.equals(linkBottleneck.getAppName()) && getBottleTypeList(type)
                .contains(String.valueOf(linkBottleneck.getBottleneckType()))).collect(Collectors.toList());

        if (bottleNeckList.isEmpty()) {
            //如果查询不到瓶颈,则该节点没有瓶颈
            return Constants.NORMAL;
        } else {
            //查到了，需要确认是严重还是普通，
            //当前节点瓶颈级别
            Integer minNodeBottleNeckLevel = bottleNeckList.stream().filter(
                linkBottleneck -> linkEntrance.equals(linkBottleneck.getKeyWords())).map(
                linkBottleneck -> linkBottleneck.getBottleneckLevel()).distinct().min(Comparator.comparingInt(o -> o))
                .orElse(3);
            //公用瓶颈类型中是否产生告警
            //TPS/RT稳定性是否瓶颈
            Integer tpsRtBottleNeckLevel = bottleNeckList.stream().filter(
                linkBottleneck -> linkBottleneck.getBottleneckType() == 3).map(
                linkBottleneck -> linkBottleneck.getBottleneckLevel()).distinct().min(Comparator.comparingInt(o -> o))
                .orElse(3);

            //基础资源是否瓶颈,
            Integer basicBottleNeckLevel = bottleNeckList.stream().filter(
                linkBottleneck -> linkBottleneck.getBottleneckType() == 1
                    && linkBottleneck.getCreateTime().getTime() > System.currentTimeMillis() - 60000).map(
                linkBottleneck -> linkBottleneck.getBottleneckLevel()).distinct().min(Comparator.comparingInt(o -> o))
                .orElse(3);
            //            Integer minValue = minNodeBottleNeckLevel > tpsRtBottleNeckLevel ? minBottleNeckLevel :
            //            minNodeBottleNeckLevel;
            List<Integer> list = Lists.newArrayList(minNodeBottleNeckLevel, tpsRtBottleNeckLevel, basicBottleNeckLevel);
            int minValue = IntStream.of(minNodeBottleNeckLevel, tpsRtBottleNeckLevel, basicBottleNeckLevel).min()
                .getAsInt();
            switch (minValue) {
                case 1:
                    return Constants.SERIOUS;
                case 2:
                    //普通, 还需要和公用瓶颈比较取最小值
                    return Constants.ERROR;
                default:
                    return Constants.NORMAL;
            }
        }
    }

    /**
     * 根据节点入口类型获取瓶颈类型列表
     *
     * @param type 接口类型
     * @return
     */
    private List<String> getBottleTypeList(String type) {
        List<String> bottleTypeList = Arrays.stream(BOTTLE_TYPE_DEFAULT).collect(Collectors.toList());
        String entranceType = StringUtils.upperCase(type);
        if (entranceType.contains(Constants.MQ)) {
            entranceType = Constants.MQ;
        }
        switch (entranceType) {
            case Constants.HTTP:
                bottleTypeList = Arrays.stream(BOTTLE_TYPE_HTTP).collect(Collectors.toList());
                break;
            case Constants.MQ:
                bottleTypeList = Arrays.stream(BOTTLE_TYPE_MQ).collect(Collectors.toList());
                break;
            case Constants.JOB:
                bottleTypeList = Arrays.stream(BOTTLE_TYPE_JOB).collect(Collectors.toList());
                break;
            case Constants.DUBBO:
                break;
            default:
                break;
        }
        return bottleTypeList;
    }

    /**
     * 查询链路瓶颈应用数量
     *
     * @return
     */
    public BottleCountVo queryLinkBottleSummary() {
        List<LinkBottleneck> preMinuteBottleNeckList = queryBottleNeckPreOneMinute();

        BottleCountVo bottleCountVo = new BottleCountVo();
        BottleLevelCountVo summary = new BottleLevelCountVo();
        //TODO 填充总的应用瓶颈数量
        List<String> totalSeriousList = Lists.newArrayList();
        List<String> totalErrorList = Lists.newArrayList();
        List<String> totalNormalList = Lists.newArrayList();

        List<BottleLevelCountVo> linkRelatedList = Lists.newArrayList();
        //TODO 查询链路组，遍历
        List<Map<String, String>> linkGroupAppList = tLinkTopologyInfoDao.queryLinkGroupInfo();
        Map<String, List<Map<String, String>>> linkGroupMaps = linkGroupAppList.stream().collect(
            Collectors.groupingByConcurrent(map -> MapUtils.getString(map, "linkGroup")));
        linkGroupMaps.keySet().stream().sorted(Comparator.comparingInt(Integer::parseInt)).forEach(linkGroup -> {
            BottleLevelCountVo linkRelated = new BottleLevelCountVo();
            //TODO 填充每个链路模块的应用瓶颈数量
            List<Map<String, String>> linkGroupMapList = linkGroupMaps.get(linkGroup);

            List<String> seriousList = Lists.newArrayList();
            List<String> errorList = Lists.newArrayList();
            List<String> normalList = Lists.newArrayList();

            linkGroupMapList.stream().forEach(linkGroupMap -> {
                String appName = MapUtils.getString(linkGroupMap, "APPLICATION_NAME");
                String entranceType = MapUtils.getString(linkGroupMap, "ENTRANCE_TYPE");
                String linkEntrance = MapUtils.getString(linkGroupMap, "LINK_ENTRANCE");
                String linkId = MapUtils.getString(linkGroupMap, "LINK_ID");
                //如果linkId为空，没法查询瓶颈，说明该链路组下没有链路
                if (StringUtils.isNotEmpty(linkId)) {
                    if (StringUtils.isEmpty(appName) && StringUtils.isEmpty(linkEntrance)) {
                        normalList.add(linkId);
                    } else {
                        String bottleLevel = getBottleLevel(entranceType, linkEntrance, appName,
                            preMinuteBottleNeckList);
                        switch (bottleLevel) {
                            case Constants.SERIOUS:
                                seriousList.add(linkId);
                                break;
                            case Constants.ERROR:
                                errorList.add(linkId);
                                break;
                            case Constants.NORMAL:
                                normalList.add(linkId);
                                break;
                            default:
                                break;
                        }
                    }

                }
            });
            linkRelated.setSeriousBottle(seriousList.size());
            linkRelated.setBottle(errorList.size());
            linkRelated.setNormal(normalList.size());
            linkRelatedList.add(linkRelated);
            //计算总数
            totalSeriousList.addAll(seriousList);
            totalErrorList.addAll(errorList);
            totalNormalList.addAll(normalList);

        });
        summary.setSeriousBottle(totalSeriousList.size());
        summary.setBottle(totalErrorList.size());
        summary.setNormal(totalNormalList.size());

        bottleCountVo.setSummary(summary);
        bottleCountVo.setLinkRelated(linkRelatedList);
        return bottleCountVo;
    }

    /**
     * 查询瓶颈链路详情
     *
     * @param paramMap 应用名，节点入口，节点类型
     * @return
     */
    public BottleNeckDetailVo queryLinkBottleDetail(Map<String, Object> paramMap) {
        String applicationName = MapUtils.getString(paramMap, "applicationName", "");
        String linkEntrance = MapUtils.getString(paramMap, "linkEntrance", "");
        String entranceType = MapUtils.getString(paramMap, "entranceType", "");

        List<LinkBottleneck> bottleNeckPre24Hour = queryBottleNeckPre24Hour();
        // 基础资源异常，严重top10
        List<BottleNeckVo> basic = bottleNeckPre24Hour.stream()
            .filter(linkBottleneck -> applicationName.equals(linkBottleneck.getAppName())
                && linkBottleneck.getBottleneckType() == 1 && linkBottleneck.getBottleneckLevel() == 1)
            .sorted((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime())).limit(10)
            .map(linkBottleNeck -> {
                BottleNeckVo bottleNeckVo = new BottleNeckVo();
                bottleNeckVo.setBottleNeckContent(linkBottleNeck.getText());
                bottleNeckVo.setCreateTime(
                    DateUtils.dateToString(linkBottleNeck.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                return bottleNeckVo;
            }).collect(Collectors.toList());

        //异步处理 严重top5 普通top5
        List<BottleNeckVo> seriousAsync = bottleNeckPre24Hour.stream()
            .filter(linkBottleneck -> applicationName.equals(linkBottleneck.getAppName()) && linkEntrance
                .equals(linkBottleneck.getKeyWords()) && linkBottleneck.getBottleneckType() == 2
                && linkBottleneck.getBottleneckLevel() == 1)
            .sorted((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime())).limit(5)
            .map(linkBottleNeck -> {
                BottleNeckVo bottleNeckVo = new BottleNeckVo();
                bottleNeckVo.setBottleNeckContent(linkBottleNeck.getText());
                bottleNeckVo.setCreateTime(
                    DateUtils.dateToString(linkBottleNeck.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                return bottleNeckVo;
            }).collect(Collectors.toList());
        List<BottleNeckVo> errorAsync = bottleNeckPre24Hour.stream()
            .filter(linkBottleneck -> applicationName.equals(linkBottleneck.getAppName()) && linkEntrance
                .equals(linkBottleneck.getKeyWords()) && linkBottleneck.getBottleneckType() == 2
                && linkBottleneck.getBottleneckLevel() == 2)
            .sorted((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime())).limit(5)
            .map(linkBottleNeck -> {
                BottleNeckVo bottleNeckVo = new BottleNeckVo();
                bottleNeckVo.setBottleNeckContent(linkBottleNeck.getText());
                bottleNeckVo.setCreateTime(
                    DateUtils.dateToString(linkBottleNeck.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                return bottleNeckVo;
            }).collect(Collectors.toList());
        List<AsyncVo> async = Lists.newArrayList();
        async.add(new AsyncVo(Constants.SERIOUS, seriousAsync));
        async.add(new AsyncVo(Constants.ERROR, errorAsync));

        //稳定性 严重top5 普通top5
        List<BottleNeckVo> seriousStability = bottleNeckPre24Hour.stream()
            .filter(linkBottleneck -> applicationName.equals(linkBottleneck.getAppName())
                && linkBottleneck.getBottleneckType() == 3 && linkBottleneck.getBottleneckLevel() == 1)
            .sorted((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime())).limit(5)
            .map(linkBottleNeck -> {
                BottleNeckVo bottleNeckVo = new BottleNeckVo();
                bottleNeckVo.setBottleNeckContent(linkBottleNeck.getText());
                bottleNeckVo.setCreateTime(
                    DateUtils.dateToString(linkBottleNeck.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                return bottleNeckVo;
            }).collect(Collectors.toList());
        List<BottleNeckVo> errorStability = bottleNeckPre24Hour.stream()
            .filter(linkBottleneck -> applicationName.equals(linkBottleneck.getAppName())
                && linkBottleneck.getBottleneckType() == 3 && linkBottleneck.getBottleneckLevel() == 2)
            .sorted((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime())).limit(5)
            .map(linkBottleNeck -> {
                BottleNeckVo bottleNeckVo = new BottleNeckVo();
                bottleNeckVo.setBottleNeckContent(linkBottleNeck.getText());
                bottleNeckVo.setCreateTime(
                    DateUtils.dateToString(linkBottleNeck.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                return bottleNeckVo;
            }).collect(Collectors.toList());
        List<StabilityVo> stability = Lists.newArrayList();
        stability.add(new StabilityVo(Constants.SERIOUS, seriousStability));
        stability.add(new StabilityVo(Constants.ERROR, errorStability));

        //RT响应时间
        List<BottleNeckVo> seriousRt = bottleNeckPre24Hour.stream()
            .filter(linkBottleneck -> applicationName.equals(linkBottleneck.getAppName()) && linkEntrance
                .equals(linkBottleneck.getKeyWords()) && linkBottleneck.getBottleneckType() == 4
                && linkBottleneck.getBottleneckLevel() == 1)
            .sorted((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime())).limit(5)
            .map(linkBottleNeck -> {
                BottleNeckVo bottleNeckVo = new BottleNeckVo();
                bottleNeckVo.setBottleNeckContent(linkBottleNeck.getText());
                bottleNeckVo.setCreateTime(
                    DateUtils.dateToString(linkBottleNeck.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                return bottleNeckVo;
            }).collect(Collectors.toList());
        List<RtVo> rt = Lists.newArrayList();
        //根据接口类型和名称查询白名单
        String type = "";
        if (entranceType.equalsIgnoreCase(Constants.HTTP)) {
            TWList wList = tWListMntDao.getWListByParam(new HashMap<String, String>(10) {{
                put("url", linkEntrance);
                put("type", "1");
            }});
            type = wList == null ? "" : wList.getHttpType();
        }
        rt.add(new RtVo(type, seriousRt));

        //数据汇总
        BottleNeckDetailVo bottleNeckDetail = new BottleNeckDetailVo();
        bottleNeckDetail.setBasic(basic);
        bottleNeckDetail.setAsync(async);
        bottleNeckDetail.setStability(stability);
        bottleNeckDetail.setRt(rt);
        return bottleNeckDetail;
    }

    /**
     * 查询24小时内的所有瓶颈
     *
     * @return
     */
    private List<LinkBottleneck> queryBottleNeckPre24Hour() {
        //1,查询前一分钟时间所有瓶颈
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, PRE_TWENTY_FOUR_HOUR);
        return queryBottleNeckPreTime(DateUtils.dateToString(calendar.getTime(), "yyyy-MM-dd HH:mm:ss"));
    }
}
