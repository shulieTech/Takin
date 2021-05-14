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

package io.shulie.tro.web.data.dao.tagmanage;

import java.util.List;

import io.shulie.tro.web.data.param.tagmanage.TagManageParam;
import io.shulie.tro.web.data.result.tagmanage.TagManageResult;

/**
 * @author zhaoyong
 */
public interface TagManageDAO {
    /**
     * 查询所有脚本标签
     *
     * @return
     */
    List<TagManageResult> selectAllScript();

    /**
     * 新增脚本tag
     *
     * @param tagManageParams
     * @return
     */
    List<Long> addScriptTags(List<TagManageParam> tagManageParams,Integer tagType);

    /**
     * 根据id批量查询脚本标签
     *
     * @param tagIds
     * @return
     */
    List<TagManageResult> selectScriptTagsByIds(List<Long> tagIds);

    /**
     * 查询所有数据源标签
     *
     * @return
     */
    List<TagManageResult> selectDataSourceTags();

    /**
     * 根据id批量查询数据源标签
     *
     * @return
     */
    List<TagManageResult> selectDataSourceTagsByIds(List<Long> tagIds);

    /**
     * 新增数据源tag
     *
     * @param tagManageParams
     * @return
     */
    List<Long> addDatasourceTags(List<TagManageParam> tagManageParams);

    /**
     *获取某类型的所有标签
     * @param type
     * @return
     */
    List<TagManageResult> selectTagByType(Integer type) ;
}
