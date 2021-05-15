package com.transformers.i18n.util;

import com.google.common.collect.Maps;
import com.transformers.i18n.config.MessagesConfig;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

/**
 * 获取配置文件工具类
 *
 * @author daniel
 * @date 2021-05-15
 */
@Component
public final class MessageUtils {

    private static MessageSource messageSource;

    private static MessagesConfig messagesConfig;

    public MessageUtils(MessageSource messageSource, MessagesConfig messagesConfig) {
        MessageUtils.messageSource = messageSource;
        MessageUtils.messagesConfig = messagesConfig;
    }

    /**
     * 获取 key 对应的国际化 value
     * 没有对应的key时，返回这个 key
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
        String path = System.getProperty("user.dir")
                + File.separator
                + messagesConfig.getBaseDir();
        String filePath = path
                + File.separator
                + messagesConfig.getBasename()
                + ".properties";
        File file = new File(filePath);
        final Map<String, String> map = Maps.newHashMap();
        try {
            Files.lines(file.toPath())
                    .filter(line -> !line.startsWith("#"))
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
