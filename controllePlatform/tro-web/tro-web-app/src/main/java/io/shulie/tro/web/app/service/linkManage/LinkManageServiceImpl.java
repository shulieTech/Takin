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

package io.shulie.tro.web.app.service.linkManage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.pamirs.tro.common.util.DateUtils;
import com.pamirs.tro.entity.dao.confcenter.TApplicationMntDao;
import com.pamirs.tro.entity.dao.linkguard.TLinkGuardMapper;
import com.pamirs.tro.entity.dao.linkmanage.TBusinessLinkManageTableMapper;
import com.pamirs.tro.entity.dao.linkmanage.TLinkManageTableMapper;
import com.pamirs.tro.entity.dao.linkmanage.TMiddlewareInfoMapper;
import com.pamirs.tro.entity.dao.linkmanage.TMiddlewareLinkRelateMapper;
import com.pamirs.tro.entity.dao.linkmanage.TSceneLinkRelateMapper;
import com.pamirs.tro.entity.dao.linkmanage.TSceneMapper;
import com.pamirs.tro.entity.domain.dto.EntranceSimpleDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.BusinessActiveIdAndNameDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.BusinessActiveViewListDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.BusinessFlowDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.BusinessFlowIdAndNameDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.BusinessLinkDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.EntranceDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.ExistBusinessActiveDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.MiddleWareNameDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.MiddleWareVersionDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.SceneDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.SystemProcessIdAndNameDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.SystemProcessViewListDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.TechLinkDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.TopologicalGraphEntity;
import com.pamirs.tro.entity.domain.dto.linkmanage.TopologicalGraphNode;
import com.pamirs.tro.entity.domain.dto.linkmanage.TopologicalGraphRelation;
import com.pamirs.tro.entity.domain.dto.linkmanage.TopologicalGraphVo;
import com.pamirs.tro.entity.domain.dto.linkmanage.linkstatistics.ApplicationRemoteDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.linkstatistics.BusinessCoverDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.linkstatistics.LinkHistoryInfoDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.linkstatistics.LinkRemarkDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.linkstatistics.LinkRemarkmiddleWareDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.linkstatistics.SystemProcessDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.mapping.LinkDomainEnumMapping;
import com.pamirs.tro.entity.domain.dto.linkmanage.mapping.enums.LinkDomainEnum;
import com.pamirs.tro.entity.domain.dto.linkmanage.mapping.enums.MiddlewareTypeEnum;
import com.pamirs.tro.entity.domain.dto.linkmanage.mapping.enums.NodeClassEnum;
import com.pamirs.tro.entity.domain.entity.linkmanage.BusinessLinkManageTable;
import com.pamirs.tro.entity.domain.entity.linkmanage.LinkManageTable;
import com.pamirs.tro.entity.domain.entity.linkmanage.LinkQueryVo;
import com.pamirs.tro.entity.domain.entity.linkmanage.Scene;
import com.pamirs.tro.entity.domain.entity.linkmanage.SceneAndBusinessLink;
import com.pamirs.tro.entity.domain.entity.linkmanage.SceneLinkRelate;
import com.pamirs.tro.entity.domain.entity.linkmanage.TMiddlewareInfo;
import com.pamirs.tro.entity.domain.entity.linkmanage.figure.LinkEdge;
import com.pamirs.tro.entity.domain.entity.linkmanage.statistics.StatisticsQueryVo;
import com.pamirs.tro.entity.domain.entity.linkmanage.structure.Category;
import com.pamirs.tro.entity.domain.entity.user.User;
import com.pamirs.tro.entity.domain.vo.linkmanage.BusinessFlowTree;
import com.pamirs.tro.entity.domain.vo.linkmanage.BusinessFlowVo;
import com.pamirs.tro.entity.domain.vo.linkmanage.BusinessLinkVo;
import com.pamirs.tro.entity.domain.vo.linkmanage.MiddleWareEntity;
import com.pamirs.tro.entity.domain.vo.linkmanage.TechLinkVo;
import com.pamirs.tro.entity.domain.vo.linkmanage.queryparam.BusinessQueryVo;
import com.pamirs.tro.entity.domain.vo.linkmanage.queryparam.SceneQueryVo;
import com.pamirs.tro.entity.domain.vo.linkmanage.queryparam.TechQueryVo;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.constant.BizOpConstants;
import io.shulie.tro.web.app.context.OperationLogContextHolder;
import io.shulie.tro.web.app.controller.linkmanage.DictionaryCache;
import io.shulie.tro.web.app.response.application.AgentPluginSupportResponse;
import io.shulie.tro.web.app.response.application.ApplicationDetailResponse;
import io.shulie.tro.web.app.response.linkmanage.BusinessActivityNameResponse;
import io.shulie.tro.web.app.response.linkmanage.BusinessLinkResponse;
import io.shulie.tro.web.app.response.linkmanage.MiddleWareResponse;
import io.shulie.tro.web.app.response.linkmanage.TechLinkResponse;
import io.shulie.tro.web.app.service.agent.AgentPluginSupportService;
import io.shulie.tro.web.app.utils.CategoryUtils;
import io.shulie.tro.web.app.utils.PageUtils;
import io.shulie.tro.web.auth.api.UserService;
import io.shulie.tro.web.data.dao.application.ApplicationDAO;
import io.shulie.tro.web.data.dao.linkmanage.BusinessLinkManageDAO;
import io.shulie.tro.web.data.dao.linkmanage.LinkManageDAO;
import io.shulie.tro.web.data.dao.linkmanage.SceneDAO;
import io.shulie.tro.web.data.model.mysql.AgentPluginEntity;
import io.shulie.tro.web.data.model.mysql.AgentPluginLibSupportEntity;
import io.shulie.tro.web.data.param.linkmanage.BusinessLinkManageCreateParam;
import io.shulie.tro.web.data.param.linkmanage.BusinessLinkManageQueryParam;
import io.shulie.tro.web.data.param.linkmanage.LinkManageCreateParam;
import io.shulie.tro.web.data.param.linkmanage.LinkManageQueryParam;
import io.shulie.tro.web.data.param.linkmanage.SceneCreateParam;
import io.shulie.tro.web.data.param.linkmanage.SceneQueryParam;
import io.shulie.tro.web.data.result.application.ApplicationResult;
import io.shulie.tro.web.data.result.application.LibraryResult;
import io.shulie.tro.web.data.result.linkmange.BusinessLinkResult;
import io.shulie.tro.web.data.result.linkmange.LinkManageResult;
import io.shulie.tro.web.data.result.linkmange.SceneResult;
import io.shulie.tro.web.data.result.linkmange.TechLinkResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @Auther: vernon
 * @Date: 2019/11/29 14:43
 * @Description:
 */
@Component
public class LinkManageServiceImpl implements LinkManageService {

    private static Logger logger = LoggerFactory.getLogger(LinkManageServiceImpl.class);
    private static List<AgentPluginEntity> agentPluginList = Lists.newArrayList();
    private static List<AgentPluginLibSupportEntity> agentPluginLibSupportList = Lists.newArrayList();
    private static String FALSE_CORE = "0";
    //事物
    @Resource(name = "transactionManager")
    DataSourceTransactionManager transactionManager;
    //技术链路管理表
    @Resource
    private TLinkManageTableMapper TLinkManageTableMapper;
    //业务链路管理表
    @Resource
    private TBusinessLinkManageTableMapper TBusinessLinkManageTableMapper;
    //场景链路关联表
    @Resource
    private TSceneLinkRelateMapper TSceneLinkRelateMapper;
    //场景表
    @Resource
    private TSceneMapper TSceneMapper;
    @Resource
    private TLinkGuardMapper TLinkGuardMapper;
    //中间件信息
    @Resource
    private TMiddlewareInfoMapper TMiddlewareInfoMapper;
    //中间件链路关联
    @Resource
    private TMiddlewareLinkRelateMapper TMiddlewareLinkRelateMapper;
    @Resource
    private LinkManage linkManage;
    @Resource
    private TApplicationMntDao tApplicationMntDao;
    @Resource
    private AgentPluginSupportService agentPluginSupportService;
    @Autowired
    private BusinessLinkManageDAO businessLinkManageDAO;
    @Autowired
    private ApplicationDAO applicationDAO;
    @Autowired
    private LinkManageDAO linkManageDAO;
    @Autowired
    private TSceneLinkRelateMapper tSceneLinkRelateMapper;
    @Autowired
    private SceneDAO sceneDAO;
    @Autowired
    private UserService userService;

    private static void iteratorChildNodes(TopologicalGraphNode parentNode,
        List<Category> childList,
        List<TopologicalGraphNode> nodes,
        List<TopologicalGraphRelation> relations) {
        if (CollectionUtils.isEmpty(childList)) {
            return;
        }
        List<Category> filterChildList = childList.stream().filter(distinctByName(c -> c.getApplicationName())).collect(
            Collectors.toList());
        int index = 0;
        for (int i = 0; i < filterChildList.size(); i++) {
            TopologicalGraphNode childNode = new TopologicalGraphNode();
            childNode.setKey(parentNode.getKey() + "." + index);
            NodeClassEnum nodeClassEnum = getNodeClassEnumByApplicationName(
                filterChildList.get(i).getApplicationName());
            MiddlewareTypeEnum middlewareTypeEnum = getMiddlewareTypeEnumByApplicationName(
                filterChildList.get(i).getApplicationName());
            childNode.setNodeType(nodeClassEnum.getCode());
            childNode.setNodeClass(nodeClassEnum.getDesc());
            if (middlewareTypeEnum != null) {
                childNode.setMiddlewareType(middlewareTypeEnum.getCode());
                childNode.setMiddlewareName(middlewareTypeEnum.getDesc());
            }
            childNode.setNodeName(filterChildList.get(i).getApplicationName());
            childNode.setNodeList(filterChildList.get(i).getNodeList());
            childNode.setUnKnowNodeList(filterChildList.get(i).getUnKnowNodeList());
            nodes.add(childNode);
            TopologicalGraphRelation relation = new TopologicalGraphRelation();
            relation.setFrom(parentNode.getKey());
            relation.setTo(childNode.getKey());
            relations.add(relation);
            if (CollectionUtils.isNotEmpty(filterChildList.get(i).getChildren())) {
                iteratorChildNodes(childNode, filterChildList.get(i).getChildren(), nodes, relations);
            }
            index++;
        }
    }

