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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pamirs.tro.common.ResponseOk;
import com.pamirs.tro.common.exception.TROModuleException;
import com.pamirs.tro.entity.dao.confcenter.TApplicationMntDao;
import com.pamirs.tro.entity.dao.confcenter.TBListMntDao;
import com.pamirs.tro.entity.dao.confcenter.TWListMntDao;
import com.pamirs.tro.entity.dao.user.TUserMapper;
import com.pamirs.tro.entity.domain.entity.TBList;
import com.pamirs.tro.entity.domain.entity.user.User;
import com.pamirs.tro.entity.domain.query.whitelist.AgentWhiteList;
import com.pamirs.tro.entity.domain.vo.user.UserQueryParam;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.service.linkManage.WhiteListService;
import io.shulie.tro.web.common.util.whitelist.WhitelistUtil;
import io.shulie.tro.web.common.vo.agent.AgentBlacklistVO;
import io.shulie.tro.web.data.dao.application.ApplicationDAO;
import io.shulie.tro.web.data.dao.application.WhiteListDAO;
import io.shulie.tro.web.data.dao.application.WhitelistEffectiveAppDao;
import io.shulie.tro.web.data.dao.blacklist.BlackListDAO;
import io.shulie.tro.web.data.param.application.ApplicationQueryParam;
import io.shulie.tro.web.data.param.whitelist.WhitelistEffectiveAppSearchParam;
import io.shulie.tro.web.data.param.whitelist.WhitelistSearchParam;
import io.shulie.tro.web.data.result.application.ApplicationDetailResult;
import io.shulie.tro.web.data.result.blacklist.BlacklistResult;
import io.shulie.tro.web.data.result.whitelist.WhitelistEffectiveAppResult;
import io.shulie.tro.web.data.result.whitelist.WhitelistResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author: mubai
 * @Date: 2020-04-20 19:16
 * @Description:
 */

@Service
@Slf4j
public class WhiteListFileService {

    @Value("${spring.config.whiteListPath}")
    private String whiteListPath;

    @Autowired
    private TBListMntDao tbListMntDao;

    @Autowired
    private TUserMapper userMapper;

    @Resource
    private TWListMntDao tWListMntDao;

    @Autowired
    private TApplicationMntDao applicationMntDao;

    @Autowired
    private BlackListDAO blackListDAO;

    @Autowired
    private ApplicationDAO applicationDAO;

    @Autowired
    private WhiteListDAO whiteListDAO;
    @Autowired
    private WhitelistEffectiveAppDao whitelistEffectiveAppDao;

    @Autowired
    private WhiteListService whiteListService;

    /**
     * 是否开启校验白名单重名
     */
    @Value("${whitelist.duplicate.name.check:false}")
    private String isCheckDuplicateName;

    @PostConstruct
    public void init() {
        UserQueryParam userQueryParam = new UserQueryParam();
        //状态为启用
        userQueryParam.setStatus(0);
        //类型为管理员
        userQueryParam.setUserType(0);
        List<User> users = userMapper.selectAllUser(userQueryParam);
        if (CollectionUtils.isNotEmpty(users)) {
            for (User user : users) {
                writeWhiteListFile(user.getId(), user.getKey());
            }
        }
    }

    public void writeWhiteListFile() {
        writeWhiteListFile(null, null);
    }

