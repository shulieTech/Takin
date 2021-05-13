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

package io.shulie.tro.web.app.controller.confcenter;

import java.util.Map;

import javax.validation.Valid;

import com.pamirs.tro.common.ResponseError;
import com.pamirs.tro.common.ResponseOk;
import com.pamirs.tro.common.constant.TROErrorEnum;
import com.pamirs.tro.common.exception.TROModuleException;
import com.pamirs.tro.entity.domain.vo.TDictionaryVo;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.service.DictionaryMntService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 说明：数据字典模块入口
 *
 * @author shulie
 * @version 1.0
 * @create 2018/10/30 0030 15:53
 */
@Api(tags = "数据字典模块入口")
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
public class DictionaryMntController {
    private final Logger LOGGER = LoggerFactory.getLogger(DictionaryMntController.class);

    @Autowired
    private DictionaryMntService dictionaryMntService;

    /**
     * 说明: 10106001 新增数据字典
     *
     * @param tDictionaryVo
     * @param bindingResult
     * @return org.springframework.http.ResponseEntity<java.lang.Object>
     * @author shulie
     * @date 2018/11/6 0006 16:05
     */
    @PostMapping(value = APIUrls.API_TRO_CONFCENTER_SAVE_DICTIONARY_URL,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> saveDictionary(@RequestBody @Valid TDictionaryVo tDictionaryVo,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseError.create(1010600102, "新增数据字典参数异常");
        }
        try {
            dictionaryMntService.saveDictionary(tDictionaryVo);
            return ResponseOk.create("保存成功!");
        } catch (TROModuleException e) {
            LOGGER.error("DictionaryMntController.saveDictionary 保存数据字典异常{}", e);
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            LOGGER.error("DictionaryMntController.saveDictionary 保存数据字典异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_SAVE_DICTIONARY_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_SAVE_DICTIONARY_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: 10106002更新数据字典
     *
     * @param tDictionaryVo
     * @param bindingResult
     * @return org.springframework.http.ResponseEntity<java.lang.Object>
     * @author shulie
     * @date 2018/11/6 0006 16:05
     */
    @PostMapping(value = APIUrls.API_TRO_CONFCENTER_UPDATE_DICTIONARY_URL,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> updateDictionary(@RequestBody @Valid TDictionaryVo tDictionaryVo,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseError.create(1010600202, "更新数据字典参数异常");
        }
        try {
            dictionaryMntService.updateDictionary(tDictionaryVo);
            return ResponseOk.create("更新成功!");
        } catch (Exception e) {
            LOGGER.error("DictionaryMntController.updateDictionary 修改数据字典异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_UPDATE_DICTIONARY_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_UPDATE_DICTIONARY_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: 10106003 查询数据字典列表
     *
     * @param paramMap
     * @return org.springframework.http.ResponseEntity<java.lang.Object>
     * @author shulie
     * @date 2018/11/6 0006 16:03
     */
    @PostMapping(value = APIUrls.API_TRO_CONFCENTER_QUERY_DICTIONARY_LIST_URL,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryDictionaryList(@RequestBody Map<String, Object> paramMap) {

        try {
            return ResponseOk.create(dictionaryMntService.queryDictionaryList(paramMap));
        } catch (Exception e) {
            LOGGER.error("DictionaryMntController.queryDictionaryList 查询数据字典列表异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_QUERY_DICTIONARY_LIST_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_QUERY_DICTIONARY_LIST_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明：10106004 查询数据字典详情
     *
     * @param tDictionaryId 字典值ID
     * @return org.springframework.http.ResponseEntity<java.lang.Object>
     * @author shulie
     * @create 2018/12/17 16:45
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_QUERY_DICTIONARY_DETAIL_URL,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryDictionaryDetail(@RequestParam("tDictId") String tDictionaryId) {
        try {
            return ResponseOk.create(dictionaryMntService.queryDictionaryDetail(tDictionaryId));
        } catch (Exception e) {
            LOGGER.error("DictionaryMntController.queryDictionaryDetail 查询数据字典详情失败{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_QUERY_DICTIONARY_DETAIL_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_QUERY_DICTIONARY_DETAIL_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明：10106005 删除数据字典（批量）
     *
     * @param tDidctionaryIds 数据字典ID列表
     * @return org.springframework.http.ResponseEntity<java.lang.Object>
     * @author shulie
     * @create 2018/12/17 16:53
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_DELETE_DICTIONARY_URL,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> deleteDictionary(@RequestParam("tDictIds") String tDidctionaryIds) {
        try {
            dictionaryMntService.deleteDictionary(tDidctionaryIds);
            return ResponseOk.create("删除成功!");
        } catch (Exception e) {
            LOGGER.error("DictionaryMntController.deleteDictionary 删除数据字典失败{" + tDidctionaryIds + "}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_DELETE_DICTIONARY_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_DELETE_DICTIONARY_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: 10106006 查询数据字典key_value值
     *
     * @param dictCode
     * @return org.springframework.http.ResponseEntity<java.lang.Object>
     * @author shulie
     * @create 2019/6/12 14:51
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_KV_DICTIONARY_URL, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryDictionaryKeyValue(@RequestParam("dictCode") String dictCode) {
        try {
            Map<String, Object> dictValue = dictionaryMntService.queryDictionaryKeyValue(dictCode);
            return ResponseOk.create(dictValue);
        } catch (Exception e) {
            LOGGER.error("DictionaryMntController.queryDictionaryKeyValue 查询数据字典失败{" + dictCode + "}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_QUERY_DICTIONARY_VALUE_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_QUERY_DICTIONARY_VALUE_EXCEPTION.getErrorMessage());
        }
    }

}
