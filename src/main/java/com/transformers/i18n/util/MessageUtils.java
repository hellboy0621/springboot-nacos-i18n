package com.transformers.i18n.util;

import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author daniel
 * @date 2021-05-15
 */
@Component
public class MessageUtils {

    private static MessageSource messageSource;

    private static ResourceLoader resourceLoader;

    public MessageUtils(MessageSource messageSource, ResourceLoader resourceLoader) {
        MessageUtils.messageSource = messageSource;
        MessageUtils.resourceLoader = resourceLoader;
    }

    /**
     * 获取 key 对应的国际化 value
     *
     * @param key 国际化 key
     * @return 国际化 value
     */
    public static String getPropertie(String key) {
        try {
            return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            return key;
        }
    }

    /**
     * 获取所有国际化配置文件
     * 基于 static/i18n/messages.properties 这个文件获取所有的国际化 keys
     * 所以必须保证所有的配置文件 key 保持一致，如果没有 value 置空即可
     *
     * @return Map<String, String>
     */
    public static Map<String, String> getProperties() {
        Resource resource = resourceLoader.getResource("classpath:static/i18n/messages.properties");
        final Map<String, String> map = Maps.newHashMap();
        try {
            InputStream inputStream = resource.getInputStream();
            String content = IOUtils.toString(inputStream, Charset.defaultCharset());

            Stream.of(content.split("\n"))
                    .filter(line -> line.contains("="))
                    .forEach(line -> {
                        String key = line.split("=")[0];
                        String value = getPropertie(key);
                        map.put(key, value);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

}
