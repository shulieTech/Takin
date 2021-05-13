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

import java.util.List;

/**
 * @author 何仲奇
 * @Package com.pamirs.tro.common.util
 * @date 2020/9/2 9:03 下午
 * @see ProcessUtils
 */
public class ProcessUtilsTest {
    public static void main(String[] args) {

        String commandResult =
            "appman   17567 15981  0 13:44 ?        00:00:00 /bin/sh /opt/tro/shell/starmqclient.sh ESB MQM2 10.230"
                + ".29.24 LMS.CLIENT 1428 1208 QU_LMS_REQUEST_COM_IN 1 PT_LMS_ESB2LMS_PRO_TEST\n"
                +
                "appman   17570 17567  3 13:44 ?        00:00:07 /opt/jdk/bin/java -cp "
                + "/opt/mq-pt-client/lib/zookeeper-3.4.6.jar:/opt/mq-pt-client/lib/spring-webmvc-3.1.4.RELEASE"
                + ".jar:/opt/mq-pt-client/lib/spring-web-3.1.4.RELEASE.jar:/opt/mq-pt-client/lib/spring-tx-3.1.4"
                + ".RELEASE.jar:/opt/mq-pt-client/lib/spring-oxm-3.1.4.RELEASE.jar:/opt/mq-pt-client/lib/spring-orm-3"
                + ".1.4.RELEASE.jar:/opt/mq-pt-client/lib/spring-jms-3.1.4.RELEASE"
                + ".jar:/opt/mq-pt-client/lib/spring-jdbc-3.1.4.RELEASE.jar:/opt/mq-pt-client/lib/spring-instrument-3"
                + ".1.4.RELEASE.jar:/opt/mq-pt-client/lib/spring-expression-3.1.4.RELEASE"
                + ".jar:/opt/mq-pt-client/lib/spring-core-3.1.4.RELEASE"
                + ".jar:/opt/mq-pt-client/lib/spring-context-support-3.1.4.RELEASE"
                + ".jar:/opt/mq-pt-client/lib/spring-context-3.1.4.RELEASE.jar:/opt/mq-pt-client/lib/spring-beans-3.1"
                + ".4.RELEASE.jar:/opt/mq-pt-client/lib/spring-aspects-3.1.4.RELEASE"
                + ".jar:/opt/mq-pt-client/lib/spring-asm-3.1.4.RELEASE.jar:/opt/mq-pt-client/lib/spring-aop-3.1.4"
                + ".RELEASE.jar:/opt/mq-pt-client/lib/slf4j-log4j12-1.6.1.jar:/opt/mq-pt-client/lib/slf4j-api-1.6.1"
                + ".jar:/opt/mq-pt-client/lib/providerutil-1.0.jar:/opt/mq-pt-client/lib/pcf-1.0"
                + ".jar:/opt/mq-pt-client/lib/oracle-jdbc-10.1.0.2.0.jar:/opt/mq-pt-client/lib/netty-3.7.0.Final"
                + ".jar:/opt/mq-pt-client/lib/mysql-connector-java-5.1.25.jar:/opt/mq-pt-client/lib/mybatis-spring-1"
                + ".0.1.jar:/opt/mq-pt-client/lib/mybatis-3.0.5.jar:/opt/mq-pt-client/lib/mqjms-1.0"
                + ".jar:/opt/mq-pt-client/lib/mqetclient-1.0.0.jar:/opt/mq-pt-client/lib/mq-1.0"
                + ".jar:/opt/mq-pt-client/lib/maxplanck-app-0.0.1-20180528.054919-23"
                + ".jar:/opt/mq-pt-client/lib/log4jdbc-remix-0.2.7.jar:/opt/mq-pt-client/lib/log4j-1.2.16"
                + ".jar:/opt/mq-pt-client/lib/jta-1.0.jar:/opt/mq-pt-client/lib/jodis-0.2.2"
                + ".jar:/opt/mq-pt-client/lib/jms-1.0.jar:/opt/mq-pt-client/lib/jmqi-1.0"
                + ".jar:/opt/mq-pt-client/lib/jline-0.9.94.jar:/opt/mq-pt-client/lib/jedis-2.7.3"
                + ".jar:/opt/mq-pt-client/lib/jackson-mapper-asl-1.8.1.jar:/opt/mq-pt-client/lib/jackson-databind-2.1"
                + ".2.jar:/opt/mq-pt-client/lib/jackson-core-asl-1.8.1.jar:/opt/mq-pt-client/lib/jackson-core-2.1.1"
                + ".jar:/opt/mq-pt-client/lib/jackson-annotations-2.1.2.jar:/opt/mq-pt-client/lib/hessian-4.0.7"
                + ".jar:/opt/mq-pt-client/lib/headers-1.0.jar:/opt/mq-pt-client/lib/guava-14.0.1"
                + ".jar:/opt/mq-pt-client/lib/fscontext-1.0.jar:/opt/mq-pt-client/lib/fastjson-1.2.23"
                + ".jar:/opt/mq-pt-client/lib/esbClient-itf.jar:/opt/mq-pt-client/lib/dpap-framework-springmvc-2.0.6"
                + ".1.jar:/opt/mq-pt-client/lib/dpap-framework-data-2.0.6.1"
                + ".jar:/opt/mq-pt-client/lib/dpap-framework-core-2.0.6.1"
                + ".jar:/opt/mq-pt-client/lib/dpap-framework-base-2.0.6.1"
                + ".jar:/opt/mq-pt-client/lib/dpap-esb-mqc-extends-2.1.0.jar:/opt/mq-pt-client/lib/dpap-esb-extends-2"
                + ".1.0.jar:/opt/mq-pt-client/lib/dpap-config-2.0.6.1.jar:/opt/mq-pt-client/lib/dpap-common-base-2.0"
                + ".6.1.jar:/opt/mq-pt-client/lib/dpap-cache-2.0.6.1.jar:/opt/mq-pt-client/lib/dhbcore-1.0"
                + ".jar:/opt/mq-pt-client/lib/curator-recipes-2.9.0.jar:/opt/mq-pt-client/lib/curator-framework-2.9.0"
                + ".jar:/opt/mq-pt-client/lib/curator-client-2.9.0.jar:/opt/mq-pt-client/lib/commons-pool2-2.3"
                + ".jar:/opt/mq-pt-client/lib/commons-pool-1.6.jar:/opt/mq-pt-client/lib/commons-net-3.2"
                + ".jar:/opt/mq-pt-client/lib/commons-logging-1.1.1.jar:/opt/mq-pt-client/lib/commons-lang-2.6"
                + ".jar:/opt/mq-pt-client/lib/commons-io-2.0.1.jar:/opt/mq-pt-client/lib/commonservices-1.0"
                + ".jar:/opt/mq-pt-client/lib/commons-digester-2.1.jar:/opt/mq-pt-client/lib/commons-dbutils-1.3"
                + ".jar:/opt/mq-pt-client/lib/commons-dbcp-1.4.jar:/opt/mq-pt-client/lib/commons-configuration-1.6"
                + ".jar:/opt/mq-pt-client/lib/commons-collections-3.2.1.jar:/opt/mq-pt-client/lib/commons-codec-1.9"
                + ".jar:/opt/mq-pt-client/lib/commons-beanutils-core-1.8.0"
                + ".jar:/opt/mq-pt-client/lib/commons-beanutils-1.8.3.jar:/opt/mq-pt-client/lib/com.ibm.mq.soap-1.0.0"
                + ".jar:/opt/mq-pt-client/lib/com.ibm.mq.jms.Nojndi-1.0.0.jar:/opt/mq-pt-client/lib/com.ibm.mq.fta-1"
                + ".0.0.jar:/opt/mq-pt-client/lib/cglib-nodep-2.2.jar:/opt/mq-pt-client/lib/casclient-2.1.1"
                + ".jar:/opt/mq-pt-client/lib/aspectjweaver-1.6.8.jar:/opt/mq-pt-client/lib/aopalliance-1.0"
                + ".jar:/opt/mq-pt-client/lib/ant-launcher-1.8.4.jar:/opt/mq-pt-client/lib/ant-1.8.4"
                + ".jar:/opt/jdk/jre/lib:.:/lib/dt.jar:/lib/tools.jar Main ESB MQM2 10.230.29.24 LMS.CLIENT 1428 1208"
                + " QU_LMS_REQUEST_COM_IN 1 PT_LMS_ESB2LMS_PRO_TEST";

        List<String> list = ProcessUtils.getProcessIdList(commandResult);
        System.out.println(list);

    }

}
