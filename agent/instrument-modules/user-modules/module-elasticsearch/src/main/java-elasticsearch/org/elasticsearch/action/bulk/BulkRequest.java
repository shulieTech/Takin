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
package org.elasticsearch.action.bulk;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.CompositeIndicesRequest;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/04/25 3:33 下午
 */
public class BulkRequest extends ActionRequest implements CompositeIndicesRequest, WriteRequest<BulkRequest> {

    @Override
    public BulkRequest setRefreshPolicy(RefreshPolicy refreshPolicy) {
        return null;
    }

    @Override
    public RefreshPolicy getRefreshPolicy() {
        return null;
    }

    @Override
    public ActionRequestValidationException validate() {
        return null;
    }

    public List requests() {
        return null;
    }

    @Nullable
    public List<Object> payloads() {
        return null;
    }

    public BulkRequest add(ActionRequest request) {
        return this;
    }

    public BulkRequest add(ActionRequest request, @Nullable Object payload) {
        return this;
    }


    public BulkRequest add(Iterable<ActionRequest> requests) {
        return this;
    }

    public BulkRequest add(DocWriteRequest... requests) {
        return this;
    }

    public BulkRequest add(DocWriteRequest request) {
        return add(request, null);
    }

    /**
     * Add a request to the current BulkRequest.
     * @param request Request to add
     * @param payload Optional payload
     * @return the current bulk request
     */
    public BulkRequest add(DocWriteRequest request, @Nullable Object payload) {
        return this;
    }

    /**
     * Adds a framed data in binary format
     */
    public BulkRequest add(byte[] data, int from, int length, XContentType xContentType) throws IOException {
        return add(data, from, length, null, null, xContentType);
    }

    /**
     * Adds a framed data in binary format
     */
    public BulkRequest add(byte[] data, int from, int length, @Nullable String defaultIndex, @Nullable String defaultType,
        XContentType xContentType) throws IOException {
        return add(new BytesArray(data, from, length), defaultIndex, defaultType, xContentType);
    }

    /**
     * Adds a framed data in binary format
     */
    public BulkRequest add(BytesReference data, @Nullable String defaultIndex, @Nullable String defaultType,
        XContentType xContentType) throws IOException {
        return add(data, defaultIndex, defaultType, null, null, null, null, null, true, xContentType);
    }

    /**
     * Adds a framed data in binary format
     */
    public BulkRequest add(BytesReference data, @Nullable String defaultIndex, @Nullable String defaultType, boolean allowExplicitIndex,
        XContentType xContentType) throws IOException {
        return add(data, defaultIndex, defaultType, null, null, null, null, null, allowExplicitIndex, xContentType);
    }

    public BulkRequest add(BytesReference data, @Nullable String defaultIndex, @Nullable String defaultType, @Nullable String
        defaultRouting, @Nullable String[] defaultFields, @Nullable FetchSourceContext defaultFetchSourceContext, @Nullable String
        defaultPipeline, @Nullable Object payload, boolean allowExplicitIndex, XContentType xContentType) throws IOException {
        return this;
    }
}