    public static NodeClassEnum getNodeClassEnumByApplicationName(String applicationName) {
        switch (applicationName) {
            case "DB中间件":
            case "MYSQL":
            case "MYSQL中间件":
            case "ORACLE":
            case "ORACLE中间件":
            case "SQLSERVER":
            case "SQLSERVER中间件":
            case "CASSANDRA":
            case "CASSANDRA中间件":
            case "HBASE":
            case "HBASE中间件":
            case "MONGODB":
            case "MONGODB中间件":
                return NodeClassEnum.DB;
            case "ELASTICSEARCH":
            case "ELASTICSEARCH中间件":
                return NodeClassEnum.ES;
            case "REDIS":
            case "REDIS中间件":
            case "MEMCACHE":
            case "MEMCACHE中间件":
                return NodeClassEnum.CACHE;
            case "ROCKETMQ":
            case "ROCKETMQ中间件":
            case "KAFKA":
            case "KAFKA中间件":
            case "ACTIVEMQ":
            case "ACTIVEMQ中间件":
            case "RABBITMQ":
            case "RABBITMQ中间件":
                return NodeClassEnum.MQ;
            case "DUBBO":
            case "DUBBO中间件":
                return NodeClassEnum.APP;
            default:
                if (applicationName.contains("未知")) {
                    return NodeClassEnum.UNKNOWN;
                }
                return NodeClassEnum.APP;
        }
    }

    public static MiddlewareTypeEnum getMiddlewareTypeEnumByApplicationName(String applicationName) {
        switch (applicationName) {
            case "MYSQL":
            case "MYSQL中间件":
                return MiddlewareTypeEnum.MySQL;
            case "ORACLE":
            case "ORACLE中间件":
                return MiddlewareTypeEnum.Oracle;
            case "SQLSERVER":
            case "SQLSERVER中间件":
                return MiddlewareTypeEnum.SQLServer;
            case "CASSANDRA":
            case "CASSANDRA中间件":
                return MiddlewareTypeEnum.Cassandra;
            case "HBASE":
            case "HBASE中间件":
                return MiddlewareTypeEnum.HBase;
            case "MONGODB":
            case "MONGODB中间件":
                return MiddlewareTypeEnum.MongoDB;
            case "ELASTICSEARCH":
            case "ELASTICSEARCH中间件":
                return MiddlewareTypeEnum.Elasticsearch;
            case "REDIS":
            case "REDIS中间件":
                return MiddlewareTypeEnum.Redis;
            case "MEMCACHE":
            case "MEMCACHE中间件":
                return MiddlewareTypeEnum.Memcache;
            case "ROCKETMQ":
            case "ROCKETMQ中间件":
                return MiddlewareTypeEnum.RocketMQ;
            case "KAFKA":
            case "KAFKA中间件":
                return MiddlewareTypeEnum.Kafka;
            case "ACTIVEMQ":
            case "ACTIVEMQ中间件":
                return MiddlewareTypeEnum.ActiveMQ;
            case "RABBITMQ":
            case "RABBITMQ中间件":
                return MiddlewareTypeEnum.RabbitMQ;
            case "DUBBO":
            case "DUBBO中间件":
                return MiddlewareTypeEnum.Dubbo;
            default:
                return null;
        }
    }

    static <T> Predicate<T> distinctByName(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @Override
    public TechLinkResponse fetchLink(String applicationName, String entrance) {
        //TechLinkResponse techLinkResponse = linkManage.getApplicationLink(entrance, applicationName);
        //获取中间件
        //return techLinkResponse;
        return null;
    }

    @Override
    public TopologicalGraphVo fetchGraph(String body) {
        TopologicalGraphVo topologicalGraphVo = new TopologicalGraphVo();
        if (StringUtils.isNotBlank(body)) {
            Category category = JSON.parseObject(body, Category.class);
            List<TopologicalGraphNode> nodes = new ArrayList<>();
            List<TopologicalGraphRelation> relations = new ArrayList<>();

            TopologicalGraphNode userNode = new TopologicalGraphNode();
            userNode.setKey("0");
            userNode.setNodeType(NodeClassEnum.OTHER.getCode());
            userNode.setNodeClass(NodeClassEnum.OTHER.getDesc());
            userNode.setNodeName("user");
            nodes.add(userNode);

            TopologicalGraphNode rootNode = new TopologicalGraphNode();
            rootNode.setKey("1");
            rootNode.setNodeType(NodeClassEnum.APP.getCode());
            rootNode.setNodeClass(NodeClassEnum.APP.getDesc());
            rootNode.setNodeName(category.getApplicationName());
            nodes.add(rootNode);

            TopologicalGraphRelation rootRelation = new TopologicalGraphRelation();
            rootRelation.setFrom(userNode.getKey());
            rootRelation.setTo(rootNode.getKey());
            relations.add(rootRelation);

            List<Category> childList = category.getChildren();
            iteratorChildNodes(rootNode, childList, nodes, relations);
            topologicalGraphVo.setGraphNodes(nodes);
            topologicalGraphVo.setGraphRelations(relations);
        }
        return topologicalGraphVo;
    }

    @Override
    public List<EntranceDto> fetchApp() {

        List<EntranceDto> result = new ArrayList<>();
        List<Map<String, Object>> mapList = tApplicationMntDao.queryApplicationdata();
        if (mapList == null || mapList.size() == 0) {
            return result;
        }
        for (Map<String, Object> map : mapList) {
            String applicationName = "applicationName";
            if (map.containsKey(applicationName)) {
                EntranceDto entranceDto = new EntranceDto();
                entranceDto.setLabel(String.valueOf(map.get(applicationName)));
                entranceDto.setValue(String.valueOf(map.get(applicationName)));
                result.add(entranceDto);
            }
        }
        return result;
    }

    public List<EntranceDto> fetchAllEntrance(String appName) {

        List<EntranceDto> result = Lists.newArrayList();
        List<LinkEdge> linkEdges = Lists.newArrayList();
        Map<String, List<LinkEdge>> group =
            linkEdges.stream().collect(
                Collectors.groupingBy(LinkEdge::getApplicationName));

        for (Map.Entry entry : group.entrySet()) {
            EntranceDto dto = new EntranceDto();

            String applicationName = entry.getKey().toString();
            dto.setLabel(applicationName);
            dto.setValue(applicationName);
            List<EntranceDto> children = Lists.newArrayList();
            if (entry.getValue() != null) {
                List<LinkEdge> values = (List<LinkEdge>)entry.getValue();

                values.stream().forEach(
                    value -> {
                        EntranceDto child = new EntranceDto();
                        String entrance
                            = LinkManage.generateEntrance(value);
                        child.setLabel(entrance);
                        child.setValue(entrance);
                        children.add(child);
                    }
                );
            }
            dto.setChildren(children);
            result.add(dto);
        }
        return result;
    }

    /**
     * 暴露给前端对入口的模糊匹配
     *
     * @param applicationName
     * @return
     */
    @Override
    public List<EntranceDto> fetchEntrance(String applicationName) {

        //    List<EntranceDto> allEntrance = fetchAllEntrance();

        List<EntranceDto> allEntrance = fetchAllEntrance(applicationName);
       /* if (StringUtils.isNotBlank(str)) {
            allEntrance = allEntrance.stream().filter(
                    entrance -> entrance.contains(str)
            ).collect(Collectors.toList());
        }*/

        return allEntrance;

    }

    /**
     * @param id      主键Id
     * @param newName 新的系统流程名字
     * @return
     */
    private Response confirmChange(String id, String newName) {
        if (id == null) {
            return Response.fail(FALSE_CORE, "id不能为空", null);
        }
        LinkManageTable oldTable = TLinkManageTableMapper.selectByPrimaryKey(Long.parseLong(id));
        if (oldTable != null) {

            LinkManageTable newTable = new LinkManageTable();
            BeanUtils.copyProperties(oldTable, newTable);
            newTable.setChangeBefore(newTable.getChangeAfter());
            newTable.setIsChange(0);
            newTable.setChangeType(0);
            newTable.setUpdateTime(new java.util.Date());
            newTable.setChangeAfter(null);
            newTable.setChangeRemark(null);
            if (StringUtils.isNotBlank(newName)) {
                newTable.setLinkName(newName);
            }
            try {
                TLinkManageTableMapper.updateByPrimaryKey(newTable);
                confirmBussinessLinkByTechId(newTable);
                modifySceneIschange(id);
                return Response.success();
            } catch (Exception e) {
                return Response.fail(FALSE_CORE, "确认变更失败", null);
            }
        }
        return null;
    }

    /**
     * 变更的时候确认业务链路的变更
     *
     * @param techTable
     * @return
     */
    private int confirmBussinessLinkByTechId(LinkManageTable techTable) {
        BusinessLinkManageTable businessLinkManageTable = new BusinessLinkManageTable();
        businessLinkManageTable.setEntrace(techTable.getEntrace());
        businessLinkManageTable.setIsDeleted(0);
        businessLinkManageTable.setIsChange(0);
        businessLinkManageTable.setRelatedTechLink(String.valueOf(techTable.getLinkId()));
        return TBusinessLinkManageTableMapper.updateByTechId(businessLinkManageTable);
    }

    /**
     * 在链路表中 修改了ischange状态后，更新scene表的ischange状态
     *
     * @param id 技术链路
     */
    private void modifySceneIschange(String id) {
        //找是否有关联的场景
        List<SceneAndBusinessLink> relatedScenes
            = TSceneLinkRelateMapper.selectSceneIdByTechLinkId(id);
        if (CollectionUtils.isEmpty(relatedScenes)) {
            //没有关联的场景
        } else {
            //看关联的场景下面是否的技术链路是否都没有变化，如果是，给场景的ischange置为0
            //按照和业务链路集合分组

            Map<Long, List<SceneAndBusinessLink>> maps = relatedScenes
                .stream()
                .collect(Collectors.groupingBy(SceneAndBusinessLink::getSceneId));

            //分别校验各个场景下面挂靠的业务场景的ischange是否都为0，如果为0，则将场景的ischange置为0
            List<Long> changeToZeroSceneIdList = new ArrayList<>();
            List<Long> changToOneSceneIdList = new ArrayList<>();
            maps.keySet().stream().forEach(
                //key为场景名字
                key -> {
                    //当前场景下的所有业务链路名集合
                    List<SceneAndBusinessLink> value = maps.get(key);
                    List<Long> techLinkIds = value
                        .stream()
                        .map(SceneAndBusinessLink::getTechId)
                        .collect(Collectors.toList());
                    //查看链路表中该场景下是否有变更的数据 大于0 有
                    int count = TLinkManageTableMapper.counItemtByTechLinkIds(techLinkIds);
                    if (count > 0) {
                        changToOneSceneIdList.add(key);
                    } else {
                        changeToZeroSceneIdList.add(key);
                    }
                }
            );
            //批量修改场景表中的状态
            if (CollectionUtils.isNotEmpty(changToOneSceneIdList)) {
                TSceneMapper.updateBySceneIdList(changToOneSceneIdList, 1);
            }
            if (CollectionUtils.isNotEmpty(changeToZeroSceneIdList)) {
                TSceneMapper.updateBySceneIdList(changeToZeroSceneIdList, 0);
            }
        }
    }

    @Override
    public Response storetechLink(TechLinkVo link) {

        //校验技术链路名不能重复
        int count = TLinkManageTableMapper.count(link.getLinkName());
        if (count > 0) {
            return Response.fail(FALSE_CORE, "不能重复添加技术链路", null);
        }
        int entranceNum = TLinkManageTableMapper.countByEntrance(link.getEntrance());
        if (entranceNum > 0) {
            return Response.fail(FALSE_CORE, "同一个入口只能添加一个系统流程", null);
        }

        LinkManageCreateParam createParam = new LinkManageCreateParam();
        String entrance = link.getEntrance();
        createParam.setEntrace(entrance);
        createParam.setLinkName(link.getLinkName());
        if (StringUtils.isNotBlank(link.getBody())) {
            List<Category> list = JSON.parseObject(link.getBody(), List.class);
            if (CollectionUtils.isNotEmpty(list)) {
                createParam.setChangeBefore(JSON.toJSONString(list.get(0)));
            }
        }
        createParam.setIsDeleted(0);
        createParam.setIsChange(0);
        createParam.setApplicationName(entrance.substring(0, entrance.indexOf("|")));
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            //插入链路信息
            linkManageDAO.insert(createParam);
            transactionManager.commit(status);
            return Response.success();
        } catch (Exception e) {
            transactionManager.rollback(status);
            logger.error("保存技术链路失败,{}", e.getMessage());
            return Response.fail(FALSE_CORE, "保存技术链路失败", null);
        }
    }

