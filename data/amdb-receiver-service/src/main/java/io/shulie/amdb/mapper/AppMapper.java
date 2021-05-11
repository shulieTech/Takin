package io.shulie.amdb.mapper;

import io.shulie.amdb.entity.AppDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.LinkedHashMap;
import java.util.List;

public interface AppMapper extends Mapper<AppDO>, MySqlMapper<AppDO> {
    int insert(AppDO record);

    int insertSelective(AppDO record);

    AppDO selectOneByParam(AppDO tAmdbApp);

    int updateByPrimaryKeySelective(AppDO record);

    int updateByPrimaryKey(AppDO record);

    List<AppDO> selectByFilter(@Param("filter") String filter);

    @Insert("insert ignore into t_amdb_app(app_name,app_type) " +
            " values(#{appName},#{appType})")
    int inUpdateSelective(AppDO record);

    List<LinkedHashMap> selectBySql(String sql);
}