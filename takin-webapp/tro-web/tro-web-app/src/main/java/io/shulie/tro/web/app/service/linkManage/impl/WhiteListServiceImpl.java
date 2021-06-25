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

package io.shulie.tro.web.app.service.linkManage.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pamirs.tro.common.constant.TRODictTypeEnum;
import com.pamirs.tro.common.constant.YNEnum;
import com.pamirs.tro.entity.dao.confcenter.TApplicationMntDao;
import com.pamirs.tro.entity.dao.confcenter.TWListMntDao;
import com.pamirs.tro.entity.dao.dict.TDictionaryTypeMapper;
import com.pamirs.tro.entity.dao.user.TUserMapper;
import com.pamirs.tro.entity.domain.dto.linkmanage.InterfaceVo;
import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import com.pamirs.tro.entity.domain.entity.TDictionaryType;
import com.pamirs.tro.entity.domain.entity.TWList;
import com.pamirs.tro.entity.domain.entity.user.User;
import com.pamirs.tro.entity.domain.query.whitelist.WhiteListCreateListVO;
import com.pamirs.tro.entity.domain.query.whitelist.WhiteListOperateVO;
import com.pamirs.tro.entity.domain.query.whitelist.WhiteListQueryVO;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.amdb.api.ApplicationClient;
import io.shulie.tro.web.amdb.bean.query.application.ApplicationInterfaceQueryDTO;
import io.shulie.tro.web.amdb.bean.result.application.ApplicationInterfaceDTO;
import io.shulie.tro.web.amdb.enums.MiddlewareTypeGroupEnum;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.constant.BizOpConstants.Vars;
import io.shulie.tro.web.app.context.OperationLogContextHolder;
import io.shulie.tro.web.app.exception.ExceptionCode;
import io.shulie.tro.web.app.exception.TroWebException;
import io.shulie.tro.web.app.init.sync.ConfigSyncService;
import io.shulie.tro.web.app.input.whitelist.WhitelistImportFromExcelInput;
import io.shulie.tro.web.app.input.whitelist.WhitelistSearchInput;
import io.shulie.tro.web.app.input.whitelist.WhitelistUpdatePartAppNameInput;
import io.shulie.tro.web.app.request.WhiteListDeleteRequest;
import io.shulie.tro.web.app.request.whitelist.WhiteListUpdateRequest;
import io.shulie.tro.web.app.service.linkManage.WhiteListService;
import io.shulie.tro.web.common.enums.whitelist.WhitelistTagEnum;
import io.shulie.tro.web.common.util.whitelist.WhitelistUtil;
import io.shulie.tro.web.common.vo.whitelist.WhiteListVO;
import io.shulie.tro.web.common.vo.whitelist.WhitelistPartVO;
import io.shulie.tro.web.data.dao.application.ApplicationDAO;
import io.shulie.tro.web.data.dao.application.ApplicationWhiteListDAO;
import io.shulie.tro.web.data.dao.application.WhiteListDAO;
import io.shulie.tro.web.data.dao.application.WhitelistEffectiveAppDao;
import io.shulie.tro.web.data.mapper.mysql.WhiteListMapper;
import io.shulie.tro.web.data.model.mysql.WhiteListEntity;
import io.shulie.tro.web.data.param.application.ApplicationQueryParam;
import io.shulie.tro.web.data.param.application.ApplicationWhiteListCreateParam;
import io.shulie.tro.web.data.param.whitelist.WhitelistAddPartAppNameParam;
import io.shulie.tro.web.data.param.whitelist.WhitelistEffectiveAppDeleteParam;
import io.shulie.tro.web.data.param.whitelist.WhitelistEffectiveAppSearchParam;
import io.shulie.tro.web.data.param.whitelist.WhitelistGlobalOrPartParam;
import io.shulie.tro.web.data.param.whitelist.WhitelistSaveOrUpdateParam;
import io.shulie.tro.web.data.param.whitelist.WhitelistSearchParam;
import io.shulie.tro.web.data.param.whitelist.WhitelistUpdatePartAppNameParam;
import io.shulie.tro.web.data.result.application.ApplicationDetailResult;
import io.shulie.tro.web.data.result.whitelist.WhitelistEffectiveAppResult;
import io.shulie.tro.web.data.result.whitelist.WhitelistResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: mubai
 * @Date: 2020-04-20 19:16
 * @Description:
 */

@Service
@Slf4j
public class WhiteListServiceImpl implements WhiteListService {

    @Resource
    private TWListMntDao tWListMntDao;
    @Resource
    private TDictionaryTypeMapper tDictionaryTypeMapper;
    @Resource
    private TApplicationMntDao applicationMntDao;
    @Autowired
    private ApplicationClient applicationClient;
    @Resource
    private TUserMapper TUserMapper;
    @Autowired
    private ConfigSyncService configSyncService;
    @Autowired
    private WhiteListFileService whiteListFileService;
    @Autowired
    private ApplicationWhiteListDAO applicationWhiteListDAO;
    @Autowired
    private ApplicationDAO applicationDAO;
    @Resource
    private WhiteListMapper whiteListMapper;
    @Autowired
    private WhitelistEffectiveAppDao whitelistEffectiveAppDao;

    @Autowired
    private WhiteListDAO whiteListDAO;

    /**
     * 是否开启校验白名单重名
     */
    @Value("${whitelist.duplicate.name.check:false}")
    private String isCheckDuplicateName;

