package io.shulie.tro.web.app.conf;

import javax.sql.DataSource;

import com.shulie.tesla.sequence.impl.DefaultSequence;
import com.shulie.tesla.sequence.impl.DefaultSequenceDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.conf
 * @date 2020/12/21 5:16 下午
 */
@Configuration
public class TeslaConfig {

    @Bean
    public DefaultSequence baseOrderLineSequence(DataSource dataSource) {
        DefaultSequenceDao defaultSequenceDao = new DefaultSequenceDao();
        defaultSequenceDao.setDataSource(dataSource);
        defaultSequenceDao.setStep(500);
        defaultSequenceDao.setTableName("t_tc_sequence");
        DefaultSequence defaultSequence = new DefaultSequence();
        defaultSequence.setSequenceDao(defaultSequenceDao);
        defaultSequence.setName("BASE_ORDER_LINE");
        return defaultSequence;
    }
    @Bean
    public DefaultSequence threadOrderLineSequence(DataSource dataSource) {
        DefaultSequenceDao defaultSequenceDao = new DefaultSequenceDao();
        defaultSequenceDao.setDataSource(dataSource);
        defaultSequenceDao.setStep(500);
        defaultSequenceDao.setTableName("t_tc_sequence");
        DefaultSequence defaultSequence = new DefaultSequence();
        defaultSequence.setSequenceDao(defaultSequenceDao);
        defaultSequence.setName("THREAD_ORDER_LINE");
        return defaultSequence;
    }
}
