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
package com.pamirs.attach.plugin.es.common;

import com.pamirs.attach.plugin.es.common.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/7/13 6:11 下午
 */
public final class RequestIndexRenameProvider {
    private static Map<String, RequestIndexRename> registry = new HashMap<String, RequestIndexRename>();

    static {
        registry.put("org.elasticsearch.client.indices.CreateIndexRequest", new CreateIndexRequestRename());
        registry.put("org.elasticsearch.client.indices.DeleteAliasRequest", new DeleteAliasRequestRename());
        registry.put("org.elasticsearch.client.indices.FreezeIndexRequest", new FreezeIndexRequestRename());
        registry.put("org.elasticsearch.action.admin.indices.create.CreateIndexRequest", new CreateIndexRequest1Rename());
        registry.put("org.elasticsearch.client.indices.GetIndexRequest", new GetIndexRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.get.GetIndexRequest", new GetIndexRequestIndex1Rename());
        registry.put("org.elasticsearch.action.search.SearchRequest", new SearchRequestIndexRename());
        registry.put("org.elasticsearch.action.index.IndexRequest", new IndexRequestIndexRename());
        registry.put("org.elasticsearch.action.get.GetRequest", new GetRequestIndexRename());
        registry.put("org.elasticsearch.action.update.UpdateRequest", new UpdateRequestIndexRename());
        registry.put("org.elasticsearch.action.delete.DeleteRequest", new DeleteRequestIndexRename());
        registry.put("org.elasticsearch.action.suggest.SuggestRequest", new SuggestRequestIndexRename());
        registry.put("org.elasticsearch.action.bulk.BulkRequest", new BulkRequestIndexRename());
        registry.put("org.elasticsearch.action.explain.ExplainRequest", new ExplainRequestIndexRename());
        registry.put("org.elasticsearch.action.termvectors.TermVectorsRequest", new TermVectorsRequestIndexRename());
        registry.put("org.elasticsearch.action.termvectors.MultiTermVectorsRequest", new MultiTermVectorsRequestIndexRename());
        registry.put("org.elasticsearch.action.percolate.PercolateRequest", new PercolateRequestIndexRename());
        registry.put("org.elasticsearch.action.percolate.MultiPercolateRequest", new MultiPercolateRequestIndexRename());
        registry.put("org.elasticsearch.action.get.MultiGetRequest", new MultiGetRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.refresh.RefreshRequest", new RefreshRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.recovery.RecoveryRequest", new RecoveryRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest", new IndicesAliasesRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest", new GetAliasesRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest", new AnalyzeRequestIndexRename());
        registry.put("org.elasticsearch.client.indices.AnalyzeRequest", new AnalyzeRequestIndexRename0());
        registry.put("org.elasticsearch.action.admin.indices.cache.clear.ClearIndicesCacheRequest", new ClearIndicesCacheRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.close.CloseIndexRequest", new CloseIndexRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest", new DeleteIndexRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest", new IndicesExistsRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest", new TypeExistsRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.flush.FlushRequest", new FlushRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.flush.ShardFlushRequest", new ShardFlushRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.flush.SyncedFlushRequest", new SyncedFlushRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.forcemerge.ForceMergeRequest", new ForceMergeRequestIndexRename());
        registry.put("org.elasticsearch.client.indices.GetFieldMappingsRequest", new GetFieldMappingsRequestIndexRename());
        registry.put("org.elasticsearch.client.indices.GetMappingsRequest", new GetMappingsRequestIndexRename());
        registry.put("org.elasticsearch.client.indices.PutMappingRequest", new PutMappingRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.open.OpenIndexRequest", new OpenIndexRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.segments.IndicesSegmentsRequest", new IndicesSegementsRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest", new GetSettingsRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest", new UpdateSettingsRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.shards.IndicesShardStoresRequest", new IndicesShardStoresRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest", new IndicesStatsRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.template.delete.DeleteIndexTemplateRequest", new DeleteIndexTemplateRequestIndexRename());
        registry.put("org.elasticsearch.client.indices.GetIndexTemplatesRequest", new GetIndexTemplatesRequestIndexRename());
        registry.put("org.elasticsearch.client.indices.PutIndexTemplateRequest", new PutMappingRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.upgrade.get.UpgradeStatusRequest", new UpgradeStatusRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.upgrade.post.ShardUpgradeRequest", new ShardUpgradeRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.upgrade.post.UpgradeRequest", new UpgradeRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.validate.query.ValidateQueryRequest", new ValidateQueryRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.warmer.delete.DeleteWarmerRequest", new DeleteWarmerRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.warmer.put.PutWarmerRequest", new PutWarmerRequestIndexRename());
        registry.put("org.elasticsearch.action.admin.indices.warmer.get.GetWarmersRequest", new GetWarmersRequestIndexRename());
        registry.put("org.elasticsearch.index.reindex.DeleteByQueryRequest", new DeleteByQueryRequestIndexRename());
        registry.put("org.elasticsearch.action.bulk.byscroll.DeleteByQueryRequest", new DeleteByQueryRequestIndexRename0());
        registry.put("org.elasticsearch.client.indices.CloseIndexRequest", new CloseIndexRequestIndexRename0());
        registry.put("org.elasticsearch.client.Request", new RestRequestIndexRename());
        registry.put("org.elasticsearch.action.search.ClearScrollRequest", new ClearScrollRequestRename());
        registry.put("org.elasticsearch.action.admin.cluster.state.ClusterStateRequest", new ClusterStateRequestRename());
    }


    public static RequestIndexRename get(Object target) {
        if (target == null) {
            return null;
        }
        return registry.get(target.getClass().getName());
    }
}
