package io.shulie.surge.data.sink.clickhouse;

import ru.yandex.clickhouse.BalancedClickhouseDataSource;
import ru.yandex.clickhouse.ClickHouseConnection;
import ru.yandex.clickhouse.ClickHouseDriver;
import ru.yandex.clickhouse.settings.ClickHouseProperties;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Author: xingchen
 * @ClassName: BalancedClickhouseDataSourceExt
 * @Package: io.shulie.surge.data.sink.clickhouse
 * @Date: 2021/3/2920:10
 * @Description:
 */
public class RoundClickhouseDataSource extends BalancedClickhouseDataSource {
    private final ClickHouseDriver driver = new ClickHouseDriver();
    private ThreadLocal<LongAdder> currentIndex = ThreadLocal.withInitial(LongAdder::new);

    public RoundClickhouseDataSource(String url) {
        super(url);
    }

    public RoundClickhouseDataSource(String url, Properties properties) {
        super(url, properties);
    }

    public RoundClickhouseDataSource(String url, ClickHouseProperties properties) {
        super(url, properties);
    }

    @Override
    public ClickHouseConnection getConnection() throws SQLException {
        return driver.connect(getRoundUrl(), super.getProperties());
    }

    @Override
    public ClickHouseConnection getConnection(String username, String password) throws SQLException {
        return driver.connect(getRoundUrl(), super.getProperties().withCredentials(username, password));
    }

    private String getRoundUrl() throws SQLException {
        List<String> localEnabledUrls = super.getEnabledClickHouseUrls();
        if (localEnabledUrls.isEmpty()) {
            throw new SQLException("Unable to get connection: there are no enabled urls");
        }
        currentIndex.get().increment();
        int index = currentIndex.get().intValue() % localEnabledUrls.size();
        if (currentIndex.get().intValue() == localEnabledUrls.size()) {
            currentIndex.get().reset();
        }
        return localEnabledUrls.get(index);
    }

    public static void main(String[] args) throws SQLException {
        ClickHouseProperties clickHouseProperties = new ClickHouseProperties();
        RoundClickhouseDataSource clickHouseDataSource = new RoundClickhouseDataSource("jdbc:clickhouse://pradar.host.clickhouse01:8123,pradar.host.clickhouse02:8123,pradar.host.clickhouse03:8123/default", clickHouseProperties);
        ExecutorService service = Executors.newFixedThreadPool(4);
        while (true) {
            service.execute(() -> {
                try {
                    if(Thread.currentThread().getName().endsWith("1")){
                        System.out.println(Thread.currentThread().getName() + "---" + clickHouseDataSource.getRoundUrl());
                    }
                } catch (Throwable e) {
                }
            });
        }
    }

}
