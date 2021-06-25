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

package io.shulie.amdb.mapper;

import io.shulie.amdb.entity.TAmdbAppInstanceDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

public interface AppInstanceMapper extends Mapper<TAmdbAppInstanceDO>, MySqlMapper<TAmdbAppInstanceDO> {
    int insert(TAmdbAppInstanceDO record);

    TAmdbAppInstanceDO selectOneByParam(TAmdbAppInstanceDO record);

    int updateByPrimaryKeySelective(TAmdbAppInstanceDO record);

    int updateByPrimaryKey(TAmdbAppInstanceDO record);

    List<TAmdbAppInstanceDO> selectByFilter(@Param("filter") String filter);

    @Update("update t_amdb_app_instance set flag=(flag^1) where (flag&1)=1")
    void initOnlineStatus();

    @Select("select ip from t_amdb_app_instance where app_name = #{appName} and (flag&1)=1")
    List<String> selectIpListByAppName(@Param("appName") String appName);
}