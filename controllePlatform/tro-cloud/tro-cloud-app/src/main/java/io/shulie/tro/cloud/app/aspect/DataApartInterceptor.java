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

package io.shulie.tro.cloud.app.aspect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Properties;

import io.shulie.tro.cloud.common.annotation.DataApartInterceptAnnotation;
import io.shulie.tro.cloud.common.context.RestContext;
import io.shulie.tro.cloud.common.utils.CustomUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

/**
 * @ClassName MySqlInterceptor
 * @Description mybatis 场景报告查询增加客户身份
 * @Author qianshui
 * @Date 2020/7/22 下午11:22
 */

@Component
@Intercepts({
    @Signature(
        type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class
    })
})
public class DataApartInterceptor implements Interceptor {

    private static final String[] tables = new String[] {"t_scene_manage", "t_report"};


    public static void main(String[] args) {
        String sql1 = "select * from t_aaa where id = 1";
        String sql2 = "select * from t_report where id = 1";
        String sql3 = "select * from t_report";
        String sql4 = "select a as A, b as aB from t_report order by id";
        DataApartInterceptor interceptor = new DataApartInterceptor();
        System.out.println("After sql1 = " + interceptor.setSql(sql1, interceptor.matchTableIndex(sql1)));
        System.out.println("After sql2 = " + interceptor.setSql(sql2, interceptor.matchTableIndex(sql2)));
        System.out.println("After sql3 = " + interceptor.setSql(sql3, interceptor.matchTableIndex(sql3)));
        System.out.println("After sql4 = " + interceptor.setSql(sql4, interceptor.matchTableIndex(sql4)));
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if(RestContext.getUser() == null || RestContext.getUser().getLoginChannel() == 0) {
            return invocation.proceed();
        }
        StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
            SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        //先拦截到RoutingStatementHandler，里面有个StatementHandler类型的delegate变量，其实现类是BaseStatementHandler
        // ，然后就到BaseStatementHandler的成员变量mappedStatement
        MappedStatement mappedStatement = (MappedStatement)metaObject.getValue("delegate.mappedStatement");
        //sql语句类型 select、delete、insert、update
        String sqlCommandType = mappedStatement.getSqlCommandType().toString();
        if (!"select".equals(sqlCommandType.toLowerCase())) {
            return invocation.proceed();
        }
        BoundSql boundSql = statementHandler.getBoundSql();
        //获取到原始sql语句
        String sql = boundSql.getSql();
        String mSql = sql;
        //非指定表，直接跳过
        int tableIndex = matchTableIndex(mSql);
        if (tableIndex < 0) {
            return invocation.proceed();
        }
        //注解逻辑判断  添加注解了才拦截
        String mName = mappedStatement.getId().substring(mappedStatement.getId().lastIndexOf(".") + 1);
        //拦截mybatis自动生成的count语句
        if (mName.endsWith("_COUNT")) {
            mSql = setSql(mSql, tableIndex);
        } else {
            Class<?> classType = Class.forName(
                mappedStatement.getId().substring(0, mappedStatement.getId().lastIndexOf(".")));
            for (Method method : classType.getDeclaredMethods()) {
                if (method.isAnnotationPresent(DataApartInterceptAnnotation.class) && mName.equals(method.getName())) {
                    mSql = setSql(mSql, tableIndex);
                }
            }
        }
        //通过反射修改sql语句
        Field field = boundSql.getClass().getDeclaredField("sql");
        field.setAccessible(true);
        field.set(boundSql, mSql);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 重置sql
     *
     * @param sql
     * @return
     */
    private String setSql(String sql, int tableIndex) {
        return insertSql(tableIndex, sql, CustomUtil.getUserId());
    }

    /**
     * 更新sql语句 增加客户id查询
     *
     * @param tableIndex
     * @param sql
     * @param userId
     * @return
     */
    private String insertSql(int tableIndex, String sql, Long userId) {
        StringBuffer sb = new StringBuffer();
        int pos = lowerIndexOf(sql, " where ");
        String filterSql = RestContext.getFilterSql();
        if(StringUtils.isNoneBlank(filterSql)) {
            filterSql = "user_id in " + filterSql;
        }
        if (pos > 0) {
            sb.append(sql.substring(0, pos));
            sb.append(" where custom_id = " + userId);
            if(StringUtils.isNoneBlank(filterSql)) {
                sb.append(" and " + filterSql);
            }
            sb.append(" and ");
            sb.append(sql.substring(pos + " where ".length()));
        } else {
            int index = lowerIndexOf(sql, tables[tableIndex]);
            sb.append(sql.substring(0, index));
            sb.append(tables[tableIndex]);
            sb.append(" where custom_id = " + userId);
            if(StringUtils.isNoneBlank(filterSql)) {
                sb.append(" and " + filterSql);
            }
            sb.append(sql.substring(index + tables[tableIndex].length()));
        }
        return sb.toString();
    }

    /**
     * 符合条件的表
     *
     * @param sql
     * @return
     */
    private int matchTableIndex(String sql) {
        for (int i = 0; i < tables.length; i++) {
            if (lowerIndexOf(sql, tables[i]) > 0
                && lowerIndexOf(sql, tables[i] + "_") == -1) {
                return i;
            }
        }
        return -1;
    }

    /**
     * sql转小写再定位
     *
     * @param sql
     * @param str
     * @return
     */
    private int lowerIndexOf(String sql, String str) {
        return sql.toLowerCase().indexOf(str.toLowerCase());
    }
}