    public void writeWhiteListFile(Long id, String key) {
        try {
            if (RestContext.getUser() != null) {
                id = RestContext.getUser().getCustomerId();
                key = RestContext.getUser().getCustomerKey();
            }
            Map<String, Object> result = queryBWList("", id);
            if (null != result && result.size() > 0) {
                File file = new File(whiteListPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                /*
                 * 白名单写入租户key
                 */
                if (file.exists()) {
                    file = new File(whiteListPath + key);
                    if (!file.isFile()) {
                        file.createNewFile();
                    }
                }

                ResponseOk.ResponseResult response = ResponseOk.result(result);
                String content = JSONObject.toJSONString(response);
                FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), false);
                BufferedWriter bufferWritter = new BufferedWriter(fileWriter);
                bufferWritter.write(content);
                bufferWritter.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 说明: 查询白名单列表，只供agent拦截使用，只选择USE_YN=1的白名单
     *
     * @return 黑白名单列表
     * @author shulie
     */
    public Map<String, Object> queryBWList(String appName, Long uid) throws TROModuleException {
        // 新获取应用
        List<String> appIdList = applicationMntDao.queryIdsByNameAndTenant(Lists.newArrayList(), uid);
        // 白名单
        List<AgentWhiteList> agentWhites  = agentListWhitelist(appIdList);
        List<Long> ids = agentWhites.stream().map(AgentWhiteList::getWlistId).collect(Collectors.toList());
        List<AgentWhiteList> agentWhiteLists = agentWhites.stream().distinct().collect(Collectors.toList());
        Map<String, Object> resultMap = Maps.newHashMapWithExpectedSize(30);
        // 获取所有白名单，是否有局部属性
        Boolean isCheckFlag = Boolean.parseBoolean(isCheckDuplicateName);
        List<String> existWhite = Lists.newArrayList();
        Map<String,List<WhitelistResult>> whitelistMap = Maps.newHashMap();

        if(isCheckFlag) {
            List<String> armdString = agentWhiteLists.stream().map(AgentWhiteList::getInterfaceName).collect(Collectors.toList());
            existWhite = whiteListService.getExistWhite(armdString, Lists.newArrayList());
            // todo 这里再获取一次，感觉很多余，但是不改上面的逻辑，所有这里数据再次从新获取，之后可以重构下
            WhitelistSearchParam param = new WhitelistSearchParam();
            param.setCustomerId(uid);
            param.setUseYn(1);
            List<WhitelistResult> results = whiteListDAO.getList(param);
            whitelistMap = results.stream().collect(Collectors.groupingBy(e -> e.getInterfaceName() + "@@" + e.getType()));
        }else {
            // 获取所有白名单，是否有全局属性
            WhitelistSearchParam param = new WhitelistSearchParam();
            param.setCustomerId(uid);
            param.setIsGlobal(true);
            param.setUseYn(1);
            List<WhitelistResult> results = whiteListDAO.getList(param);
            whitelistMap = results.stream()
                .collect(Collectors.groupingBy(e -> WhitelistUtil.buildWhiteId(e.getType(),e.getInterfaceName())));
        }

        // 获取所有生效效应，是否有局部应用
        WhitelistEffectiveAppSearchParam searchParam = new WhitelistEffectiveAppSearchParam();
        searchParam.setCustomerId(uid);
        // 只加开启的生效应用
        searchParam.setWlistIds(ids);
        List<WhitelistEffectiveAppResult> appResults = whitelistEffectiveAppDao.getList(searchParam);
        Map<String,List<WhitelistEffectiveAppResult>> appResultsMap = appResults.stream()
            .collect(Collectors.groupingBy(e -> WhitelistUtil.buildWhiteId(e.getType(),e.getInterfaceName())));

        List<Map<String, Object>> wListsResult = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(agentWhiteLists)) {
            for (AgentWhiteList agentWhiteList : agentWhiteLists) {
                String type = agentWhiteList.getType();
                String interfaceName = agentWhiteList.getInterfaceName();
                // 是否有全局
                String id = WhitelistUtil.buildWhiteId(agentWhiteList.getSourceType(),interfaceName);
                if ("2".equals(type)) {
                    if (StringUtils.contains(interfaceName, "#")) {
                        interfaceName = StringUtils.substringBefore(interfaceName, "#");
                    }
                }
                Map<String, Object> whiteItemNew = new HashMap<String, Object>();
                whiteItemNew.put("TYPE", type);
                whiteItemNew.put("INTERFACE_NAME", interfaceName);
                if(isCheckFlag) {
                    if(existWhite.stream().filter(e -> e.equals(id)).count() == 1) {
                        List<WhitelistResult> list = whitelistMap.get(id);
                        if(CollectionUtils.isNotEmpty(list)) {
                            whiteItemNew.put("isGlobal", list.get(0).getIsGlobal());
                        }else {
                            whiteItemNew.put("isGlobal", true);
                        }
                    }else if(existWhite.stream().filter(e -> e.equals(id)).count() > 1) {
                        whiteItemNew.put("isGlobal", false);
                    }else {
                        whiteItemNew.put("isGlobal", true);
                    }

                }else {
                    // 是否有全局
                    List<WhitelistResult> list = whitelistMap.get(id);
                    whiteItemNew.put("isGlobal",CollectionUtils.isNotEmpty(list));
                }
                //生效应用
                List<WhitelistEffectiveAppResult> appLists = appResultsMap.get(id);
                whiteItemNew.put("appNames",CollectionUtils.isNotEmpty(appLists)?
                    appLists.stream().map(WhitelistEffectiveAppResult::getEffectiveAppName).distinct().collect(Collectors.toList())
                    : Lists.newArrayList());
                wListsResult.add(whiteItemNew);
            }
        }
        // 新版黑名单
        List<AgentBlacklistVO> newBlacklist = getNewBlackList(appIdList);
        // 老版黑名单
        List<Map<String, Object>> blackList = getBlackList(uid);

        resultMap.put("wLists", wListsResult);
        resultMap.put("bLists", blackList);
        resultMap.put("newBlists", newBlacklist);
        return resultMap;
    }


    private List<Map<String, Object>> getBlackList(Long uid) {
        List<TBList> tbLists = tbListMntDao.getAllEnabledBlockList();
        if (CollectionUtils.isEmpty(tbLists)) {
            return Lists.newArrayList();
        }
        return tbLists.stream().map(tbList -> {
            Map<String, Object> map = new HashMap<>();
            map.put("REDIS_KEY", tbList.getRedisKey());
            return map;
        }).collect(Collectors.toList());
    }

    private List<AgentBlacklistVO> getNewBlackList(List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
        List<Long> appIds = list.stream().filter(Objects::nonNull).map(Long::valueOf).collect(Collectors.toList());
        List<BlacklistResult> results = blackListDAO.getAllEnabledBlockList(appIds);
        ApplicationQueryParam param = new ApplicationQueryParam();
        if (CollectionUtils.isEmpty(results)) {
            return Lists.newArrayList();
        }
        List<ApplicationDetailResult> detailResults = applicationDAO.getApplicationList(param);

        Map<Long,List<BlacklistResult>> redisMap = results.stream()
            .collect(Collectors.groupingBy(BlacklistResult::getApplicationId));

        Map<Long,List<ApplicationDetailResult>> detailResultMap = detailResults.stream()
            .collect(Collectors.groupingBy(ApplicationDetailResult::getApplicationId));
        List<AgentBlacklistVO> vos = Lists.newArrayList();
        for(Long id :redisMap.keySet()) {
            List<ApplicationDetailResult> app = detailResultMap.get(id);
            if(CollectionUtils.isEmpty(app)) {
                continue;
            }
            AgentBlacklistVO vo = new AgentBlacklistVO();
            vo.setAppName(app.get(0).getApplicationName());
            List<BlacklistResult> blacklist = redisMap.get(id);
            if(CollectionUtils.isNotEmpty(blacklist)) {
                vo.setBlacklists(blacklist.stream().map(BlacklistResult::getRedisKey).collect(Collectors.toList()));
            }else {
                vo.setBlacklists(Lists.newArrayList());
            }
            vos.add(vo);
        }
        return vos;
    }

    private List<AgentWhiteList> agentListWhitelist(List<String> list ) {
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
        List<Map<String, Object>> maps = tWListMntDao.getWListByAppIds(list);
        return maps.stream().map(it -> {
                AgentWhiteList whiteListDTO = new AgentWhiteList();
                whiteListDTO.setInterfaceName((String)it.get("interfaceName"));
                whiteListDTO.setType(getType(Integer.parseInt((String)it.get("type"))));
                whiteListDTO.setSourceType((String)it.get("type"));
                // 过滤生效应用用
                whiteListDTO.setWlistId((Long)it.get("wlistId"));
                return whiteListDTO;
            }
        ).collect(Collectors.toList());
    }

    private String getType(int dbType) {
        String type;
        switch (dbType) {
            case 1:
                type = "http";
                break;
            case 2:
                type = "dubbo";
                break;
            case 3:
                type = "rabbitmq";
                break;
            default:
                type = "unknow";
                break;
        }
        return type;
    }

}
