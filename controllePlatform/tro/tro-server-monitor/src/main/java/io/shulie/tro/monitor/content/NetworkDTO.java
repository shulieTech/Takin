package io.shulie.tro.monitor.content;

import lombok.Data;

/**
 * @author zhaoyong
 */
@Data
public class NetworkDTO {

    private long bytesRecv;

    private long bytesSent;

    private long time;
}
