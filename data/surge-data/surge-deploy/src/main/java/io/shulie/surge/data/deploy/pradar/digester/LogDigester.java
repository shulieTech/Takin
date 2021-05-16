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

package io.shulie.surge.data.deploy.pradar.digester;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.pamirs.pradar.log.parser.trace.RpcBased;
import io.shulie.surge.data.deploy.pradar.common.PradarUtils;
import io.shulie.surge.data.deploy.pradar.digester.command.*;
import io.shulie.surge.data.runtime.common.remote.DefaultValue;
import io.shulie.surge.data.runtime.common.remote.Remote;
import io.shulie.surge.data.runtime.digest.DataDigester;
import io.shulie.surge.data.runtime.digest.DigestContext;
import io.shulie.surge.data.sink.clickhouse.ClickHouseSupport;
import io.shulie.surge.data.sink.mysql.MysqlSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 日志原文按插入顺序直接存储-写入ClickHouse
 *
 * @author pamirs
 */
@Singleton
public class LogDigester implements DataDigester<RpcBased> {
    private static final Logger logger = LoggerFactory.getLogger(LogDigester.class);
    @Inject
    private ClickHouseSupport clickHouseSupport;

    @Inject
    @DefaultValue("false")
    @Named("/pradar/config/rt/clickhouseDisable")
    private Remote<Boolean> clickhouseDisable;

    @Inject
    @DefaultValue("1")
    @Named("/pradar/config/rt/clickhouseSampling")
    private Remote<Integer> clickhouseSampling;

    private transient AtomicBoolean isRunning = new AtomicBoolean(false);

    private ClickhouseFacade clickhouseFacade = ClickhouseFacade.Factory.getInstace();

    private String sql = "";

    public void init() {
        clickhouseFacade.addCommond(new BaseCommand());
        sql = "insert into t_trace_all (" + clickhouseFacade.getCols() + ") values (" + clickhouseFacade.getParam() + ")";
    }

    @Override
    public void digest(DigestContext<RpcBased> context) {
        if (clickhouseDisable.get()) {
            return;
        }
        if (isRunning.compareAndSet(false, true)) {
            init();
        }
        RpcBased rpcBased = context.getContent();
        try {
            if (rpcBased == null) {
                return;
            }
            if (!PradarUtils.isTraceSampleAccepted(context.getContent().getTraceId(), clickhouseSampling.get())) {
                return;
            }
            List<Object[]> batchs = Lists.newArrayList();
            batchs.add(clickhouseFacade.toObjects(clickhouseFacade.invoke(rpcBased)));
            clickHouseSupport.batchUpdate(sql, batchs);
        } catch (Throwable e) {
            logger.warn("fail to write clickhouse, log: " + rpcBased.getLog() + ", error:" + e.getMessage());
        }
    }


    @Override
    public int threadCount() {
        return 1;
    }

    @Override
    public void stop() {
        try {
            this.clickHouseSupport.stop();
        } catch (Throwable e) {
            logger.error("clickhouse stop fail");
        }
    }

    public Remote<Boolean> getClickhouseDisable() {
        return clickhouseDisable;
    }

    public Remote<Integer> getClickhouseSampling() {
        return clickhouseSampling;
    }
}
