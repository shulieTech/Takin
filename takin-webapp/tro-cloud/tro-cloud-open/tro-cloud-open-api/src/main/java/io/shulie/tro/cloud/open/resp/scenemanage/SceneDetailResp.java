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

package io.shulie.tro.cloud.open.resp.scenemanage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;



import io.shulie.tro.cloud.common.bean.TimeBean;
import io.shulie.tro.cloud.common.enums.machine.EnumResult;

import lombok.Data;

/**
 * @ClassName SceneDetailResult
 * @Description
 * @Author qianshui
 * @Date 2020/5/18 下午8:26
 */
@Data
public class SceneDetailResp implements Serializable {

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

    private List<BusinessActivityDetailResp> businessActivityConfig;

    private List<ScriptDetailResp> uploadFile;

    private List<SlaDetailResp> stopCondition;

    private List<SlaDetailResp> warningCondition;

    @Data
    public static class ScriptDetailResp implements Serializable {

        private static final long serialVersionUID = 2391812420921319265L;

        private String fileName;

        private String uploadTime;

        private EnumResult fileType;

        private Long uploadedData;

        private EnumResult isSplit;

    }
    @Data
    public static class SlaDetailResp implements Serializable {

        private static final long serialVersionUID = 9171434959213456889L;

        private String ruleName;

        private String businessActivity;

        private String rule;

        private EnumResult status;
    }

}
