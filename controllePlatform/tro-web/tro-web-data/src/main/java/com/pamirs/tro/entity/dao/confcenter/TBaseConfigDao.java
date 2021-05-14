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

package com.pamirs.tro.entity.dao.confcenter;

import com.pamirs.tro.entity.domain.entity.TBaseConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * tro基础配置
 *
 * @author 298403
 * @date 2019-03-28
 */

@Mapper
public interface TBaseConfigDao {
    /**
     * 通过配置编码 主键删除
     *
     * @param configCode
     * @return
     */
    int deleteByPrimaryKey(String configCode);

    /**
     * 插入一条数据
     *
     * @param record
     * @return
     */
    int insert(TBaseConfig record);

    /**
     * 不为null的值 选择性插入
     *
     * @param record
     * @return
     */
    int insertSelective(TBaseConfig record);

    /**
     * 通过主键 配置编码 搜索
     *
     * @param configCode
     * @return
     */
    TBaseConfig selectByPrimaryKey(String configCode);

    /**
     * 根据主键 配置编码 选择性字段更新
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(TBaseConfig record);

    /**
     * 根据主键 配置编码 全部更新
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(TBaseConfig record);
}
