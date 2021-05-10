package io.shulie.surge.data.deploy.pradar.digester.command;

import com.pamirs.pradar.log.parser.trace.RpcBased;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * @author vincent
 */
public interface ClickhouseCommand {

    LinkedHashSet<String> meta();

    LinkedHashMap<String, Object> action(RpcBased rpcBased);

}
