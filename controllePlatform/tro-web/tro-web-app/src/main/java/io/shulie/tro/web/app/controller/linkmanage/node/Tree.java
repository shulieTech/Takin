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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: vernon
 * @Date: 2019/12/26 18:47
 * @Description:
 */

public class Tree {

    private static String NODE_ID = "id";
    private static String NODE_PID = "pid";
    private static String NODE_NAME = "name";
    private Node node = null;
    private List<Map> datas = null;
    private StringBuffer jsonStr = new StringBuffer();

    private Tree(List<Map> datas) {

        this.datas = datas;

    }

 /*   public static Tree getInstance(List<Map> datas, String id) {

        Tree temp = new Tree(datas);

        try {

            temp.init(id);

            return temp;

        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;

    }*/

    public String getJSONString() {

        return jsonStr.toString();

    }

    //初始化单树父节点

    private void init(String id) throws Exception {

        //搜索父节点

        Map t = FindDatasById(id);

        if (null == t) {//假如搜索的父节点id数据不存在，抛出异常

            throw new Exception();

        }

        //初始化父节点

        this.node = new Node(t);

        init(this.node);

        initJsonStr(this.node);

    }

    //初始化树

    private void init(Node node) {

        String id = node.getId();

        for (Map temp : FindListDatasByPId(id)) {

            //假如遇到节点id为空或者节点数据为空的，跳过本次循环

            if (isEmpty(temp.get(NODE_ID)) || isEmpty(temp.get(NODE_NAME))) {

                continue;

            }

            Node tempNode = new Node(temp);

            init(tempNode); //初始化子节点

            node.nodes.add(tempNode); //将子节点加入当前父节点

        }

    }

    public void initJsonStr(Node node) {

        Node tempNode = node;

        if (node.getId() != node.getId()) {

            jsonStr.append(",");

        }

        jsonStr.append("{").

            append("\"" + NODE_ID + "\":\"").append(tempNode.getId()).append("\",").

            append("\"" + NODE_NAME + "\":\"").append(tempNode.getContent()).append("\",").

            append("\"" + NODE_PID + "\":\"").append(tempNode.getPid()).append("\"");

        if (isHaveLeafWithPid(tempNode)) {

            jsonStr.append(",\"nodes\":[");

            List<Node> tempnodes = tempNode.nodes;

            for (int i = 0; i < tempnodes.size(); i++) {

                initJsonStr(tempnodes.get(i));

                if (tempnodes.size() - 1 != i) {

                    jsonStr.append(",");

                }

            }

            jsonStr.append("]");

        }

        jsonStr.append("}");

    }

    //判断父节点下是否有子节点

    public boolean isHaveLeafWithPid(String pid) {

        for (int i = 0; i < this.datas.size(); i++) {

            Map temp = this.datas.get(i);

            if (temp.get(NODE_PID) != null && temp.get(NODE_PID).toString().equals(pid)) {

                return true;

            }
        }

        return false;

    }

    //判断父节点下是否有子节点

    public boolean isHaveLeafWithPid(Node node) {

        if (node.nodes != null && node.nodes.size() > 0) {

            return true;

        }

        return false;

    }

    //找到父节点下的所有子节点

    private List<Map> FindListDatasByPId(String pid) {

        List<Map> map = new ArrayList<>();

        for (int i = this.datas.size() - 1; i >= 0; i--) {

            Map temp = this.datas.get(i);

            if (temp.get(NODE_PID) != null && temp.get(NODE_PID).toString().equals(pid)) {

                this.datas.remove(i);

                map.add(temp);

            }

        }

        return map;

    }

    //找到父节点

    private Map FindDatasById(String id) {

        for (int i = 0; i < this.datas.size(); i++) {

            Map temp = this.datas.get(i);

            if (temp.get(NODE_ID).toString().equals(id)) {

                this.datas.remove(i);

                return temp;

            }

        }

        return null;

    }

    private boolean isEmpty(Object obj) {

        if (null == obj || "".equals(obj)) {

            return true;

        }

        return false;

    }

}
