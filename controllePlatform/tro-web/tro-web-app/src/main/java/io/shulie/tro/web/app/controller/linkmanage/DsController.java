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
package io.shulie.tro.web.app.controller.linkmanage;

import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.pamirs.tro.common.enums.ds.DbTypeEnum;
import com.pamirs.tro.entity.dao.confcenter.TApplicationMntDao;
import com.pamirs.tro.entity.dao.user.TUserMapper;
import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import com.pamirs.tro.entity.domain.entity.simplify.AppBusinessTableInfo;
import com.pamirs.tro.entity.domain.entity.user.User;
import com.pamirs.tro.entity.domain.query.agent.AppBusinessTableQuery;
import io.shulie.tro.common.beans.annotation.ModuleDef;
import io.shulie.tro.web.app.annotation.AuthVerification;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.constant.BizOpConstants;
import io.shulie.tro.web.app.constant.BizOpConstants.OpTypes;
import io.shulie.tro.web.app.context.OperationLogContextHolder;
import io.shulie.tro.web.app.request.application.ApplicationDsCreateRequest;
import io.shulie.tro.web.app.request.application.ApplicationDsDeleteRequest;
import io.shulie.tro.web.app.request.application.ApplicationDsEnableRequest;
import io.shulie.tro.web.app.request.application.ApplicationDsUpdateRequest;
import io.shulie.tro.web.app.response.application.ApplicationDsDetailResponse;
import io.shulie.tro.web.app.service.dsManage.DsService;
import io.shulie.tro.web.auth.api.enums.ActionTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: fanxx
 * @Date: 2020/3/12 下午3:10
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
@Api(tags = "影子库表管理", value = "影子库表管理")
public class DsController  {

    @Value("${application.ds.config.is.new.version: false}")
    private Boolean isNewVersion;

    private static final String DATABASE = "影子库 URL：";

    private static final String TABLE = "影子表 URL：";

    private static final String SERVER = "影子server URL：";

    @Autowired
    private DsService dsService;

    @Resource
    private TApplicationMntDao applicationMntDao;

    @Resource
    private TUserMapper TUserMapper;

