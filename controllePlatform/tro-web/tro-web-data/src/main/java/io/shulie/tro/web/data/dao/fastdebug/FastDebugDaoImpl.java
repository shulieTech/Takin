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

package io.shulie.tro.web.data.dao.fastdebug;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.utils.json.JsonHelper;
import io.shulie.tro.web.common.vo.fastdebug.ContentTypeVO;
import io.shulie.tro.web.common.vo.fastdebug.FastDebugExceptionVO;
import io.shulie.tro.web.data.convert.fastdebug.FastDebugExceptionConvert;
import io.shulie.tro.web.data.mapper.custom.fastdebug.CustomFastDebugExceptionMapper;
import io.shulie.tro.web.data.mapper.mysql.FastDebugConfigInfoMapper;
import io.shulie.tro.web.data.mapper.mysql.FastDebugResultMapper;
import io.shulie.tro.web.data.model.mysql.FastDebugConfigInfoEntity;
import io.shulie.tro.web.data.model.mysql.FastDebugExceptionEntity;
import io.shulie.tro.web.data.model.mysql.FastDebugResultEntity;
import io.shulie.tro.web.data.param.fastdebug.FastDebugConfigCreateParam;
import io.shulie.tro.web.data.param.fastdebug.FastDebugConfigSearchParam;
import io.shulie.tro.web.data.param.fastdebug.FastDebugConfigUpdateParam;
import io.shulie.tro.web.data.param.fastdebug.FastDebugExceptionParam;
import io.shulie.tro.web.data.param.fastdebug.FastDebugExceptionSearchParam;
import io.shulie.tro.web.data.param.fastdebug.FastDebugResultParam;
import io.shulie.tro.web.data.param.fastdebug.FastDebugResultSearchParam;
import io.shulie.tro.web.data.param.fastdebug.FastDebugResultUpdateParam;
import io.shulie.tro.web.data.result.fastdebug.FastDebugConfigResult;
import io.shulie.tro.web.data.result.fastdebug.FastDebugResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.data.dao.fastdebug
 * @date 2020/12/28 9:49 上午
 */
@Component
public class FastDebugDaoImpl implements FastDebugDao {
    @Autowired
    private FastDebugConfigInfoMapper fastDebugConfigInfoMapper;

    @Autowired
    private FastDebugResultMapper fastDebugResultMapper;

    @Autowired
    private CustomFastDebugExceptionMapper customFastDebugExceptionMapper;



    @Override
    public Long createFastDebugConfig(FastDebugConfigCreateParam param) {
        FastDebugConfigInfoEntity entity = new FastDebugConfigInfoEntity();
        BeanUtils.copyProperties(param,entity);
        fastDebugConfigInfoMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void deleteFastDebugConfig(Long id) {
        fastDebugConfigInfoMapper.deleteById(id);
    }

    @Override
    public void updateFastDebugConfig(FastDebugConfigUpdateParam param) {
        FastDebugConfigInfoEntity entity = new FastDebugConfigInfoEntity();
        BeanUtils.copyProperties(param,entity);
        fastDebugConfigInfoMapper.updateById(entity);
    }

    @Override
    public FastDebugConfigResult selectById(Long id) {
        FastDebugConfigInfoEntity entity = fastDebugConfigInfoMapper.selectById(id);
        if(entity == null) {
            return null;
        }
        FastDebugConfigResult result = new FastDebugConfigResult();
        BeanUtils.copyProperties(entity,result);
        result.setContentTypeVo(JsonHelper.json2Bean(entity.getContentType(), ContentTypeVO.class));
        return result;
    }

    @Override
    public FastDebugConfigResult selectByName(String name) {
        LambdaQueryWrapper<FastDebugConfigInfoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FastDebugConfigInfoEntity::getName,name);
        FastDebugConfigInfoEntity entity = fastDebugConfigInfoMapper.selectOne(wrapper);
        if(entity == null) {
            return null;
        }
        FastDebugConfigResult result = new FastDebugConfigResult();
        BeanUtils.copyProperties(entity,result);
        result.setContentTypeVo(JsonHelper.json2Bean(entity.getContentType(), ContentTypeVO.class));
        return result;
    }

