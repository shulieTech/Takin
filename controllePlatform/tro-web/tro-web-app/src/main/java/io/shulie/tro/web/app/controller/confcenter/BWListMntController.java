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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.pamirs.tro.common.ResponseError;
import com.pamirs.tro.common.ResponseOk;
import com.pamirs.tro.common.constant.TRODictTypeEnum;
import com.pamirs.tro.common.constant.TROErrorEnum;
import com.pamirs.tro.common.exception.TROModuleException;
import com.pamirs.tro.entity.domain.entity.TWList;
import com.pamirs.tro.entity.domain.query.TWListVo;
import com.pamirs.tro.entity.domain.vo.TApplicationInterface;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.service.ConfCenterService;
import io.shulie.tro.web.app.utils.ExcelUtil;
import io.swagger.annotations.Api;
import org.apache.commons.collections4.CollectionUtils;
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
 * 说明: 黑白名单管理接口
 *
 * @author shulie
 * @version v1.0
 * @2018年4月13日
 */
@Api(tags = "黑白名单管理接口")
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
public class BWListMntController {
    private final Logger LOGGER = LoggerFactory.getLogger(BWListMntController.class);

    @Autowired
    private ConfCenterService confCenterService;

    /**
     * 说明: API.01.02.001 添加白名单接口
     *
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @PostMapping(value = APIUrls.API_TRO_CONFCENTER_ADD_WLIST_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> saveWList(@RequestBody @Valid TWListVo twlistVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseError.create(1010200101, bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            List<String> list = confCenterService.saveWList(twlistVo);
            if (CollectionUtils.isEmpty(list)) {
                return ResponseOk.create("succeed");
            } else {
                String join = Joiner.on(",").skipNulls().join(list);
                return ResponseOk.create(join + " 已经存在,请勿重新添加");
            }
        } catch (TROModuleException e) {
            LOGGER.error("BWListMntController.saveWList 新增白名单异常{}", e);
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            LOGGER.error("BWListMntController.saveWList 新增白名单异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_ADD_WLIST_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_ADD_WLIST_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 白名单文件上传
     *
     * @param files
     * @return
     */
    @PostMapping(value = APIUrls.API_TRO_CONFCENTER_UPLOAD_WLIST_URI)
    public ResponseEntity<Object> uploadWList(@RequestParam(value = "file") MultipartFile[] files) throws Exception {

        try {
            new ExcelUtil().verify(files);
            confCenterService.batchUploadWList(files);
            return ResponseEntity.ok("success");
        } catch (TROModuleException e) {
            return ResponseError.create(500, e.getErrorMessage());
        } catch (Exception e) {
            return ResponseError.create(500, e.getMessage());
        }
    }

