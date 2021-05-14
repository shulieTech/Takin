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

package io.shulie.tro.cloud.biz.service.strategy.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.pamirs.tro.entity.dao.strategy.TStrategyConfigMapper;
import com.pamirs.tro.entity.domain.dto.strategy.StrategyConfigDTO;
import com.pamirs.tro.entity.domain.dto.strategy.StrategyConfigDetailDTO;
import com.pamirs.tro.entity.domain.entity.strategy.StrategyConfig;
import com.pamirs.tro.entity.domain.vo.strategy.StrategyConfigAddVO;
import com.pamirs.tro.entity.domain.vo.strategy.StrategyConfigQueryVO;
import com.pamirs.tro.entity.domain.vo.strategy.StrategyConfigUpdateVO;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.Quantity;
import io.shulie.tro.cloud.biz.output.strategy.StrategyOutput;
import io.shulie.tro.cloud.biz.service.strategy.StrategyConfigService;
import io.shulie.tro.cloud.common.enums.deployment.DeploymentMethodEnum;
import io.shulie.tro.cloud.common.enums.engine.EngineStartTypeEnum;
import io.shulie.tro.cloud.common.exception.TroCloudException;
import io.shulie.tro.cloud.common.exception.TroCloudExceptionEnum;
import io.shulie.tro.cloud.common.utils.DateUtil;
import io.shulie.tro.k8s.service.MicroService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @ClassName StrategyConfigServiceImpl
 * @Description
 * @Author qianshui
 * @Date 2020/5/9 下午3:17
 */
@Slf4j
@Service
public class StrategyConfigServiceImpl implements StrategyConfigService {

    @Resource
    private TStrategyConfigMapper tStrategyConfigMapper;

    @Autowired
    private MicroService microService;

    @Value("${tro.cloud.deployment.method:localThread}")
    private String deploymentMethod;

    @Value("${micro.type:localThread}")
    private String microType;

    @Override
    public Boolean add(StrategyConfigAddVO addVO) {
        StrategyConfig config = new StrategyConfig();
        config.setStrategyName(addVO.getStrategyName());
        config.setStrategyConfig(addVO.getStrategyConfig());
        tStrategyConfigMapper.insert(config);
        return true;
    }

    @Override
    public Boolean update(StrategyConfigUpdateVO updateVO) {
        StrategyConfig config = new StrategyConfig();
        config.setId(updateVO.getId());
        config.setStrategyName(updateVO.getStrategyName());
        config.setStrategyConfig(updateVO.getStrategyConfig());
        tStrategyConfigMapper.updateByPrimaryKeySelective(config);
        return true;
    }

    @Override
    public Boolean delete(Long id) {
        tStrategyConfigMapper.deleteByPrimaryKey(id);
        return true;
    }

    @Override
    public StrategyConfigDetailDTO getDetail(Long id) {
        StrategyConfig strategyConfig = tStrategyConfigMapper.selectByPrimaryKey(id);
        if (strategyConfig == null) {
            return null;
        }
        StrategyConfigDetailDTO dto = new StrategyConfigDetailDTO();
        dto.setStrategyName(strategyConfig.getStrategyName());
        dto.setStrategyConfig(strategyConfig.getStrategyConfig());
        return dto;
    }

    @Override
    public StrategyOutput getStrategy(Integer expectThroughput,Integer tpsNum) {
        StrategyOutput result = new StrategyOutput();
        BigDecimal min = BigDecimal.ONE;
        BigDecimal max = BigDecimal.ONE;
        // 获取k8s的机器
        List<Node> nodes = null;
        if(EngineStartTypeEnum.K8S.getType().equals(microType)) {
            nodes = microService.getNodeList();
            if(deploymentMethod.equals(DeploymentMethodEnum.PRIVATE.getDesc())) {
                // nodes
                if(CollectionUtils.isEmpty(nodes)) {
                    throw new TroCloudException(TroCloudExceptionEnum.K8S_NODE_EMPTY, "未找到k8s节点");
                }
            }
        }

        // 获取分配策略
        PageInfo<StrategyConfigDTO> strategyConfig = queryPageList(new StrategyConfigQueryVO());

        if (strategyConfig != null && strategyConfig.getSize() > 0) {
            // 配置读取
            StrategyConfigDTO config = null;
            for(StrategyConfigDTO dto :strategyConfig.getList()) {
               if(dto.getDeploymentMethod().equals(deploymentMethod)) {
                   config = dto;
                   break;
               }
            }
            // 取最新一份
            if(config == null) {
                config = strategyConfig.getList().get(0);
            }

            if (tpsNum != null ){
                min = new BigDecimal(tpsNum).divide(new BigDecimal(config.getTpsNum()), 0, RoundingMode.CEILING);
                if(deploymentMethod.equals(DeploymentMethodEnum.PRIVATE.getDesc())) {
                    // 私有化部署
                    max = getMaxByNode(nodes, config.getMemorySize());
                }else if(tpsNum > config.getTpsNum()){
                    // 公有化计算规则，根据pod的maxTps计算
                    max = min.add(min.multiply(new BigDecimal("0.8")).setScale(0, RoundingMode.CEILING));
                }
            }

            if (expectThroughput != null) {
                min = new BigDecimal(expectThroughput).divide(new BigDecimal(config.getThreadNum()), 0,
                    RoundingMode.CEILING);
                if(deploymentMethod.equals(DeploymentMethodEnum.PRIVATE.getDesc())) {
                    max = getMaxByNode(nodes, config.getMemorySize());
                }else if(expectThroughput > config.getThreadNum()){
                    // 公有化计算规则，根据pod并发数计算
                    BigDecimal rate = BigDecimal.ZERO;
                    //增加一定的浮动比例
                    if (min.intValue() < 5) {
                        rate = new BigDecimal("0.5");
                    } else if (min.intValue() < 10) {
                        rate = new BigDecimal("0.7");
                    } else if (min.intValue() < 20) {
                        rate = new BigDecimal("0.8");
                    } else {
                        rate = new BigDecimal("0.9");
                    }
                    max = min.add(min.multiply(rate).setScale(0, RoundingMode.CEILING));
                }
            }

        }
        // 做一个逻辑判断

        result.setMax(max.intValue());
        result.setMin(min.intValue());
        if(deploymentMethod.equals(DeploymentMethodEnum.PRIVATE.getDesc()) && min.intValue() > max.intValue()) {
            //throw new TroCloudException(TroCloudExceptionEnum.POD_NUM_EMPTY, "现有k8s集群达不到该tps值");
            result.setMin(max.intValue());
        }
        return result;
    }

