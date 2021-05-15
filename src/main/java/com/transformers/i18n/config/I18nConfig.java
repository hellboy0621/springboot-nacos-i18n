package com.transformers.i18n.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.LocaleResolver;

/**
 * @author daniel
 * @date 2021-05-15
 */
@Configuration
public class I18nConfig {

    // 默认解析器
//    @Bean
//    public LocaleResolver localeResolver() {
//        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
//        sessionLocaleResolver.setDefaultLocale(Locale.CHINA);
//        return sessionLocaleResolver;
//    }

    /**
     * 自定义解析器
     *
     * @return LocaleResolver
     */
    @Bean
    public LocaleResolver localeResolver() {
        return new CustomLocaleResolver();
    }

    /**
     * 从国际化配置文件中获取所有的 key
     *
     * @return ResourceLoader
     */
    @Bean
    public ResourceLoader resourceLoader() {
        return new DefaultResourceLoader();
    }
}
