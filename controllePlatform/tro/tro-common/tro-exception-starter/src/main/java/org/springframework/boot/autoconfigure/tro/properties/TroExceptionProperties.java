package org.springframework.boot.autoconfigure.tro.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author shiyajian
 * create: 2020-09-25
 */
@ConfigurationProperties(prefix = "tro.exception")
@Component
@Data
public class TroExceptionProperties {

    private String messageFilesPath = "exception";

    private String httpStatusFileName = "http_status.properties";

    private String debugFileName = "debug.properties";

    private String messageFileName = "message.properties";

    private String solutionFileName = "solution.properties";
}
