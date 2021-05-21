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

package io.shulie.amdb.response.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.shulie.amdb.entity.AppDO;
import io.shulie.amdb.response.app.model.InstanceInfo;
import io.shulie.amdb.response.app.model.Library;
import lombok.Data;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class AmdbAppResponse implements Serializable {
    List<Library> library;
    // 应用 id
    Long appId;
    // 应用名称
    String appName;
    // 应用描述
    String appSummary;
    // app最新发布版本
    String appVersionCode;
    // 应用负责人
    String appManagerName;
    // 应用最后修改时间
    String appUpdateTime;
    // 应用是否异常
    Boolean appIsException;
    // 实例节点信息
    InstanceInfo instanceInfo;

    public AmdbAppResponse(List<String> resultFields, AppDO amdbApp) {
        this.setAppId(amdbApp.getId());
        //this.setAppIsException((amdbApp.getFlag() & 1) == 1);
        this.setAppName(amdbApp.getAppName());
        this.setAppSummary(amdbApp.getRemark());
        this.setAppVersionCode(amdbApp.getProjectVersion());
        this.setAppManagerName(amdbApp.getAppManager());
        this.setAppUpdateTime(DateFormatUtils.format(amdbApp.getGmtModify(), "yyyy-MM-dd HH:mm:ss"));
        // library 暂时不需要处理
        //JSONObject jsonObject = JSON.parseObject(amdbApp.getExt());
        if (!CollectionUtils.isEmpty(resultFields) && resultFields.contains("library")) {
            library = new ArrayList<>();
        //    if (jsonObject != null && jsonObject.get("jars") != null) {
        //        String[] libraryAry = ((String) jsonObject.get("jars")).split(";");
        //        if (libraryAry.length > 0) {
        //            for (String s : libraryAry) {
        //                library.add(new Library(s));
        //            }
        //        }
        //    }
        }
    }
}