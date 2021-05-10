/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pamirs.pradar.upload.uploader;

import com.pamirs.pradar.upload.HttpOnceUploader;
import com.pamirs.pradar.upload.info.AgentInfo;

/**
 * 应用启动时候，将 Agent、应用、机器等信息上报
 *
 * @author shiyajian
 * create: 2020-07-20
 */
public class AgentOnlineUploader extends HttpOnceUploader<String> {

    public static final String UPLOADER_NAME = "agent_online_uploader";

    public static final String POST_URL = "api/smon/app/addAppInfo";

    public static final int RETRY_TIMES = -1;

    public static final int RETRY_SECOND = 10;

    private AgentOnlineUploader() {
        super(UPLOADER_NAME, POST_URL, RETRY_TIMES, RETRY_SECOND);
    }

    private final static AgentOnlineUploader INSTANCE = new AgentOnlineUploader();

    public static AgentOnlineUploader getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    protected String getData() {
        return AgentInfo.getInstance().toJson();
    }
}
