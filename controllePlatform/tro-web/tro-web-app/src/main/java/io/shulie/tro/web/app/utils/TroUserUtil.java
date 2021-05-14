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

package io.shulie.tro.web.app.utils;

import com.pamirs.tro.entity.domain.entity.user.User;

/**
 * @ClassName TroUserUtil
 * @Description TroUser工具类
 * @Author qianshui
 * @Date 2020/11/13 下午5:00
 */
public class TroUserUtil {

    /**
     * 校验用户是否超级管理员
     *
     * @param user
     * @return
     */
    public static Boolean validateSuperAdmin(User user) {
        if (user == null) {
            return false;
        }
        if (user.getUserType() == null) {
            return false;
        }
        return user.getUserType() == 0 ? true : false;
    }
}
