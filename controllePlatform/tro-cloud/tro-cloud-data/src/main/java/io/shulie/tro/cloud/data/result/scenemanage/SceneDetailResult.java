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

package io.shulie.tro.cloud.data.result.scenemanage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import io.shulie.tro.cloud.common.enums.machine.EnumResult;
import io.shulie.tro.cloud.common.bean.TimeBean;
import lombok.Data;

/**
 * @ClassName SceneDetailResult
 * @Description
 * @Author qianshui
 * @Date 2020/5/18 下午8:26
 */
@Data
public class SceneDetailResult implements Serializable {

    private static final long serialVersionUID = 1453217777875591954L;

    private Long id;

    private String sceneName;

    private Long customId;

    private String customName;

    private String updateTime;

    private String lastPtTime;

    private EnumResult status;

    private Integer concurrenceNum;

    private Integer ipNum;

    private TimeBean pressureTestTime;

    private EnumResult pressureMode;

    private TimeBean increasingTime;

    private Integer step;

    private BigDecimal estimateFlow;

    private List<BusinessActivityDetailResult> businessActivityConfig;

    private List<ScriptDetailResult> uploadFile;

    private List<SlaDetailResult> stopCondition;

    private List<SlaDetailResult> warningCondition;

    public static void main(String[] args) {
        SceneDetailResult dto = new SceneDetailResult();
        dto.setBusinessActivityConfig(Arrays.asList(new BusinessActivityDetailResult()));
        dto.setUploadFile(Arrays.asList(new ScriptDetailResult()));
        dto.setStopCondition(Arrays.asList(new SlaDetailResult()));
        dto.setWarningCondition(Arrays.asList(new SlaDetailResult()));
        System.out.println(JSON.toJSONString(dto, SerializerFeature.WriteMapNullValue));
    }
}
