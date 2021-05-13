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

package io.shulie.tro.web.app.controller.linkmanage.node;

/**
 * @Auther: vernon
 * @Date: 2019/12/26 18:45
 * @Description:
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Node {

    public String id;

    public String pid;

    public String content;

    List<Node> nodes = null;

    public Node() {
        this.nodes = new ArrayList<>();
    }

    public Node(String id, String pid, String content) {
        this.id = id;
        this.pid = pid;
        this.content = content;
        if (nodes == null) {
            nodes = new ArrayList<>();
        }
    }

    public Node(Map map) {
        System.out.println(map);
        id = (String)map.get("id");
        pid = (String)map.get("pid");
        content = (String)map.get("name");
        if (nodes == null) {
            nodes = new ArrayList<>();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setNode(Node node) {
        this.nodes.add(node);
    }

    public List<Node> getNodes() {
        return this.nodes;
    }
}
