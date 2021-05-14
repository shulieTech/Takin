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

package io.shulie.tro.web.app.constant;

/**
 * 应用常量
 *
 * @author loseself
 * @date 2021/4/5 10:42 上午
 **/
public class AppConstants {

    public static final String CHARSET = "UTF-8";

    /**
     * 问号
     */
    public static final String QUESTION_MARK = "?";

    /**
     * 脱敏后的密码, 8个*
     */
    public static final String PASSWORD_COVER = "********";

    /**
     * xml 下的密码, %s 占位符密码
     */
    public static final String PASSWORD_XML = "<property name=\"password\" value=\"%s\"/>";

    /**
     * json 下的密码, %s 占位符密码
     */
    public static final String PASSWORD_JSON = "\"password\":\"%s\"";



}
