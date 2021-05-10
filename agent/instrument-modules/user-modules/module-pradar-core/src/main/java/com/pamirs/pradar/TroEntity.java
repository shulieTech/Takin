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
package com.pamirs.pradar;


import java.util.List;

/**
 * 说明:
 *
 * @author JasonYan
 * @since : Create in 2018/7/4 15:34
 */
public class TroEntity {

    /**
     * DefaultMessage : {"agentId":"es-server@10.10.101.107:28081","aliveTrace":1,"msgType":"0","methodName":"doFilter","traceMessages":[{"sqls":["select admin0_.id as id1_2_0_, admin0_.address as address2_2_0_, admin0_.adminiconurl as adminico3_2_0_, admin0_.adminname as adminnam4_2_0_, admin0_.alipay as alipay5_2_0_, admin0_.cash_password as cash_pas6_2_0_, admin0_.client_icon as client_i7_2_0_, admin0_.client_qr_url as client_q8_2_0_, admin0_.clienturl as clientur9_2_0_, admin0_.id_code as id_code10_2_0_, admin0_.idcode_url as idcode_11_2_0_, admin0_.is_set_password as is_set_12_2_0_, admin0_.name as name13_2_0_, admin0_.nickname as nicknam14_2_0_, admin0_.password as passwor15_2_0_, admin0_.phone as phone16_2_0_, admin0_.register_date as registe17_2_0_, admin0_.sell_url as sell_ur18_2_0_, admin0_.status as status19_2_0_, admin0_.store_info_secrecy as store_i20_2_0_, admin0_.store_url1 as store_u21_2_0_, admin0_.store_url2 as store_u22_2_0_, admin0_.store_url3 as store_u23_2_0_, admin0_.train_url as train_u24_2_0_, admin0_.wei_chat as wei_cha25_2_0_ from admin admin0_ where admin0_.id=2"],"msgType":"2","methodName":"executeQuery","className":"com.mysql.jdbc.PreparedStatement","interval":592999,"startTime":"2018-05-22 14:19:14","url":"jdbc:mysql://10.10.10.10:3306/dbtest1?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull"},{"sqls":["select count(*) from user where store_id = 'ds2'"],"msgType":"2","methodName":"executeQuery","className":"com.mysql.jdbc.PreparedStatement","interval":449021,"startTime":"2018-05-22 14:19:14","url":"jdbc:mysql://10.10.10.10:3306/dbtest1?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull"}],"className":"org.springframework.web.filter.OncePerRequestFilter","parentSpanId":-1,"sampleRate":"1.0","transactionId":"2ba5ab8c57fbd499380ba10c8551e367b^2189","url":"/apmServer-sl/app/mapUrls","spanId":1,"cpuTime":2534917,"gcTime":0,"interval":3557880,"startTime":"2018-03-28 13:40:51","statusCode":200}
     * SimpleExceptions : [{"exceptionType":"org.elasticsearch.index.IndexNotFoundException","message":"no such index","stackTraceElement":[{"fileName":"IndexNameExpressionResolver.java","nativeMethod":false,"methodName":"infe","className":"org.elasticsearch.cluster.metadata.IndexNameExpressionResolver$WildcardExpressionResolver","lineNumber":676},{"fileName":"IndexNameExpressionResolver.java","nativeMethod":false,"methodName":"innerResolve","className":"org.elasticsearch.cluster.metadata.IndexNameExpressionResolver$WildcardExpressionResolver","lineNumber":630},{"fileName":"IndexNameExpressionResolver.java","nativeMethod":false,"methodName":"resolve","className":"org.elasticsearch.cluster.metadata.IndexNameExpressionResolver$WildcardExpressionResolver","lineNumber":578},{"fileName":"IndexNameExpressionResolver.java","nativeMethod":false,"methodName":"concreteIndices","className":"org.elasticsearch.cluster.metadata.IndexNameExpressionResolver","lineNumber":168},{"fileName":"IndexNameExpressionResolver.java","nativeMethod":false,"methodName":"concreteIndices","className":"org.elasticsearch.cluster.metadata.IndexNameExpressionResolver","lineNumber":140}]}]
     */

    private DefaultMessage defaultMessage;
    private List<SimpleExceptions> simpleExceptions;

    public DefaultMessage getDefaultMessage() {
        return defaultMessage;
    }

