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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeStatus;
import io.fabric8.kubernetes.api.model.Quantity;
import io.shulie.tro.cloud.biz.service.strategy.impl.StrategyConfigServiceImpl;
import org.junit.Test;

/**
 * @author 无涯
 * @Package PACKAGE_NAME
 * @date 2021/4/25 4:55 下午
 */
public class StrategyConfigServiceImplTest {
    @Test
    public void test() {
        StrategyConfigServiceImpl impl = new StrategyConfigServiceImpl();
        List<Node> nodes = Lists.newArrayList();
        //0 = {Quantity@3596} "15296805876"
        //1 = {Quantity@3597} "15296801985"
        //2 = {Quantity@3598} "15296801985"
        //3 = {Quantity@3599} "15297988801"
        nodes.add(getNode("15296805876"));
        nodes.add(getNode("15296801985"));
        nodes.add(getNode("15296801985"));
        nodes.add(getNode("15297988801"));

        BigDecimal bigDecimal =  impl.getMaxByNode(nodes,BigDecimal.valueOf(5000));
    }

    private Node getNode(String amount) {
        Node node =  new Node();
        NodeStatus nodeStatus = new NodeStatus();
        Map<String, Quantity> allocatable = Maps.newHashMap();
        Quantity quantity = new Quantity();
        quantity.setAmount(amount);
        allocatable.put("memory",quantity);
        nodeStatus.setAllocatable(allocatable);
        node.setStatus(nodeStatus);
        return node;
    }
}