    @Override
    public PagingList<FastDebugConfigResult> selectList(FastDebugConfigSearchParam param) {
        LambdaQueryWrapper<FastDebugConfigInfoEntity> wrapper = new LambdaQueryWrapper<>();
        if(param.getBusinessLinkId() != null) {
            wrapper.eq(FastDebugConfigInfoEntity::getBusinessLinkId,param.getBusinessLinkId());
        }
        wrapper.eq(FastDebugConfigInfoEntity::getCustomerId,param.getCustomerId());
        Page<FastDebugConfigInfoEntity> page = new Page<>(param.getCurrent() + 1, param.getPageSize());
        wrapper.orderByDesc(FastDebugConfigInfoEntity::getGmtModified);

        IPage<FastDebugConfigInfoEntity> infoEntityIPage = fastDebugConfigInfoMapper.selectPage(page,wrapper);
        if(CollectionUtils.isEmpty(infoEntityIPage.getRecords())) {
            return PagingList.empty();
        }
        List<FastDebugConfigResult> results = infoEntityIPage.getRecords().stream().map(entity -> {
            FastDebugConfigResult result = new FastDebugConfigResult();
            BeanUtils.copyProperties(entity,result);
            result.setContentTypeVo(JsonHelper.json2Bean(entity.getContentType(), ContentTypeVO.class));
            return result;
        }).collect(Collectors.toList());
        return PagingList.of(results,infoEntityIPage.getTotal());
    }

    @Override
    public List<FastDebugConfigResult> getConfigByIds(List<Long> ids) {
        LambdaQueryWrapper<FastDebugConfigInfoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(FastDebugConfigInfoEntity::getId,ids);
        wrapper.orderByDesc(FastDebugConfigInfoEntity::getGmtModified);
        List<FastDebugConfigInfoEntity> entities = fastDebugConfigInfoMapper.selectList(wrapper);
        if(entities == null || entities.size() == 0) {
            return Lists.newArrayList();
        }

        return entities.stream().map(entity -> {
            FastDebugConfigResult result = new FastDebugConfigResult();
            BeanUtils.copyProperties(entity,result);
            result.setContentTypeVo(JsonHelper.json2Bean(entity.getContentType(), ContentTypeVO.class));
            return result;
        }).collect(Collectors.toList());

    }

    @Override
    public PagingList<FastDebugResult> selectDebugList(FastDebugResultSearchParam param) {
        LambdaQueryWrapper<FastDebugResultEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FastDebugResultEntity::getCustomerId,param.getCustomerId());
        Page<FastDebugResultEntity> page = new Page<>(param.getCurrent() + 1, param.getPageSize());
        wrapper.orderByDesc(FastDebugResultEntity::getGmtModified);

