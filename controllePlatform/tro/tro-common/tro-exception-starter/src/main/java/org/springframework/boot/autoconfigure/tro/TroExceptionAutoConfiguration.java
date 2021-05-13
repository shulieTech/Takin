package org.springframework.boot.autoconfigure.tro;

import io.shulie.tro.exception.holder.ExceptionMessageHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.tro.properties.TroExceptionProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shiyajian
 * create: 2020-09-25
 */
@Configuration
@EnableConfigurationProperties(TroExceptionProperties.class)
public class TroExceptionAutoConfiguration {

    @Autowired
    private TroExceptionProperties troExceptionProperties;

    @Bean
    public ExceptionMessageHolder exceptionMessageHolder() {
        return new ExceptionMessageHolder(troExceptionProperties);
    }

}