    public void setDefaultMessage(DefaultMessage defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public List<SimpleExceptions> getSimpleExceptions() {
        return simpleExceptions;
    }

    public void setSimpleExceptions(List<SimpleExceptions> simpleExceptions) {
        this.simpleExceptions = simpleExceptions;
    }

    public static class DefaultMessage {
        /**
         * agentId : es-server@10.10.101.107:28081
         * aliveTrace : 1
         * msgType : 0
         * methodName : doFilter
         * traceMessages : [{"sqls":["select admin0_.id as id1_2_0_, admin0_.address as address2_2_0_, admin0_.adminiconurl as adminico3_2_0_, admin0_.adminname as adminnam4_2_0_, admin0_.alipay as alipay5_2_0_, admin0_.cash_password as cash_pas6_2_0_, admin0_.client_icon as client_i7_2_0_, admin0_.client_qr_url as client_q8_2_0_, admin0_.clienturl as clientur9_2_0_, admin0_.id_code as id_code10_2_0_, admin0_.idcode_url as idcode_11_2_0_, admin0_.is_set_password as is_set_12_2_0_, admin0_.name as name13_2_0_, admin0_.nickname as nicknam14_2_0_, admin0_.password as passwor15_2_0_, admin0_.phone as phone16_2_0_, admin0_.register_date as registe17_2_0_, admin0_.sell_url as sell_ur18_2_0_, admin0_.status as status19_2_0_, admin0_.store_info_secrecy as store_i20_2_0_, admin0_.store_url1 as store_u21_2_0_, admin0_.store_url2 as store_u22_2_0_, admin0_.store_url3 as store_u23_2_0_, admin0_.train_url as train_u24_2_0_, admin0_.wei_chat as wei_cha25_2_0_ from admin admin0_ where admin0_.id=2"],"msgType":"2","methodName":"executeQuery","className":"com.mysql.jdbc.PreparedStatement","interval":592999,"startTime":"2018-05-22 14:19:14","url":"jdbc:mysql://10.10.10.10:3306/dbtest1?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull"},{"sqls":["select count(*) from user where store_id = 'ds2'"],"msgType":"2","methodName":"executeQuery","className":"com.mysql.jdbc.PreparedStatement","interval":449021,"startTime":"2018-05-22 14:19:14","url":"jdbc:mysql://10.10.10.10:3306/dbtest1?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull"}]
         * className : org.springframework.web.filter.OncePerRequestFilter
         * parentSpanId : -1
         * sampleRate : 1.0
         * transactionId : 2ba5ab8c57fbd499380ba10c8551e367b^2189
         * url : /apmServer-sl/app/mapUrls
         * spanId : 1
         * cpuTime : 2534917
         * gcTime : 0
         * interval : 3557880
         * startTime : 2018-03-28 13:40:51
         * statusCode : 200
         */

        private String agentId;
        private int aliveTrace;
        private String msgType;
        private String methodName;
        private String className;
        private int parentSpanId;
        private String sampleRate;
        private String transactionId;
        private String url;
        private int spanId;
        private int cpuTime;
        private int gcTime;
        private int interval;
        private String startTime;
        private int statusCode;
        private List<TraceMessages> traceMessages;

        public String getAgentId() {
            return agentId;
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }

        public int getAliveTrace() {
            return aliveTrace;
        }

        public void setAliveTrace(int aliveTrace) {
            this.aliveTrace = aliveTrace;
        }

        public String getMsgType() {
            return msgType;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public int getParentSpanId() {
            return parentSpanId;
        }

        public void setParentSpanId(int parentSpanId) {
            this.parentSpanId = parentSpanId;
        }

        public String getSampleRate() {
            return sampleRate;
        }

        public void setSampleRate(String sampleRate) {
            this.sampleRate = sampleRate;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getSpanId() {
            return spanId;
        }

        public void setSpanId(int spanId) {
            this.spanId = spanId;
        }

        public int getCpuTime() {
            return cpuTime;
        }

        public void setCpuTime(int cpuTime) {
            this.cpuTime = cpuTime;
        }

        public int getGcTime() {
            return gcTime;
        }

        public void setGcTime(int gcTime) {
            this.gcTime = gcTime;
        }

        public int getInterval() {
            return interval;
        }

        public void setInterval(int interval) {
            this.interval = interval;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public List<TraceMessages> getTraceMessages() {
            return traceMessages;
        }

        public void setTraceMessages(List<TraceMessages> traceMessages) {
            this.traceMessages = traceMessages;
        }

        public static class TraceMessages {
            /**
             * sqls : ["select admin0_.id as id1_2_0_, admin0_.address as address2_2_0_, admin0_.adminiconurl as adminico3_2_0_, admin0_.adminname as adminnam4_2_0_, admin0_.alipay as alipay5_2_0_, admin0_.cash_password as cash_pas6_2_0_, admin0_.client_icon as client_i7_2_0_, admin0_.client_qr_url as client_q8_2_0_, admin0_.clienturl as clientur9_2_0_, admin0_.id_code as id_code10_2_0_, admin0_.idcode_url as idcode_11_2_0_, admin0_.is_set_password as is_set_12_2_0_, admin0_.name as name13_2_0_, admin0_.nickname as nicknam14_2_0_, admin0_.password as passwor15_2_0_, admin0_.phone as phone16_2_0_, admin0_.register_date as registe17_2_0_, admin0_.sell_url as sell_ur18_2_0_, admin0_.status as status19_2_0_, admin0_.store_info_secrecy as store_i20_2_0_, admin0_.store_url1 as store_u21_2_0_, admin0_.store_url2 as store_u22_2_0_, admin0_.store_url3 as store_u23_2_0_, admin0_.train_url as train_u24_2_0_, admin0_.wei_chat as wei_cha25_2_0_ from admin admin0_ where admin0_.id=2"]
             * msgType : 2
             * methodName : executeQuery
             * className : com.mysql.jdbc.PreparedStatement
             * interval : 592999
             * startTime : 2018-05-22 14:19:14
             * url : jdbc:mysql://10.10.10.10:3306/dbtest1?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull
             */

            private String msgType;
            private String methodName;
            private String className;
            private int interval;
            private String startTime;
            private String url;
            private List<String> sqls;

            public String getMsgType() {
                return msgType;
            }

            public void setMsgType(String msgType) {
                this.msgType = msgType;
            }

            public String getMethodName() {
                return methodName;
            }

            public void setMethodName(String methodName) {
                this.methodName = methodName;
            }

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }

            public int getInterval() {
                return interval;
            }

            public void setInterval(int interval) {
                this.interval = interval;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public List<String> getSqls() {
                return sqls;
            }

            public void setSqls(List<String> sqls) {
                this.sqls = sqls;
            }
        }
    }

    public static class SimpleExceptions {
        /**
         * exceptionType : org.elasticsearch.index.IndexNotFoundException
         * message : no such index
         * stackTraceElement : [{"fileName":"IndexNameExpressionResolver.java","nativeMethod":false,"methodName":"infe","className":"org.elasticsearch.cluster.metadata.IndexNameExpressionResolver$WildcardExpressionResolver","lineNumber":676},{"fileName":"IndexNameExpressionResolver.java","nativeMethod":false,"methodName":"innerResolve","className":"org.elasticsearch.cluster.metadata.IndexNameExpressionResolver$WildcardExpressionResolver","lineNumber":630},{"fileName":"IndexNameExpressionResolver.java","nativeMethod":false,"methodName":"resolve","className":"org.elasticsearch.cluster.metadata.IndexNameExpressionResolver$WildcardExpressionResolver","lineNumber":578},{"fileName":"IndexNameExpressionResolver.java","nativeMethod":false,"methodName":"concreteIndices","className":"org.elasticsearch.cluster.metadata.IndexNameExpressionResolver","lineNumber":168},{"fileName":"IndexNameExpressionResolver.java","nativeMethod":false,"methodName":"concreteIndices","className":"org.elasticsearch.cluster.metadata.IndexNameExpressionResolver","lineNumber":140}]
         */

        private String exceptionType;
        private String message;
        private List<StackTraceElement> stackTraceElement;

        public String getExceptionType() {
            return exceptionType;
        }

        public void setExceptionType(String exceptionType) {
            this.exceptionType = exceptionType;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<StackTraceElement> getStackTraceElement() {
            return stackTraceElement;
        }

        public void setStackTraceElement(List<StackTraceElement> stackTraceElement) {
            this.stackTraceElement = stackTraceElement;
        }

        public static class StackTraceElement {
            /**
             * fileName : IndexNameExpressionResolver.java
             * nativeMethod : false
             * methodName : infe
             * className : org.elasticsearch.cluster.metadata.IndexNameExpressionResolver$WildcardExpressionResolver
             * lineNumber : 676
             */

            private String fileName;
            private boolean nativeMethod;
            private String methodName;
            private String className;
            private int lineNumber;

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

            public boolean isNativeMethod() {
                return nativeMethod;
            }

            public void setNativeMethod(boolean nativeMethod) {
                this.nativeMethod = nativeMethod;
            }

            public String getMethodName() {
                return methodName;
            }

            public void setMethodName(String methodName) {
                this.methodName = methodName;
            }

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }

            public int getLineNumber() {
                return lineNumber;
            }

            public void setLineNumber(int lineNumber) {
                this.lineNumber = lineNumber;
            }
        }
    }
}
