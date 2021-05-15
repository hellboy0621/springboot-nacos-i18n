package com.transformers.i18n.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import java.io.File;

/**
 * @author daniel
 * @date 2021-05-15
 */
@Slf4j
@Configuration
public class I18nConfig {

    @Resource
    private MessagesConfig messagesConfig;

    /**
     * 自定义解析器
     *
     * @return LocaleResolver
     */
    @Bean
    public LocaleResolver localeResolver() {
        return new CustomLocaleResolver();
    }

    @Primary
    @Bean
    @DependsOn(value = "messagesConfig")
    public ReloadableResourceBundleMessageSource messageSource() {
        String path = ResourceUtils.FILE_URL_PREFIX
                + System.getProperty("user.dir")
                + File.separator
                + messagesConfig.getBaseDir()
                + File.separator
                + messagesConfig.getBasename();

        log.debug("messagesConfig {}", messagesConfig);
        log.debug("path {}", path);

        // 定时刷新配置文件
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(path);
        messageSource.setDefaultEncoding(messagesConfig.getEncoding());
        messageSource.setCacheMillis(messagesConfig.getCacheMillis());
        return messageSource;
    }
}
