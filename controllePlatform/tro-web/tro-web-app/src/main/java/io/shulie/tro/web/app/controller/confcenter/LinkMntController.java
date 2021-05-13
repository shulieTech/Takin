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

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.google.common.collect.Maps;
import com.pamirs.tro.common.ResponseError;
import com.pamirs.tro.common.ResponseOk;
import com.pamirs.tro.common.constant.TRODictTypeEnum;
import com.pamirs.tro.common.constant.TROErrorEnum;
import com.pamirs.tro.common.exception.TROModuleException;
import com.pamirs.tro.entity.domain.vo.TLinkServiceMntVo;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.service.ConfCenterService;
import io.shulie.tro.web.app.utils.ExcelUtil;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.multipart.MultipartFile;

/**
 * 说明: 链路管理
 * 链路管理包含业务链路和技术链路,业务链路包含多个技术链路,业务链路也可是技术链路
 *
 * @author shulie
 * @version v1.0
 * @date 2018年4月13日
 */
@Api(tags = "链路管理")
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
public class LinkMntController {
    private final Logger LOGGER = LoggerFactory.getLogger(LinkMntController.class);

    @Autowired
    private ConfCenterService confCenterService;

    /**
     * 说明: API.01.03.001 添加基础链路信息接口
     *
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @PostMapping(value = APIUrls.API_TRO_CONFCENTER_ADD_LINK_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> saveBasicLink(@RequestBody @Valid TLinkServiceMntVo tLinkServiceMntVo,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseError.create(1010300101, bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            //            confCenterService.saveBasicLink(tLinkServiceMntVo);
            confCenterService.saveLink(tLinkServiceMntVo);
            return ResponseOk.create("succeed");
        } catch (TROModuleException e) {
            LOGGER.error("BasicLinkMntController.saveBasicLink 该基础链路名称已经存在,请勿重新添加 {}", e);
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            LOGGER.error("BasicLinkMntController.saveBasicLink 添加基础链路信息异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_ADD_LINK_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_ADD_LINK_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.01.03.002 查询基础链路信息列表接口
     *
     * @return 成功, 则返回基础链路信息列表, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @PostMapping(value = APIUrls.API_TRO_CONFCENTER_QUERY_LINKLIST_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryBasicLinkList(@RequestBody Map<String, Object> paramMap) {
        try {
            return ResponseOk.create(confCenterService.queryBasicLinkList(paramMap));
        } catch (Exception e) {
            LOGGER.error("BasicLinkMntController.queryBasicLinkList 查询链路列表异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_QUERY_LINKLIST_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_QUERY_LINKLIST_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 链路管理导出
     *
     * @param response
     * @param applicationName 应用名
     * @param interfaceName   接口名
     * @param linkName        链路名字
     * @param linkRank        链路等级
     * @param linkType        链路类型
     * @param principalNo     负责人工号
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_LINKLIST_DOWNLOAD)
    public void downloadLinkList(HttpServletResponse response, String applicationName,
        String interfaceName,
        String linkName,
        String linkRank,
        String linkType,
        String principalNo,
        String applicationIds) {
        try {
            Map paramMap = Maps.newHashMap();
            paramMap.put("applicationName", applicationName);
            paramMap.put("interfaceName", interfaceName);
            paramMap.put("linkName", linkName);
            paramMap.put("linkRank", linkRank);
            paramMap.put("linkType", linkType);
            paramMap.put("principalNo", principalNo);
            if (StringUtils.isNotBlank(applicationIds)) {
                paramMap.put("applicationIds", Arrays.asList(applicationIds.split(",")));
            }
            paramMap.put("pageSize", -1);
            new ExcelUtil().export(response, confCenterService.queryBasicLinkListDownload(paramMap), null, "链路管理");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * 链路管理上传
     * 模版:链路类型,目标rt,目标tps,目标RT-SA,目标成功率,选择二级链路,链路模块,链路名称,链路描述,阿斯旺场景 ID,链路等级,链路入口(HTTP接口),链路负责人工号
     * ,是否可用,计算单量方式,接口名称,接口描述
     *
     * @param files
     */
    @PostMapping(value = APIUrls.API_TRO_CONFCENTER_LINKLIST_UPLOAD)
    public ResponseEntity<Object> batchUploadLinkList(@RequestParam(value = "file") MultipartFile[] files)
        throws Exception {
        try {
            //校验文件
            new ExcelUtil().verify(files);
            confCenterService.batchUploadLinkList(files);
            return ResponseOk.create("succeed");
        } catch (TROModuleException e) {
            return ResponseError.create(500, e.getErrorMessage());
        } catch (Exception e) {
            return ResponseError.create(500, e.getMessage());
        }
    }

