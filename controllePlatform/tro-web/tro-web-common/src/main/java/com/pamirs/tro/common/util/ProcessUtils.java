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

package com.pamirs.tro.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

/**
 * @see ProcessUtilsTest
 */
public class ProcessUtils {

    /**
     * 说明: 解析字符串并且获取进程号集合
     *
     * @param commandResult 待解析的脚本标准输出内容
     * @return 进程号集合
     * @author shulie
     * @date 2018/8/9 11:10
     */
    public static List<String> getPIdsList(String commandResult) {
        //        String s = "appman    4960  4646  0 15:22 ?        00:00:00 /bin/sh /opt/tro/shell/starmqclient.sh
        //        ESB MQM2 10.230.29.24 LMS.CLIENT 1428 1208 QU_LMS_REQUEST_COM_IN 1 PT_LMS_ESB2LMS_PRO_TEST\n" +
        //                "appman    4963  4960  7 15:22 ?        00:00:04 /opt/jdk/bin/java -cp
        //                /opt/mq-pt-client/lib/zookeeper-3.4.6.jar:/opt/mq-pt-client/lib/spring-webmvc-3.1.4.RELEASE
        //                .jar:/opt/mq-pt-client/lib/spring-web-3.1.4.RELEASE.jar:/opt/mq-pt-client/lib/spring-tx-3.1
        //                .4.RELEASE.jar:/opt/mq-pt-client/lib/spring-oxm-3.1.4.RELEASE
        //                .jar:/opt/mq-pt-client/lib/spring-orm-3.1.4.RELEASE.jar:/opt/mq-pt-client/lib/spring-jms-3
        //                .1.4.RELEASE.jar:/opt/mq-pt-client/lib/spring-jdbc-3.1.4.RELEASE
        //                .jar:/opt/mq-pt-client/lib/spring-instrument-3.1.4.RELEASE
        //                .jar:/opt/mq-pt-client/lib/spring-expression-3.1.4.RELEASE
        //                .jar:/opt/mq-pt-client/lib/spring-core-3.1.4.RELEASE
        //                .jar:/opt/mq-pt-client/lib/spring-context-support-3.1.4.RELEASE
        //                .jar:/opt/mq-pt-client/lib/spring-context-3.1.4.RELEASE
        //                .jar:/opt/mq-pt-client/lib/spring-beans-3.1.4.RELEASE
        //                .jar:/opt/mq-pt-client/lib/spring-aspects-3.1.4.RELEASE
        //                .jar:/opt/mq-pt-client/lib/spring-asm-3.1.4.RELEASE.jar:/opt/mq-pt-client/lib/spring-aop-3
        //                .1.4.RELEASE.jar:/opt/mq-pt-client/lib/slf4j-log4j12-1.6.1
        //                .jar:/opt/mq-pt-client/lib/slf4j-api-1.6.1.jar:/opt/mq-pt-client/lib/providerutil-1.0
        //                .jar:/opt/mq-pt-client/lib/pcf-1.0.jar:/opt/mq-pt-client/lib/oracle-jdbc-10.1.0.2.0
        //                .jar:/opt/mq-pt-client/lib/netty-3.7.0.Final
        //                .jar:/opt/mq-pt-client/lib/mysql-connector-java-5.1.25
        //                .jar:/opt/mq-pt-client/lib/mybatis-spring-1.0.1.jar:/opt/mq-pt-client/lib/mybatis-3.0.5
        //                .jar:/opt/mq-pt-client/lib/mqjms-1.0.jar:/opt/mq-pt-client/lib/mqetclient-1.0.0
        //                .jar:/opt/mq-pt-client/lib/mq-1.0.jar:/opt/mq-pt-client/lib/maxplanck-app-0.0.1-20180528
        //                .054919-23.jar:/opt/mq-pt-client/lib/log4jdbc-remix-0.2.7.jar:/opt/mq-pt-client/lib/log4j-1
        //                .2.16.jar:/opt/mq-pt-client/lib/jta-1.0.jar:/opt/mq-pt-client/lib/jodis-0.2.2
        //                .jar:/opt/mq-pt-client/lib/jms-1.0.jar:/opt/mq-pt-client/lib/jmqi-1.0
        //                .jar:/opt/mq-pt-client/lib/jline-0.9.94.jar:/opt/mq-pt-client/lib/jedis-2.7.3
        //                .jar:/opt/mq-pt-client/lib/jackson-mapper-asl-1.8.1
        //                .jar:/opt/mq-pt-client/lib/jackson-databind-2.1.2
        //                .jar:/opt/mq-pt-client/lib/jackson-core-asl-1.8.1.jar:/opt/mq-pt-client/lib/jackson-core-2
        //                .1.1.jar:/opt/mq-pt-client/lib/jackson-annotations-2.1.2
        //                .jar:/opt/mq-pt-client/lib/hessian-4.0.7.jar:/opt/mq-pt-client/lib/headers-1.0
        //                .jar:/opt/mq-pt-client/lib/guava-14.0.1.jar:/opt/mq-pt-client/lib/fscontext-1.0
        //                .jar:/opt/mq-pt-client/lib/fastjson-1.2.23.jar:/opt/mq-pt-client/lib/esbClient-itf
        //                .jar:/opt/mq-pt-client/lib/dpap-framework-springmvc-2.0.6.1
        //                .jar:/opt/mq-pt-client/lib/dpap-framework-data-2.0.6.1
        //                .jar:/opt/mq-pt-client/lib/dpap-framework-core-2.0.6.1
        //                .jar:/opt/mq-pt-client/lib/dpap-framework-base-2.0.6.1
        //                .jar:/opt/mq-pt-client/lib/dpap-esb-mqc-extends-2.1.0
        //                .jar:/opt/mq-pt-client/lib/dpap-esb-extends-2.1.0.jar:/opt/mq-pt-client/lib/dpap-config-2.0
        //                .6.1.jar:/opt/mq-pt-client/lib/dpap-common-base-2.0.6.1
        //                .jar:/opt/mq-pt-client/lib/dpap-cache-2.0.6.1.jar:/opt/mq-pt-client/lib/dhbcore-1.0
        //                .jar:/opt/mq-pt-client/lib/curator-recipes-2.9.0
        //                .jar:/opt/mq-pt-client/lib/curator-framework-2.9.0
        //                .jar:/opt/mq-pt-client/lib/curator-client-2.9.0.jar:/opt/mq-pt-client/lib/commons-pool2-2.3
        //                .jar:/opt/mq-pt-client/lib/commons-pool-1.6.jar:/opt/mq-pt-client/lib/commons-net-3.2
        //                .jar:/opt/mq-pt-client/lib/commons-logging-1.1.1.jar:/opt/mq-pt-client/lib/commons-lang-2.6
        //                .jar:/opt/mq-pt-client/lib/commons-io-2.0.1.jar:/opt/mq-pt-client/lib/commonservices-1.0
        //                .jar:/opt/mq-pt-client/lib/commons-digester-2.1.jar:/opt/mq-pt-client/lib/commons-dbutils-1
        //                .3.jar:/opt/mq-pt-client/lib/commons-dbcp-1.4
        //                .jar:/opt/mq-pt-client/lib/commons-configuration-1.6
        //                .jar:/opt/mq-pt-client/lib/commons-collections-3.2.1
        //                .jar:/opt/mq-pt-client/lib/commons-codec-1.9
        //                .jar:/opt/mq-pt-client/lib/commons-beanutils-core-1.8.0
        //                .jar:/opt/mq-pt-client/lib/commons-beanutils-1.8.3.jar:/opt/mq-pt-client/lib/com.ibm.mq
        //                .soap-1.0.0.jar:/opt/mq-pt-client/lib/com.ibm.mq.jms.Nojndi-1.0.0
        //                .jar:/opt/mq-pt-client/lib/com.ibm.mq.fta-1.0.0.jar:/opt/mq-pt-client/lib/cglib-nodep-2.2
        //                .jar:/opt/mq-pt-client/lib/casclient-2.1.1.jar:/opt/mq-pt-client/lib/aspectjweaver-1.6.8
        //                .jar:/opt/mq-pt-client/lib/aopalliance-1.0.jar:/opt/mq-pt-client/lib/ant-launcher-1.8.4
        //                .jar:/opt/mq-pt-client/lib/ant-1.8.4.jar:/opt/jdk/jre/lib:.:/lib/dt.jar:/lib/tools.jar Main
        //                ESB MQM2 10.230.29.24 LMS.CLIENT 1428 1208 QU_LMS_REQUEST_COM_IN 1 PT_LMS_ESB2LMS_PRO_TEST";

        if (StringUtils.isEmpty(commandResult)) {
            return Lists.newArrayList();
        }
        return Arrays.stream(StringUtils.split(commandResult, "\n")).map(string -> Splitter.on(" ").omitEmptyStrings()
            .trimResults().splitToList(string).get(1)).collect(Collectors.toList());
    }

    /**
     * 说明: 解析字符串并且获取进程号集合
     *
     * @param commandResult 待解析的脚本标准输出内容
     * @return 进程号集合
     * @author shulie
     * @date 2018/8/9 11:10
     */
    public static List<String> getProcessIdList(String commandResult) {
        List<String> processIdList = new ArrayList<String>();

        if (commandResult == null) {
            return processIdList;
        }

        String[] afterArray = StringUtils.split(commandResult, "\n");

        if (afterArray == null || afterArray.length == 0) {
            return processIdList;
        }

        for (int i = 0; i < afterArray.length; i++) {
            String[] command = StringUtils.split(afterArray[i], " ");
            if (command.length < 2) {
                continue;
            }
            processIdList.add(command[1]);
        }
        return processIdList;
    }
}
