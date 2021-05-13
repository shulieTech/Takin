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

import io.shulie.tro.cloud.biz.input.scenemanage.SceneBusinessActivityRefInput;
import io.shulie.tro.cloud.open.req.scenemanage.SceneBusinessActivityRefOpen;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: mubai
 * @Date: 2020-10-29 15:20
 * @Description:
 */
public class SceneBusinessActivityRefInputConvert {

    public static SceneBusinessActivityRefInput of(SceneBusinessActivityRefOpen open) {
        SceneBusinessActivityRefInput out = new SceneBusinessActivityRefInput();
        BeanUtils.copyProperties(open, out);
        return out;
    }

    public static List<SceneBusinessActivityRefInput> ofLists(List<SceneBusinessActivityRefOpen> list) {
        List<SceneBusinessActivityRefInput> outs = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return outs;
        }
        list.stream().forEach(open -> {
            outs.add(of(open));
        });
        return outs;
    }
}
