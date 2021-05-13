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

package com.pamirs.tro.entity.dao.common;

import java.util.List;

import com.pamirs.tro.entity.domain.query.Query;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author <a href="mailto:sanjing#shulie.io">mitsui</a>
 * @version 1.0
 * @since 2017/09/04
 */
@Mapper
public interface BaseDao<T> {

    /**
     * 属性值为空值时，不插入，使用数据库默认值
     *
     * @param record
     * @return
     */
    int insert(T record);

    /**
     * 属性值为空值时，不更新
     * 可防止属性不赋值时更新为空
     *
     * @param record
     * @return
     */
    int update(T record);

    /**
     * 逻辑删除，对应的是UPDATE STATUS=-1
     *
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     * 根据条件查询某一条记录
     *
     * @param record
     * @return 返回数据对象
     */
    T selectOne(T record);

    /**
     * 根据主键查询某条记录
     *
     * @param id
     * @return 返回数据对象
     */
    T selectOneById(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param query
     * @return
     */
    List<T> selectList(Query<T> query);

    /**
     * 根据查询条件获取记录数
     *
     * @param query
     * @return 记录数
     */
    long selectListCount(Query<T> query);

}
