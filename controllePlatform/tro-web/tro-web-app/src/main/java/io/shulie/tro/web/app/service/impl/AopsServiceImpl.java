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

package io.shulie.tro.web.app.service.impl;

import java.util.Map;

import io.shulie.tro.web.app.service.AopsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * aops reader server
 *
 * @author 311183
 */
@Service
public class AopsServiceImpl implements AopsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AopsServiceImpl.class);

    private static final String ZERO = "0";
    @Value("${tro.html.url}")
    protected String trohtml = "http://10.230.44.5:8080/tro/index.html";
    //http://10.230.20.181/dc_console/api/v1/collector/list/host";
    @Value("${aops.url}")
    private String url = "http://192.168.128.71/dc_console/api/v1/collector/list/host";
    //"164e76df4f36aea7c4ab74f810c862ad2224b9c740cc7155b3956d6e241ef566" ;
    @Value("${aops.secretkey}")
    private String secretkey = "e203de14927ef0393106f0e81936f3e64d4f3b625587dc5b7a3b8015aa0d14f6";
    //"78951f25d9d58aef1ed96b23";
    @Value("${aops.accesskey}")
    private String accesskey = "374759e084dbbc59b6cb176c";

    @Override
    public Map getAopsData(String ip, String startSecond, String endSecond, String time) {
        return null;
    }

}