        IPage<FastDebugResultEntity> infoEntityIPage = fastDebugResultMapper.selectPage(page,wrapper);
        if(CollectionUtils.isEmpty(infoEntityIPage.getRecords())) {
            return PagingList.empty();
        }
        List<FastDebugResult> results = infoEntityIPage.getRecords().stream().map(entity -> {
            FastDebugResult result = new FastDebugResult();
            BeanUtils.copyProperties(entity,result);
            return result;
        }).collect(Collectors.toList());
        return PagingList.of(results,infoEntityIPage.getTotal());
    }

    @Override
    public FastDebugResult insertDebugResult(FastDebugResultParam param) {
        FastDebugResultEntity entity = new FastDebugResultEntity();
        BeanUtils.copyProperties(param,entity);
        fastDebugResultMapper.insert(entity);
        FastDebugResult fastDebugResult = new FastDebugResult();
        BeanUtils.copyProperties(entity,fastDebugResult);
        return fastDebugResult;
    }

    @Override
    public void batchInsertException(List<FastDebugExceptionParam> params) {
        if (CollectionUtils.isEmpty(params)) {
            return;
        }
        List<FastDebugExceptionEntity> entities = FastDebugExceptionConvert.INSTANCE.ofList(params);
        customFastDebugExceptionMapper.saveBatch(entities);
    }

    @Override
    public void insertException(FastDebugExceptionParam param) {
        FastDebugExceptionEntity entity = FastDebugExceptionConvert.INSTANCE.of(param);
        customFastDebugExceptionMapper.save(entity);
    }

    @Override
    public List<FastDebugExceptionVO> getException(String traceId, String rpcId,String appName,String description) {
        LambdaQueryWrapper<FastDebugExceptionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FastDebugExceptionEntity::getTraceId,traceId);
        if(StringUtils.isNotBlank(appName)) {
            wrapper.eq(FastDebugExceptionEntity::getApplicationName,appName);
        }
        if(StringUtils.isNotBlank(rpcId)) {
            wrapper.eq(FastDebugExceptionEntity::getRpcId, rpcId);
        }
        if (StringUtils.isNotBlank(description)){
            wrapper.eq(FastDebugExceptionEntity::getDescription,description);
        }
        wrapper.orderByDesc(FastDebugExceptionEntity::getGmtModified);
        List<FastDebugExceptionEntity>  entities = customFastDebugExceptionMapper.list(wrapper);
        if(entities == null) {
            return Lists.newArrayList();
        }
        return  FastDebugExceptionConvert.INSTANCE.ofListResult(entities);
    }

    @Override
    public PagingList<FastDebugExceptionVO> getExceptionPage(FastDebugExceptionSearchParam param) {
        LambdaQueryWrapper<FastDebugExceptionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FastDebugExceptionEntity::getTraceId,param.getTraceId());
        wrapper.eq(FastDebugExceptionEntity::getCustomerId,param.getCustomerId());
        if(StringUtils.isNotBlank(param.getAppName())) {
            wrapper.eq(FastDebugExceptionEntity::getApplicationName,param.getAppName());
        }
        if(StringUtils.isNotBlank(param.getCode())) {
            wrapper.eq(FastDebugExceptionEntity::getCode,param.getCode());
        }
        if(StringUtils.isNotBlank(param.getType())) {
            wrapper.eq(FastDebugExceptionEntity::getType,param.getType());
        }
        wrapper.orderByDesc(FastDebugExceptionEntity::getGmtModified);
        Page<FastDebugExceptionEntity> page = new Page<>(param.getCurrent() + 1, param.getPageSize());
        IPage<FastDebugExceptionEntity> infoEntityIPage = customFastDebugExceptionMapper.page(page,wrapper);
        if(CollectionUtils.isEmpty(infoEntityIPage.getRecords())) {
            return PagingList.empty();
        }
        return PagingList.of(FastDebugExceptionConvert.INSTANCE.ofListVo(infoEntityIPage.getRecords()),infoEntityIPage.getTotal());
    }

    @Override
    public Long getExceptionCount(String traceId,Long customerId,String appName) {
        LambdaQueryWrapper<FastDebugExceptionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FastDebugExceptionEntity::getTraceId,traceId);
        wrapper.eq(FastDebugExceptionEntity::getCustomerId,customerId);
        if(StringUtils.isNotBlank(appName)) {
            wrapper.eq(FastDebugExceptionEntity::getApplicationName,appName);
        }
        wrapper.orderByDesc(FastDebugExceptionEntity::getGmtModified);
        List<FastDebugExceptionEntity> entities = customFastDebugExceptionMapper.list(wrapper);
        List<FastDebugExceptionVO> vos  = FastDebugExceptionConvert.INSTANCE.ofListResult(entities);
        return vos.stream().distinct().count();

    }

    @Override
    public void updateFastDebugResult(FastDebugResultUpdateParam param) {
        FastDebugResultEntity entity = new FastDebugResultEntity();
        BeanUtils.copyProperties(param,entity);
        fastDebugResultMapper.updateById(entity);
    }

    @Override
    public FastDebugResult selectDebugResultById(Long id) {
        FastDebugResultEntity entity = fastDebugResultMapper.selectById(id);
        if(entity == null) {
            return null;
        }
        FastDebugResult result = new FastDebugResult();
        BeanUtils.copyProperties(entity,result);
        return result;
    }

    @Override
    public void deleteFastDebugResultById(Long id) {
        fastDebugResultMapper.deleteById(id);
    }
}
