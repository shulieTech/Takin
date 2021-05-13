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

package io.shulie.tro.web.data.dao.application.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.shulie.tro.utils.string.StringUtil;
import io.shulie.tro.web.common.secure.SecureUtil;
import io.shulie.tro.web.data.constant.DataConstants;
import io.shulie.tro.web.data.dao.application.ApplicationDsManageDAO;
import io.shulie.tro.web.data.mapper.mysql.ApplicationDsManageMapper;
import io.shulie.tro.web.data.model.mysql.ApplicationDsManageEntity;
import io.shulie.tro.web.data.util.MPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author loseself
 * @date 2021/3/26 6:04 下午
 **/
@Slf4j
@Service
public class ApplicationDsManageDAOImpl
        extends ServiceImpl<ApplicationDsManageMapper, ApplicationDsManageEntity>
        implements ApplicationDsManageDAO, MPUtil<ApplicationDsManageEntity> {

    /**
     * 数据解密密通用处理
     * @param entity 实体
     */
    private void des(ApplicationDsManageEntity entity) {
        String config = entity.getConfig();
        String parseConfig = entity.getParseConfig();
        try{
            if (StringUtil.isNotBlank(config)){
                entity.setConfig(SecureUtil.decrypt(config));
            }
            if (StringUtil.isNotBlank(parseConfig)){
                entity.setParseConfig(SecureUtil.decrypt(parseConfig));
            }
        }catch (Exception ex){
            log.warn("解密数据源配置失败，返回原始配置； 数据源ID：{}",entity.getId());
            entity.setConfig(config);
            entity.setParseConfig(parseConfig);
        }
    }

    @Override
    public List<ApplicationDsManageEntity> listByApplicationId(Long applicationId) {
        List<ApplicationDsManageEntity> list = this.list(
            this.getLQW().eq(ApplicationDsManageEntity::getApplicationId, applicationId));

        return processSecure(list);
    }

    @Override
    public ApplicationDsManageEntity getByApplicationIdAndUrl(Long applicationId, String url) {
        return this.getOne(this.getLQW().eq(ApplicationDsManageEntity::getApplicationId, applicationId)
                .eq(ApplicationDsManageEntity::getUrl, url).last(DataConstants.LIMIT_ONE));
    }

    @Override
    public List<ApplicationDsManageEntity> listByApplicationIdAndDsType(Long applicationId, Integer dsType) {

        List<ApplicationDsManageEntity> list = this.list(
            this.getLQW().eq(ApplicationDsManageEntity::getApplicationId, applicationId)
                .eq(ApplicationDsManageEntity::getDsType, dsType));

        return processSecure(list);
    }

    private List<ApplicationDsManageEntity> processSecure(List<ApplicationDsManageEntity> list) {
        list.stream().forEach((manageEntity) -> {
            des(manageEntity);
        });
        return list;
    }

    @Override
    public void updateAppName(Long applicationId,String appName) {
        LambdaUpdateWrapper<ApplicationDsManageEntity> wrapper = this.getLUW();
        wrapper.eq(ApplicationDsManageEntity::getApplicationId, applicationId);
        wrapper.set(ApplicationDsManageEntity::getApplicationName, appName);
        this.update(wrapper);
    }
}
