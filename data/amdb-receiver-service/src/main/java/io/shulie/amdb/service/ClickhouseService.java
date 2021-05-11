package io.shulie.amdb.service;

import java.util.List;
import java.util.Map;

public interface ClickhouseService {
    /**
     * 查询map
     *
     * @param sql
     * @return
     */
    Map<String, Object> queryForMap(String sql);

    /**
     * 查询map
     *
     * @param sql
     * @return
     */
    <T> T queryForObject(String sql, Class<T> clazz);

    /**
     * 查询map
     *
     * @param sql
     * @return
     */
    public <T> List<T> queryForList(String sql, Class<T> clazz);


    /**
     * 查询list
     *
     * @param sql
     * @return
     */
    public List<Map<String, Object>> queryForList(String sql);
}
