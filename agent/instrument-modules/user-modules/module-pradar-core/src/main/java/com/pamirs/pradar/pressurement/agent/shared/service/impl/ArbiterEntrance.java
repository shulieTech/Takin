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
package com.pamirs.pradar.pressurement.agent.shared.service.impl;

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.PradarSwitcher;
import com.pamirs.pradar.pressurement.agent.shared.constant.DoorConstants;
import com.pamirs.pradar.pressurement.agent.shared.custominterfacebase.Entrance;
import com.pamirs.pradar.pressurement.agent.shared.domain.DoorPlank;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;

import java.util.Set;

/**
 * 通过aribiter的状态来判断门是否是开着的
 * <p>
 * <p>
 * 手动出口 门
 * 质量配置质量配置是在分析时使用的规则集合。
 * 每个语言都有默认配置。没有指定其他配置的项目会使用默认配置。
 * <p>
 * 手动出口 门
 * 质量配置质量配置是在分析时使用的规则集合。
 * 每个语言都有默认配置。没有指定其他配置的项目会使用默认配置。
 * <p>
 * 手动出口 门
 * 质量配置质量配置是在分析时使用的规则集合。
 * 每个语言都有默认配置。没有指定其他配置的项目会使用默认配置。
 * <p>
 * 手动出口 门
 * 质量配置质量配置是在分析时使用的规则集合。
 * 每个语言都有默认配置。没有指定其他配置的项目会使用默认配置。
 * <p>
 * 手动出口 门
 * 质量配置质量配置是在分析时使用的规则集合。
 * 每个语言都有默认配置。没有指定其他配置的项目会使用默认配置。
 * <p>
 * 手动出口 门
 * 质量配置质量配置是在分析时使用的规则集合。
 * 每个语言都有默认配置。没有指定其他配置的项目会使用默认配置。
 * <p>
 * 手动出口 门
 * 质量配置质量配置是在分析时使用的规则集合。
 * 每个语言都有默认配置。没有指定其他配置的项目会使用默认配置。
 * <p>
 * 手动出口 门
 * 质量配置质量配置是在分析时使用的规则集合。
 * 每个语言都有默认配置。没有指定其他配置的项目会使用默认配置。
 */
/**
 * 手动出口 门  
 * 质量配置质量配置是在分析时使用的规则集合。
 *	每个语言都有默认配置。没有指定其他配置的项目会使用默认配置。
 */

/**
 * 手动出口 门  
 * 质量配置质量配置是在分析时使用的规则集合。
 *	每个语言都有默认配置。没有指定其他配置的项目会使用默认配置。
 */
public class ArbiterEntrance implements Entrance {
    public static final String CLOSE_REASON = DoorConstants.ARBITER_DOOR_NOT_READY;
    public static String contextPath = null;

    public static void setContextPath(String c) {
        ArbiterEntrance.contextPath = c;
    }


    /**
     * 判断压测数据是否能通过这道门
     * @return
     */
    public static DoorPlank shallWePassHttp() {
        DoorPlank doorPlank = new DoorPlank();
        if (!PradarSwitcher.isClusterTestReady()) {
            doorPlank.setCloseReason(CLOSE_REASON);
            doorPlank.setStatus(Pradar.DOOR_CLOSED);
        } else {
            if (contextPath != null) {
                Set<String> set = GlobalConfig.getInstance().getContextPathBlockList();
                for (String blockWar : set) {
                    if (blockWar.equals(contextPath)) {
                        doorPlank.setCloseReason(DoorConstants.ARBITER_DOOR_BLOCK);
                        doorPlank.setStatus(Pradar.DOOR_CLOSED);
                        return doorPlank;
                    }
                }
            }
            doorPlank.setCloseReason("");
            doorPlank.setStatus(Pradar.DOOR_OPENED);
        }
        return doorPlank;
    }
}
