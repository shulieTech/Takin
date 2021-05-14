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

package io.shulie.tro.web.data.dao.perfomanceanaly;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.collect.Lists;
import io.shulie.tro.utils.json.JsonHelper;
import io.shulie.tro.web.data.mapper.mysql.PerformanceThreadDataMapper;
import io.shulie.tro.web.data.mapper.mysql.PerformanceThreadStackDataMapper;
import io.shulie.tro.web.data.model.mysql.PerformanceThreadDataEntity;
import io.shulie.tro.web.data.model.mysql.PerformanceThreadStackDataEntity;
import io.shulie.tro.web.data.param.perfomanceanaly.PerformanceThreadDataParam;
import io.shulie.tro.web.data.param.perfomanceanaly.PerformanceThreadQueryParam;
import io.shulie.tro.web.data.result.perfomanceanaly.PerformanceThreadCountResult;
import io.shulie.tro.web.data.result.perfomanceanaly.PerformanceThreadDataResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @ClassName PerformanceThreadDataDAOImpl
 * @Description
 * @Author qianshui
 * @Date 2020/11/10 下午4:44
 */
@Service
@Slf4j
public class PerformanceThreadDataDAOImpl implements PerformanceThreadDataDAO {


    @Resource
    private PerformanceThreadDataMapper performanceThreadDataMapper;
    @Resource
    private PerformanceThreadStackDataMapper performanceThreadStackDataMapper;

    @Override
    public List<PerformanceThreadCountResult> getPerformanceThreadCountList(List<String> baseIds) {
        long start = System.currentTimeMillis();
        LambdaQueryWrapper<PerformanceThreadDataEntity> wrapper = new LambdaQueryWrapper<>();
        if (CollectionUtils.isNotEmpty(baseIds)) {
            wrapper.in(PerformanceThreadDataEntity::getBaseId, baseIds);
        }
        List<PerformanceThreadDataEntity> entities = performanceThreadDataMapper.selectList(wrapper);

        if(CollectionUtils.isEmpty(entities)) {
            return Lists.newArrayList();
        }
        Map<String, List<PerformanceThreadDataEntity>> groupMap = entities.stream().collect(
            Collectors.groupingBy(o -> o.getBaseId()+"_" +o.getTimestamp()));

        List<PerformanceThreadCountResult> results = groupMap.keySet().stream().map(key -> {
            PerformanceThreadCountResult result = new PerformanceThreadCountResult();
            String[] temp = key.split("_");
            result.setBaseId(temp[0]);
            result.setTimestamp(temp[1]);
            List<PerformanceThreadDataEntity> list = groupMap.get(key);
            int count = 0;
            for(PerformanceThreadDataEntity entity :list) {
                List<PerformanceThreadDataParam> params = JsonHelper.json2List(entity.getThreadData(),PerformanceThreadDataParam.class);
                count += params.size();
            }
            result.setThreadCount(count);
            return result;
        }).collect(Collectors.toList());
        log.info("getPerformanceThreadCountList.query运行时间:{}，数据量:{}",System.currentTimeMillis() - start,results.size());
        return results;

    }