    private Integer getInterfaceIntType(String interfaceType) {
        int type = -1;
        String interfaceTypeString = String.valueOf(interfaceType);
        interfaceTypeString = MiddlewareTypeGroupEnum.getMiddlewareGroupType(interfaceTypeString).getType();
        switch (interfaceTypeString) {
            case "HTTP":
            case "1":
                type = 1;
                break;
            case "DUBBO":
            case "2":
                type = 2;
                break;
            case "UNKNOWN":
            case "-1":
                type = -1;
                break;
            default:
        }
        return type;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void importWhiteListFromExcel(List<WhitelistImportFromExcelInput> inputs) {
        if(CollectionUtils.isEmpty(inputs)) {
            return;
        }
        Long applicationId = inputs.get(0).getApplicationId();
        // 应用详情
        ApplicationDetailResult applicationDetailResult = applicationDAO.getApplicationById(applicationId);
        if(applicationDetailResult == null) {
            throw new TroWebException(ExceptionCode.EXCEL_IMPORT_ERROR, "未找到应用");
        }
        // tro 用户详情
        User user = TUserMapper.selectById(applicationDetailResult.getUserId());
        List<WhitelistResult> whitelistResults = whiteListDAO.listByApplicationId(applicationId);
        Map<String, List<WhitelistResult>> whitelistResultsMap = whitelistResults.stream()
            .collect(Collectors.groupingBy(w -> WhitelistUtil.buildWhiteId(w.getType(),w.getInterfaceName())));
        // 重名白名单
        List<String> armdString = whitelistResults.stream().map(WhitelistResult::getInterfaceName).collect(Collectors.toList());
        List<String> existWhite = getExistWhite(armdString,Lists.newArrayList());

        // 区分更新 还是 新增
        List<WhitelistSaveOrUpdateParam> saveOrUpdateParams = Lists.newArrayList();
        inputs.forEach(input ->{
            String id = WhitelistUtil.buildWhiteId(input.getInterfaceType(),input.getInterfaceName());
            List<WhitelistResult> results = whitelistResultsMap.get(id);
            WhitelistSaveOrUpdateParam saveOrUpdateParam = new WhitelistSaveOrUpdateParam();
            if(CollectionUtils.isNotEmpty(results)) {
                WhitelistResult whitelistResult = results.get(0);
                BeanUtils.copyProperties(input,saveOrUpdateParam);
                saveOrUpdateParam.setWlistId(whitelistResult.getWlistId());
                saveOrUpdateParam.setGmtModified(new Date());
                saveOrUpdateParam.setType(String.valueOf(input.getInterfaceType()));
                saveOrUpdateParam.setUseYn(input.getUseYn());
                // 白名单重复
                if(isDuplicate(existWhite,WhitelistUtil.buildWhiteId(saveOrUpdateParam.getType(),saveOrUpdateParam.getInterfaceName()))) {
                    saveOrUpdateParam.setIsGlobal(!Boolean.parseBoolean(isCheckDuplicateName));
                }
                saveOrUpdateParam.setIsHandwork(input.getIsHandwork());
            }else {
                BeanUtils.copyProperties(input,saveOrUpdateParam);
                saveOrUpdateParam.setType(String.valueOf(input.getInterfaceType()));
                saveOrUpdateParam.setUseYn(input.getUseYn());
                // 白名单重复
                if(isDuplicate(existWhite,WhitelistUtil.buildWhiteId(saveOrUpdateParam.getType(),saveOrUpdateParam.getInterfaceName()))) {
                    saveOrUpdateParam.setIsGlobal(!Boolean.parseBoolean(isCheckDuplicateName));
                }
                saveOrUpdateParam.setIsHandwork(input.getIsHandwork());
            }
            saveOrUpdateParams.add(saveOrUpdateParam);
        });
        whiteListDAO.batchSaveOrUpdate(saveOrUpdateParams);
        // 再次获取
        List<WhitelistAddPartAppNameParam> addPartAppNameParams = Lists.newArrayList();
        List<WhitelistResult> againResult = whiteListDAO.listByApplicationId(applicationId);
        Map<String, List<WhitelistResult>> againResultsMap = againResult.stream()
            .collect(Collectors.groupingBy(w ->WhitelistUtil.buildWhiteId(w.getType(),w.getInterfaceName())));
        for(WhitelistImportFromExcelInput input : inputs) {
            if(CollectionUtils.isEmpty(input.getEffectAppNames())) {
                continue;
            }
            String id = WhitelistUtil.buildWhiteId(input.getInterfaceType(),input.getInterfaceName());
            List<WhitelistResult> listResult = againResultsMap.get(id);

            input.getEffectAppNames().forEach(appName -> {
                WhitelistAddPartAppNameParam addPartAppNameParam = new WhitelistAddPartAppNameParam();
                addPartAppNameParam.setInterfaceName(listResult.get(0).getInterfaceName());
                addPartAppNameParam.setType(listResult.get(0).getType());
                addPartAppNameParam.setEffectiveAppName(appName);
                addPartAppNameParam.setCustomerId(applicationDetailResult.getCustomerId());
                addPartAppNameParam.setUserId(applicationDetailResult.getUserId());
                addPartAppNameParam.setWlistId(listResult.get(0).getWlistId());
                addPartAppNameParams.add(addPartAppNameParam);
            });
        }
        // 删除原有
        List<Long> wlistIds = addPartAppNameParams.stream().map(WhitelistAddPartAppNameParam::getWlistId).collect(Collectors.toList());
        WhitelistEffectiveAppDeleteParam deleteParam = new WhitelistEffectiveAppDeleteParam();
        deleteParam.setCustomerId(applicationDetailResult.getCustomerId());
        deleteParam.setWlistIds(wlistIds);
        whitelistEffectiveAppDao.batchDelete(deleteParam);
        // 新增
        whitelistEffectiveAppDao.addPartAppName(addPartAppNameParams);
        // 更新agent
        whiteListFileService.writeWhiteListFile();
        configSyncService.syncAllowList(user.getKey(), applicationId, applicationDetailResult.getApplicationName());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void saveWhitelist(WhiteListCreateListVO vo) {
        Long applicationId = vo.getApplicationId();
        Integer interfaceType = vo.getInterfaceType();
        List<String> interfaceList = vo.getInterfaceList();
        TDictionaryType tDictionaryType = tDictionaryTypeMapper.selectDictionaryByTypeAlias(TRODictTypeEnum.WLIST.name());

        List<TWList> duplicateList = Lists.newArrayList();
        List<TWList> toAddList = Lists.newArrayList();
        List<TWList> toUpdateList = Lists.newArrayList();

        ApplicationDetailResult applicationDetailResult = applicationDAO.getApplicationById(applicationId);
        //从数据库中查出该应用下所有白名单和新增白名单进行比对
        User user = TUserMapper.selectById(applicationDetailResult.getUserId());
        List<TWList> twLists = tWListMntDao.queryWListTotalByApplicationId(applicationId);

        List<String> armdString = twLists.stream().map(TWList::getInterfaceName).collect(Collectors.toList());
        // 重名白名单
        List<String> existWhite = this.getExistWhite(armdString,Lists.newArrayList());

        if (CollectionUtils.isEmpty(twLists)) {
            for (String interfaceName : interfaceList) {
                TWList tWList = TWList.build(String.valueOf(applicationId),
                    String.valueOf(interfaceType),
                    interfaceName,
                    String.valueOf(YNEnum.YES.getValue()),
                    tDictionaryType.getId());
                twLists.add(tWList);
            }
            List<ApplicationWhiteListCreateParam> paramList = twLists.stream().map(twList -> {
                ApplicationWhiteListCreateParam param = new ApplicationWhiteListCreateParam();
                BeanUtils.copyProperties(twList, param);
                param.setCustomerId(applicationDetailResult.getCustomerId());
                param.setUserId(applicationDetailResult.getUserId());
                param.setIsHandwork(true);
                // 白名单重复
                if(isDuplicate(existWhite,WhitelistUtil.buildWhiteId(param.getType(),param.getInterfaceName()))) {
                    param.setIsGlobal(!Boolean.parseBoolean(isCheckDuplicateName));
                }
                return param;
            }).collect(Collectors.toList());
            applicationWhiteListDAO.insertBatch(paramList);
            whiteListFileService.writeWhiteListFile();
            configSyncService.syncAllowList(user.getKey(), applicationId, applicationDetailResult.getApplicationName());
            return;
        } else {
            List<String> existInterfaceList = twLists.stream().map(e -> WhitelistUtil.buildWhiteId(e.getType(),e.getInterfaceName())).collect(Collectors.toList());
            for (String interfaceName : interfaceList) {
                // 增加；类型判断
                if (existInterfaceList.contains(WhitelistUtil.buildWhiteId(interfaceType,interfaceName))) {
                    TWList whitelist = twLists.stream()
                        .filter(twList -> interfaceName.equals(twList.getInterfaceName()))
                        .findFirst()
                        .orElse(new TWList());
                    // 手工添加
                    whitelist.setHandwork(true);
                    if (String.valueOf(YNEnum.YES.getValue()).equals(whitelist.getUseYn())) {
                        //忽略重复的白名单接口
                        duplicateList.add(whitelist);
                    } else {
                        //更新已存在但未启用的白名单接口
                        whitelist.setUseYn(String.valueOf(YNEnum.YES.getValue()));
                        toUpdateList.add(whitelist);
                    }
                } else {
                    //新增数据库中不存在的白名单接口
                    TWList tWList = TWList.build(String.valueOf(applicationId),
                        String.valueOf(interfaceType),
                        interfaceName,
                        String.valueOf(YNEnum.YES.getValue()),
                        tDictionaryType.getId());
                    toAddList.add(tWList);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(toAddList)) {
            List<ApplicationWhiteListCreateParam> paramList = toAddList.stream().map(twList -> {
                ApplicationWhiteListCreateParam param = new ApplicationWhiteListCreateParam();
                BeanUtils.copyProperties(twList, param);
                param.setCustomerId(applicationDetailResult.getCustomerId());
                param.setUserId(applicationDetailResult.getUserId());
                // 白名单重复
                if(isDuplicate(existWhite,WhitelistUtil.buildWhiteId(param.getType(),param.getInterfaceName()))) {
                    param.setIsGlobal(!Boolean.parseBoolean(isCheckDuplicateName));
                }
                if(twList.getHandwork() == null) {
                    param.setIsHandwork(true);
                }
                return param;
            }).collect(Collectors.toList());
            applicationWhiteListDAO.insertBatch(paramList);
        }

        if (CollectionUtils.isNotEmpty(toUpdateList)) {
            List<Long> wlistIdList = toUpdateList.stream().map(TWList::getWlistId).collect(Collectors.toList());
            tWListMntDao.batchEnableWList(wlistIdList);
        }
        whiteListFileService.writeWhiteListFile();
        configSyncService.syncAllowList(user.getKey(), applicationId, applicationDetailResult.getApplicationName());
    }


    @Override
    public void operateWhitelist(WhiteListOperateVO vo) {
        if (vo.getApplicationId() == null) {
            throw new RuntimeException("没有传入应用id");
        }
        List<TWList> twLists = tWListMntDao.getWListByApplicationId(vo.getApplicationId());
        ApplicationDetailResult applicationDetailResult = applicationDAO.getApplicationById(vo.getApplicationId());
        Map<String, TWList> twMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(twLists)) {
            twMap = twLists.stream().collect(
                    Collectors.toMap(e -> WhitelistUtil.buildWhiteId(e.getType(), e.getInterfaceName()), e -> e, (ov, nv) -> ov));
        }

        List<String> armdString = twLists.stream().map(TWList::getInterfaceName).collect(Collectors.toList());
        // 重名白名单
        List<String> existWhite = this.getExistWhite(armdString,Lists.newArrayList());
        // 加入白名单
        if (vo.getType() == 1) {
            // 数据库没有的就加进去
            if (CollectionUtils.isNotEmpty(vo.getIds())) {
                List<TWList> beAddList = Lists.newArrayList();
                List<Long> beUpdateList = Lists.newArrayList();
                for (String id : vo.getIds()) {
                    TWList twList = twMap.get(id);
                    if (twList == null) {
                        String[] split = splitId(id);
                        twList = new TWList();
                        twList.setInterfaceName(split[0]);
                        twList.setType(String.valueOf(getInterfaceIntType(split[1])));
                        twList.setDictType("ca888ed801664c81815d8c4f5b8dff0c");
                        twList.setApplicationId(vo.getApplicationId() + "");
                        twList.setUseYn("1");
                        twList.setCustomerId(applicationDetailResult.getCustomerId());
                        twList.setUserId(applicationDetailResult.getUserId());
                        twList.setGlobal(true);
                        // 全局设置
                        if(isDuplicate(existWhite,WhitelistUtil.buildWhiteId(twList.getType(),twList.getInterfaceName()))) {
                            if(Boolean.parseBoolean(isCheckDuplicateName)) {
                                twList.setGlobal(false);
                            }
                        }
                        beAddList.add(twList);
                    } else {
                        beUpdateList.add(twList.getWlistId());
                    }
                }
                if (CollectionUtils.isNotEmpty(beAddList)) {
                    tWListMntDao.batchAddWList(beAddList);
                }
                if (CollectionUtils.isNotEmpty(beUpdateList)) {
                    tWListMntDao.batchEnableWList(beUpdateList);
                }
            }
        }

        // 移除白名单
        if (vo.getType() == 0) {
            if (CollectionUtils.isNotEmpty(vo.getIds())) {
                List<Long> beList = Lists.newArrayList();
                for (String id : vo.getIds()) {
                    TWList twList = twMap.get(id);
                    if (twList != null) {
                        beList.add(twList.getWlistId());
                    }
                }
                if (CollectionUtils.isNotEmpty(beList)) {
                    tWListMntDao.batchDisableWList(beList);
                }
            }
        }
        TApplicationMnt tApplicationMnt = applicationMntDao.queryApplicationinfoById(vo.getApplicationId());
        User user = TUserMapper.selectById(tApplicationMnt.getUserId());
        whiteListFileService.writeWhiteListFile();
        configSyncService.syncAllowList(user.getKey(), vo.getApplicationId(), tApplicationMnt.getApplicationName());
    }

    public List<String> getExistWhite(List<String> interfaceNames,List<ApplicationDetailResult> appDetailResults) {
        List<String> existWhites = Lists.newArrayList();
        // 判断重名
        List<WhitelistResult> results = whiteListDAO.getList(new WhitelistSearchParam());
        //
        if(CollectionUtils.isEmpty(appDetailResults)) {
            appDetailResults = getApplicationDetailResults();
        }
        Map<Long,List<ApplicationDetailResult>> appMap = appDetailResults
            .stream().collect(Collectors.groupingBy(ApplicationDetailResult::getApplicationId));
        List<String> appNameWhiteIds = results.stream().map(result -> {
            // 应用名
            List<ApplicationDetailResult> appList = appMap.get(result.getApplicationId());
            return  WhitelistUtil.buildAppNameWhiteId(CollectionUtils.isNotEmpty(appList)?appList.get(0).getApplicationName():"",
                result.getType(),result.getInterfaceName());
        }).collect(Collectors.toList());

        List<String> list = results.stream().map(result -> WhitelistUtil.buildWhiteId(result.getType(),result.getInterfaceName()))
            .collect(Collectors.toList());
        existWhites.addAll(list);
        // amdb
        ApplicationInterfaceQueryDTO query = new ApplicationInterfaceQueryDTO();
        query.setPageSize(1000);
        query.setServiceName(StringUtils.join(interfaceNames,","));
        List<ApplicationInterfaceDTO> dtos = applicationClient.listInterfaces(query);
        if (CollectionUtils.isNotEmpty(dtos)) {
            List<String> amdbInferfaces = dtos.stream().filter(item -> {
                Integer interfaceType = getInterfaceIntType(item.getInterfaceType());
                String appNameWhiteId =  WhitelistUtil.buildAppNameWhiteId(item.getAppName(), interfaceType,item.getInterfaceName());
                // 从AMDB加载过来的白名单，过滤掉类型不支持的白名单--20210303 CYF
                return (appNameWhiteIds.stream().filter(e -> e.equals(appNameWhiteId)).count() == 0) && (!"-1".equals(interfaceType));
            }).map(item -> WhitelistUtil.buildWhiteId(getInterfaceIntType(item.getInterfaceType()),item.getInterfaceName()))
                .collect(Collectors.toList());
            existWhites.addAll(amdbInferfaces);
        }
        return existWhites;
    }

    @Override
    public PageInfo<WhiteListVO> queryWhitelist(WhiteListQueryVO vo) {
        Map<String, WhiteListVO> totalResult = Maps.newHashMap();
        List<TWList> dbResult = tWListMntDao.queryDistinctWListTotalByApplicationId(vo.getApplicationId());
        String applicationName = applicationMntDao.selectApplicationName(String.valueOf(vo.getApplicationId()));
        ApplicationDetailResult applicationDetailResult = applicationDAO.getApplicationById(vo.getApplicationId());
        if (applicationName == null) {
            throw new RuntimeException("根据应用id 找不到应用名");
        }
        if (vo.getUseYn() == null) {
            //查询链路梳理出来的所有接口
            List<InterfaceVo> cardingResult = getAllInterface(applicationName);
            //查询mysql数据库中应用下所有白名单
            if (CollectionUtils.isNotEmpty(cardingResult)) {
                mergeCardingList(totalResult, cardingResult, applicationDetailResult);
            }
            if (CollectionUtils.isNotEmpty(dbResult)) {
                mergeDbList(totalResult, dbResult);
            }

        } else if (vo.getUseYn() == 1) {
            if (CollectionUtils.isNotEmpty(dbResult)) {
                List<TWList> dbFilterResult = dbResult.stream().filter(w -> "1".equals(w.getUseYn())).collect(
                        Collectors.toList());
                mergeDbList(totalResult, dbFilterResult);
            }

        } else {
            //查询链路梳理出来的所有接口
            List<InterfaceVo> cardingResult = getAllInterface(applicationName);
            //查询mysql数据库中应用下所有白名单
            if (CollectionUtils.isNotEmpty(cardingResult)) {
                mergeCardingList(totalResult, cardingResult, applicationDetailResult);
            }

            if (CollectionUtils.isNotEmpty(dbResult)) {
                mergeDbList(totalResult, dbResult);
                List<TWList> dbFilterResult = dbResult.stream().filter(w -> "1".equals(w.getUseYn())).collect(Collectors.toList());
                mergeOutList(totalResult, dbFilterResult);
            }
        }

        if (totalResult.size() == 0) {
            return new PageInfo<>(Lists.newArrayList());
        }
        Iterator<Map.Entry<String, WhiteListVO>> iterator = totalResult.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, WhiteListVO> next = iterator.next();
            if (vo.getInterfaceType() != null && (!next.getValue().getInterfaceType().equals(vo.getInterfaceType()))) {
                iterator.remove();
                continue;
            }
            if (StringUtils.isNotBlank(vo.getInterfaceName()) && (!next.getValue().getInterfaceName().contains(
                    vo.getInterfaceName()))) {
                iterator.remove();
            }
            // nothing;
        }

        int start = Math.min(vo.getOffset(), totalResult.size());
        int end = Math.min((vo.getOffset() + vo.getPageSize()), totalResult.size());
        List<WhiteListVO> resList = new ArrayList<>(totalResult.values());
        //排序
        resList.sort(this::whiteListCompare);
        resList = resList.subList(start, end);

        List<String> armdString = resList.stream().map(WhiteListVO::getInterfaceName).collect(Collectors.toList());
        // 重名白名单
        List<String> existWhite = getExistWhite(armdString,Lists.newArrayList());

        for (WhiteListVO dto : resList) {
            List<Long> allowUpdateUserIdList = RestContext.getUpdateAllowUserIdList();
            if (dto.getIsDbValue()) {
                if (CollectionUtils.isEmpty(allowUpdateUserIdList)) {
                    //管理员
                    dto.setCanEdit(true);
                } else {
                    //普通用户
                    dto.setCanEdit(allowUpdateUserIdList.contains(dto.getUserId()));
                }
                //手动新增的白名单才能编辑
                if(dto.getIsHandwork() == null) {
                    dto.setIsHandwork(true);
                }
                // 非手工，不允许编辑
                if(!dto.getIsHandwork()) {
                    dto.setCanEdit(false);
                }
            } else {
                //自动上报的白名单不能编辑
                dto.setCanEdit(false);
                dto.setIsHandwork(false);
            }

            // 补充标签
            dto.setTags(getTags(existWhite,dto));
            List<Long> allowDeleteUserIdList = RestContext.getDeleteAllowUserIdList();
            if (dto.getIsDbValue()) {
                if (CollectionUtils.isEmpty(allowDeleteUserIdList)) {
                    dto.setCanRemove(true);
                } else {
                    dto.setCanRemove(allowDeleteUserIdList.contains(dto.getUserId()));
                }
                // 非手工，不允许编辑
                if(!dto.getIsHandwork()) {
                    dto.setCanEdit(false);
                }
            } else {
                dto.setCanRemove(false);
            }

            List<Long> allowEnableDisableUserIdList = RestContext.getEnableDisableAllowUserIdList();
            if (CollectionUtils.isNotEmpty(allowEnableDisableUserIdList)) {
                dto.setCanEnableDisable(allowEnableDisableUserIdList.contains(dto.getUserId()));
            }
        }
        PageInfo<WhiteListVO> whiteListDTOPageInfo = new PageInfo<>(resList);
        whiteListDTOPageInfo.setTotal(totalResult.size());
        return whiteListDTOPageInfo;
    }

    public int whiteListCompare(WhiteListVO o1, WhiteListVO o2) {
        int sort = 0;
        if (o1.getGmtUpdate() != null && o2.getGmtUpdate() != null) {
            // 更新时间倒序
            sort = -o1.getGmtUpdate().compareTo(o2.getGmtUpdate());
            if (sort != 0) {
                return sort;
            }
        }
        if (o1.getDbId() != null && o2.getDbId() != null) {
            sort = -o1.getDbId().compareTo(o2.getDbId());
            if (sort != 0) {
                return sort;
            }
        }
        return o1.getInterfaceName().compareTo(o2.getInterfaceName());
    }

    private void mergeOutList(Map<String, WhiteListVO> totalResult, List<TWList> dbResult) {
        if (MapUtils.isEmpty(totalResult) || CollectionUtils.isEmpty(dbResult)) {
            return;
        }
        dbResult.forEach(it -> {
            String id = WhitelistUtil.buildWhiteId(it.getType(), it.getInterfaceName());
            totalResult.remove(id);
        });
    }

    private void mergeCardingList(Map<String, WhiteListVO> totalResult, List<InterfaceVo> graphResult,
                                  ApplicationDetailResult applicationDetailResult) {
        if (CollectionUtils.isEmpty(graphResult)) {
            return;
        }
        graphResult.forEach(graph -> {
            String interfaceType = graph.getInterfaceType();
            String id = WhitelistUtil.buildWhiteId(interfaceType, graph.getInterfaceName());
            WhiteListVO whiteListVO = new WhiteListVO();
            whiteListVO.setWlistId(id);
            whiteListVO.setUseYn(0);
            whiteListVO.setInterfaceType(getInterfaceIntType(interfaceType));
            whiteListVO.setInterfaceName(graph.getInterfaceName());
            whiteListVO.setCustomerId(applicationDetailResult.getCustomerId());
            whiteListVO.setUserId(applicationDetailResult.getUserId());
            whiteListVO.setIsDbValue(false);
            // 默认全局生效
            whiteListVO.setIsGlobal(true);
            // 手工
            whiteListVO.setIsHandwork(false);
            totalResult.putIfAbsent(id, whiteListVO);
        });
    }

    private void mergeDbList(Map<String, WhiteListVO> totalResult, List<TWList> dbResults) {
        if (CollectionUtils.isEmpty(dbResults)) {
            return;
        }
        dbResults.forEach(dbResult -> {
            String interfaceType = dbResult.getType();
            String id = WhitelistUtil.buildWhiteId(interfaceType, dbResult.getInterfaceName());

            WhiteListVO whiteListVO = new WhiteListVO();
            // 全局配置
            whiteListVO.setIsGlobal(dbResult.getGlobal());
            //是否手工
            whiteListVO.setIsHandwork(dbResult.getHandwork());
            whiteListVO.setWlistId(id);
            whiteListVO.setUseYn(Integer.parseInt(dbResult.getUseYn()));
            Integer type = getInterfaceIntType(interfaceType);
            whiteListVO.setInterfaceType(type);
            whiteListVO.setInterfaceName(dbResult.getInterfaceName());
            // todo 之后名字得改
            whiteListVO.setGmtUpdate(dbResult.getGmtModified());
            whiteListVO.setCustomerId(dbResult.getCustomerId());
            whiteListVO.setUserId(dbResult.getUserId());
            whiteListVO.setIsDbValue(true);
            whiteListVO.setGmtCreate(dbResult.getCreateTime());
            whiteListVO.setDbId(String.valueOf(dbResult.getWlistId()));
            // 如果数据的记录和自动梳理的结果合并时候存在冲突，那么说明这个数据是图库中的数据
            totalResult.merge(id, whiteListVO, (ov, nv) -> {
                // 数据库可能有重复记录，两条一样的重复记录，那么保留一条，可删除；
                // 一条AMDB，一条数据库，不可删除
                nv.setIsDbValue(ov.getIsDbValue() & nv.getIsDbValue());
                return nv;
            });
        });
    }

    @Override
    public List<TWList> getAllEnableWhitelists(String applicationId) {
        return tWListMntDao.getAllEnableWhitelists(applicationId);
    }

    @Override
    public void updateWhitelist(WhiteListUpdateRequest request) {
        WhiteListEntity whiteListEntity = whiteListMapper.selectById(request.getDbId());
        if (whiteListEntity == null) {
            throw new RuntimeException(String.format("ID[%s]对应的白名单，在数据库中没有发现", request.getDbId()));
        }


        // 是否有重名类型的
        if(!request.getInterfaceType().equals(whiteListEntity.getType()) || request.getInterfaceName().equals(whiteListEntity.getInterfaceName())) {
            // 有修改，需要判断是否有重名
            //从数据库中查出该应用下所有白名单和新增白名单进行比对
            List<TWList> existWhiteLists = tWListMntDao.queryWListTotalByApplicationId(whiteListEntity.getApplicationId());
            List<String> data = existWhiteLists.stream().map(tw -> WhitelistUtil.buildWhiteId(tw.getType(),tw.getInterfaceName())).collect(Collectors.toList());
            if(data.contains(WhitelistUtil.buildWhiteId(request.getInterfaceType(),request.getInterfaceName()))) {
                throw new RuntimeException(String.format("对应的白名单，在数据库中有重名白名单", request.getDbId()));
            }
        }
        WhiteListEntity updateEntity = new WhiteListEntity();
        updateEntity.setInterfaceName(request.getInterfaceName());
        updateEntity.setType(request.getInterfaceType());
        updateEntity.setWlistId(request.getDbId());
        whiteListMapper.updateById(updateEntity);
        // 同步更改生效应用的白名单信息
        WhitelistEffectiveAppSearchParam searchParam = new WhitelistEffectiveAppSearchParam();
        searchParam.setWlistId(request.getDbId());
        List<WhitelistEffectiveAppResult> appResults = whitelistEffectiveAppDao.getList(searchParam);
        List<WhitelistUpdatePartAppNameParam> params = appResults.stream().map(result -> {
            WhitelistUpdatePartAppNameParam param = new WhitelistUpdatePartAppNameParam();
            BeanUtils.copyProperties(result,param);
            param.setInterfaceName(request.getInterfaceName());
            param.setType(request.getInterfaceType());
            param.setGmtModified(new Date());
            return param;
        }).collect(Collectors.toList());
        whitelistEffectiveAppDao.updatePartAppName(params);
        whiteListFileService.writeWhiteListFile();
        configSyncService.syncAllowList(RestContext.getUser().getCustomerKey(), whiteListEntity.getApplicationId(),
                null);
    }

    @Override
    public void deleteWhitelist(WhiteListDeleteRequest request) {
        List<WhiteListEntity> listEntities = whiteListMapper.selectBatchIds(request.getDbIds());
        if (CollectionUtils.isEmpty(listEntities)) {
            return;
        }
        List<String> interfaceNames = listEntities.stream().map(WhiteListEntity::getInterfaceName).collect(
                Collectors.toList());
        OperationLogContextHolder.addVars(Vars.INTERFACE, StringUtils.join(interfaceNames, ","));
        whiteListMapper.deleteBatchIds(request.getDbIds());
        // 同时删除部分应用
        List<Long> ids = listEntities.stream().map(WhiteListEntity::getWlistId).collect(Collectors.toList());
        WhitelistEffectiveAppDeleteParam deleteParam = new WhitelistEffectiveAppDeleteParam();
        deleteParam.setWlistIds(ids);
        whitelistEffectiveAppDao.batchDelete(deleteParam);
        whiteListFileService.writeWhiteListFile();
        listEntities.forEach(entry -> configSyncService.syncAllowList(RestContext.getUser().getCustomerKey(), entry.getApplicationId(), null));
    }

    @Override
    public WhitelistPartVO getPart(Long wlistId) {
        WhitelistPartVO vo = new WhitelistPartVO();
        // 生效应用
        WhitelistEffectiveAppSearchParam appSearchParam = new WhitelistEffectiveAppSearchParam();
        appSearchParam.setCustomerId(RestContext.getCustomerId());
        appSearchParam.setWlistId(wlistId);
        List<WhitelistEffectiveAppResult> appResults = whitelistEffectiveAppDao.getList(appSearchParam);
        vo.setEffectiveAppNames(CollectionUtils.isNotEmpty(appResults)?
            appResults.stream().map(WhitelistEffectiveAppResult::getEffectiveAppName).collect(Collectors.toList()) : Lists.newArrayList());
        // all
        ApplicationQueryParam queryParam = new ApplicationQueryParam();
        queryParam.setCustomerId(RestContext.getCustomerId());
        List<String> allAppNames = applicationDAO.getAllApplicationName(queryParam);
        vo.setAllAppNames(allAppNames);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void part(WhitelistUpdatePartAppNameInput input) {
        WhitelistResult whitelistResult = whiteListDAO.selectById(input.getWlistId());
        if(whitelistResult == null ) {
            throw new TroWebException(ExceptionCode.WHITELIST_EFFECTIVE_APP_ERROR, "未找到该白名单");
        }
        if(CollectionUtils.isEmpty(input.getEffectiveAppName())) {
            return;
        }
        User user = RestContext.getUser();
        if(user == null) {
            throw new TroWebException(ExceptionCode.WHITELIST_EFFECTIVE_APP_ERROR, "未找到登录账号信息");
        }
        // 先删除原先的
        WhitelistEffectiveAppDeleteParam deleteParam = new WhitelistEffectiveAppDeleteParam();
        deleteParam.setCustomerId(user.getCustomerId());
        deleteParam.setWlistId(input.getWlistId());
        whitelistEffectiveAppDao.delete(deleteParam);
        // 添加新的
        List<WhitelistAddPartAppNameParam> params = Lists.newArrayList();
        input.getEffectiveAppName().forEach(appName -> {
            WhitelistAddPartAppNameParam param = new WhitelistAddPartAppNameParam();
            param.setInterfaceName(whitelistResult.getInterfaceName());
            param.setType(whitelistResult.getType());
            param.setEffectiveAppName(appName);
            param.setCustomerId(user.getCustomerId());
            param.setUserId(user.getId());
            param.setWlistId(input.getWlistId());
            params.add(param);
        });
        whitelistEffectiveAppDao.addPartAppName(params);
        // 更新应用状态
        WhitelistGlobalOrPartParam param = new WhitelistGlobalOrPartParam();
        param.setIsGlobal(false);
        param.setWlistId(input.getWlistId());
        whiteListDAO.updateWhitelistGlobal(param);
        // agent生效
        whiteListFileService.writeWhiteListFile();
        configSyncService.syncAllowList(RestContext.getUser().getCustomerKey(), whitelistResult.getApplicationId(), null);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void global(Long wlistId) {
        WhitelistResult whitelistResult = whiteListDAO.selectById(wlistId);
        if(whitelistResult == null ) {
            throw new TroWebException(ExceptionCode.WHITELIST_EFFECTIVE_APP_ERROR, "未找到该白名单");
        }
        // 判断下不允许修改
        if(Boolean.parseBoolean(isCheckDuplicateName)) {
            // 重名白名单
            List<String> amdbString = Lists.newArrayList();
            amdbString.add(whitelistResult.getInterfaceName());
            List<String> existWhite = this.getExistWhite(amdbString,Lists.newArrayList());
            if(isDuplicate(existWhite,WhitelistUtil.buildWhiteId(whitelistResult.getType(),whitelistResult.getInterfaceName()))) {
                throw new TroWebException(ExceptionCode.WHITELIST_EFFECTIVE_APP_ERROR, "重名白名单不允许全局生效");
            }
        }
        WhitelistGlobalOrPartParam param = new WhitelistGlobalOrPartParam();
        param.setIsGlobal(true);
        param.setWlistId(wlistId);
        whiteListDAO.updateWhitelistGlobal(param);
        // agent生效
        whiteListFileService.writeWhiteListFile();
        configSyncService.syncAllowList(RestContext.getUser().getCustomerKey(), whitelistResult.getApplicationId(), null);
    }

    @Override
    public PagingList<WhiteListVO> pagingList(WhitelistSearchInput input) {
        // 获取应用
        List<ApplicationDetailResult> appDetailResults = getApplicationDetailResults();
        // 从mysql查出数据
        PagingList<WhiteListVO> dbPagingList = getDbPagingList(input,appDetailResults);
        // 从amdb查出数据
        PagingList<WhiteListVO> amdbPagingList = getAmdbPagingList(input,appDetailResults);
        // 增加amdb数据
        List<WhiteListVO> results = Lists.newArrayList();
        if((long)(input.getPageSize() * (input.getCurrentPage() + 1)) >= dbPagingList.getTotal() || dbPagingList.getTotal() < input.getPageSize()) {
            // 大于 数据库页数
            results.addAll(dbPagingList.getList());
            results.addAll(amdbPagingList.getList());
        }else {
            results.addAll(dbPagingList.getList());
        }
        List<String> armdString = results.stream().map(WhiteListVO::getInterfaceName).collect(Collectors.toList());
        // 重名白名单
        List<String> existWhite = getExistWhite(armdString,appDetailResults);
        results.forEach(vo -> {
            // 补充标签
            vo.setTags(getTags(existWhite, vo));
        });
        return PagingList.of(results,dbPagingList.getTotal() + amdbPagingList.getTotal());
    }

    private List<ApplicationDetailResult> getApplicationDetailResults() {
        ApplicationQueryParam queryParam = new ApplicationQueryParam();
        queryParam.setCustomerId(RestContext.getCustomerId());
        return applicationDAO.getApplicationList(queryParam);
    }

    private PagingList<WhiteListVO> getAmdbPagingList(WhitelistSearchInput input,List<ApplicationDetailResult> appDetailResults) {
        ApplicationInterfaceQueryDTO query = new ApplicationInterfaceQueryDTO();
        // 大数据拿到页内容
        query.setPageSize(input.getPageSize());
        if(StringUtils.isNotBlank(input.getInterfaceName())) {
            query.setServiceName(StringUtils.join(input.getInterfaceName(),","));
        }
        if(StringUtils.isNotBlank(input.getAppName())) {
            query.setAppName(input.getAppName());
        }
        // 判断重名
        List<WhitelistResult> results = whiteListDAO.getList(new WhitelistSearchParam());
        Map<Long,List<ApplicationDetailResult>> appMap =
            appDetailResults.stream().collect(Collectors.groupingBy(ApplicationDetailResult::getApplicationId));
        List<String> appNameWhiteIds = results.stream().map(result -> {
                // 应用名
                List<ApplicationDetailResult> appList = appMap.get(result.getApplicationId());
               return  WhitelistUtil.buildAppNameWhiteId(CollectionUtils.isNotEmpty(appList)?appList.get(0).getApplicationName():"",
                   result.getType(),result.getInterfaceName());
            }).collect(Collectors.toList());

        AtomicLong atomicLong = new AtomicLong(0);
        PagingList<ApplicationInterfaceDTO> dtos = applicationClient.pageInterfaces(query);
        if (CollectionUtils.isNotEmpty(dtos.getList())) {
            List<WhiteListVO> whiteListVOS = dtos.getList().stream().filter(item -> {
                Integer interfaceType = getInterfaceIntType(item.getInterfaceType());
                String appNameWhiteId =  WhitelistUtil.buildAppNameWhiteId(item.getAppName(), interfaceType,item.getInterfaceName());
                // 从AMDB加载过来的白名单，过滤掉类型不支持的白名单--20210303 CYF
                Boolean flag = (appNameWhiteIds.stream().filter(e -> e.equals(appNameWhiteId)).count() == 0) && (!"-1".equals(interfaceType));
                if(!flag) {
                    // 去除不要的数据
                    atomicLong.incrementAndGet();
                }
                return flag;
            }).map(item -> {
                WhiteListVO vo = new WhiteListVO();
                vo.setAppName(item.getAppName());
                vo.setInterfaceType(getInterfaceIntType(item.getInterfaceType()));
                vo.setInterfaceName(item.getInterfaceName());
                vo.setIsHandwork(false);
                // 未加入
                vo.setUseYn(0);
                vo.setIsGlobal(true);
                return vo;
            }).collect(Collectors.toList());
            return PagingList.of(whiteListVOS,dtos.getTotal() - atomicLong.get());
        }else {
            return PagingList.empty();
        }
    }

    private PagingList<WhiteListVO> getDbPagingList(WhitelistSearchInput input,List<ApplicationDetailResult> appDetailResults) {
        WhitelistSearchParam param = new WhitelistSearchParam();
        BeanUtils.copyProperties(input,param);
        // 应用名模糊查询
        if(StringUtils.isNotBlank(input.getAppName())) {
            List<Long> ids = appDetailResults.stream().filter(app -> app.getApplicationName().contains(input.getAppName()))
                .map(ApplicationDetailResult::getApplicationId).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(ids)) {
               return PagingList.empty();
            }
            param.setIds(ids);
        }
        if(StringUtils.isNotBlank(input.getInterfaceName())) {
            List<String> interfaceNames = Lists.newArrayList(StringUtils.split(input.getInterfaceName(),","));
            param.setInterfaceNames(interfaceNames);
        }

        PagingList<WhiteListVO> pagingList = whiteListDAO.pagingList(param);
        // 生效应用
        // 获取所有生效效应，是否有局部应用
        WhitelistEffectiveAppSearchParam searchParam = new WhitelistEffectiveAppSearchParam();
        searchParam.setCustomerId(RestContext.getCustomerId());
        List<WhitelistEffectiveAppResult> effectiveAppDaoList = whitelistEffectiveAppDao.getList(searchParam);
        Map<Long,List<WhitelistEffectiveAppResult>> appResultsMap = effectiveAppDaoList.stream()
            .collect(Collectors.groupingBy(WhitelistEffectiveAppResult::getWlistId));

        // 应用名
        Map<Long,List<ApplicationDetailResult>> appMap =
            appDetailResults.stream().collect(Collectors.groupingBy(ApplicationDetailResult::getApplicationId));

        // 显示
        pagingList.getList().forEach(vo -> {
            List<ApplicationDetailResult> appList = appMap.get(Long.valueOf(vo.getApplicationId()));
            if(CollectionUtils.isNotEmpty(appList)) {
                vo.setAppName(appList.get(0).getApplicationName());
            }
            // 适配白名单类型
            vo.setInterfaceType(getInterfaceIntType(String.valueOf(vo.getInterfaceType())));
            // 应用
            List<WhitelistEffectiveAppResult> appResults = appResultsMap.get(Long.valueOf(vo.getWlistId()));
            vo.setEffectiveAppNames(CollectionUtils.isNotEmpty(appResults)?
                appResults.stream().map(WhitelistEffectiveAppResult::getEffectiveAppName).collect(Collectors.toList()) : Lists.newArrayList());
        });
        return pagingList;
    }

    private List<String> getTags(List<String> list, WhiteListVO vo) {
        List<String> tags = Lists.newArrayList();
        // 是否重复
        if(isDuplicate(list,WhitelistUtil.buildWhiteId(vo.getInterfaceType(),vo.getInterfaceName()))) {
            tags.add(WhitelistTagEnum.DUPLICATE_NAME.getTagName());
            if(Boolean.parseBoolean(isCheckDuplicateName)) {
                vo.setIsGlobal(false);
            }
        }
        // 是否手工添加
        if(vo.getIsHandwork() != null && vo.getIsHandwork()) {
            tags.add(WhitelistTagEnum.MANUALLY_ADD.getTagName());
        }
        return tags;
    }

    private boolean isDuplicate(List<String> list,String buildWhiteId) {
        return CollectionUtils.isNotEmpty(list) && list.stream().filter(e -> e.equals(buildWhiteId)).count() > 1;
    }


    private String[] splitId(String id) {
        return id.split("@@");
    }

    @Override
    public List<InterfaceVo> getAllInterface(String appName) {
        ApplicationInterfaceQueryDTO query = new ApplicationInterfaceQueryDTO();
        query.setAppName(appName);
        List<ApplicationInterfaceDTO> applicationInterfaceDTOS = applicationClient.listInterfaces(query);
        if (CollectionUtils.isEmpty(applicationInterfaceDTOS)) {
            return Lists.newArrayList();
        }
        return applicationInterfaceDTOS.stream().map(item -> {
            InterfaceVo interfaceVo = new InterfaceVo();
            interfaceVo.setId(item.getId());
            Integer interfaceType = getInterfaceIntType(item.getInterfaceType());
            interfaceVo.setInterfaceType(String.valueOf(interfaceType));
            interfaceVo.setInterfaceName(item.getInterfaceName());
            return interfaceVo;
        }).filter(
                // 从AMDB加载过来的白名单，过滤掉类型不支持的白名单--20210303 CYF
                interfaceVo -> !"-1".equals(interfaceVo.getInterfaceType())
        ).collect(Collectors.toList());
    }
}
