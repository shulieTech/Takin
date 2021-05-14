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

package io.shulie.tro.cloud.open.entrypoint.convert;

import io.shulie.tro.cloud.biz.input.scenemanage.SceneScriptRefInput;
import io.shulie.tro.cloud.open.req.scenemanage.SceneScriptRefOpen;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: mubai
 * @Date: 2020-10-29 16:12
 * @Description:
 */
public class SceneScriptRefInputConvert {

    public static  SceneScriptRefInput  of (SceneScriptRefOpen in){
        SceneScriptRefInput  out = new SceneScriptRefInput();
        BeanUtils.copyProperties(in,out);
        return out ;
    }

    public static List<SceneScriptRefInput> ofList (List<SceneScriptRefOpen> list){
        List<SceneScriptRefInput> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)){
            return result ;
        }
        list.stream().forEach(open -> {
            result.add(of(open));
        });
        return result ;
    }

}
