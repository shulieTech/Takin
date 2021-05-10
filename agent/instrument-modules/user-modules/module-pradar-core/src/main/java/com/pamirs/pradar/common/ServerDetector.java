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
package com.pamirs.pradar.common;


/**
 * fork from
 *
 * @author shiyajian
 * @since : 2020-07-21
 */
public final class ServerDetector {

    private ServerDetector() { /* no instance */ }

    public enum ServerType {
        GLASSFISH("GlassFish"),
        JBOSS("JBoss"),
        JETTY("Jetty"),
        JONAS("JOnAS"),
        OC4J("OC4J"),
        RESIN("Resin"),
        TOMCAT("Tomcat"),
        UNKNOWN("Unknown"),
        WEBLOGIC("WebLogic"),
        WEBSPHERE("WebSphere"),
        WILDFLY("WildFly");

        private String desc;

        ServerType(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }

    private static boolean _hasSystemProperty(String key) {
        return System.getProperty(key) != null;
    }

    private static boolean _detect(String className) {
        try {
            ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
            systemClassLoader.loadClass(className);
            return true;
        } catch (ClassNotFoundException classNotFoundException) {
            return ServerDetector.class.getResource(className) != null;
        }
    }

    public static ServerType getServerType() {

        if (_hasSystemProperty("com.sun.aas.instanceRoot")) {
            return ServerType.GLASSFISH;
        }

        if (_hasSystemProperty("jboss.home.dir")) {
            return ServerType.JBOSS;
        }

        if (_hasSystemProperty("jonas.base")) {
            return ServerType.JONAS;
        }

        if (_detect("oracle.oc4j.util.ClassUtils")) {
            return ServerType.OC4J;
        }

        if (_hasSystemProperty("resin.home")) {
            return ServerType.RESIN;
        }

        if (_detect("/weblogic/Server.class")) {
            return ServerType.WEBLOGIC;
        }

        if (_detect("/com/ibm/websphere/product/VersionInfo.class")) {
            return ServerType.WEBSPHERE;
        }

        if (_hasSystemProperty("jboss.home.dir")) {
            return ServerType.WILDFLY;
        }

        if (_hasSystemProperty("jetty.home")) {
            return ServerType.JETTY;
        }

        if (_hasSystemProperty("catalina.base")) {
            return ServerType.TOMCAT;
        }

        return ServerType.UNKNOWN;
    }


}
