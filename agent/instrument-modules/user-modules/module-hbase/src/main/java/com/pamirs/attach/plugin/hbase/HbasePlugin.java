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
package com.pamirs.attach.plugin.hbase;

import com.pamirs.attach.plugin.hbase.interceptor.*;
import com.pamirs.pradar.interceptor.Interceptors;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.ModuleLifecycleAdapter;
import com.shulie.instrument.simulator.api.instrument.EnhanceCallback;
import com.shulie.instrument.simulator.api.instrument.InstrumentClass;
import com.shulie.instrument.simulator.api.instrument.InstrumentMethod;
import com.shulie.instrument.simulator.api.listener.Listeners;
import com.shulie.instrument.simulator.api.scope.ExecutionPolicy;
import org.kohsuke.MetaInfServices;


/**
 * @author vincent
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "aliyun-hbase", version = "1.0.0", author = "xiaobin@shulie.io",description = "hbase 数据库,支持apache hbase 和阿里云 hbase")
public class HbasePlugin extends ModuleLifecycleAdapter implements ExtensionModule {

    @Override
    public void onActive() throws Throwable {
        addDdlTracer();

        enhanceTemplate.enhance(this, "com.flipkart.hbaseobjectmapper.WrappedHBTable", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod instrumentMethod = target.getDeclaredMethod("getName");
                instrumentMethod.addInterceptor(Listeners.of(WrappedHBTableGetNameInterceptor.class));
            }
        });

        enhanceTemplate.enhance(this, "org.apache.hadoop.hbase.client.HTable", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod getMethod = target.getDeclaredMethod("get", "org.apache.hadoop.hbase.client.Get");
                getMethod.addInterceptor(Listeners.of(HbaseInterceptor.class, "HBASE_OP_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

                InstrumentMethod method = target.getDeclaredMethods("append", "increment", "exists", "existsAll", "getScanner", "put", "checkAndPut", "delete", "checkAndDelete", "mutateRow", "checkAndMutate");
                method.addInterceptor(Listeners.of(HbaseInterceptor.class, "HBASE_OP_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));
            }
        });

        enhanceTemplate.enhance(this, "org.apache.hadoop.hbase.client.AliHBaseUETable", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {

                InstrumentMethod getMethod = target.getDeclaredMethod("get", "org.apache.hadoop.hbase.client.Get");
                getMethod.addInterceptor(Listeners.of(AliHBaseUETableInterceptor.class, "HBASE_OP_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

                InstrumentMethod methods = target.getDeclaredMethods("append", "increment", "exists", "existsAll", "getScanner", "put", "checkAndPut", "delete", "checkAndDelete", "mutateRow", "checkAndMutate");
                methods.addInterceptor(Listeners.of(AliHBaseUETableInterceptor.class, "HBASE_OP_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));
            }
        });

        enhanceTemplate.enhance(this, "org.apache.hadoop.hbase.client.AliHBaseMultiTable", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {

                InstrumentMethod getMethod = target.getDeclaredMethod("get", "org.apache.hadoop.hbase.client.Get");
                getMethod.addInterceptor(Listeners.of(AliHBaseMultiTableInterceptor.class, "HBASE_OP_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

                InstrumentMethod methods = target.getDeclaredMethods("append", "increment", "exists", "existsAll", "getScanner", "put", "checkAndPut", "delete", "checkAndDelete", "mutateRow", "checkAndMutate");
                methods.addInterceptor(Listeners.of(AliHBaseMultiTableInterceptor.class, "HBASE_OP_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));
            }
        });

        enhanceTemplate.enhance(this, "org.apache.hadoop.hbase.ipc.AbstractRpcClient", new RpcClientImplTransform());


        enhanceTemplate.enhance(this, "org.apache.hadoop.hbase.TableName", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod valueOfMethod0 = target.getDeclaredMethod("valueOf", "byte[]");
                valueOfMethod0.addInterceptor(Listeners.of(HbaseTableInterceptor.class, "HBASE_OP_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

                InstrumentMethod valueOfMethod1 = target.getDeclaredMethod("valueOf", "java.lang.String");
                valueOfMethod1.addInterceptor(Listeners.of(HbaseTableInterceptor.class, "HBASE_OP_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

                InstrumentMethod valueOfMethod2 = target.getDeclaredMethod("valueOf", "java.lang.String", "java.lang.String");
                valueOfMethod2.addInterceptor(Listeners.of(HbaseTableValueOfInterceptor.class, "HBASE_OP_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

                InstrumentMethod valueOfMethod3 = target.getDeclaredMethod("valueOf", "byte[]", "byte[]");
                valueOfMethod3.addInterceptor(Listeners.of(HbaseTableValueOfInterceptor.class, "HBASE_OP_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

                InstrumentMethod valueOfMethod4 = target.getDeclaredMethod("valueOf", "java.nio.ByteBuffer", "java.nio.ByteBuffer");
                valueOfMethod4.addInterceptor(Listeners.of(HbaseTableValueOfInterceptor.class, "HBASE_OP_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));
            }
        });

        enhanceTemplate.enhance(this,"com.alibaba.lindorm.client.core.LindormWideColumnService",
                new EnhanceCallback() {
                    @Override
                    public void doEnhance(InstrumentClass target) {
                        target.getDeclaredMethod("get", "java.lang.String",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WGet")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("put", "java.lang.String",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WPut")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("append", "java.lang.String",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WAppend")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("increment", "java.lang.String",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WIncrement")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("delete", "java.lang.String",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WDelete")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("exists", "java.lang.String",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WGet")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("addColumn", "java.lang.String", "java.util.List")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("addColumn", "java.lang.String",
                                "com.alibaba.lindorm.client.schema.ColumnSchema", "int")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("addColumn", "java.lang.String", "java.util.List", "int")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("addColumn", "java.lang.String",
                                "com.alibaba.lindorm.client.schema.ColumnSchema")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("existsAsync", "java.lang.String",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WGet")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("existsAsync", "java.lang.String",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WGet",
                                "com.alibaba.lindorm.client.AsyncCallback")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("getAsync", "java.lang.String",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WGet",
                                "com.alibaba.lindorm.client.AsyncCallback")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("getAsync", "java.lang.String",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WGet")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("batchGet", "java.lang.String", "java.util.List")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("batchGetAsync", "java.lang.String", "java.util.List")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("batchGetAsync", "java.lang.String", "java.util.List",
                                "com.alibaba.lindorm.client.AsyncCallback")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("getScanner", "java.lang.String",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WScan")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("putAsync", "java.lang.String",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WPut",
                                "com.alibaba.lindorm.client.AsyncCallback")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("putAsync", "java.lang.String",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WPut")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("batchPut", "java.lang.String", "java.util.List")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("batchPutAsync", "java.lang.String", "java.util.List")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("batchPutAsync", "java.lang.String", "java.util.List",
                                "com.alibaba.lindorm.client.AsyncCallback")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("checkAndPut", "java.lang.String", "byte[]", "byte[]", "byte[]", "byte[]",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WPut")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("checkAndPut", "java.lang.String", "byte[]", "byte[]", "byte[]",
                                "com.alibaba.lindorm.client.core.widecolumnservice.filter.WCompareFilter$CompareOp", "byte[]",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WPut")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("checkAndPutAsync", "java.lang.String", "byte[]", "byte[]", "byte[]",
                                "byte[]", "com.alibaba.lindorm.client.core.widecolumnservice.WPut",
                                "com.alibaba.lindorm.client.AsyncCallback")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("checkAndPutAsync", "java.lang.String", "byte[]", "byte[]", "byte[]",
                                "com.alibaba.lindorm.client.core.widecolumnservice.filter.WCompareFilter$CompareOp", "byte[]",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WPut",
                                "com.alibaba.lindorm.client.AsyncCallback")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("checkAndPutAsync", "java.lang.String", "byte[]", "byte[]", "byte[]",
                                "com.alibaba.lindorm.client.core.widecolumnservice.filter.WCompareFilter$CompareOp", "byte[]",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WPut")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("checkAndPutAsync", "java.lang.String", "byte[]", "byte[]", "byte[]",
                                "byte[]", "com.alibaba.lindorm.client.core.widecolumnservice.WPut")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("deleteAsync", "java.lang.String",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WDelete")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("deleteAsync", "java.lang.String",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WDelete",
                                "com.alibaba.lindorm.client.AsyncCallback")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("batchDelete", "java.lang.String", "java.util.List")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("batchDeleteAsync", "java.lang.String", "java.util.List")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("batchDeleteAsync", "java.lang.String", "java.util.List",
                                "com.alibaba.lindorm.client.AsyncCallback")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("checkAndDelete", "java.lang.String", "byte[]", "byte[]", "byte[]",
                                "byte[]", "com.alibaba.lindorm.client.core.widecolumnservice.WDelete")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("checkAndDelete", "java.lang.String", "byte[]", "byte[]", "byte[]",
                                "com.alibaba.lindorm.client.core.widecolumnservice.filter.WCompareFilter$CompareOp", "byte[]",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WDelete")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("checkAndDeleteAsync", "java.lang.String", "byte[]", "byte[]", "byte[]",
                                "byte[]", "com.alibaba.lindorm.client.core.widecolumnservice.WDelete",
                                "com.alibaba.lindorm.client.AsyncCallback")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("checkAndDeleteAsync", "java.lang.String", "byte[]", "byte[]", "byte[]",
                                "com.alibaba.lindorm.client.core.widecolumnservice.filter.WCompareFilter$CompareOp", "byte[]",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WDelete")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("checkAndDeleteAsync", "java.lang.String", "byte[]", "byte[]", "byte[]",
                                "com.alibaba.lindorm.client.core.widecolumnservice.filter.WCompareFilter$CompareOp", "byte[]",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WDelete",
                                "com.alibaba.lindorm.client.AsyncCallback")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("checkAndDeleteAsync", "java.lang.String", "byte[]", "byte[]", "byte[]",
                                "byte[]", "com.alibaba.lindorm.client.core.widecolumnservice.WDelete")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("checkAndMutate", "java.lang.String", "byte[]", "byte[]", "byte[]",
                                "com.alibaba.lindorm.client.core.widecolumnservice.filter.WCompareFilter$CompareOp", "byte[]",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WRowMutations")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("checkAndMutateAsync", "java.lang.String", "byte[]", "byte[]", "byte[]",
                                "com.alibaba.lindorm.client.core.widecolumnservice.filter.WCompareFilter$CompareOp", "byte[]",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WRowMutations")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("checkAndMutateAsync", "java.lang.String", "byte[]", "byte[]", "byte[]",
                                "com.alibaba.lindorm.client.core.widecolumnservice.filter.WCompareFilter$CompareOp", "byte[]",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WRowMutations",
                                "com.alibaba.lindorm.client.AsyncCallback")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("appendAsync", "java.lang.String",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WAppend")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("appendAsync", "java.lang.String",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WAppend",
                                "com.alibaba.lindorm.client.AsyncCallback")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("batchAppend", "java.lang.String", "java.util.List")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("batchAppendAsync", "java.lang.String", "java.util.List",
                                "com.alibaba.lindorm.client.AsyncCallback")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("batchAppendAsync", "java.lang.String", "java.util.List")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("incrementAsync", "java.lang.String",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WIncrement",
                                "com.alibaba.lindorm.client.AsyncCallback")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("incrementAsync", "java.lang.String",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WIncrement")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("batchIncrement", "java.lang.String", "java.util.List")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("batchIncrementAsync", "java.lang.String", "java.util.List")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("batchIncrementAsync", "java.lang.String", "java.util.List",
                                "com.alibaba.lindorm.client.AsyncCallback")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("scanAsync", "java.lang.String",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WScan",
                                "com.alibaba.lindorm.client.AsyncCallback")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("scanAsync", "java.lang.String",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WScan")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("addColumnAsync", "java.lang.String",
                                "com.alibaba.lindorm.client.schema.ColumnSchema")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("addColumnAsync", "java.lang.String", "java.util.List")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("setWriteBufferSize", "java.lang.String", "int")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("getClearBufferOnFailure", "java.lang.String")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("getWriteBufferSize", "java.lang.String")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("setAutoFlush", "java.lang.String", "boolean")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("setAutoFlush", "java.lang.String", "boolean", "boolean")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("getAutoFlush", "java.lang.String")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("flushCommits", "java.lang.String")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("getStartKeys", "java.lang.String")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("getStartEndKeys", "java.lang.String")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("getEndKeys", "java.lang.String")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("addExternalIndex", "java.lang.String", "java.util.List")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("addExternalIndex", "java.lang.String", "java.lang.String",
                                "com.alibaba.lindorm.client.core.meta.ExternalIndexType",
                                "com.alibaba.lindorm.client.core.meta.ExternalIndexRowFormatterType",
                                "com.alibaba.lindorm.client.core.meta.ExternalIndexField[]")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("addExternalIndex", "java.lang.String", "java.lang.String",
                                "com.alibaba.lindorm.client.core.meta.ExternalIndexType",
                                "com.alibaba.lindorm.client.core.meta.ExternalIndexRowFormatterType", "java.util.List")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("addExternalIndex", "java.lang.String",
                                "com.alibaba.lindorm.client.core.meta.ExternalIndexConfig", "java.util.List")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("addExternalIndex", "java.lang.String",
                                "com.alibaba.lindorm.client.core.meta.ExternalIndexConfig",
                                "com.alibaba.lindorm.client.core.meta.ExternalIndexField[]")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("addExternalIndex", "java.lang.String",
                                "com.alibaba.lindorm.client.core.meta.ExternalIndexField[]")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("removeExternalIndex", "java.lang.String", "java.lang.String[]")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("removeExternalIndex", "java.lang.String", "java.util.List")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("buildExternalIndex", "java.lang.String")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("cancelBuildExternalIndex", "java.lang.String")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));

                        target.getDeclaredMethod("scan", "java.lang.String",
                                "com.alibaba.lindorm.client.core.widecolumnservice.WScan")
                                .addInterceptor(Listeners.of(LindormWideColumnServiceInterceptor.class));
                    }
                });


    }

    public static class RpcClientImplTransform implements EnhanceCallback {

        @Override
        public void doEnhance(InstrumentClass target) {
            InstrumentMethod method = target.getDeclaredMethods("getConnection");
            method.addInterceptor(Listeners.of(HbaseClientMethodInterceptor.class, "HBASE_OP_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));
        }
    }

    private void addDdlTracer() {
        enhanceTemplate.enhance(this, "org.apache.hadoop.hbase.client.AliHBaseUEAdmin",
                new EnhanceCallback() {
                    @Override
                    public void doEnhance(InstrumentClass target) {
                        InstrumentMethod createMethod =
                                target.getDeclaredMethod("createTable",
                                        "org.apache.hadoop.hbase.client.TableDescriptor"
                                        , "byte[][]");
                        createMethod.addInterceptor(Listeners.of(DdlTracerInterceptor.class));

                        InstrumentMethod deleteMethod = target.getDeclaredMethod("deleteTable",
                                "org.apache.hadoop.hbase.TableName");
                        deleteMethod.addInterceptor(Listeners.of(DdlTracerInterceptor.class));

                        InstrumentMethod truncateTableMethod = target.getDeclaredMethod("truncateTable",
                                "org.apache.hadoop.hbase.TableName", "boolean");
                        truncateTableMethod.addInterceptor(Listeners.of(DdlTracerInterceptor.class));

                        InstrumentMethod enableTableMethod = target.getDeclaredMethod("enableTable",
                                "org.apache.hadoop.hbase.TableName");
                        enableTableMethod.addInterceptor(Listeners.of(DdlTracerInterceptor.class));

                        InstrumentMethod disableTableMethod = target.getDeclaredMethod("disableTable",
                                "org.apache.hadoop.hbase.TableName");
                        disableTableMethod.addInterceptor(Listeners.of(DdlTracerInterceptor.class));
                    }
                });

    }

}
