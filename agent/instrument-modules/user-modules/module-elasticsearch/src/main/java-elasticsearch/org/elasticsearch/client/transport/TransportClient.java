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
package org.elasticsearch.client.transport;

import java.util.List;

import org.elasticsearch.action.Action;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.ActionType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.support.AbstractClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.threadpool.ThreadPool;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/04/20 10:20 上午
 */
public class TransportClient extends AbstractClient {

    public TransportClient(Settings settings,
        ThreadPool threadPool) {
        super(settings, threadPool);
    }

    public static TransportClient.Builder builder() {
        return new Builder();
    }

    @Override
    protected <Request extends ActionRequest, Response extends ActionResponse,
        RequestBuilder extends ActionRequestBuilder<Request, Response, RequestBuilder>> void doExecute(
        Action<Request, Response, RequestBuilder> action, Request request, ActionListener<Response> listener) {
    }

    public <Request extends ActionRequest, Response extends ActionResponse> void execute(
        ActionType<Response> action, Request request, ActionListener<Response> listener) {
    }

    public List<DiscoveryNode> listedNodes() {
        return null;
    }

    @Override
    public void close() {

    }

    public TransportClient addTransportAddress(TransportAddress transportAddress) {
        return this;
    }

    @Override
    public Client getRemoteClusterClient(String clusterAlias) {
        return null;
    }

    public interface HostFailureListener {
    }

    public static class Builder {

        public Builder() {
        }

        public TransportClient.Builder settings(org.elasticsearch.common.settings.Settings.Builder settings) {
            return this.settings(settings.build());
        }

        public TransportClient.Builder settings(Settings settings) {
            return this;
        }

        public TransportClient.Builder addPlugin(Class<? extends Plugin> pluginClass) {
            return this;
        }

        public TransportClient build() {
            return null;
        }
    }
}
