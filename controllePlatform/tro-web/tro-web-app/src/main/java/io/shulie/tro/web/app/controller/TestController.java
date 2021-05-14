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

package io.shulie.tro.web.app.controller;

import javax.validation.constraints.NotNull;

import com.pamirs.tro.entity.domain.entity.settle.Account;
import io.shulie.tro.web.app.cache.webimpl.AllUserCache;
import io.shulie.tro.web.app.exception.ExceptionCode;
import io.shulie.tro.web.app.exception.TroWebException;
import io.shulie.tro.web.app.service.report.impl.ReportDataCache;
import io.shulie.tro.web.app.service.risk.ProblemAnalysisService;
import io.shulie.tro.web.common.domain.WebResponse;
import io.shulie.tro.web.data.result.user.UserCacheResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ApiController
 * @Description
 * @Author qianshui
 * @Date 2020/5/14 下午7:34
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ReportDataCache reportDataCache;

    @Autowired
    private ProblemAnalysisService problemAnalysisService;

    @Autowired
    private AllUserCache allUserCache;

    @GetMapping("/test")
    public WebResponse test(@Validated @NotNull @RequestParam(value = "id") Long id) {
        UserCacheResult cachedUserById = allUserCache.getCachedUserById(1L);
        System.out.println(cachedUserById);
        UserCacheResult cachedUserByKey = allUserCache.getCachedUserByKey(
            "302afca3-977c-4016-8d4c-56077d7a0000");
        System.out.println(cachedUserByKey);
        return WebResponse.success();
        //Map<String, String> map = new HashMap<>();
        //map.put("test", "test");
        //throw new TroWebException(ExceptionCode.LICENSE_NOT_VALIDATE, map);
    }

    @GetMapping("/test1")
    public WebResponse test1() {
        Account account = new Account();
        account.setId(10L);
        throw new TroWebException(ExceptionCode.TEST_2, account);
    }

    @GetMapping("/report")
    public void report() {
        Long reportId = 454L;
        reportDataCache.readyCloudReportData(reportId);
        //problemAnalysisService.syncMachineData(reportId);
        // 检查风险机器
        problemAnalysisService.checkRisk(reportId);
        // 瓶颈处理
        problemAnalysisService.processBottleneck(reportId);
    }
}
