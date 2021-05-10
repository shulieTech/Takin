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

import com.pamirs.pradar.upload.HttpScheduledUploader;
import com.pamirs.pradar.upload.info.AgentInfo;

/**
 * 应用启动时候，将 Agent、应用、机器等信息上报
 *
 * @author shiyajian
 * create: 2020-07-20
 */
public class AgentUpdateStatusUploader extends HttpScheduledUploader<String> {

    public static final String UPLOADER_NAME = "agent_update_uploader";

    public static final String POST_URL = "api/smon/app/updateAppInfo";

    public static final int UPLOAD_PERIOD_SECOND = 5;

    private AgentUpdateStatusUploader() {
        super(UPLOADER_NAME, POST_URL, UPLOAD_PERIOD_SECOND);
    }

    private final static AgentUpdateStatusUploader INSTANCE = new AgentUpdateStatusUploader();

    public static AgentUpdateStatusUploader getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    protected String getData() {
        return String.format("{\"agentId\":\"%s\"}", AgentInfo.getInstance().getAgentId());
    }

    @Override
    protected boolean isReady() {
        return AgentOnlineUploader.getInstance().isSuccess();
    }
}