    /**
     * 白名单导出接口
     *
     * @param response
     * @param applicationName 应用名
     * @param principalNo     负责人工号
     * @param type            白名单类型
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_EXCEL_DOWNLOAD_URI)
    public void whiteListDownload(HttpServletResponse response,
        String applicationName,
        String principalNo,
        String type,
        String whiteListUrl,
        String wlistIds) {
        try {
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("applicationName", applicationName);
            paramMap.put("principalNo", principalNo);
            paramMap.put("type", type);
            paramMap.put("whiteListUrl", whiteListUrl);
            if (StringUtils.isNotBlank(wlistIds)) {
                paramMap.put("wlistIds", Arrays.asList(wlistIds.split(",")));
            }
            ExcelUtil<TApplicationInterface> excelUtil = new ExcelUtil<>();
            excelUtil.export(response, confCenterService.queryWListDownLoad(paramMap), null, "白名单管理");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * 说明: API.01.02.002 查询白名单列表
     *
     * @return 成功, 则返回白名单列表, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @PostMapping(value = APIUrls.API_TRO_CONFCENTER_QUERY_WLIST_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryWList(@RequestBody Map<String, Object> paramMap) {
        try {
            return ResponseOk.create(confCenterService.queryWList(paramMap));
        } catch (Exception e) {
            LOGGER.error("BWListMntController.queryBList 查询白名单列表异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_QUERY_WLIST_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_QUERY_WLIST_EXCEPTION.getErrorMessage());
        }
    }

    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_QUERY_WLIST_4AGENT_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseOk.ResponseResult queryWList4Agent() {
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        try {
            result = confCenterService.queryBWList("");
        } catch (Exception e) {
            LOGGER.error("BWListMntController.queryBList 查询白名单列表异常{}", e);
        }
        return ResponseOk.result(result);
    }

    /**
     * 说明: API.01.02.003 根据id查询白名单详情接口
     *
     * @return 成功, 则返回白名单xiangqing, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_QUERY_WLISTBYID_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> querySingleWListById(@RequestParam("wlistId") String wlistId) {
        if (StringUtils.isEmpty(wlistId)) {
            return ResponseError.create(TROErrorEnum.CONFCENTER_QUERY_WLISTBYID_PARAMLACK.getErrorCode(),
                TROErrorEnum.CONFCENTER_QUERY_WLISTBYID_PARAMLACK.getErrorMessage());
        }
        try {
            return ResponseOk.create(confCenterService.querySingleWListById(wlistId));
        } catch (TROModuleException e) {
            LOGGER.error("ConfCenterController.querySingleWListById 该白名单信息不存在{}", e);
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            LOGGER.error("BWListMntController.querySingleWListById  根据id查询白名单信息异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_QUERY_WLISTBYID_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_QUERY_WLISTBYID_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.01.02.004 根据id更新白名单接口
     *
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @PostMapping(value = APIUrls.API_TRO_CONFCENTER_UPDATE_WLIST_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> updateWListById(@RequestBody @Valid TWList tWList, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseError.create(1010200401, bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            confCenterService.updateWListById(tWList);
            return ResponseOk.create("succeed");
        } catch (TROModuleException e) {
            LOGGER.error("ConfCenterController.updateWListById 该白名单信息不存在{}", e);
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            LOGGER.error("BWListMntController.updateWListById  根据id更新白名单信息异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_UPDATE_WLIST_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_UPDATE_WLIST_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.01.02.005 批量删除白名单接口
     *
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_DELETE_WLIST_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> deleteWListByIds(@RequestParam("wlistIds") String wlistIds) {
        try {
            String diableDeleteWList = confCenterService.deleteWListByIds(wlistIds);
            if (StringUtils.isNotEmpty(diableDeleteWList)) {
                return ResponseError.create("该白名单{ " + diableDeleteWList + " }在基础链路中使用,不允许删除");
            } else {
                return ResponseOk.create("succeed");
            }

        } catch (Exception e) {
            LOGGER.error("BWListMntController.deleteWListByIds  删除白名单信息异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_DELETE_WLIST_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_DELETE_WLIST_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.01.02.006 查询白名单字典列表接口
     *
     * @return 成功, 则返回白名单字典列表;失败则返回错误编码和错误信息
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_DIC_QUERY_WLIST_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryWListDic() {
        try {
            return ResponseOk.create(confCenterService.queryDicList(TRODictTypeEnum.WLIST));
        } catch (Exception e) {
            LOGGER.error("BWListMntController.queryWListDic  查询白名单字典列表异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_QUERY_WLISTDIC_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_QUERY_WLISTDIC_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.01.02.007 根据appname查询该应用下的白名单
     *
     * @param paramMap applicationName：应用名称
     *                 interfaceName：接口名称,当接口名称为空时,不进行模糊查询(非必填)
     *                 pageNum：当前页(必填)
     *                 pageSize：每页显示大小(必填)
     *                 type：接口类型(必填包含http,dubbo,job)
     * @return 成功, 则返回白名单字典列表;失败则返回错误编码和错误信息
     * 响应结果：{
     * "code": 200,
     * "message": "succeed",
     * "data": {
     * "total": 100,
     * "data": [
     * "http://dpjjwms.pamirs.com/dpjjwms/76B060FA478E7934EC1BDBF7963FEF6C.cache.html"
     * ],
     * "pageSize": 10,
     * "type": "http",
     * "pageNum": 1,
     * "applicationName": "NVAS-vas-cas-web"
     * }
     * }
     * @author shulie
     * @date 2019/3/1 14:42
     */
    @PostMapping(value = APIUrls.API_TRO_CONFCENTER_QUERY_WLISTBYAPPNAME_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryWListByAppName(@RequestBody Map<String, Object> paramMap) {
        try {
            return ResponseOk.create(confCenterService.queryWListByAppName(paramMap));
        } catch (Exception e) {
            LOGGER.error("BWListMntController.queryWListByAppName  查询异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_QUERY_WLISTBYAPPNAME_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_QUERY_WLISTBYAPPNAME_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.01.02.008 根据应用ID查询该应用下的白名单列表
     *
     * @param applicationId 应用ID
     * @return 响应：
     * {
     * code: 200
     * data: [
     * {
     * wlistId: 604,
     * interfaceName: "com.test.pradar.service.DubboTestDemoService#test"
     * },
     * {
     * wlistId: 601,
     * interfaceName: "http://127.0.0.1:8080/pradar_test2/webservice/testDemoCxfService/testCxf"
     * }
     * ]
     * message: "succeed"
     * }
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_QUERY_WLISTBYAPPID_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryWListByAppId(@RequestParam("applicationId") String applicationId) {
        if (StringUtils.isBlank(applicationId)) {
            return ResponseError.create(TROErrorEnum.CONFCENTER_QUERY_WLISTBYAPPID_PARAM_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_QUERY_WLISTBYAPPID_PARAM_EXCEPTION.getErrorMessage());
        }
        try {
            return ResponseOk.create(confCenterService.queryWListByAppId(applicationId));
        } catch (Exception e) {
            LOGGER.error("BWListMntController.queryWListByAppId  查询异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_QUERY_WLISTBYAPPID_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_QUERY_WLISTBYAPPID_EXCEPTION.getErrorMessage());
        }
    }

    //=============================== 黑名单管理  ===============================

    ///**
    // * 说明: API.01.02.007 添加黑名单接口
    // *
    // * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
    // * @author shulie
    // */
    //@Deprecated
    //@ApiOperation(value = "添加黑名单接口")
    //@PostMapping(value = APIUrls.API_TRO_CONFCENTER_ADD_BLIST_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    //@ModuleDef(
    //    moduleName = BizOpConstants.Modules.CONFIG_CENTER,
    //    subModuleName = BizOpConstants.SubModules.BLACKLIST,
    //    logMsgKey = BizOpConstants.Message.MESSAGE_BLACKLIST_CREATE
    //)
    //@AuthVerification(
    //        moduleCode = BizOpConstants.ModuleCode.BLACKLIST,
    //        needAuth = ActionTypeEnum.CREATE
    //)
    //public Response saveBList(@RequestBody @Valid TBList tBList, BindingResult bindingResult) {
    //    if (bindingResult.hasErrors()) {
    //        return Response.fail("0", bindingResult.getFieldError().getDefaultMessage());
    //    }
    //    OperationLogContextHolder.operationType(BizOpConstants.OpTypes.CREATE);
    //    OperationLogContextHolder.addVars(BizOpConstants.Vars.REDIS_KEY, tBList.getRedisKey());
    //    try {
    //        confCenterService.saveBList(tBList);
    //        return Response.success();
    //    } catch (TROModuleException e) {
    //        LOGGER.error("ConfCenterController.queryWList 应用已经存在,请勿重新添加 {}", e);
    //        return Response.fail("0", e.getErrorMessage());
    //    } catch (Exception e) {
    //        LOGGER.error("BWListMntController.saveBList 新增黑名单异常{}", e);
    //        return Response.fail("0", TROErrorEnum.CONFCENTER_ADD_BLIST_EXCEPTION.getErrorMessage());
    //    }
    //}

    ///**
    // * 说明: API.01.02.008 查询黑名单列表
    // *
    // * @return 成功, 则返回黑名单列表, 失败则返回错误编码和错误信息
    // * @author shulie
    // */
    //@Deprecated
    //@ApiOperation(value = "查询黑名单列表")
    //@RequestMapping(method = RequestMethod.GET, value = APIUrls.API_TRO_CONFCENTER_QUERY_BLIST_URI,
    //    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    //@AuthVerification(
    //        moduleCode = BizOpConstants.ModuleCode.BLACKLIST,
    //        needAuth = ActionTypeEnum.QUERY
    //)
    //public Response queryBList(@ApiParam(name = "redisKey", value = "Redis Key名称") String redisKey,
    //    Integer current,
    //    Integer pageSize) {
    //    try {
    //        current = current + 1;
    //        BListQueryParam bListQueryParam = new BListQueryParam();
    //        bListQueryParam.setCurrentPage(current);
    //        bListQueryParam.setPageSize(pageSize);
    //        bListQueryParam.setRedisKey(redisKey);
    //        return confCenterService.queryBList(bListQueryParam);
    //    } catch (Exception e) {
    //        LOGGER.error("BWListMntController.queryBList 查询黑名单列表异常{}", e);
    //        return Response.fail(String.valueOf(TROErrorEnum.CONFCENTER_QUERY_BLIST_EXCEPTION.getErrorCode()),
    //            TROErrorEnum.CONFCENTER_QUERY_BLIST_EXCEPTION.getErrorMessage());
    //    }
    //}

    ///**
    // * 说明: API.01.02.009 根据id查询黑名单详情接口
    // *
    // * @return 成功, 则返回当个黑名单详情, 失败则返回错误编码和错误信息
    // * @author shulie
    // */
    //@Deprecated
    //@ApiOperation(value = "根据id查询黑名单详情接口")
    //@ApiImplicitParam(name = "blistId", value = "黑名单编号")
    //@GetMapping(value = APIUrls.API_TRO_CONFCENTER_QUERY_BLISTBYID_URI,
    //    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    //@AuthVerification(
    //        moduleCode = BizOpConstants.ModuleCode.BLACKLIST,
    //        needAuth = ActionTypeEnum.QUERY
    //)
    //public Response querySingleBListById(@RequestParam("blistId") String blistId) {
    //    try {
    //        return Response.success(confCenterService.querySingleBListById(blistId));
    //    } catch (Exception e) {
    //        LOGGER.error("BWListMntController.querySingleBListById  根据id查询黑名单信息异常{}", e);
    //        return Response.fail("0", TROErrorEnum.CONFCENTER_QUERY_SINGLEBLISTBYID_EXCEPTION.getErrorMessage());
    //    }
    //}

    ///**
    // * 说明: API.01.02.010 根据id更新黑名单接口
    // *
    // * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
    // * @author shulie
    // */
    //@Deprecated
    //@ApiOperation(value = "根据id更新黑名单接口")
    //@PutMapping(value = APIUrls.API_TRO_CONFCENTER_UPDATE_BLIST_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    //@ModuleDef(
    //    moduleName = BizOpConstants.Modules.CONFIG_CENTER,
    //    subModuleName = BizOpConstants.SubModules.BLACKLIST,
    //    logMsgKey = BizOpConstants.Message.MESSAGE_BLACKLIST_UPDATE
    //)
    //@AuthVerification(
    //        moduleCode = BizOpConstants.ModuleCode.BLACKLIST,
    //        needAuth = ActionTypeEnum.UPDATE
    //)
    //public Response updateBListById(@RequestBody @Valid TBList tBList, BindingResult bindingResult) {
    //    if (bindingResult.hasErrors()) {
    //        return Response.fail("0", Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    //    }
    //    OperationLogContextHolder.operationType(BizOpConstants.OpTypes.UPDATE);
    //    OperationLogContextHolder.addVars(BizOpConstants.Vars.REDIS_KEY, tBList.getRedisKey());
    //    try {
    //        confCenterService.updateBListById(tBList);
    //        return Response.success();
    //    } catch (Exception e) {
    //        LOGGER.error("BWListMntController.updateBListById  根据id更新黑名单信息异常{}", e);
    //        return Response.fail("0", TROErrorEnum.CONFCENTER_UPDATE_BLIST_EXCEPTION.getErrorMessage());
    //    }
    //}

    ///**
    // * 说明: API.01.02.010 根据id启用禁用黑名单接口
    // *
    // * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
    // * @author shulie
    // */
    //@Deprecated
    //@ApiOperation(value = "根据id启用禁用黑名单接口")
    //@PutMapping(value = APIUrls.API_TRO_CONFCENTER_USEYN_BLIST_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    //@ModuleDef(
    //    moduleName = BizOpConstants.Modules.CONFIG_CENTER,
    //    subModuleName = BizOpConstants.SubModules.BLACKLIST,
    //    logMsgKey = BizOpConstants.Message.MESSAGE_BLACKLIST_ACTION
    //)
    //@AuthVerification(
    //        moduleCode = BizOpConstants.ModuleCode.BLACKLIST,
    //        needAuth = ActionTypeEnum.ENABLE_DISABLE
    //)
    //public Response updateStatusBListById(@RequestBody @Valid TBList tBList, BindingResult bindingResult) {
    //    if (bindingResult.hasErrors()) {
    //        return Response.fail("0", Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    //    }
    //    TBList tbList = confCenterService.querySingleBListById(String.valueOf(tBList.getBlistId()));
    //    OperationLogContextHolder.operationType(
    //        "1".equals(tBList.getUseYn()) ? BizOpConstants.OpTypes.ENABLE : BizOpConstants.OpTypes.DISABLE);
    //    OperationLogContextHolder.addVars(BizOpConstants.Vars.ACTION,
    //        "1".equals(tBList.getUseYn()) ? BizOpConstants.OpTypes.ENABLE : BizOpConstants.OpTypes.DISABLE);
    //    OperationLogContextHolder.addVars(BizOpConstants.Vars.REDIS_KEY, tbList.getRedisKey());
    //    try {
    //        confCenterService.updateBListById(tBList);
    //        return Response.success();
    //    } catch (Exception e) {
    //        LOGGER.error("BWListMntController.updateBListById  根据id更新黑名单信息异常{}", e);
    //        return Response.fail("0", TROErrorEnum.CONFCENTER_UPDATE_BLIST_EXCEPTION.getErrorMessage());
    //    }
    //}

    ///**
    // * 说明: API.01.02.011 批量删除黑名单接口
    // *
    // * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
    // * @author shulie
    // */
    //@Deprecated
    //@ApiOperation(value = "批量删除黑名单接口")
    //@DeleteMapping(value = APIUrls.API_TRO_CONFCENTER_DELETE_BLIST_URI,
    //    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    //@ModuleDef(
    //    moduleName = BizOpConstants.Modules.CONFIG_CENTER,
    //    subModuleName = BizOpConstants.SubModules.BLACKLIST,
    //    logMsgKey = BizOpConstants.Message.MESSAGE_BLACKLIST_DELETE
    //)
    //@AuthVerification(
    //        moduleCode = BizOpConstants.ModuleCode.BLACKLIST,
    //        needAuth = ActionTypeEnum.DELETE
    //)
    //public Response deleteBListByIds(@RequestBody @Valid TBListDelete tbListDelete) {
    //    try {
    //        if (CollectionUtils.isEmpty(tbListDelete.getBlistIds())) {
    //            return Response.fail("0", "黑名单id不能为空");
    //        }
    //        List<TBList> tbListList = confCenterService.queryBListByIds(tbListDelete.getBlistIds());
    //        if (CollectionUtils.isEmpty(tbListList)) {
    //            return Response.success();
    //        }
    //        OperationLogContextHolder.operationType(BizOpConstants.OpTypes.DELETE);
    //        OperationLogContextHolder.addVars(BizOpConstants.Vars.REDIS_KEY,
    //                tbListList.stream().map(TBList::getRedisKey).collect(Collectors.joining(",")));
    //        confCenterService.deleteBListByIds(StringUtils.join(tbListDelete.getBlistIds().toArray(), ","));
    //        return Response.success();
    //    } catch (Exception e) {
    //        LOGGER.error("BWListMntController.deleteBListByIds  删除黑名单信息异常{}", e);
    //        return Response.fail("0", TROErrorEnum.CONFCENTER_DELETE_BLIST_EXCEPTION.getErrorMessage());
    //    }
    //}
}