    /**
     * 说明: API.01.03.003 根据链路id查询基础链路信息详情接口
     *
     * @return 成功, 则返回链路详情, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_QUERY_LINKINFO_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryLinkByLinkId(@RequestParam("linkId") String linkId) {
        try {
            TLinkServiceMntVo queryLinkByLinkId = confCenterService.queryLinkByLinkId(linkId);
            return ResponseOk.create(queryLinkByLinkId);
        } catch (Exception e) {
            LOGGER.error("BasicLinkMntController.queryLinkByLinkId 查询链路详情异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_QUERY_LINKDETAIL_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_QUERY_LINKDETAIL_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.01.03.004 删除基础链路信息接口
     *
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_DELETE_LINKINFO_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> deleteBasicLinkByLinkIds(@RequestParam("linkIds") String linkIds) {
        try {
            if (StringUtils.isEmpty(linkIds)) {
                return ResponseError.create(TROErrorEnum.CONFCENTER_DELETE_LINK_PARAMLACK.getErrorCode(),
                    TROErrorEnum.CONFCENTER_DELETE_LINK_PARAMLACK.getErrorMessage());
            }
            String diableDeleteBasicLink = confCenterService.deleteLinkByLinkIds(linkIds);
            if (StringUtils.isNotEmpty(diableDeleteBasicLink)) {
                return ResponseError.create("该基础链路{" + diableDeleteBasicLink + "}在二级链路使用不允许删除");
            } else {
                return ResponseOk.create("succeed");
            }
        } catch (Exception e) {
            LOGGER.error("BasicLinkMntController.deleteLinkByLinkIds 删除链路信息异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_DELETE_LINK_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_DELETE_LINK_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.01.03.005 查询链路等级字典列表
     *
     * @return 成功, 则链路等级字典列表, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_QUERY_LINKRANK_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryLinkRankList() {
        try {
            return ResponseOk.create(confCenterService.queryDicList(TRODictTypeEnum.LINKRANK));
        } catch (Exception e) {
            LOGGER.error("BasicLinkMntController.queryLinkRankList 查询链路等级列表异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_QUERY_LINKRANK_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_QUERY_LINKRANK_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.01.03.006 根据id更新链路信息接口
     *
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @PostMapping(value = APIUrls.API_TRO_CONFCENTER_UPDATE_LINKINFO_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> updateLinkinfo(@RequestBody @Valid TLinkServiceMntVo tLinkServiceMntVo,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseError.create(1010300101, bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            //            confCenterService.updateLinkinfo(tLinkServiceMntVo);
            confCenterService.updateLinkInfo(tLinkServiceMntVo);
            return ResponseOk.create("succeed");
        } catch (TROModuleException e) {
            LOGGER.error("BasicLinkMntController.updateLinkinfo 添加链路的服务缺失{}", e);
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            LOGGER.error("BasicLinkMntController.updateLinkinfo 更新链路信息异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_UPDATE_LINKINFO_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_UPDATE_LINKINFO_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.01.03.007 删除链路服务接口
     *
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_DELETE_LINKINTERFACE_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> deleteLinkInterfaceByLinkServiceId(
        @RequestParam("linkServiceIds") String linkServiceIds) {
        try {
            if (StringUtils.isEmpty(linkServiceIds)) {
                return ResponseError.create(TROErrorEnum.CONFCENTER_DELETE_LINKSERVICE_PARAMLACK.getErrorCode(),
                    TROErrorEnum.CONFCENTER_DELETE_LINKSERVICE_PARAMLACK.getErrorMessage());
            }
            confCenterService.deleteLinkInterfaceByLinkServiceId(linkServiceIds);
            return ResponseOk.create("succeed");
        } catch (Exception e) {
            LOGGER.error("BasicLinkMntController.deleteLinkInterfaceByLinkServiceId 删除链路服务信息异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_DELETE_LINKSERVICE_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_DELETE_LINKSERVICE_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.01.03.008 查询链路类型字典列表
     *
     * @return 成功, 则链路类型字典列表, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_QUERY_LINKTYPE_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryLinkTypeList() {
        try {
            return ResponseOk.create(confCenterService.queryDicList(TRODictTypeEnum.LINK_TYPE));
        } catch (Exception e) {
            LOGGER.error("LinkMntController.queryLinkTypeList 查询链路类型列表异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_QUERY_LINKTYPE_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_QUERY_LINKTYPE_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * @param paramMap
     * @return
     */
    @PostMapping(value = APIUrls.API_TRO_CONFCENTER_QUERY_LINKBYLINKTYPE_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryLinkByLinkType(@RequestBody Map<String, Object> paramMap) {
        try {
            return ResponseOk.create(confCenterService.queryLinkByLinkType(paramMap));
        } catch (Exception e) {
            LOGGER.error("BasicLinkMntController.queryLinkByLinkType 查询链路列表失败{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_QUERY_LINKBYLINKTYPE_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_UPDATE_LINKINFO_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * @return
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_QUERY_LINKIDNAME_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryLinkIdNameList() {
        try {
            return ResponseOk.create(confCenterService.queryLinkIdName());
        } catch (Exception e) {
            LOGGER.error("BasicLinkMntController.queryLinkByLinkType 查询链路列表失败{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_QUERY_LINKBYLINKTYPE_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_UPDATE_LINKINFO_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.01.03.011 查询链路模块字典列表
     *
     * @return 成功, 则链路模块字典列表, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_QUERY_LINKMODULE_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryLinkModuleList() {
        try {
            return ResponseOk.create(confCenterService.queryDicList(TRODictTypeEnum.LINK_MODULE));
        } catch (Exception e) {
            LOGGER.error("LinkMntController.queryLinkModuleList 查询链路模块列表异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_QUERY_LINKMODULE_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_QUERY_LINKMODULE_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.01.03.012 查询白名单列表，给链路服务使用
     *
     * @return 成功, 则链路模块字典列表, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_QUERY_WHITELIST_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> getWhiteList() {
        try {
            return ResponseOk.create(confCenterService.getWhiteListForLink());
        } catch (Exception e) {
            LOGGER.error("LinkMntController.getWhiteList 查询链路模块列表异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_QUERY_WHITELIST_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_QUERY_WHITELIST_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.01.03.013 查询单量计算方式字典列表
     *
     * @return 成功, 则链路模块字典列表, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_QUERY_CALC_VOLUME_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryCalcVolumeList() {
        try {
            return ResponseOk.create(confCenterService.queryDicList(TRODictTypeEnum.VOLUME_CALC_STATUS));
        } catch (Exception e) {
            LOGGER.error("LinkMntController.queryCalcVolumeList 查询单量计算方式字典列表异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_QUERY_VOLUME_CALC_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_QUERY_VOLUME_CALC_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.01.03.014 查询链路头信息, 包含：
     * 链路所属模块、数量、计算单量列表
     * [
     * {
     * "name": "下单",
     * "order": "1",
     * "count": 5,
     * "calcVolumeLinkList": [
     * {
     * "linkId": "10000",
     * "linkName": "下单链路",
     * "volumeCalcStatus":"1",
     * "applicationName": "aaaa",
     * "interfaceName": "http://www.baidu.com",
     * "interfaceType": "http",
     * "secondLinkId": "000000",
     * "secondLinkName": "菜鸟下单"
     * }
     * ]
     * }
     * ]
     *
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_QUERY_LINK_HEADER_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryLinkHeaderList() {
        try {
            return ResponseOk.create(confCenterService.queryLinkHeaderInfoList());
        } catch (Exception e) {
            LOGGER.error("LinkMntController.queryLinkHeaderList 查询链路头信息异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_QUERY_LINK_HEADER_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_QUERY_LINK_HEADER_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.01.03.015 查询链路模块下有哪些二级链路
     *
     * @param linkModule 链路模块
     * @return org.springframework.http.ResponseEntity<java.lang.Object>
     * @author shulie
     * @create 2019/4/15 17:36
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_QUERY_SECONDLINK_BY_MODULE_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> querySecondLinkByModule(@RequestParam("linkModule") String linkModule) {
        try {
            long moduleValue = Long.parseLong(linkModule);
            if (StringUtils.isEmpty(linkModule) || moduleValue < 0) {
                return ResponseError.create(1010301401, "linkModule={" + linkModule + "}参数异常");
            }
        } catch (NumberFormatException e) {
            LOGGER.error("LinkMntController.querySecondLinkByModule 参数异常{}", e);
            return ResponseError.create(1010301401, "linkModule={" + linkModule + "}参数异常");
        }
        try {
            return ResponseOk.create(confCenterService.querySecondLinkByModule(linkModule));
        } catch (Exception e) {
            LOGGER.error("LinkMntController.querySecondLinkByModule 查询链路模块下有哪些二级链路异常{}", e);
            return ResponseError.create(
                TROErrorEnum.CONFCENTER_QUERY_LINK_SECONDLINK_BY_MODULE_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_QUERY_LINK_SECONDLINK_BY_MODULE_EXCEPTION.getErrorMessage());
        }
    }

}
