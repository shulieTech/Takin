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
package com.pamirs.pradar.pressurement.agent.shared.constant;

import java.util.HashSet;
import java.util.Set;

public class BusinssClassIgnore {

    public static Set<String> embbedIgnoreClass = new HashSet<String>();

    static {
        embbedIgnoreClass.add("com.pamirs.gis.module.log.server.service.impl.MatchLogDipanService");
        embbedIgnoreClass.add("com.pamirs.gis.module.edi.rocketmq.service.impl.ExpBackFlowQuietCNServiceImpl");
        embbedIgnoreClass.add("com.pamirs.gis.module.edi.rocketmq.service.impl.ExpBackFlowQuietDBServiceImpl");
        embbedIgnoreClass.add("com.pamirs.gis.module.edi.rocketmq.service.impl.ExpBackFlowServiceImpl");
        embbedIgnoreClass.add("com.pamirs.gis.module.edi.rocketmq.service.impl.ScreenOrderLogService");
        embbedIgnoreClass.add("com.pamirs.gis.module.basedata.server.proxy.InterfaceIntercepter");
        embbedIgnoreClass.add("com.pamirs.gis.module.log.server.service.impl.MatchLogService");
        embbedIgnoreClass.add("com.pamirs.gis.module.log.server.service.impl.MatchLogSimpleCodeService");
        embbedIgnoreClass.add("com.pamirs.gis.module.log.server.service.impl.ScreenOrderLogService");
        embbedIgnoreClass.add("com.pamirs.gis.module.autocoding.service.impl.MatchLogService");
        embbedIgnoreClass.add("com.pamirs.oms.module.server.job.PtCNOfflineJob");
        embbedIgnoreClass.add("com.pamirs.gis.module.ws.server.waybillSortScheduling.WaybillSort");

        embbedIgnoreClass.add("com.pamirs.pmc.gateway.web.controller.common.CommonController");
        embbedIgnoreClass.add("com.pamirs.pmc.gateway.core.service.impl.PayOrderSingleCreateWebService");
        embbedIgnoreClass.add("com.pamirs.pmc.gateway.core.service.impl.ConfirmPayOrderService");
        embbedIgnoreClass.add("com.pamirs.pmc.pay.order.dubbo.PayOrderServiceImpl");
        embbedIgnoreClass.add("com.pamirs.pmc.gateway.web.controller.callback.WeiXinPayCallBackController");
        embbedIgnoreClass.add("com.pamirs.pmc.pay.facade.service.cubc.CubcPaySuccessNotifyServiceImpl");
        embbedIgnoreClass.add("com.pamirs.pmc.gateway.web.controller.callback.AlipayCallBackController");

        //pda can't load no PT redis key needed
        embbedIgnoreClass.add("com.pamirs.bse.module.inner.service.server.service.impl.PdaDownloadService");

        //zhao chaofei deliver team added
        embbedIgnoreClass.add("com.pamirs.dlv.module.deliver.server.service.impl.ChildDeliverTaskDetailService");
        embbedIgnoreClass.add("com.pamirs.dlv.module.deliver.server.service.impl.DeliverTrackService");
        embbedIgnoreClass.add("com.pamirs.dlv.module.deliver.server.service.impl.DlvDockDlsMethodService");
        embbedIgnoreClass.add("com.pamirs.dlv.module.pda.server.service.impl.PDACreateInstockTaskService");
        embbedIgnoreClass.add("com.pamirs.dlv.module.pda.server.service.impl.PDADeliveryTaskDetailsService");
        embbedIgnoreClass.add("com.pamirs.dlv.module.pda.server.service.impl.PDAScanWaybillService");
        embbedIgnoreClass.add("com.pamirs.dlv.module.pda.server.service.impl.PDASubmitWaybillInstockService");
        embbedIgnoreClass.add("com.pamirs.dlv.module.pda.server.service.impl.PDAUpdateSendMsgService");
        embbedIgnoreClass.add("com.pamirs.dlv.module.deliver.server.cubc.foss.service.impl.SettlementCallService");
        embbedIgnoreClass.add("com.pamirs.dlv.module.deliver.server.service.impl.TrajectoryRecordService");


    }
}
