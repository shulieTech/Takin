package io.shulie.amdb;

import io.shulie.amdb.AMDBAPIBootstrap;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Date: 2020/03/12
 *
 * @author wangxian
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration("src/main/resources")
@SpringBootTest(classes = AMDBAPIBootstrap.class)
public abstract class BaseTest {
}
