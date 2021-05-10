package io.shulie.surge.data.deploy.pradar.digester.command;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.pamirs.pradar.log.parser.trace.RpcBased;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * @author vincent
 */
public class ClickhouseFacade {
    private List<ClickhouseCommand> commands;

    public ClickhouseFacade() {
        commands = Lists.newArrayList();
    }

    /**
     * @param clickhouseCommand
     */
    public ClickhouseFacade(ClickhouseCommand... clickhouseCommand) {
        commands = Lists.newArrayList(clickhouseCommand);
    }

    /**
     * 添加命令
     *
     * @param clickhouseCommand
     * @return
     */
    public ClickhouseFacade addCommond(ClickhouseCommand clickhouseCommand) {
        commands.add(clickhouseCommand);
        return this;
    }

    /**
     * @param rpcBased
     * @return
     */
    public Map<String, Object> invoke(RpcBased rpcBased) {
        if (commands.isEmpty()) {
            throw new IllegalArgumentException("Not add command.");
        }
        Map<String, Object> colMap = Maps.newLinkedHashMap();
        for (ClickhouseCommand command : commands) {
            colMap.putAll(command.action(rpcBased));
        }
        return colMap;
    }

    /**
     * @return
     */
    private LinkedHashSet<String> metas() {
        LinkedHashSet<String> metas = Sets.newLinkedHashSet();
        for (ClickhouseCommand command : commands) {
            metas.addAll(command.meta());
        }
        return metas;
    }

    /**
     * 获取列
     *
     * @return
     */
    public String getCols() {
        return Joiner.on(',').join(metas()).toString();
    }

    /**
     * 获取获取值的占位符
     *
     * @return
     */
    public String getParam() {
        LinkedHashSet<String> meta = metas();
        List<String> list = Lists.newArrayList();
        for (String key : meta) {
            list.add("?");
        }
        return Joiner.on(',').join(list).toString();
    }

    public Object[] toObjects(Map<String, Object> keyValueMap) {
        return keyValueMap.values().toArray();
    }

    /**
     * @author vincent
     */
    public static class Factory {
        private static ClickhouseFacade INSTANCE = new ClickhouseFacade();

        public static ClickhouseFacade getInstace() {
            return INSTANCE;
        }
    }
}
