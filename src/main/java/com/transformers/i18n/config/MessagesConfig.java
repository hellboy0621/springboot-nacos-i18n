package com.transformers.i18n.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author daniel
 * @date 2021-05-15
 */
@Data
@RefreshScope
@Component
@ConfigurationProperties(prefix = "spring.messages")
public class MessagesConfig {

    private String baseDir;

    private String basename;

    private String encoding;

    private long cacheMillis;
}
