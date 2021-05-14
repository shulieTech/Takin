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

import com.pamirs.tro.common.ResponseError;
import com.pamirs.tro.common.ResponseOk;
import com.pamirs.tro.common.exception.TROModuleException;
import com.pamirs.tro.entity.domain.vo.bottleneck.BottleCountVo;
import com.pamirs.tro.entity.domain.vo.bottleneck.BottleNeckDetailVo;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.service.LinkBottleneckService;
import io.shulie.tro.web.app.service.LinkTopologyInfoService;
import io.swagger.annotations.Api;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 说明: 链路拓扑图接口类
 *
 * @author shulie
 * @create 2019/6/12 19:12
 */
@Api(tags = "链路拓扑图接口类")
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
public class LinkTopologyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkTopologyController.class);

    @Autowired
    private LinkTopologyInfoService linkTopologyInfoService;
    @Autowired
    private LinkBottleneckService linkBottleneckService;

    /**
     * 导入拓扑图excel
     *
     * @param excel
     * @return
     */
    @PostMapping(value = APIUrls.API_TRO_LINKTOPOLOGY_IMPORT_EXCEL_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> importExel(@Param("excel") MultipartFile excel) {
        try {
            linkTopologyInfoService.importExcelData(excel);
            return ResponseOk.create("上传成功");
        } catch (TROModuleException e) {
            LOGGER.error("LinkTopologyController.importExcel" + e.getMessage(), e);
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            LOGGER.error("LinkTopologyController.importExcel 上传excel失败 {}", e);
            return ResponseError.create("上传excel失败");
        }
    }

    /**
     * 通过链路分组查询出 拓扑图
     *
     * @param linkGroup
     * @return
     */
    @GetMapping(value = APIUrls.API_TRO_LINKTOPOLOGY_QUERY_LINK_GROUP_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryLinkTopologyByLinkGroup(@Param("linkGroup") String linkGroup,
        @Param("secondLinkId") String secondLinkId) {
        try {
            return ResponseOk.create(linkTopologyInfoService.queryLinkTopologyByLinkGroup(linkGroup, secondLinkId));
        } catch (TROModuleException e) {
            LOGGER.error("LinkTopologyController.queryLinkTopologyByLinkGroup" + e.getMessage(), e);
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            LOGGER.error("LinkTopologyController.queryLinkTopologyByLinkGroup 查询失败 {}", e);
            return ResponseError.create("获取失败");
        }
    }

    /**
     * 说明: API.10.01.003 查询应用瓶颈数量
     *
     * @return org.springframework.http.ResponseEntity<java.lang.Object>
     * @author shulie
     * @create 2019/6/12 19:19
     */
    @GetMapping(value = APIUrls.API_TRO_LINKTOPOLOGY_QUERY_BOTTLENECK_COUNT_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryLinkBottleSummary() {
        try {
            BottleCountVo bottleCountVo = linkTopologyInfoService.queryLinkBottleSummary();
            return ResponseOk.create(bottleCountVo);
        } catch (Exception e) {
            LOGGER.error("LinkTopologyController.queryLinkBottleSummary 查询应用瓶颈数量统计异常 {}", e);
            return ResponseError.create(e.getMessage());
        }
    }

    /**
     * 说明: API.10.01.004 查询链路节点瓶颈详情
     *
     * @param paramMap
     * @return org.springframework.http.ResponseEntity<java.lang.Object>
     * @author shulie
     * @create 2019/6/15 8:26
     */
    @PostMapping(value = APIUrls.API_TRO_LINKTOPOLOGY_QUERY_BOTTLENECK_DETAIL_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryLinkBottleDetail(@RequestBody Map<String, Object> paramMap) {
        try {
            BottleNeckDetailVo bottleNeckDetail = linkTopologyInfoService.queryLinkBottleDetail(paramMap);
            return ResponseOk.create(bottleNeckDetail);
        } catch (Exception e) {
            LOGGER.error("LinkTopologyController.queryLinkBottleDetail 查询应用瓶颈详情异常 {}", e);
            return ResponseError.create(e.getMessage());
        }

    }

}
