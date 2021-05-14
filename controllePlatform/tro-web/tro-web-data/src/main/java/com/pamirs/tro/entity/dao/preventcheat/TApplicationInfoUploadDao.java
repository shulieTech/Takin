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

package com.pamirs.tro.entity.dao.preventcheat;

import java.util.List;
import java.util.Map;

import com.pamirs.tro.entity.domain.entity.TApplicationInfoUpload;
import org.apache.ibatis.annotations.Param;

/**
 * 应用信息上报
 *
 * @author 298403
 * @date 2019-03-28
 */
public interface TApplicationInfoUploadDao {
    /**
     * 根据主键删除
     *
     * @param taiuId
     * @return
     */
    int deleteByPrimaryKey(Long taiuId);

    /**
     * 全参数 插入 上传信息
     *
     * @param record
     * @return
     */
    int insert(TApplicationInfoUpload record);

    /**
     * 不为null的值  插入 上传信息
     *
     * @param record
     * @return
     */
    int insertSelective(TApplicationInfoUpload record);

    /**
     * 通过主键id 查询
     *
     * @param taiuId
     * @return
     */
    TApplicationInfoUpload selectByPrimaryKey(Long taiuId);

    /**
     * 通过主键选择性更新
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(TApplicationInfoUpload record);

    /**
     * 通过主键id 更新
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(TApplicationInfoUpload record);

    /**
     * 分页查寻 上传信息
     *
     * @param paramMap
     * @return
     */
    List<TApplicationInfoUpload> queryUploadInfoPage(Map<String, Object> paramMap);

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    int insertList(@Param("list") List<TApplicationInfoUpload> list);
}
