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

package io.shulie.tro.web.app.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.pamirs.tro.common.constant.TROConstantEnum;
import com.pamirs.tro.common.exception.TROModuleException;
import com.pamirs.tro.common.util.PageInfo;
import com.pamirs.tro.entity.dao.dict.TDictionaryDataMapper;
import com.pamirs.tro.entity.domain.entity.TDictionaryData;
import com.pamirs.tro.entity.domain.entity.TDictionaryType;
import com.pamirs.tro.entity.domain.vo.TDictionaryVo;
import io.shulie.tro.web.app.common.CommonService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 说明：数据字典服务
 *
 * @author shulie
 * @version 1.0
 * @create 2018/10/31 0031 16:46
 */
@Service
public class DictionaryMntService extends CommonService {

    @Autowired
    private TDictionaryDataMapper tDictionaryDataMapper;

    /**
     * 说明: 保存数据字典
     *
     * @param tDictionaryVo
     * @return void
     * @author shulie
     * @date 2018/10/31 0031 17:06
     */
    @Transactional(value = "troTransactionManager", rollbackFor = Exception.class)
    public void saveDictionary(TDictionaryVo tDictionaryVo) throws TROModuleException {
        String userName = null;

        //判断类型名称是否已经存在
        TDictionaryType tDictionaryType = tDictionaryTypeMapper.selectDictionaryByTypeAlias(
            tDictionaryVo.getTypeAlias());

        if (tDictionaryType == null) {
            //1，保存到t_dictionary_type表中
            tDictionaryType = new TDictionaryType();
            tDictionaryType.setCreateUserCode(userName);
            tDictionaryType.setModifyUserCode(userName);
            tDictionaryType.setTypeAlias(tDictionaryVo.getTypeAlias());
            tDictionaryType.setTypeName(tDictionaryVo.getTypeName());
            tDictionaryType.setActive(tDictionaryVo.getTypeActive());
            tDictionaryTypeMapper.insert(tDictionaryType);
        }
        // 2, 保存到t_dictionary_data表中
        TDictionaryData tDictionaryData = new TDictionaryData();
        tDictionaryData.setDictType(tDictionaryType.getId());
        tDictionaryData.setValueCode(tDictionaryVo.getValueCode());
        tDictionaryData.setValueName(tDictionaryVo.getValueName());
        tDictionaryData.setValueOrder(Integer.valueOf(tDictionaryVo.getValueOrder()));
        tDictionaryData.setLanguage(tDictionaryVo.getLanguage());
        tDictionaryData.setActive(tDictionaryVo.getValueActive());

        tDictionaryData.setCreateUserCode(userName);
        tDictionaryData.setModifyUserCode(userName);
        tDictionaryDataMapper.insert(tDictionaryData);

    }

    /**
     * 1，更新数据字典类型中的类型名称、类型别名、是否可维护
     * 2，更新数据字典值中的值顺序、值名称、值代码、是否激活
     *
     * @param tDictionaryVo
     */
    public void updateDictionary(TDictionaryVo tDictionaryVo) {
        tDictionaryDataMapper.updateDictionary(tDictionaryVo);
    }

    /**
     * 查询数据字典列表
     *
     * @param paramMap
     * @return
     */
    public PageInfo<TDictionaryVo> queryDictionaryList(Map<String, Object> paramMap) {
        PageHelper.startPage(PageInfo.getPageNum(paramMap), PageInfo.getPageSize(paramMap));
        List<TDictionaryVo> dictionaryVoList = tDictionaryDataMapper.queryDictionaryList(paramMap);

        return new PageInfo<>(dictionaryVoList);
    }

    /**
     * 查询数据字典详情
     *
     * @param tDictionaryId
     * @return
     */
    public TDictionaryVo queryDictionaryDetail(String tDictionaryId) {
        return tDictionaryDataMapper.queryDictionaryDetail(tDictionaryId);
    }

    /**
     * 删除数据字典值
     * 1, 删除后，如果数据字典类型中还有值
     * 2，删除后，如果数据字典类型中没有值了，也需要把数据字典类型删除
     *
     * @param tDictionaryIdList
     */
    public void deleteDictionary(String tDictionaryIdList) {

        tDictionaryDataMapper.deleteDictionary(Splitter.on(",").trimResults().omitEmptyStrings()
            .splitToList(tDictionaryIdList));
        //判断字典类型有无对应的字典值，没有则删除。
        //        DELETE t FROM t_dictionary_type t LEFT JOIN t_dictionary_data d ON t.ID = d.DICT_TYPE WHERE d.ID IS
        //        NULL
        tDictionaryDataMapper.deleteEmptyDictType();
    }

    /**
     * 查询数据字典KEY_VALUE值
     *
     * @param dictCode
     * @return
     */
    public Map<String, Object> queryDictionaryKeyValue(String dictCode) {
        List<Map<String, Object>> dictMaps = tDicDao.queryDicList(StringUtils.upperCase(dictCode));
        if (dictMaps.isEmpty()) {
            return Maps.newHashMap();
        }
        Map<String, Object> resultMap = Maps.newTreeMap(Comparator.comparingInt(Integer::parseInt));
        dictMaps.forEach(map -> resultMap.put(MapUtils.getString(map, TROConstantEnum.VALUE_ORDER.toString()),
            MapUtils.getString(map, TROConstantEnum.VALUE_NAME.toString())));
        return resultMap;
    }
}
