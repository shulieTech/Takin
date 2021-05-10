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
package com.pamirs.attach.plugin.alibaba.druid.interceptor;

import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleSysdateExpr;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleExprParser;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.Token;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.PradarSwitcher;
import com.pamirs.pradar.interceptor.ParametersWrapperInterceptorAdaptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;


public class OracleExprParserPrimaryRestInterceptor extends ParametersWrapperInterceptorAdaptor {

    @Override
    public Object[] getParameter0(Advice advice) {
        Object[] args = advice.getParameterArray();
        if (!PradarSwitcher.isClusterTestEnabled()
                || !Pradar.isClusterTest()
                || args == null
                || args.length == 0
                || !(advice.getTarget() instanceof OracleExprParser)) {
            return args;
        }

        OracleExprParser oracleParser = (OracleExprParser) advice.getTarget();
        Lexer lexer = oracleParser.getLexer();
        if (args[0] instanceof OracleSysdateExpr
                // 修复ORACLE SYSDATE函数问题
                && lexer.token() == Token.LPAREN) {
            args[0] = new SQLIdentifierExpr("SYSDATE");
        }
        return args;
    }


}

