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

package io.shulie.tro.web.data.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.shulie.tro.web.data.pojo.dto.PageDTO;

/**
 * mybatis-plus 辅助工具类
 *
 * @param <T> 相对应实体类
 * @author liuchuan
 * @date 2019/6/19
 */
public interface MPUtil<T> {

    /* ---------------- 默认方法, 实现后直接调用 -------------- */

    /**
     * 设置分页参数
     *
     * @param pageDTO 分页参数类
     * @return mybatis plus page 类
     */
    default Page<T> setPage(PageDTO pageDTO) {
        return new Page<>(pageDTO.getPage(), pageDTO.getSize());
    }


    /**
     * 设置分页参数
     *
     * @param page 当前页数
     * @param size 每页大小
     * @return mybatis plus page 类
     */
    default Page<T> setPage(Integer page, Integer size) {
        return new Page<>(page, size);
    }

    /**
     * 设置分页参数, 无总数的返回
     *
     * @param pageDTO 分页参数类
     * @return mybatis plus page 类
     */
    default Page<T> setPageNoCount(PageDTO pageDTO) {
        return setPage(pageDTO, false);
    }

    /**
     * 设置分页参数, 根据入参, 返回是否需要总数的返回
     *
     * @param pageDTO 分页参数类
     * @param isSearchCount 是否需要总数
     * @return mybatis plus page 类
     */
    default Page<T> setPage(PageDTO pageDTO, boolean isSearchCount) {
        return new Page<>(pageDTO.getPage(), pageDTO.getSize(), isSearchCount);
    }

    /**
     * 获得 query 包装类
     *
     * @return query 普通包装类
     */
    default QueryWrapper<T> getQW() {
        return new QueryWrapper<>();
    }

    /**
     * 获得 query lambda 包装类
     *
     * @return query lambda 包装类
     */
    default LambdaQueryWrapper<T> getLQW() {
        return new LambdaQueryWrapper<>();
    }

    /**
     * 获得 update 包装类
     *
     * @return update 普通包装类
     */
    default UpdateWrapper<T> getUW() {
        return new UpdateWrapper<>();
    }

    /**
     * 获得 update lambda 包装类
     *
     * @return update lambda 包装类
     */
    default LambdaUpdateWrapper<T> getLUW() {
        return new LambdaUpdateWrapper<>();
    }

    /* ---------------- 静态方法, 类名.xx() 调用 -------------- */

    /**
     * 静态方法获得 query 包装类
     *
     * @param <T> 实体类
     * @return query 包装类
     */
    static <T> QueryWrapper<T> getQWStatic() {
        return new QueryWrapper<>();
    }

    /**
     * 静态方法获得 query lambda 包装类
     *
     * @param <T> 实体类
     * @return query lambda 包装类
     */
    static <T> LambdaQueryWrapper<T> getLQWStatic() {
        return new LambdaQueryWrapper<>();
    }

    /**
     * 静态方法 获得 update 包装类
     *
     * @param <T> 实体类
     * @return update 包装类
     */
    static <T> UpdateWrapper<T> getUWStatic() {
        return new UpdateWrapper<>();
    }


    /**
     * 静态方法 获得 update lambda 包装类
     *
     * @param <T> 实体类
     * @return update lambda 包装类
     */
    static <T> LambdaUpdateWrapper<T> getLUWStatic() {
        return new LambdaUpdateWrapper<>();
    }

}
