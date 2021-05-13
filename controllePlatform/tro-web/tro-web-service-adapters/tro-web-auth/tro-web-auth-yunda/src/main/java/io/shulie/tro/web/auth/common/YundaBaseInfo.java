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

package io.shulie.tro.web.auth.common;

/**
 * 韵达的基础配置信息
 */
public class YundaBaseInfo {

    /**
     * 韵达内部链接
     */
    public static String YUNDA_INNER_URL = "http://192.168.1.115";

    /**
     * 韵达重定向回调地址
     */
    public static String YUNDA_REDIRECT_URL = "http://192.168.1.115/tro/#/";

    /**
     * 韵达统一授权url
     */
    public static String YUNDA_URL = "http://uat.ydauth.yundasys.com:16120";

    /**
     * 韵达退出url
     */
    public static String YUNDA_LOGOUT_URL = "https://ssocas.yundasys.com:38443/ssocas/logout?service=http://192.168.1.115/oauth/login?redirectUrl=http://192.168.1.115/tro/#/";

    /**
     * 韵达统一授权 应用注册码（韵达使用）
     */
    public static String authId = "100000378";

    /**
     * 韵达获取组织APPID
     */
    public static String YUNDA_DEPT_APP_ID = "YD_QLLYC";

    /**
     * 韵达获取组织秘钥
     */
    public static String YUNDA_DEPT_SECRET_KEY = "xOprxFwuPMN1f4AqxGSQJfthAPDqxyC1";

    /**
     * 韵达获取组织链接
     */
    public static String YUNDA_DEPT_URL = "http://ogm.yundasys.com:30009";

    /**
     * 韵达科技板块组织id
     */
    public static String YUNDA_DEPT_SCIENCE_ORG_ID = "63000279";

    /**
     * 韵达获取用户信息url
     */
    public static String YUNDA_GET_USER_INFO_BY_SESSION = "/oauth/getUserInfo";
    /**
     * 韵达校验cookie，获取用户信息url
     */
    public static String YUNDA_CHECK_SESSION = "/oauth/checkSession";

    /**
     * 请求失败调用sso流程
     */
    public static String YUNDA_LOGIN_REDIRECT_URL = "/oauth/login?redirectUrl=";

    public static String YUNDA_LOGOUT_REDIRECT_URL = "/oauth/logoutAjax";

    /**
     * 给韵达提供的重定向接口
     */
    public static String YUNDA_REDIRECT_URI = "/tro-web/api/redirect";

    /**
     * 获取用户菜单
     */
    public static String GET_USER_MENU = "/ydauth/actions/outer/user/menu.action";

    /**
     * 统一授权查询用户列表
     */
    public static String YUNDA_GET_USER_LIST = "/ydauth/actions/outer/user/getUsersOnApp.action";

    /**
     * 统一授权查询用户信息
     */
    public static String YUNDA_GET_USER_INFO = "/ydauth/actions/outer/user/queryUserInfo.action";

    /**
     * 获取用户和角色信息
     */
    public static String YUNDA_GET_USER_AND_ROLE = "/ydauth/actions/outer/user/queryUserInfoAndRole.action";

    /**
     * 韵达成功返回标识
     */
    public static String YUNDA_SUCCESS_RETURN = "0";

    /**
     * 韵达组织-获取token信息
     */
    public static String YUNDA_DEPT_GET_TOKEN = "/sysAppInfo/getToken";

    /**
     * 韵达组织-根据token信息获取组织信息
     */
    public static String YUNDA_DEPT_GET_HCM_ORG = "/orgApi/getHCMOrgByToken";

    /**
     * 韵达组织-获取用户信息
     */
    public static String YUNDA_DEPT_GET_USER_INFO = "/orgApi/getHCMUserByToken";

    /**
     * 韵达组织-通过组织和用户获取用户信息
     */
    public static String YUNDA_DEPT_GET_USER_INFO_BY_DEPT_USER = "/orgApi/getEmpInfoByOrg";

    /**
     * 韵达获取组织-获取用户信息
     */
    public static String YUNDA_DEPT_GET_USER_IFO_BY_ID = "/orgApi/getEmpInfoById";
}
