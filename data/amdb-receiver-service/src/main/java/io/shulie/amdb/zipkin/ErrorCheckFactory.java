package io.shulie.amdb.zipkin;

import com.pamirs.pradar.log.parser.trace.RpcBased;
import io.shulie.pradar.log.rule.BaseErrorCodeFactory;

/**
 * @author vincent
 */
public class ErrorCheckFactory extends BaseErrorCodeFactory<RpcBased> {

    public static final ErrorCheckFactory INSTANCE = new ErrorCheckFactory();

    public ErrorCheckFactory() {
        super(new Class[]{RpcBased.class},new String[]{ "node"});
    }
}