    /**
     * 添加影子库表配置
     *
     * @param createRequest
     * @return
     */
    @ApiOperation("影子库表添加接口")
    @PostMapping("link/ds/manage")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.APPLICATION_MANAGE,
        subModuleName = BizOpConstants.SubModules.SHADOW_DATABASE_TABLE,
        logMsgKey = BizOpConstants.Message.MESSAGE_SHADOW_DATABASE_TABLE_CREATE,
        opTypes = OpTypes.CREATE
    )
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
        needAuth = ActionTypeEnum.CREATE
    )
    public Response dsAdd(@RequestBody ApplicationDsCreateRequest createRequest) {
        return dsService.dsAdd(createRequest);
    }

    /**
     * 添加影子库表配置
     * @param createRequest
     * @return
     */
    @ApiOperation("影子库表添加接口-老版本")
    @PostMapping("link/ds/manage/old")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.APPLICATION_MANAGE,
        subModuleName = BizOpConstants.SubModules.SHADOW_DATABASE_TABLE,
        logMsgKey = BizOpConstants.Message.MESSAGE_SHADOW_DATABASE_TABLE_CREATE,
        opTypes = OpTypes.CREATE
    )
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
        needAuth = ActionTypeEnum.CREATE
    )
    public Response dsAddOld(@RequestBody ApplicationDsCreateRequest createRequest) {
        createRequest.setOldVersion(true);
        return this.dsAdd(createRequest);
    }




    /**
     * 初始化数据库加密配置
     * @return
     */
    @ApiOperation("加密数据库数据源配置")
    @PostMapping("link/ds/manage/secure/init")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.APPLICATION_MANAGE,
        subModuleName = BizOpConstants.SubModules.SHADOW_DATABASE_TABLE,
        logMsgKey = BizOpConstants.Message.MESSAGE_SHADOW_DATABASE_TABLE_UPDATE,
        opTypes = OpTypes.UPDATE
    )
    public Response secureInit() {
        return dsService.secureInit();
    }



    /**
     * 查询影子库表配置
     *
     * @return
     */
    @ApiOperation("查询影子库表配置")
    @GetMapping("link/ds/manage")
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
        needAuth = ActionTypeEnum.QUERY
    )
    public Response dsQuery(@RequestParam(value = "applicationId", required = true) Long applicationId) {
        return dsService.dsQuery(applicationId);
    }

    @ApiOperation("影子库表配置详情")
    @GetMapping("link/ds/manage/detail")
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
        needAuth = ActionTypeEnum.UPDATE
    )
    public Response dsQueryDetail(@RequestParam(value = "id") Long dsId) {
        return dsService.dsQueryDetail(dsId, false);
    }

    /**
     * 影子库表配置详情
     * @return
     */
    @ApiOperation("影子库表配置详情-老版本")
    @GetMapping("link/ds/manage/detail/old")
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
        needAuth = ActionTypeEnum.UPDATE
    )
    public Response dsQueryDetailOld(@RequestParam(value = "id", required = true) Long id) {
        return dsService.dsQueryDetail(id, true);
    }

    /**
     * 修改影子库表配置
     * @param updateRequest
     * @return
     */
    @ApiOperation("修改影子库表配置")
    @PutMapping("link/ds/manage")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.APPLICATION_MANAGE,
        subModuleName = BizOpConstants.SubModules.SHADOW_DATABASE_TABLE,
        logMsgKey = BizOpConstants.Message.MESSAGE_SHADOW_DATABASE_TABLE_UPDATE,
        opTypes = OpTypes.UPDATE
    )
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
        needAuth = ActionTypeEnum.UPDATE
    )
    public Response dsUpdate(@RequestBody ApplicationDsUpdateRequest updateRequest) {
        return dsService.dsUpdate(updateRequest);
    }

    /**
     * 修改影子库表配置
     *
     * @param updateRequest
     * @return
     */
    @ApiOperation("修改影子库表配置-老版本")
    @PutMapping("link/ds/manage/old")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.APPLICATION_MANAGE,
        subModuleName = BizOpConstants.SubModules.SHADOW_DATABASE_TABLE,
        logMsgKey = BizOpConstants.Message.MESSAGE_SHADOW_DATABASE_TABLE_UPDATE
    )
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
        needAuth = ActionTypeEnum.UPDATE
    )
    public Response dsUpdateOld(@RequestBody ApplicationDsUpdateRequest updateRequest) {
        updateRequest.setOldVersion(true);
        return this.dsUpdate(updateRequest);
    }

    /**
     * 启用禁用影子库表配置
     *
     * @param enableRequest
     * @return
     */
    @ApiOperation("启用禁用影子库表配置")
    @PutMapping("link/ds/enable")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.APPLICATION_MANAGE,
        subModuleName = BizOpConstants.SubModules.SHADOW_DATABASE_TABLE,
        logMsgKey = BizOpConstants.Message.MESSAGE_SHADOW_DATABASE_ENABLE_DISABLE
    )
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
        needAuth = ActionTypeEnum.ENABLE_DISABLE
    )
    public Response enableConfig(@RequestBody ApplicationDsEnableRequest enableRequest) {
        OperationLogContextHolder.operationType(
            Integer.valueOf(0).equals(enableRequest.getStatus()) ? OpTypes.ENABLE
                : OpTypes.DISABLE);
        Response<ApplicationDsDetailResponse> response = dsService.dsQueryDetail(enableRequest.getId(), false);
        if (null == response) {
            return Response.fail("影子库表不存在");
        }
        ApplicationDsDetailResponse data = response.getData();
        return dsService.enableConfig(enableRequest);
    }

    /**
     * 删除影子库表配置
     *
     * @param deleteRequest
     * @return
     */
    @ApiOperation("删除影子库表配置")
    @RequestMapping(value = "link/ds/manage", method = RequestMethod.DELETE)
    @ModuleDef(
        moduleName = BizOpConstants.Modules.APPLICATION_MANAGE,
        subModuleName = BizOpConstants.SubModules.SHADOW_DATABASE_TABLE,
        logMsgKey = BizOpConstants.Message.MESSAGE_SHADOW_DATABASE_TABLE_DELETE,
        opTypes = OpTypes.DELETE
    )
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
        needAuth = ActionTypeEnum.DELETE
    )
    public Response dsDelete(@RequestBody ApplicationDsDeleteRequest deleteRequest) {
        Response<ApplicationDsDetailResponse> response = dsService.dsQueryDetail(deleteRequest.getId(), false);
        if (!response.getSuccess()) {
            return response;
        }
        ApplicationDsDetailResponse data = response.getData();
        return dsService.dsDelete(deleteRequest);
    }

    @ApiOperation("agent上传影子表")
    @PostMapping(value = "link/ds/agent/report")
    public Response reportTable(@RequestBody Map<String, Set<String>> requestMap,
        @RequestParam("appName") String appName) {
        if (null == requestMap || requestMap.size() < 1) {
            return Response.fail("请求数据null");
        }
        User user = TUserMapper.queryByKey(RestContext.getTenantUserKey());
        if (null == user) {
            return Response.fail("当前用户不存在");
        }

        TApplicationMnt tApplicationMnt = applicationMntDao.queryApplicationinfoByNameTenant(appName, user.getId());
        if (null == tApplicationMnt) {
            return Response.fail("应用信息不存在");
        }

        try {
            for (Map.Entry<String, Set<String>> entry : requestMap.entrySet()) {
                StringBuilder tablesBuilder = new StringBuilder();
                String url = entry.getKey();
                for (String table : entry.getValue()) {
                    tablesBuilder.append(table).append(",");
                }

                String table = tablesBuilder.toString();
                if (table.contains(",")) {
                    table = table.substring(0, table.length() - 1);
                }
                ApplicationDsCreateRequest createRequest = new ApplicationDsCreateRequest();
                createRequest.setApplicationId(tApplicationMnt.getApplicationId());
                createRequest.setApplicationName(tApplicationMnt.getApplicationName());
                createRequest.setDbType(DbTypeEnum.DB.getCode());
                createRequest.setDsType(1);
                createRequest.setConfig(table);
                createRequest.setUrl(url);
                dsService.dsAdd(createRequest);
                AppBusinessTableInfo info = new AppBusinessTableInfo();
                info.setUrl(url);
                info.setTableName(table);
                info.setApplicationId(tApplicationMnt.getApplicationId());
                info.setUserId(user.getId());
                dsService.addBusiness(info);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.fail(e.getMessage());
        }
        return Response.success();
    }

    @ApiOperation("查询agent上传业务表信息")
    @GetMapping(value = "link/ds/business/query")
    public Response queryPage(@RequestParam("pageSize") Integer pageSize,
        @RequestParam("pageNum") Integer pageNum,
        @RequestParam("applicationId") Long applicationId) {
        try {
            AppBusinessTableQuery query = new AppBusinessTableQuery();
            query.setPageNum(pageNum);
            query.setPageSize(pageSize);
            query.setApplicationId(applicationId);
            return dsService.queryPageBusiness(query);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.fail(e.getMessage());
        }
    }

}
