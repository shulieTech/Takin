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

package com.pamirs.tro.entity.domain.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 说明:测试结果明细
 *
 * @author shulie
 * @version v1.0
 * @2018年5月17日
 * @since 2018/05/08
 */
public class TReportResult implements Serializable {

    private static final long serialVersionUID = -5775627052003653982L;

    /**
     * 菜鸟阿斯旺返回结果对象
     */
    private TScenario tScenario;

    /**
     * 链路服务
     */
    private TLinkServiceMntVo tLinkServiceMntVo;

    /**
     * 报告详情
     */
    private List<TReportAppIpDetail> tReportAppIpDetails;

    /**
     * 是否通过
     */
    private Boolean pass;

    /**
     * web服务器情况
     */
    private Map<String, Float> webServer;

    /**
     * 数据库服务器情况
     */
    private Map<String, Float> dbServer;

    /**
     * 中间件服务器情况
     */
    private Map<String, Float> middlewareServer;

    /**
     * 压测时间（分钟）
     */
    private Long duration;

    //    private Float maxCpuUsageRate;
    //
    //    private Float maxMemoryUsageRate;

    /**
     * 查询TPS、RT、成功率，时间精度分钟
     */
    private List<TScenario> tScenarioList;

    /**
     * 2018年5月17日
     *
     * @return the tScenarioList
     * @author shulie
     * @version 1.0
     */
    public List<TScenario> gettScenarioList() {
        return tScenarioList;
    }

    /**
     * 2018年5月17日
     *
     * @param tScenarioList the tScenarioList to set
     * @author shulie
     * @version 1.0
     */
    public void settScenarioList(List<TScenario> tScenarioList) {
        this.tScenarioList = tScenarioList;
    }

    /**
     * Gets the value of tScenario.
     *
     * @return the value of tScenario
     * @author shulie
     * @version 1.0
     */
    public TScenario gettScenario() {
        return tScenario;
    }

    /**
     * Sets the tScenario.
     *
     * <p>You can use gettScenario() to get the value of tScenario</p>
     *
     * @param tScenario tScenario
     * @author shulie
     * @version 1.0
     */
    public void settScenario(TScenario tScenario) {
        this.tScenario = tScenario;
    }

    /**
     * Gets the value of tLinkServiceMntVo.
     *
     * @return the value of tLinkServiceMntVo
     * @author shulie
     * @version 1.0
     */
    public TLinkServiceMntVo gettLinkServiceMntVo() {
        return tLinkServiceMntVo;
    }

    /**
     * Sets the tLinkServiceMntVo.
     *
     * <p>You can use gettLinkServiceMntVo() to get the value of tLinkServiceMntVo</p>
     *
     * @param tLinkServiceMntVo tLinkServiceMntVo
     * @author shulie
     * @version 1.0
     */
    public void settLinkServiceMntVo(TLinkServiceMntVo tLinkServiceMntVo) {
        this.tLinkServiceMntVo = tLinkServiceMntVo;
    }

    /**
     * Gets the value of tReportAppIpDetails.
     *
     * @return the value of tReportAppIpDetails
     * @author shulie
     * @version 1.0
     */
    public List<TReportAppIpDetail> gettReportAppIpDetails() {
        return tReportAppIpDetails;
    }

    /**
     * Sets the tReportAppIpDetails.
     *
     * <p>You can use gettReportAppIpDetails() to get the value of tReportAppIpDetails</p>
     *
     * @param tReportAppIpDetails tReportAppIpDetails
     * @author shulie
     * @version 1.0
     */
    public void settReportAppIpDetails(List<TReportAppIpDetail> tReportAppIpDetails) {
        this.tReportAppIpDetails = tReportAppIpDetails;
    }

    /**
     * Gets the value of pass.
     *
     * @return the value of pass
     * @author shulie
     * @version 1.0
     */
    public Boolean getPass() {
        return pass;
    }

    /**
     * Sets the pass.
     *
     * <p>You can use getPass() to get the value of pass</p>
     *
     * @param pass pass
     * @author shulie
     * @version 1.0
     */
    public void setPass(Boolean pass) {
        this.pass = pass;
    }

    /**
     * Gets the value of webServer.
     *
     * @return the value of webServer
     * @author shulie
     * @version 1.0
     */
    public Map<String, Float> getWebServer() {
        return webServer;
    }

    /**
     * Sets the webServer.
     *
     * <p>You can use getWebServer() to get the value of webServer</p>
     *
     * @param webServer webServer
     * @author shulie
     * @version 1.0
     */
    public void setWebServer(Map<String, Float> webServer) {
        this.webServer = webServer;
    }

    /**
     * Gets the value of dbServer.
     *
     * @return the value of dbServer
     * @author shulie
     * @version 1.0
     */
    public Map<String, Float> getDbServer() {
        return dbServer;
    }

    /**
     * Sets the dbServer.
     *
     * <p>You can use getDbServer() to get the value of dbServer</p>
     *
     * @param dbServer dbServer
     * @author shulie
     * @version 1.0
     */
    public void setDbServer(Map<String, Float> dbServer) {
        this.dbServer = dbServer;
    }

    /**
     * Gets the value of middlewareServer.
     *
     * @return the value of middlewareServer
     * @author shulie
     * @version 1.0
     */
    public Map<String, Float> getMiddlewareServer() {
        return middlewareServer;
    }

    /**
     * Sets the middlewareServer.
     *
     * <p>You can use getMiddlewareServer() to get the value of middlewareServer</p>
     *
     * @param middlewareServer middlewareServer
     * @author shulie
     * @version 1.0
     */
    public void setMiddlewareServer(Map<String, Float> middlewareServer) {
        this.middlewareServer = middlewareServer;
    }

    /**
     * Gets the value of duration.
     *
     * @return the value of duration
     * @author shulie
     * @version 1.0
     */
    public Long getDuration() {
        return duration;
    }

    /**
     * Sets the duration.
     *
     * <p>You can use getDuration() to get the value of duration</p>
     *
     * @param duration duration
     * @author shulie
     * @version 1.0
     */
    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
