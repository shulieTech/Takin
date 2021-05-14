package utils.linux;

/**
 * @author 无涯
 * @Package utils.linux
 * @date 2020/12/10 5:44 下午
 */

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.shulie.tro.utils.linux.LinuxHelper;
import org.junit.Test;

public class LinuxHelperTest {
    private  static ThreadFactory nameThreadFactory =
        new ThreadFactoryBuilder().setNameFormat("linux-run-thread-%d").build();
    private  static ThreadPoolExecutor poolExecutor =
        new ThreadPoolExecutor(20, 20, 60L,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), nameThreadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy());
    @Test
    public void test() throws InterruptedException {
        String path = "sh /Users/hezhongqi/shulie/test.sh";
        poolExecutor.execute(() -> {
            AtomicReference<Process> shellProcess = new AtomicReference<>() ;
            int state = LinuxHelper.runShell(
                path, 60L,
                process -> shellProcess.set(process),
                message -> System.out.println(message)
            );
            System.out.println(state);
       });
        Thread.sleep(11*1000);
    }
}
