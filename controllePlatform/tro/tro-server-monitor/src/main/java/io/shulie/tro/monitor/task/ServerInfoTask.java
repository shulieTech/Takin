package io.shulie.tro.monitor.task;

import avro.shaded.com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.shulie.tro.monitor.service.SystemInfoSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * @author zhaoyong
 */
@Component
public class ServerInfoTask {

    @Value("${send.time}")
    private Long sendTime;
    @Autowired
    private SystemInfoSendService systemInfoSendService;

    private final static ExecutorService THREAD_POOL = new ThreadPoolExecutor(2, 4,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), new ThreadFactoryBuilder()
            .setNameFormat("server-send-%d").build(), new ThreadPoolExecutor.AbortPolicy());


    @Scheduled(cron = "*/2 * * * * ?")
    public void sendServerInfo(){
        long timeMillis = System.currentTimeMillis() /1000;
        if (timeMillis % sendTime == 0){
            THREAD_POOL.execute(new Runnable() {
                @Override
                public void run() {
                    systemInfoSendService.sendSystemInfo();
                }
            });
        }

    }

}
