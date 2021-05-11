package io.shulie.amdb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

import java.io.IOException;

/**
 * @description: 启动类
 * @author: CaoYanFei@ShuLie.io
 * @create: 2020-07-15 22:26
 **/
@MapperScan("io.shulie.amdb.mapper")
@SpringBootApplication
@EnableScheduling
@EnableAsync
@Slf4j
public class AMDBAPIBootstrap {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(AMDBAPIBootstrap.class, args);
    }

}
