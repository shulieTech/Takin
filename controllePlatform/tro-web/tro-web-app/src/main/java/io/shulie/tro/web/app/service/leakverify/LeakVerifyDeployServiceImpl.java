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

package io.shulie.tro.web.app.service.leakverify;

import java.util.List;

import io.shulie.tro.web.app.convert.leakverify.LeakVerifyDeployOutputConverter;
import io.shulie.tro.web.app.output.leakverify.LeakVerifyDeployDetailOutput;
import io.shulie.tro.web.app.output.leakverify.LeakVerifyDeployOutput;
import io.shulie.tro.web.data.dao.leakverify.LeakVerifyDeployDAO;
import io.shulie.tro.web.data.result.leakverify.LeakVerifyDeployDetailResult;
import io.shulie.tro.web.data.result.leakverify.LeakVerifyDeployResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shulie
 */
@Component
public class LeakVerifyDeployServiceImpl implements LeakVerifyDeployService {

    @Autowired
    private LeakVerifyDeployDAO leakVerifyDeployDAO;

    @Override
    public List<LeakVerifyDeployOutput> listLeakVerifyDeploy(Long leakVerifyId) {
        List<LeakVerifyDeployResult> leakVerifyDeployResults = leakVerifyDeployDAO.listLeakVerifyDeploy(leakVerifyId,
            false);
        return LeakVerifyDeployOutputConverter.INSTANCE.ofListLeakVerifyDeployOutput(leakVerifyDeployResults);
    }

    @Override
    public List<LeakVerifyDeployDetailOutput> queryLeakVerifyDeployDetail(Long leakVerifyDeployId) {
        List<LeakVerifyDeployDetailResult> leakVerifyDeployDetailResults = leakVerifyDeployDAO
            .queryLeakVerifyDeployDetail(leakVerifyDeployId, null, null, null);
        return LeakVerifyDeployOutputConverter.INSTANCE.ofListLeakVerifyDeployDetailOutput(
            leakVerifyDeployDetailResults);
    }

}