    @Override
    public List<PerformanceThreadDataResult> getPerformanceThreadDataList(PerformanceThreadQueryParam param) {
        long start = System.currentTimeMillis();
        LambdaQueryWrapper<PerformanceThreadDataEntity> wrapper = new LambdaQueryWrapper<>();

        if (CollectionUtils.isNotEmpty(param.getBaseIds())) {
            wrapper.in(PerformanceThreadDataEntity::getBaseId, param.getBaseIds());
        }
        if (StringUtils.isNotBlank(param.getBaseId())) {
            wrapper.eq(PerformanceThreadDataEntity::getBaseId, param.getBaseId());
        }

        List<PerformanceThreadDataEntity> entities = performanceThreadDataMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(entities)) {
            return Lists.newArrayList();
        }
        List<PerformanceThreadDataResult> results = Lists.newArrayList();
        entities.forEach(entity -> {
            List<PerformanceThreadDataParam> params = JsonHelper.json2List(entity.getThreadData(),PerformanceThreadDataParam.class);
            params.forEach(data -> {
                PerformanceThreadDataResult result = new PerformanceThreadDataResult();
                result.setBaseId(entity.getBaseId());
                result.setTimestamp(entity.getTimestamp());
                result.setThreadName(data.getThreadName());
                result.setThreadCpuUseRate(data.getThreadCpuUsage());
                result.setThreadStatus(data.getThreadStatus());
                result.setThreadStackLink(data.getThreadStackLink());
                results.add(result);
            });
        });
        log.info("getPerformanceThreadDataList.query运行时间:{}，数据量:{}",System.currentTimeMillis() - start,results.size());
        return results;
        //StringBuffer influxDBSQL = new StringBuffer();
        //influxDBSQL.append("select *");
        //influxDBSQL.append(" from t_performance_thread_data");
        //influxDBSQL.append(" where 1=1 ");
        //if(param.getBaseId() != null) {
        //    influxDBSQL.append(" and base_id = ").append("'").append(param.getBaseId()).append("'");
        //}
        //if(CollectionUtils.isNotEmpty(param.getBaseIds())) {
        //    //"agent_id"=~/agent_id_001|agent_test_id_001/
        //    influxDBSQL.append(" and base_id =")
        //        .append("~/")
        //        .append(param.getBaseIds().stream().collect(Collectors.joining("|"))).append("/");
        //}
        //if(StringUtils.isNotEmpty(param.getThreadStatus())) {
        //    influxDBSQL.append(" and thread_status = ").append("'").append(param.getThreadStatus()).append("'");
        //}
        //if(StringUtils.isNotEmpty(param.getThreadName())) {
        //    influxDBSQL.append(" and thread_name = ").append("'").append(param.getThreadName()).append("'");
        //}
        //List<PerformanceThreadDataResult> dataList = Optional.ofNullable(
        //    influxDBWriter.query(influxDBSQL.toString(),PerformanceThreadDataResult.class))
        //    .orElse(Lists.newArrayList());

    }

    @Override
    public String getThreadStackInfo(String link) {
        long start = System.currentTimeMillis();
        LambdaQueryWrapper<PerformanceThreadStackDataEntity> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(link)) {
            wrapper.eq(PerformanceThreadStackDataEntity::getThreadStackLink, link);
        }
        List<PerformanceThreadStackDataEntity> entity = performanceThreadStackDataMapper.selectList(wrapper);
        log.info("getThreadStackInfo.selectOne,,link:{}运行时间:{},数据量：{},",link,System.currentTimeMillis() - start,entity.size());
        if(entity != null && entity.size() > 0) {
            return entity.get(0).getThreadStack();
        }
        return "null";
    }

    @Override
    public void clearData(String time) {
        // influxdb
        if (StringUtils.isBlank(time)) {
            return;
        }
        //StringBuilder influxDBSQL = new StringBuilder();
        //influxDBSQL.append("delete");
        //influxDBSQL.append(" from t_performance_thread_data");
        //influxDBSQL.append(" where time <= ");
        //influxDBSQL.append("'");
        //influxDBSQL.append(time);
        //influxDBSQL.append("'");
        //influxDBWriter.query(influxDBSQL.toString(), PerformanceThreadDataResult.class);
        // mysql
        LambdaUpdateWrapper<PerformanceThreadStackDataEntity> stackWrapper = new LambdaUpdateWrapper<>();
        stackWrapper.lt(PerformanceThreadStackDataEntity::getGmtCreate,time);
        stackWrapper.orderByAsc(PerformanceThreadStackDataEntity::getThreadStackLink);
        stackWrapper.last("limit 10000");
        performanceThreadStackDataMapper.delete(stackWrapper);
        // mysql
        LambdaUpdateWrapper<PerformanceThreadDataEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.lt(PerformanceThreadDataEntity::getGmtCreate,time);
        stackWrapper.orderByAsc(PerformanceThreadStackDataEntity::getThreadStackLink);
        stackWrapper.last("limit 10000");
        performanceThreadDataMapper.delete(wrapper);
    }
}