    public BigDecimal getMaxByNode( List<Node> nodes, BigDecimal memorySize) {
        if(nodes == null) {
            // 默认100
            return BigDecimal.valueOf(100);
        }
        BigDecimal tempMax = BigDecimal.ZERO;
        for (Node node : nodes) {
            Map<String, Quantity> map = node.getStatus().getAllocatable();
            // 通过内存计算pod
            Quantity quantity = map.get("memory");
            tempMax = tempMax.add(BigDecimal.valueOf(getMemory(quantity))
                .divide(memorySize.multiply(BigDecimal.valueOf(1024 * 1024L)),0,RoundingMode.DOWN));
        }
        // 初始化
        return tempMax;
    }

    private long getMemory(Quantity quantity) {
        if(quantity.getFormat().toLowerCase().equals("Ki".toLowerCase())) {
            return Long.parseLong(quantity.getAmount()) * 1024L;
        }else if(quantity.getFormat().toLowerCase().equals("Mi".toLowerCase())) {
            return Long.parseLong(quantity.getAmount()) * 1024L * 1024L;
        } else if(quantity.getFormat().toLowerCase().equals("Gi".toLowerCase())) {
            return Long.parseLong(quantity.getAmount()) * 1024L * 1024L * 1024L;
        }else {
            return Long.parseLong(quantity.getAmount());
        }
    }

    @Override
    public StrategyConfigDTO getDefaultStrategyConfig() {
        PageInfo<StrategyConfigDTO> strategyConfig = queryPageList(new StrategyConfigQueryVO());
        if (strategyConfig != null && strategyConfig.getSize() > 0) {
            return strategyConfig.getList().get(0);
        }
        return null;
    }

    @Override
    public PageInfo<StrategyConfigDTO> queryPageList(StrategyConfigQueryVO queryVO) {
        Page page = PageHelper.startPage(queryVO.getCurrentPage() + 1, queryVO.getPageSize());

        List<StrategyConfig> queryList = tStrategyConfigMapper.getPageList(queryVO);
        if (CollectionUtils.isEmpty(queryList)) {
            return new PageInfo<>(Lists.newArrayList());
        }
        List<StrategyConfigDTO> resultList = Lists.newArrayList();
        queryList.forEach(data -> {
            StrategyConfigDTO dto = new StrategyConfigDTO();
            dto.setId(data.getId());
            dto.setStrategyName(data.getStrategyName());
            parseConfig(dto, data.getStrategyConfig());
            dto.setUpdateTime(DateUtil.getYYYYMMDDHHMMSS(data.getUpdateTime()));
            resultList.add(dto);
        });

        PageInfo pageInfo = new PageInfo<>(resultList);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    private void parseConfig(StrategyConfigDTO dto, String config) {
        try {
            JSONObject object = JSON.parseObject(config);
            dto.setThreadNum(object.getInteger("threadNum"));
            dto.setCpuNum(object.getBigDecimal("cpuNum"));
            dto.setMemorySize(object.getBigDecimal("memorySize"));
            dto.setTpsNum(object.getInteger("tpsNum"));
            dto.setDeploymentMethod(DeploymentMethodEnum.getByType(object.getInteger("deploymentMethod")));
        } catch (Exception e) {
            log.error("Parse Config Failure = {}", config);
        }
    }
}
