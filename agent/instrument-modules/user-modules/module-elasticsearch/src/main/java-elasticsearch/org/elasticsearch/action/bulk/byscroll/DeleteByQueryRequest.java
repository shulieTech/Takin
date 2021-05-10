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
package org.elasticsearch.action.bulk.byscroll;

import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.IndicesRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.common.logging.DeprecationLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.tasks.TaskId;

import static org.elasticsearch.action.ValidateActions.addValidationError;

public class DeleteByQueryRequest extends AbstractBulkByScrollRequest<DeleteByQueryRequest> implements IndicesRequest.Replaceable {

    private static final DeprecationLogger DEPRECATION_LOGGER = new DeprecationLogger(Loggers.getLogger(DeleteByQueryRequest.class));

    public DeleteByQueryRequest() {
    }

    public DeleteByQueryRequest(SearchRequest search) {
    }

    @Override
    protected DeleteByQueryRequest self() {
        return this;
    }

    @Override
    public ActionRequestValidationException validate() {
        return null;
    }

    @Override
    protected DeleteByQueryRequest forSlice(TaskId slicingTask, SearchRequest slice) {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }

    //delete by query deletes all documents that match a query. The indices and indices options that affect how
    //indices are resolved depend entirely on the inner search request. That's why the following methods delegate to it.
    @Override
    public IndicesRequest indices(String... indices) {
        return this;
    }

    @Override
    public String[] indices() {
        return getSearchRequest().indices();
    }

    @Override
    public IndicesOptions indicesOptions() {
        assert getSearchRequest() != null;
        return getSearchRequest().indicesOptions();
    }

    public String[] types() {
        assert getSearchRequest() != null;
        return getSearchRequest().types();
    }

    public DeleteByQueryRequest types(String... types) {
        assert getSearchRequest() != null;
        getSearchRequest().types(types);
        return this;
    }

}