    @Override
    public Response deleteLink(String id) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            TLinkManageTableMapper.logicDeleteByPrimaryKey(Long.parseLong(id));
            //删除中间件关联表中的关联
            TMiddlewareLinkRelateMapper.deleteByTechLinkId(id);
            transactionManager.commit(status);
            return Response.success();
        } catch (Exception e) {
            transactionManager.rollback(status);
            return Response.fail(FALSE_CORE, "删除链路失败", null);
        }
    }

    @Transactional
    @Override
    public Response modifyLink(TechLinkVo link) {
        if (link == null || link.getLinkId() == null) {
            throw new RuntimeException("id不能为空");
        }

        Long linkId = Long.parseLong(link.getLinkId());
        String frontEntrance = link.getEntrance();
        try {
            LinkManageTable oldTable =
                TLinkManageTableMapper.selectByPrimaryKey(linkId);
            //校验技术链路名不能重复
            if (!StringUtils.equals(oldTable.getLinkName(), link.getLinkName())) {
                int count = TLinkManageTableMapper.count(link.getLinkName());
                if (count > 0) {
                    return Response.fail(FALSE_CORE, "不能重复添加技术链路", null);
                }
            }
            String oldEntrance = oldTable.getEntrace();

            if (StringUtils.equals(oldEntrance, frontEntrance)) {
                //没有改变入口，判断是否是变更状态,如果变更了,则确认变更
                String isChange = String.valueOf(oldTable.getIsChange());
                if ("1".equals(isChange)) {
                    confirmChange(String.valueOf(linkId), link.getLinkName());
                } else {
                    // 在入口没有改变的情况下,只允许更新系统流程的名字
                    String newName = link.getLinkName();
                    LinkManageTable newTable = new LinkManageTable();
                    newTable.setLinkName(newName);
                    newTable.setLinkId(linkId);
                    if (StringUtils.isNotBlank(link.getBody())) {
                        List<Category> list = JSON.parseObject(link.getBody(), List.class);
                        if (CollectionUtils.isNotEmpty(list)) {
                            newTable.setChangeBefore(JSON.toJSONString(list.get(0)));
                        }
                    }
                    TLinkManageTableMapper.updateByPrimaryKeySelective(newTable);
                }
            } else {

                // 同一个入口只能添加一个系统流程
                int entranceNum = TLinkManageTableMapper.countByEntrance(link.getEntrance());
                if (entranceNum > 0) {
                    return Response.fail(FALSE_CORE, "同一个入口只能添加一个系统流程", null);
                }

                //入口改变,先删除中间件关联表对于该原始入口的中间件信息,从图数据库从从新拉取入口,
                TMiddlewareLinkRelateMapper.deleteByTechLinkId(String.valueOf(link.getLinkId()));

                TechLinkResponse newSystemProcess = fetchLink(null, frontEntrance);
                String newEntrance = newSystemProcess.getEntrance();
                String new_body_before = newSystemProcess.getBody_before();
                String new_body_after = newSystemProcess.getBody_after();
                //String ApplicationName =
                LinkManageTable newTable = new LinkManageTable();
                newTable.setEntrace(newEntrance);
                newTable.setChangeBefore(new_body_before);
                newTable.setChangeAfter(new_body_after);
                newTable.setLinkId(linkId);
                newTable.setLinkName(link.getLinkName());
                newTable.setIsChange(0);
                newTable.setChangeType(0);
                TLinkManageTableMapper.updateByPrimaryKeySelective(newTable);
                //然后跟新中间件信息表
                //                List<MiddleWareEntity> middleWareEntities = newSystemProcess.getMiddleWareEntities();
                //                if (CollectionUtils.isNotEmpty(middleWareEntities)) {
                //                    //插入中间表信息并返回主键集合
                //                    Set<Long> middleWareIdSet = insertMiddleWare(middleWareEntities);
                //                    //更新关联信息
                //                    List<MiddlewareLinkRelate> bathInsert = Lists.newArrayListWithCapacity
                //                    (middleWareIdSet.size());
                //                    middleWareIdSet.stream().forEach(set -> {
                //                        MiddlewareLinkRelate middlewareLinkRelate = new MiddlewareLinkRelate();
                //                        middlewareLinkRelate.setTechLinkId(String.valueOf(link.getLinkId()));
                //                        middlewareLinkRelate.setMiddlewareId(String.valueOf(set));
                //                        bathInsert.add(middlewareLinkRelate);
                //
                //                    });
                //                    TMiddlewareLinkRelateMapper.batchInsert(bathInsert);
                //                }
                //然后更新业务活动和业务流程中的entrance
                //1.根据linkId(原始的系统流程Id号,这个不能改变) 更新业务活动的entrance
                int count = TBusinessLinkManageTableMapper.updateEntranceNameBySystemProcessId(link.getLinkId(),
                    newEntrance);

                //2.根据linkId(原始的系统流程Id号,这个不能改变) 更新业务流程链路关联表中的entrance
                TSceneLinkRelateMapper.updateEntranceNameBySystemProcessId(link.getLinkId(), newEntrance);
            }
            return Response.success();
        } catch (Exception e) {
            return Response.fail("0", e.getMessage(), null);
        }

    }

    @Override
    public Response<List<SystemProcessViewListDto>> gettechLinksViwList(TechQueryVo vo) {
        List<SystemProcessViewListDto> result = new ArrayList<>();
        LinkQueryVo queryVo = new LinkQueryVo();
        queryVo.setEntrance(vo.getEntrance());
        queryVo.setIsChange(vo.getIschange());
        queryVo.setMiddleWareName(vo.getMiddleWareName());
        queryVo.setMiddleWareType(vo.getMiddleWareType());
        queryVo.setMiddleWareVersion(vo.getMiddleWareVersion());
        queryVo.setName(vo.getLinkName());
        List<TechLinkDto> queryResult = TLinkManageTableMapper.selectTechLinkListBySelective2(queryVo);
        List<TechLinkDto> pageDate = PageUtils.getPage(true, vo.getCurrentPage(), vo.getPageSize(), queryResult);
        //用户ids
        List<Long> userIds = queryResult.stream().filter(data -> null != data.getUserId()).map(TechLinkDto::getUserId)
            .collect(Collectors.toList());
        //用户信息Map key:userId  value:user对象
        Map<Long, User> userMap = userService.getUserMapByIds(userIds);

        pageDate.stream().forEach(single -> {
            SystemProcessViewListDto dto = new SystemProcessViewListDto();
            dto.setLinkId(single.getLinkId());
            dto.setCandelete(single.getCandelete());
            dto.setCreateTime(single.getCreateTime());
            dto.setTechLinkName(single.getTechLinkName());
            dto.setIsChange(single.getIsChange());
            //负责人id
            dto.setManagerId(single.getUserId());
            //负责人名称
            String userName = Optional.ofNullable(userMap.get(single.getUserId()))
                .map(u -> u.getName())
                .orElse("");
            dto.setManagerName(userName);
            List<Long> allowUpdateUserIdList = RestContext.getUpdateAllowUserIdList();
            if (CollectionUtils.isNotEmpty(allowUpdateUserIdList)) {
                dto.setCanEdit(allowUpdateUserIdList.contains(dto.getManagerId()));
            }
            List<Long> allowDeleteUserIdList = RestContext.getDeleteAllowUserIdList();
            if (CollectionUtils.isNotEmpty(allowDeleteUserIdList)) {
                dto.setCanRemove(allowDeleteUserIdList.contains(dto.getManagerId()));
            }

            List<TMiddlewareInfo> middlewareInfos = TMiddlewareInfoMapper.selectBySystemProcessId(single.getLinkId());
            List<String> middleWareNames = middlewareInfos.stream().map(
                entity -> entity.getMiddlewareName() + " " + entity.getMiddlewareVersion()
            ).collect(Collectors.toList());
            dto.setMiddleWareEntities(middleWareNames);

            String before_body = single.getBody_before();
            if (null != before_body) {
                int count = LinkManage.getSubCount(before_body, "serviceName");
                dto.setTechLinkCount(String.valueOf(count));
            }
            dto.setChangeType(single.getChangeType());

            result.add(dto);
        });
        PageInfo<SystemProcessViewListDto> data = new PageInfo<>(result);
        data.setTotal(queryResult.size());
        return Response.success(data);
    }

    @Override
    public Response addBusinessLink(BusinessLinkVo link) {
        BusinessLinkManageCreateParam createParam = new BusinessLinkManageCreateParam();
        createParam.setLinkName(link.getLinkName());
        createParam.setIsCore(link.getIsCore());
        createParam.setLinkLevel(link.getLink_level());
        createParam.setIsDeleted(0);
        createParam.setBusinessDomain(link.getBusinessDomain());

        //重复校验
        Integer repeate = TBusinessLinkManageTableMapper.countByBussinessName(link.getLinkName());
        if (repeate != 0) {
            return Response.fail(FALSE_CORE, "业务活动名称不能重复", null);
        }
        String relatedTechLinkId = link.getRelatedTechLinkId();

        if (relatedTechLinkId == null) {
            Response.fail(FALSE_CORE, "请绑定系统流程", null);
        }
        createParam.setRelatedTechLink(relatedTechLinkId);
        LinkManageTable systemProcessInfo =
            TLinkManageTableMapper.selectByPrimaryKey(Long.parseLong(relatedTechLinkId));
        if (systemProcessInfo != null) {
            createParam.setEntrace(systemProcessInfo.getEntrace());
            createParam.setIsChange(systemProcessInfo.getIsChange());

        }
        createParam.setCanDelete(0);
        int count = businessLinkManageDAO.insert(createParam);
        //设置关联的系统流程不能被删除
        TLinkManageTableMapper.cannotdelete(Long.parseLong(relatedTechLinkId), 1L);
        //添加技术链路和中间件的绑定
        return count > 0 ? Response.success() : Response.fail(FALSE_CORE, "新增业务链路失败", null);
    }

    @Override
    public Response modifyBussinessLink(BusinessLinkVo link) {
        BusinessLinkManageTable insertTable = new BusinessLinkManageTable();
        insertTable.setLinkName(link.getLinkName());
        insertTable.setIsCore(link.getIsCore());
        insertTable.setLinkLevel(link.getLink_level());
        insertTable.setBusinessDomain(link.getBusinessDomain());
        insertTable.setIsDeleted(0);
        insertTable.setLinkId(Long.parseLong(link.getId()));
        String relatedTechLinkId = link.getRelatedTechLinkId();

        BusinessLinkManageTable oldTable = TBusinessLinkManageTableMapper.selectByPrimaryKey(
            Long.parseLong(link.getId()));
        if (!StringUtils.equals(oldTable.getLinkName(), link.getLinkName())) {
            //重复校验
            Integer repeate = TBusinessLinkManageTableMapper.countByBussinessName(link.getLinkName());
            if (repeate != 0) {
                return Response.fail(FALSE_CORE, "业务活动名称不能重复", null);
            }
        }

        if (null != relatedTechLinkId) {
            insertTable.setRelatedTechLink(relatedTechLinkId);
            LinkManageTable systemProcessInfo =
                TLinkManageTableMapper.selectByPrimaryKey(Long.parseLong(relatedTechLinkId));
            if (systemProcessInfo != null) {
                insertTable.setEntrace(systemProcessInfo.getEntrace());
            }
        }
        try {
            TBusinessLinkManageTableMapper.updateByPrimaryKeySelective(insertTable);
            return Response.success();
        } catch (Exception e) {

        }
        return Response.fail(FALSE_CORE, "修改业务链路失败", null);
    }

    @Override
    public Response getBussisnessLinks(BusinessQueryVo vo) {
        List<BusinessActiveViewListDto> result = Lists.newArrayList();
        LinkQueryVo queryVo = new LinkQueryVo();
        queryVo.setMiddleWareVersion(vo.getVersion());
        queryVo.setMiddleWareName(vo.getMiddleWareName());
        queryVo.setMiddleWareType(vo.getMiddleWareType());
        queryVo.setEntrance(vo.getEntrance());
        queryVo.setName(vo.getBusinessLinkName());
        queryVo.setIsChange(vo.getIschange());
        queryVo.setSystemProcessName(vo.getTechLinkName());
        queryVo.setDomain(vo.getDomain());
        List<BusinessLinkDto> queryResult = TBusinessLinkManageTableMapper.selectBussinessLinkListBySelective2(queryVo);
        //用户ids
        List<Long> userIds = queryResult.stream().filter(data -> null != data.getUserId()).map(
            BusinessLinkDto::getUserId).collect(Collectors.toList());
        //用户信息Map key:userId  value:user对象
        Map<Long, User> userMap = userService.getUserMapByIds(userIds);

        List<BusinessLinkDto> pageData = PageUtils.getPage(true, vo.getCurrentPage(), vo.getPageSize(), queryResult);
        if (CollectionUtils.isNotEmpty(pageData) && pageData.size() > 0) {
            pageData.stream().forEach(
                single -> {
                    BusinessActiveViewListDto dto = new BusinessActiveViewListDto();
                    dto.setBusinessActiceId(single.getId());
                    dto.setBusinessActiveName(single.getLinkName());
                    dto.setCandelete(single.getCandelete());
                    dto.setCreateTime(single.getCreateTime());
                    dto.setIschange(single.getIschange());
                    //负责人id
                    dto.setManagerId(single.getUserId());
                    //负责人name
                    String userName = Optional.ofNullable(userMap.get(single.getUserId()))
                        .map(u -> u.getName())
                        .orElse("");
                    dto.setManagerName(userName);
                    List<Long> allowUpdateUserIdList = RestContext.getUpdateAllowUserIdList();
                    if (CollectionUtils.isNotEmpty(allowUpdateUserIdList)) {
                        dto.setCanEdit(allowUpdateUserIdList.contains(dto.getManagerId()));
                    }
                    List<Long> allowDeleteUserIdList = RestContext.getDeleteAllowUserIdList();
                    if (CollectionUtils.isNotEmpty(allowDeleteUserIdList)) {
                        dto.setCanRemove(allowDeleteUserIdList.contains(dto.getManagerId()));
                    }
                    //新版本设置业务域
                    if (StringUtils.isNotBlank(single.getBusinessDomain())) {
                        String desc = DictionaryCache.getObjectByParam("domain",
                            Integer.parseInt(single.getBusinessDomain())).getLabel();
                        if (StringUtils.isNotBlank(desc)) {
                            dto.setBusinessDomain(desc);
                        } else {
                            //兼容历史版本
                            LinkDomainEnum domainEnum = LinkDomainEnumMapping.getByCode(single.getBusinessDomain());
                            dto.setBusinessDomain(domainEnum == null ? null : domainEnum.getDesc());
                        }
                    }
                    TechLinkDto techLinkDto = single.getTechLinkDto();

                    if (techLinkDto != null) {
                        List<TMiddlewareInfo> middlewareInfos =
                            TMiddlewareInfoMapper.selectBySystemProcessId(techLinkDto.getLinkId());
                        List<String> middleWareStrings = middlewareInfos
                            .stream()
                            .map(entity ->
                                entity.getMiddlewareName() + " " + entity.getMiddlewareVersion()
                            ).collect(Collectors.toList());
                        dto.setMiddleWareList(middleWareStrings);
                        dto.setSystemProcessName(single.getTechLinkDto().getTechLinkName());
                    }
                    result.add(dto);
                }
            );
        }
        return Response.success(result, CollectionUtils.isEmpty(queryResult) ? 0 : queryResult.size());
    }

    private void convertBusinessLinkResponse(BusinessLinkResult businessLinkResult,
        BusinessLinkResponse businessLinkResponse) {
        businessLinkResponse.setId(businessLinkResult.getId());
        businessLinkResponse.setLinkName(businessLinkResult.getLinkName());
        businessLinkResponse.setEntrance(businessLinkResult.getEntrance());
        businessLinkResponse.setIschange(businessLinkResult.getIschange());
        businessLinkResponse.setCreateTime(businessLinkResult.getCreateTime());
        businessLinkResponse.setUpdateTime(businessLinkResult.getUpdateTime());
        businessLinkResponse.setCandelete(businessLinkResult.getCandelete());
        businessLinkResponse.setIsCore(businessLinkResult.getIsCore());
        businessLinkResponse.setLinkLevel(businessLinkResult.getLinkLevel());
        businessLinkResponse.setBusinessDomain(businessLinkResult.getBusinessDomain());

        TechLinkResponse techLinkResponse = new TechLinkResponse();
        businessLinkResponse.setTechLinkResponse(techLinkResponse);
        TechLinkResult techLinkResult = businessLinkResult.getTechLinkResult();
        techLinkResponse.setLinkId(techLinkResult.getLinkId());
        techLinkResponse.setTechLinkName(techLinkResult.getTechLinkName());
        techLinkResponse.setIsChange(techLinkResult.getIsChange());
        techLinkResponse.setChange_remark(techLinkResult.getChangeRemark());
        techLinkResponse.setBody_before(techLinkResult.getBodyBefore());
        techLinkResponse.setBody_after(techLinkResult.getBodyAfter());
        techLinkResponse.setChangeType(techLinkResult.getChangeType());
    }

    @Override
    public BusinessLinkResponse getBussisnessLinkDetail(String id) {
        if (null == id) {
            throw new RuntimeException("主键不能为空");
        }
        LinkQueryVo queryVo = new LinkQueryVo();
        queryVo.setId(Long.parseLong(id));
        BusinessLinkResult businessLinkResult = businessLinkManageDAO.selectBussinessLinkById(Long.parseLong(id));

        if (Objects.nonNull(businessLinkResult)) {
            BusinessLinkResponse businessLinkResponse = new BusinessLinkResponse();
            convertBusinessLinkResponse(businessLinkResult, businessLinkResponse);
            if (businessLinkResponse.getTechLinkResponse() != null) {
                Long systemProcessId = businessLinkResponse.getTechLinkResponse().getLinkId();
                if (systemProcessId != null) {
                    LinkManageResult linkManageResult = linkManageDAO.selecLinkManageById(
                        businessLinkResult.getTechLinkResult().getLinkId());
                    businessLinkResponse.getTechLinkResponse().setMiddleWareResponses(
                        getMiddleWareResponses(linkManageResult.getApplicationName()));
                    //处理系统流程前端展示数据
                    TechLinkResponse techLinkResponse = businessLinkResponse.getTechLinkResponse();
                    String linkBody = null;
                    if (StringUtils.isNotBlank(techLinkResponse.getBody_after())) {
                        linkBody = techLinkResponse.getBody_after();
                    } else {
                        linkBody = techLinkResponse.getBody_before();
                    }
                    if (linkBody != null) {
                        Category category = JSON.parseObject(linkBody, Category.class);
                        CategoryUtils.assembleVo(category);
                        List<Category> list = new ArrayList<>();
                        list.add(category);
                        businessLinkResponse.getTechLinkResponse().setLinkNode(JSON.toJSONString(list));
                    }
                    TopologicalGraphEntity topologicalGraphEntity = new TopologicalGraphEntity();
                    if (StringUtils.isNotBlank(techLinkResponse.getBody_before())) {
                        TopologicalGraphVo topologicalGraphBeforeVo = fetchGraph(techLinkResponse.getBody_before());
                        topologicalGraphEntity.setTopologicalGraphBeforeVo(topologicalGraphBeforeVo);
                    }
                    if (StringUtils.isNotBlank(techLinkResponse.getBody_after())) {
                        TopologicalGraphVo topologicalGraphAfterVo = fetchGraph(techLinkResponse.getBody_after());
                        topologicalGraphEntity.setTopologicalGraphAfterVo(topologicalGraphAfterVo);
                    }
                    businessLinkResponse.getTechLinkResponse().setTopologicalGraphEntity(topologicalGraphEntity);
                }
            }
            return businessLinkResponse;
        }
        return new BusinessLinkResponse();
    }

    @Override
    public Response deleteScene(String sceneId) {
        //手动控制事物,减小事物的范围
        if (null == sceneId) {
            return Response.fail(FALSE_CORE, "primary key cannot be none.", null);
        }
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            TSceneMapper.deleteByPrimaryKey(Long.parseLong(sceneId));

            //取出关联的业务活动id是否可以被删除
            List<SceneLinkRelate> relates = TSceneLinkRelateMapper.selectBySceneId(Long.parseLong(sceneId));
            List<Long> businessLinkIds = relates.stream().map(relate -> {
                if (relate.getBusinessLinkId() != null) {
                    return Long.parseLong(relate.getBusinessLinkId());
                }
                return 0L;
            }).collect(Collectors.toList());

            //删除关联表
            TSceneLinkRelateMapper.deleteBySceneId(sceneId);
            //过滤出可以设置为删除状态的业务活动id并设置为可以删除
            enableBusinessActiveCanDelte(businessLinkIds);

            transactionManager.commit(status);
            return Response.success();
        } catch (Exception e) {
            transactionManager.rollback(status);
            return Response.fail(FALSE_CORE, "删除场景失败", e.getMessage());
        } finally {
        }
    }

    private void enableBusinessActiveCanDelte(List<Long> businessLinkIds) {
        if (CollectionUtils.isEmpty(businessLinkIds)) {
            return;
        }
        List<Long> candeletedList = businessLinkIds.stream()
            .map(single -> {
                long count = TSceneLinkRelateMapper.countByBusinessLinkId(single);
                if (!(count > 0)) {
                    return single;
                }
                return 0L;
            }).collect(Collectors.toList());

        TBusinessLinkManageTableMapper.cannotdelete(candeletedList, 0L);
    }

    @Override
    public Response<List<SceneDto>> getScenes(SceneQueryVo vo) {
        List<SceneDto> sceneDtos = TSceneMapper.selectByRelatedQuery(vo);
        List<SceneDto> pageData = PageUtils.getPage(true, vo.getCurrentPage(), vo.getPageSize(), sceneDtos);
        //用户ids
        List<Long> userIds = sceneDtos.stream().filter(data -> null != data.getManagerId()).map(SceneDto::getManagerId)
            .collect(Collectors.toList());
        //用户信息Map key:userId  value:user对象
        Map<Long, User> userMap = userService.getUserMapByIds(userIds);

        pageData = pageData.stream().map(single -> {
            int count = TSceneLinkRelateMapper.countBySceneId(single.getId());
            single.setTechLinkCount(count);
            single.setBusinessLinkCount(count);
            //负责人名称
            String userName = Optional.ofNullable(userMap.get(single.getManagerId()))
                .map(u -> u.getName())
                .orElse("");
            single.setManagerName(userName);
            List<Long> allowUpdateUserIdList = RestContext.getUpdateAllowUserIdList();
            if (CollectionUtils.isNotEmpty(allowUpdateUserIdList)) {
                single.setCanEdit(allowUpdateUserIdList.contains(single.getManagerId()));
            }
            List<Long> allowDeleteUserIdList = RestContext.getDeleteAllowUserIdList();
            if (CollectionUtils.isNotEmpty(allowDeleteUserIdList)) {
                single.setCanRemove(allowDeleteUserIdList.contains(single.getManagerId()));
            }
            return single;
        }).collect(Collectors.toList());
        return Response.success(pageData, CollectionUtils.isEmpty(sceneDtos) ? 0 : sceneDtos.size());

    }

    @Override
    public Response getMiddleWareInfo(StatisticsQueryVo vo) {
        try {
            List<LinkRemarkmiddleWareDto> list = TMiddlewareInfoMapper.selectforstatistics(vo);
            List<LinkRemarkmiddleWareDto> pageData = PageUtils.getPage(true, vo.getCurrentPage(), vo.getPageSize(),
                list);

            pageData = pageData.stream().map(
                single -> {
                    long id = single.getMiddleWareId();
                    List<String> techLinkIds = TMiddlewareLinkRelateMapper.selectTechIdsByMiddleWareIds(id);
                    single.setSystemProcessCount(String.valueOf(techLinkIds.size()));
                    //统计业务流程条数
                    if (CollectionUtils.isNotEmpty(techLinkIds)) {
                        int countBusinessProcess = TSceneLinkRelateMapper.countByTechLinkIds(techLinkIds);
                        single.setBussinessProcessCount(String.valueOf(countBusinessProcess));
                    }
                    return single;
                }
            ).collect(Collectors.toList());

            return Response.success(pageData, CollectionUtils.isEmpty(list) ? 0 : list.size());
        } catch (Exception e) {
            return Response.fail("0", e.getMessage(), null);
        }

    }

    @Override
    public LinkRemarkDto getstatisticsInfo() {
        //  List<LinkRemarkmiddleWareDto> middlewareInfo = middlewareInfoMapper.selectforstatistics(null);
        LinkRemarkDto dto = new LinkRemarkDto();
        /*  dto.setLinkRemarkmiddleWareDtos(middlewareInfo);*/

        long businessProcessCount = TSceneMapper.count();
        long businessActiveCount = TBusinessLinkManageTableMapper.count();
        long systemProcessCount = TLinkManageTableMapper.countTotal();
        long systemChangeCount = TLinkManageTableMapper.countChangeNum();
        long onLineApplicationCount = TLinkManageTableMapper.countApplication();
        long linkGuardCount = TLinkGuardMapper.countGuardNum();
        dto.setBusinessProcessCount(String.valueOf(businessProcessCount));
        dto.setBusinessActiveCount(String.valueOf(businessActiveCount));
        dto.setSystemProcessCount(String.valueOf(systemProcessCount));
        dto.setSystemChangeCount(String.valueOf(systemChangeCount));
        dto.setOnLineApplicationCount(String.valueOf(onLineApplicationCount));
        dto.setLinkGuardCount(String.valueOf(linkGuardCount));
        return dto;
    }

    @Override
    public Response deletBusinessLink(String id) {
        if (null == id) {
            return Response.fail("0", "主键不可以为空", null);
        }
        BusinessLinkManageTable table = TBusinessLinkManageTableMapper
            .selectByPrimaryKey(Long.parseLong(id));
        if (table != null) {
            String techLinkId = table.getRelatedTechLink();
            TLinkManageTableMapper.cannotdelete(Long.parseLong(techLinkId), 0L);
        }

        TBusinessLinkManageTableMapper.deleteByPrimaryKey(Long.parseLong(id));

        return Response.success();
    }

    @Override
    public LinkHistoryInfoDto getChart() {

        LinkHistoryInfoDto dto = new LinkHistoryInfoDto();

        String begin = DateUtils.preYear(new java.util.Date());
        String end = new SimpleDateFormat("yyyy-MM").format(new java.util.Date());
        //获取过去一年到现在的日期集合
        List<Date> dateRange = DateUtils.getRangeSet2(begin, end);

        List<BusinessCoverDto> businessCoverList = new ArrayList<>();
        dateRange.stream().forEach(date -> {
            BusinessCoverDto businessCoverDto = new BusinessCoverDto();
            businessCoverDto.setMonth(DateUtils.dateToString(date));
            long count = TSceneMapper.countByTime(date);
            businessCoverDto.setCover(String.valueOf(count));
            businessCoverList.add(businessCoverDto);
        });
        dto.setBusinessCover(businessCoverList);

        List<SystemProcessDto> systemProcessList = Lists.newArrayList();
        dateRange.stream().forEach(date -> {
            SystemProcessDto systemProcessDto = new SystemProcessDto();
            systemProcessDto.setMonth(DateUtils.dateToString(date));
            long count = TLinkManageTableMapper.countSystemProcessByTime(date);
            systemProcessDto.setCover(String.valueOf(count));
            systemProcessList.add(systemProcessDto);
        });
        dto.setSystemProcess(systemProcessList);

        List<ApplicationRemoteDto> applicationRemoteList = Lists.newArrayList();
        dateRange.stream().forEach(date -> {
            ApplicationRemoteDto applicationRemoteDto = new ApplicationRemoteDto();
            applicationRemoteDto.setMonth(DateUtils.dateToString(date));
            long count = TLinkManageTableMapper.countApplicationByTime(date);
            applicationRemoteDto.setCover(String.valueOf(count));
            applicationRemoteList.add(applicationRemoteDto);
        });
        dto.setApplicationRemote(applicationRemoteList);

        Long businessFlowTotalCountNum = TSceneMapper.count();
        String businessFlowTotalCount = String.valueOf(businessFlowTotalCountNum);
        String businessFlowPressureCount = "0";
        String businessFlowPressureRate =
            (businessFlowTotalCountNum == 0L || "0".equals(businessFlowPressureCount)) ?
                "0" : String.valueOf(businessFlowTotalCountNum / Long.parseLong(businessFlowPressureCount));
        dto.setBusinessFlowTotalCount(businessFlowTotalCount);
        dto.setBusinessFlowPressureCount(businessFlowPressureCount);
        dto.setBusinessFlowPressureRate(businessFlowPressureRate);

        // TODO: 2020/1/7 暂时统计系统流程总数
        long applicationTotalCountNum = TLinkManageTableMapper.countTotal();
        String applicationTotalCount = String.valueOf(applicationTotalCountNum);
        String applicationPressureCount = "0";
        String applicationPressureRate = (applicationTotalCountNum == 0L || applicationPressureCount.equals("0")) ?
            "0" : String.valueOf(applicationTotalCountNum / Long.parseLong(applicationPressureCount));
        dto.setApplicationTotalCount(applicationTotalCount);
        dto.setApplicationPressureCount(applicationPressureCount);
        dto.setApplicationPressureRate(applicationPressureRate);

        return dto;
    }

    @Override
    public List<MiddleWareEntity> businessProcessMiddleWares(List<String> ids) {
        List<MiddleWareEntity> result = Lists.newArrayList();

        List<Long> businessIds =
            ids.stream().map(id -> Long.parseLong(String.valueOf(id))).collect(Collectors.toList());
        //查系统流程id集合
        List<String> techIds = TBusinessLinkManageTableMapper.selectTechIdsByBusinessIds(businessIds);
        if (CollectionUtils.isEmpty(techIds)) {
            return result;
        }
        //查中间件id集合
        List<String> middleWareIds = TMiddlewareLinkRelateMapper.selectMiddleWareIdsByTechIds(techIds);
        if (CollectionUtils.isEmpty(middleWareIds)) {
            return result;
        }
        //查中间件信息
        List<Long> midllewareIdslong = middleWareIds.stream()
            .map(id -> Long.parseLong(id)).collect(Collectors.toList());
        List<MiddleWareEntity> middleWareEntities = TMiddlewareInfoMapper.selectByIds(midllewareIdslong);

        result = middleWareEntities;

        return result;
    }

    @Override
    public List<MiddleWareEntity> getAllMiddleWareTypeList() {
        List<MiddleWareEntity> result = Lists.newArrayList();
        List<TMiddlewareInfo> infos = TMiddlewareInfoMapper
            .selectBySelective(new TMiddlewareInfo());

        //按照中间件类型去重
        infos.stream().forEach(info -> {
            MiddleWareEntity entity = new MiddleWareEntity();
            entity.setId(info.getId());
            entity.setMiddleWareType(info.getMiddlewareType());
            entity.setVersion(info.getMiddlewareVersion());
            entity.setMiddleWareName(info.getMiddlewareName());
            result.add(entity);
        });
        List<MiddleWareEntity> distinct = result.stream()
            .collect(Collectors.collectingAndThen(Collectors.toCollection(
                () -> new TreeSet<>(
                    Comparator.comparing(MiddleWareEntity::getMiddleWareType))),
                ArrayList::new));

        return distinct;
    }

    @Override
    public List<SystemProcessIdAndNameDto> ggetAllSystemProcess(String systemProcessName) {
        List<SystemProcessIdAndNameDto> result = Lists.newArrayList();
        LinkManageQueryParam queryParam = new LinkManageQueryParam();
        queryParam.setUserIdList(RestContext.getQueryAllowUserIdList());
        queryParam.setSystemProcessName(systemProcessName);
        List<LinkManageResult> linkManageResultList = linkManageDAO.selectList(queryParam);
        if (CollectionUtils.isNotEmpty(linkManageResultList)) {
            linkManageResultList.stream().forEach(table -> {
                SystemProcessIdAndNameDto dto = new SystemProcessIdAndNameDto();
                dto.setId(String.valueOf(table.getLinkId()));
                dto.setSystemProcessName(table.getLinkName());
                result.add(dto);
            });
        }
        return result;
    }

    @Override
    public List<SystemProcessIdAndNameDto> getAllSystemProcessCanrelateBusiness(String systemProcessName) {
        List<SystemProcessIdAndNameDto> result = Lists.newArrayList();
        LinkManageTable serachTable = new LinkManageTable();
        serachTable.setLinkName(systemProcessName);
        serachTable.setCanDelete(0);

        List<LinkManageTable> tables =
            TLinkManageTableMapper.selectBySelective(serachTable);
        if (CollectionUtils.isNotEmpty(tables)) {
            tables.stream().forEach(table -> {
                SystemProcessIdAndNameDto dto = new SystemProcessIdAndNameDto();
                dto.setId(String.valueOf(table.getLinkId()));
                dto.setSystemProcessName(table.getLinkName());
                result.add(dto);

            });
        }
        return result;
    }

    @Override
    public List<String> entranceFuzzSerach(String entrance) {
        List<String> entrances = TLinkManageTableMapper.entranceFuzzSerach(entrance);
        return entrances;
    }

    @Override
    public List<BusinessActiveIdAndNameDto> businessActiveNameFuzzSearch(String businessActiveName) {
        List<BusinessActiveIdAndNameDto> businessActiveIdAndNameDtoList = Lists.newArrayList();
        BusinessLinkManageQueryParam queryParam = new BusinessLinkManageQueryParam();
        queryParam.setUserIdList(RestContext.getQueryAllowUserIdList());
        queryParam.setBussinessActiveName(businessActiveName);
        List<BusinessLinkResult> businessLinkResultList = businessLinkManageDAO.selectList(queryParam);
        if (CollectionUtils.isNotEmpty(businessLinkResultList)) {
            businessActiveIdAndNameDtoList = businessLinkResultList.stream().map(businessLinkResult -> {
                BusinessActiveIdAndNameDto bActive = new BusinessActiveIdAndNameDto();
                bActive.setId(businessLinkResult.getId());
                bActive.setBusinessActiveName(businessLinkResult.getLinkName());
                return bActive;
            }).collect(Collectors.toList());
        }
        return businessActiveIdAndNameDtoList;
    }

    @Override
    public TechLinkDto fetchTechLinkDetail(String id) {
        if (id == null) {
            throw new RuntimeException("id不能为空");
        }
        LinkQueryVo queryVo = new LinkQueryVo();
        queryVo.setId(Long.parseLong(id));
        TechLinkDto dto =
            TLinkManageTableMapper.selectTechLinkById(Long.parseLong(id));
        if (Objects.nonNull(dto)) {
            //处理变更
            String isChange = dto.getIsChange();
            Category current = null;
            String body_before = dto.getBody_before();
            String body_after = dto.getBody_after();
            if ("1".equals(isChange)) {
                if (StringUtils.isNotBlank(body_after)) {
                    // 这个方法去掉， 现在没用到， 以免浪费资源 - 慕白
                    //Category old = JSON.parseObject(body_before, Category.class);
                    current = JSON.parseObject(body_after, Category.class);
                    //CategoryUtils.markTreeBaseOnOldTree(current, old);
                }
            } else {
                current = JSON.parseObject(body_before, Category.class);
                CategoryUtils.assembleVo(current);
            }
            List<Category> list = new ArrayList<>();
            if (current != null) {
                list.add(current);
            }
            if (CollectionUtils.isNotEmpty(list)) {
                dto.setLinkNode(JSON.toJSONString(list));
            } else {
                dto.setLinkNode(JSON.toJSONString(Lists.newArrayList()));
            }
            dto.setMiddleWareEntities(Lists.newArrayList());
            return dto;
        }
        return new TechLinkDto();
    }

    @Override
    public void addBusinessFlow(BusinessFlowVo vo) throws Exception {
        //添加业务活动主表并返回业务活动的id
        if (CollectionUtils.isEmpty(vo.getRoot())) {
            throw new RuntimeException("关联业务活动不能为空");
        }
        Long sceneId = addScene(vo);
        List<SceneLinkRelate> relates = parsingTree(vo, sceneId);
        if (CollectionUtils.isNotEmpty(relates)) {
            //补全信息
            infoCompletion(relates);

            TSceneLinkRelateMapper.batchInsert(relates);
            //设置业务活动不能被删除
            diableDeleteBusinessActives(relates);
        }

    }

    /**
     * 设置业务活动不能被删除
     *
     * @param relates
     */
    private void diableDeleteBusinessActives(List<SceneLinkRelate> relates) {

        List<Long> relateBusinessLinkIds =
            relates.stream().map(
                single -> {
                    if (single.getBusinessLinkId() != null) {
                        return Long.parseLong(single.getBusinessLinkId());
                    }
                    return 0L;
                }
            ).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(relateBusinessLinkIds)) {
            TBusinessLinkManageTableMapper.cannotdelete(relateBusinessLinkIds, 1L);
        }
    }

    @Override
    public List<BusinessFlowIdAndNameDto> businessFlowIdFuzzSearch(String businessFlowName) {
        List<BusinessFlowIdAndNameDto> businessFlowIdAndNameDtoList = Lists.newArrayList();
        SceneQueryParam queryParam = new SceneQueryParam();
        queryParam.setUserIdList(RestContext.getQueryAllowUserIdList());
        queryParam.setSceneName(businessFlowName);
        List<SceneResult> sceneResultList = sceneDAO.selectList(queryParam);
        if (CollectionUtils.isNotEmpty(sceneResultList)) {
            businessFlowIdAndNameDtoList = sceneResultList.stream().map(sceneResult -> {
                BusinessFlowIdAndNameDto businessFlowIdAndNameDto = new BusinessFlowIdAndNameDto();
                businessFlowIdAndNameDto.setId(String.valueOf(sceneResult.getId()));
                businessFlowIdAndNameDto.setBusinessFlowName(sceneResult.getSceneName());
                return businessFlowIdAndNameDto;
            }).collect(Collectors.toList());
        }
        return businessFlowIdAndNameDtoList;
    }

    /**
     * 解析树并返回关联表封装集合
     *
     * @param vo
     * @param sceneId
     * @return
     * @throws Exception
     */
    private List<SceneLinkRelate> parsingTree(BusinessFlowVo vo, Long sceneId) throws Exception {

        List<SceneLinkRelate> relates = Lists.newArrayList();
        //根节点集合
        List<BusinessFlowTree> roots = vo.getRoot();
        for (int i = 0; i < roots.size(); i++) {
            String parentId = null;
            BusinessFlowTree root = roots.get(i);
            String businessId = root.getId();
            if (StringUtils.isBlank(businessId)) {
                continue;
            }
            SceneLinkRelate relate = new SceneLinkRelate();
            relate.setSceneId(String.valueOf(sceneId));
            relate.setParentBusinessLinkId(parentId);
            relate.setBusinessLinkId(businessId);
            relate.setIsDeleted(0);
            //前端产生的uuid
            relate.setFrontUUIDKey(root.getKey());
            relates.add(relate);
            List<BusinessFlowTree> children = root.getChildren();
            if (CollectionUtils.isNotEmpty(children)) {
                parsing(children, businessId, sceneId, relates);
            }
        }

        return relates;
    }

    private Long addScene(BusinessFlowVo vo) {
        SceneCreateParam param = new SceneCreateParam();
        param.setSceneName(vo.getSceneName());
        param.setSceneLevel(vo.getSceneLevel());
        param.setIsCore(Integer.parseInt(vo.getIsCore()));
        param.setIsChanged(0);
        param.setIsDeleted(0);
        sceneDAO.insert(param);
        return param.getId();
    }

    /**
     * 对业务流程链路关联表的信息补全
     *
     * @param relates 业务流程链路关联集合
     */
    private void infoCompletion(List<SceneLinkRelate> relates) {
        //获取出所有的业务活动ID
        List<Long> businessIds
            = relates
            .stream()
            .map(relate -> Long.parseLong(relate.getBusinessLinkId())).collect(Collectors.toList());

        List<BusinessLinkManageTable> tables =
            TBusinessLinkManageTableMapper.selectByPrimaryKeys(businessIds);

        Map<Long, List<BusinessLinkManageTable>> map
            = tables.stream()
            .collect(Collectors.groupingBy(
                BusinessLinkManageTable::getLinkId));

        relates.stream().forEach(
            relate -> {
                Long businessLinkId = Long.parseLong(relate.getBusinessLinkId());
                List<BusinessLinkManageTable> lists = map.get(businessLinkId);
                if (CollectionUtils.isNotEmpty(lists)) {
                    BusinessLinkManageTable table = lists.get(0);
                    relate.setEntrance(table.getEntrace());
                    relate.setTechLinkId(table.getRelatedTechLink());
                }
            }
        );
    }

    /**
     * @param children 子节点集合
     * @param parentId 父亲节点
     * @param sceneId  业务流程id
     * @param result   返回结果的集合
     * @return
     */
    private List<SceneLinkRelate> parsing(List<BusinessFlowTree> children, String parentId, Long sceneId,
        List<SceneLinkRelate> result) {
        for (int i = 0; i < children.size(); i++) {
            SceneLinkRelate relate = new SceneLinkRelate();
            BusinessFlowTree child = children.get(i);
            String businessId = child.getId();
            if (StringUtils.isNotBlank(businessId)) {
                relate.setBusinessLinkId(child.getId());
                relate.setParentBusinessLinkId(parentId);
                relate.setIsDeleted(0);
                relate.setFrontUUIDKey(child.getKey());
                relate.setSceneId(String.valueOf(sceneId));
                result.add(relate);
            }

            List<BusinessFlowTree> lowerChildren = children.get(i).getChildren();
            if (CollectionUtils.isNotEmpty(lowerChildren)) {
                parsing(lowerChildren, child.getId(), sceneId, result);
            }
        }
        return result;
    }

    @Override
    public BusinessFlowDto getBusinessFlowDetail(String id) {
        BusinessFlowDto dto = new BusinessFlowDto();

        //获取业务流程基本信息
        Scene scene = TSceneMapper.selectByPrimaryKey(Long.parseLong(id));
        dto.setId(String.valueOf(scene.getId()));
        dto.setIsCode(String.valueOf(scene.getIsCore()));
        dto.setLevel(scene.getSceneLevel());
        dto.setBusinessProcessName(scene.getSceneName());

        List<SceneLinkRelate> relates = TSceneLinkRelateMapper.selectBySceneId(Long.parseLong(id));

        List<ExistBusinessActiveDto> existBusinessActiveIds =
            relates.stream().map(relate ->
            {
                ExistBusinessActiveDto single = new ExistBusinessActiveDto();
                single.setKey(relate.getFrontUUIDKey());
                single.setId(relate.getBusinessLinkId());
                return single;
            }).collect(Collectors.toList());

        dto.setExistBusinessActive(existBusinessActiveIds);

        List<BusinessFlowTree> roots = TSceneLinkRelateMapper.findAllRecursion(id);
        dto.setRoots(roots);

        //中间件信息
        List<String> techLinkIds = relates.stream().map(relate -> relate.getTechLinkId()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(techLinkIds)) {
            List<String> middleWareIdStrings = TMiddlewareLinkRelateMapper.selectMiddleWareIdsByTechIds(techLinkIds);
            List<Long> middleWareIds = middleWareIdStrings.stream().map(single -> Long.parseLong(single)).collect(
                Collectors.toList());

            List<MiddleWareEntity> middleWareEntityList = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(middleWareIds)) {
                middleWareEntityList = TMiddlewareInfoMapper.selectByIds(middleWareIds);
            }

            dto.setMiddleWareEntities(middleWareEntityList);
        }

        return dto;

    }

    @Override
    public void modifyBusinessFlow(BusinessFlowVo vo) throws Exception {
        if (CollectionUtils.isEmpty(vo.getRoot())) {
            throw new RuntimeException("关联业务活动不能为空");
        }
        recordBusinessFlowLog(vo);
        //修改主表
        modifyScene(vo);
        //激活可以业务活动可以删除
        enableBusinessDelete(vo);
        //删除老的关联信息
        TSceneLinkRelateMapper.deleteBySceneId(vo.getId());
        //重新生成关联信息
        List<SceneLinkRelate> relates = parsingTree(vo, Long.parseLong(vo.getId()));
        if (CollectionUtils.isNotEmpty(relates)) {
            //补全信息
            infoCompletion(relates);
            TSceneLinkRelateMapper.batchInsert(relates);
        }
        //冻结业务活动可以删除
        diableDeleteBusinessActives(relates);
    }

    private void recordBusinessFlowLog(BusinessFlowVo vo) throws Exception {
        //记录变更日志
        Scene oldScene = TSceneMapper.selectByPrimaryKey(Long.parseLong(vo.getId()));
        OperationLogContextHolder.addVars(BizOpConstants.Vars.BUSINESS_PROCESS, vo.getSceneName());
        List<SceneLinkRelate> oldSceneLinkRelateList = TSceneLinkRelateMapper.selectBySceneId(
            Long.parseLong(vo.getId()));
        List<Long> oldBusinessLinkIdList = oldSceneLinkRelateList.stream().map(SceneLinkRelate::getBusinessLinkId).map(
            Long::parseLong).collect(Collectors.toList());
        List<SceneLinkRelate> currentSceneLinkRelateList = parsingTree(vo, Long.parseLong(vo.getId()));
        List<Long> currentBusinessLinkIdList = currentSceneLinkRelateList.stream().map(
            SceneLinkRelate::getBusinessLinkId).map(Long::parseLong).collect(Collectors.toList());
        List<Long> toDeleteIdList = Lists.newArrayList();
        toDeleteIdList.addAll(oldBusinessLinkIdList);
        toDeleteIdList.removeAll(currentBusinessLinkIdList);
        List<Long> toAddIdList = Lists.newArrayList();
        toAddIdList.addAll(currentBusinessLinkIdList);
        toAddIdList.removeAll(oldBusinessLinkIdList);
        String selectiveContent = "";
        if (oldScene.getSceneName().equals(vo.getSceneName())
            && CollectionUtils.isEmpty(toAddIdList)
            && CollectionUtils.isEmpty(toDeleteIdList)) {
            OperationLogContextHolder.ignoreLog();
        }
        if (CollectionUtils.isNotEmpty(toAddIdList)) {
            List<BusinessLinkManageTable> businessLinkManageTableList = TBusinessLinkManageTableMapper
                .selectBussinessLinkByIdList(toAddIdList);
            if (CollectionUtils.isNotEmpty(businessLinkManageTableList)) {
                String addNodeNames = businessLinkManageTableList.stream().map(BusinessLinkManageTable::getLinkName)
                    .collect(Collectors.joining(","));
                selectiveContent = selectiveContent + "｜新增节点：" + addNodeNames;
            }
        }
        if (CollectionUtils.isNotEmpty(toDeleteIdList)) {
            List<BusinessLinkManageTable> businessLinkManageTableList = TBusinessLinkManageTableMapper
                .selectBussinessLinkByIdList(toDeleteIdList);
            if (CollectionUtils.isNotEmpty(businessLinkManageTableList)) {
                String deleteNodeNames = businessLinkManageTableList.stream().map(BusinessLinkManageTable::getLinkName)
                    .collect(Collectors.joining(","));
                selectiveContent = selectiveContent + "｜删除节点：" + deleteNodeNames;
            }
        }
        OperationLogContextHolder.addVars(BizOpConstants.Vars.BUSINESS_PROCESS_SELECTIVE_CONTENT, selectiveContent);
    }

    private void enableBusinessDelete(BusinessFlowVo vo) {
        if (vo.getId() == null) {
            return;
        }
        List<SceneLinkRelate> oldRelates =
            TSceneLinkRelateMapper.selectBySceneId(Long.parseLong(vo.getId()));
        if (CollectionUtils.isEmpty(oldRelates)) {
            return;
        }

        List<Long> candeleteList = oldRelates.stream()
            .map(single ->
            {
                if (single.getBusinessLinkId() == null) {
                    return 0L;
                }
                return Long.parseLong(single.getBusinessLinkId());
            }).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(candeleteList)) {
            return;
        }
        TBusinessLinkManageTableMapper.cannotdelete(candeleteList, 0L);
    }

    /**
     * 修改场景的主表
     *
     * @param vo
     */
    private void modifyScene(BusinessFlowVo vo) {
        String sceneId = vo.getId();
        String sceneName = vo.getSceneName();
        String isCore = vo.getIsCore();
        String sceneLevel = vo.getSceneLevel();
        Scene updateScene = new Scene();
        updateScene.setId(Long.parseLong(sceneId));
        updateScene.setSceneName(sceneName);
        updateScene.setIsCore(Integer.parseInt(isCore));
        updateScene.setSceneLevel(sceneLevel);
        TSceneMapper.updateByPrimaryKeySelective(updateScene);
    }

    @Override
    public List<MiddleWareNameDto> cascadeMiddleWareNameAndVersion(String middleWareType) throws Exception {
        List<MiddleWareNameDto> result = Lists.newArrayList();

        //拿出所有的中间件名字
        TMiddlewareInfo info = new TMiddlewareInfo();
        if (StringUtils.isNotBlank(middleWareType)) {
            info.setMiddlewareType(middleWareType);
        }
        List<TMiddlewareInfo> infos =
            TMiddlewareInfoMapper.selectBySelective(info);
        if (CollectionUtils.isNotEmpty(infos)) {
            Map<String, List<TMiddlewareInfo>> groupByMiddleWareName =
                infos.stream().collect(Collectors.groupingBy(TMiddlewareInfo::getMiddlewareName));

            for (Map.Entry<String, List<TMiddlewareInfo>> entry : groupByMiddleWareName.entrySet()) {
                MiddleWareNameDto dto = new MiddleWareNameDto();
                String middleWareName = entry.getKey();
                dto.setLabel(middleWareName);
                dto.setValue(middleWareName);
                List<TMiddlewareInfo> values = entry.getValue();
                if (CollectionUtils.isNotEmpty(values)) {
                    List<MiddleWareVersionDto> children = values.stream().map(
                        single -> {
                            MiddleWareVersionDto versionDto = new MiddleWareVersionDto();
                            String version = single.getMiddlewareVersion();
                            versionDto.setLabel(version);
                            versionDto.setValue(version);
                            return versionDto;
                        }
                    ).collect(Collectors.toList());
                    dto.setChildren(children);
                }
                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public List<MiddleWareNameDto> getDistinctMiddleWareName() {
        List<MiddleWareNameDto> result = Lists.newArrayList();

        List<TMiddlewareInfo> infos = TMiddlewareInfoMapper
            .selectBySelective(new TMiddlewareInfo());

        //按照中间件类型去重
        infos.stream().forEach(single -> {
            MiddleWareNameDto entity = new MiddleWareNameDto();
            entity.setValue(single.getMiddlewareName());
            entity.setLabel(single.getMiddlewareName());
            result.add(entity);
        });
        List<MiddleWareNameDto> distinct = result.stream()
            .collect(Collectors.collectingAndThen(Collectors.toCollection(
                () -> new TreeSet<>(
                    Comparator.comparing(MiddleWareNameDto::getLabel))),
                ArrayList::new));
        return distinct;
    }

    @Override
    public List<EntranceSimpleDto> getEntranceByAppName(String applicationName) {
        //List<EntranceSimpleDto> result = Lists.newArrayList();
        //List<LinkEdge> linkEdges = linkManage.getAllEntrance(applicationName);
        //Map<String, List<LinkEdge>> group =
        //        linkEdges.stream().collect(
        //                Collectors.groupingBy(LinkEdge::getApplicationName));
        //
        //for (Map.Entry entry : group.entrySet()) {
        //
        //    if (entry.getValue() != null) {
        //        List<LinkEdge> values = (List<LinkEdge>) entry.getValue();
        //        values.stream().forEach(
        //                value -> {
        //                    EntranceSimpleDto dto = new EntranceSimpleDto();
        //                    String entrance
        //                            = LinkManage.generateEntranceWithoutAppName(value);
        //                    dto.setLabel(entrance);
        //                    dto.setValue(entrance);
        //                    result.add(dto);
        //                }
        //        );
        //    }
        //}
        //return result;
        return null;
    }

    @Override
    public List<MiddleWareResponse> getMiddleWareResponses(String applicationName) {
        List<MiddleWareResponse> middleWareResponses = Lists.newArrayList();
        List<AgentPluginSupportResponse> supportList = agentPluginSupportService.queryAgentPluginSupportList();
        List<ApplicationResult> applicationResultList = applicationDAO.getApplicationByName(
            Arrays.asList(applicationName));
        if (CollectionUtils.isEmpty(applicationResultList)) {
            return middleWareResponses;
        }
        LibraryResult[] libraryResults = applicationResultList.get(0).getLibrary();
        if (null == libraryResults || libraryResults.length == 0) {
            return middleWareResponses;
        }
        for (LibraryResult libraryResult : libraryResults) {
            MiddleWareResponse middleWareResponse = agentPluginSupportService.convertLibInfo(supportList,
                libraryResult.getLibraryName());
            if (!Objects.isNull(middleWareResponse)) {
                middleWareResponses.add(middleWareResponse);
            }
        }
        middleWareResponses.sort((a, b) -> {
            if (a.getStatusResponse().getValue() > b.getStatusResponse().getValue()) {
                return 1;
            } else if (a.getStatusResponse().getValue() < b.getStatusResponse().getValue()) {
                return -1;
            } else {
                return 0;
            }
        });
        return middleWareResponses;
    }

    @Override
    public List<BusinessActivityNameResponse> getBusinessActiveByFlowId(Long businessFlowId) {
        List<BusinessActivityNameResponse> sceneBusinessActivityRefVOS = new ArrayList<>();
        List<SceneLinkRelate> sceneLinkRelates = tSceneLinkRelateMapper.selectBySceneId(businessFlowId);
        if (CollectionUtils.isNotEmpty(sceneLinkRelates)) {
            List<Long> businessActivityIds = sceneLinkRelates.stream().map(o -> Long.valueOf(o.getBusinessLinkId()))
                .collect(Collectors.toList());
            List<BusinessLinkManageTable> businessLinkManageTables = TBusinessLinkManageTableMapper
                .selectBussinessLinkByIdList(businessActivityIds);
            sceneBusinessActivityRefVOS = businessLinkManageTables.stream().map(businessLinkManageTable -> {
                BusinessActivityNameResponse businessActivityNameResponse = new BusinessActivityNameResponse();
                businessActivityNameResponse.setBusinessActivityId(businessLinkManageTable.getLinkId());
                businessActivityNameResponse.setBusinessActivityName(businessLinkManageTable.getLinkName());
                return businessActivityNameResponse;
            }).collect(Collectors.toList());
        }
        return sceneBusinessActivityRefVOS;
    }

    @Override
    public List<ApplicationDetailResponse> getApplicationDetailsByAppName(String applicationName, String entrance,
        String linkApplicationName) throws Exception {
        //List<LinkVertex> LinkVertexList = linkManage.getAppDetails(applicationName, entrance, linkApplicationName);
        //List<ApplicationDetailResponse> applicationDetailResponseList = LinkVertexList.stream().filter(
        //        linkVertex -> (!linkVertex.getApplicationName().endsWith("-Virtual"))
        //).filter(linkVertex -> linkVertex.getRpcType() != null).map(linkVertex -> {
        //    ApplicationDetailResponse applicationDetailResponse = new ApplicationDetailResponse();
        //    applicationDetailResponse.setApplicationName(linkVertex.getApplicationName());
        //    if (0 == linkVertex.getRpcType()) {
        //        applicationDetailResponse.setMiddleWareResponses(getMiddleWareResponses(applicationName));
        //    }
        //    applicationDetailResponse.setRpcType(linkVertex.getRpcType());
        //    VertexOpData vertexOpData = linkVertex.getVertexOpData();
        //    RpcType rpcType = RpcType.getByValue(linkVertex.getRpcType(), RpcType.UNKNOWN);
        //    //已知节点、未知节点、数据内容
        //    if (vertexOpData != null) {
        //        applicationDetailResponse.setNodes(vertexOpData.getIpList());
        //        applicationDetailResponse.setUnKnowNodes(vertexOpData.getUnKnowIpList());
        //        String rpcData = StringUtils.join(vertexOpData.getDataList(), ",");
        //
        //        if (StringUtils.isNotBlank(rpcData)) {
        //            rpcData = rpcType.getDataType() + ":" + rpcData;
        //        }
        //        applicationDetailResponse.setRpcData(rpcData);
        //    }
        //    //类型名称
        //    applicationDetailResponse.setRpcTypeTitle(rpcType.getText());
        //    return applicationDetailResponse;
        //}).collect(Collectors.toList());
        //return applicationDetailResponseList;
        return null;
    }
}



