package com.transformers.i18n.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 配置拉取，配置更新
 *
 * @author daniel
 * @date 2021-05-15
 */
@Slf4j
@Component
public class NacosConfig {

    private static final String DEFAULT_GROUP = "DEFAULT_GROUP";
    private static final String DEFAULT_NAMESPACE = "77d40ebc-a868-4057-ae65-f3b53901a0cf";
    private static final String UNDERSCORE = "_";
    private static final String SUFFIX_PROPERTIES = ".properties";
    private static final String SERVER_ADDR_PROPERTY_KEY = "spring.cloud.nacos.config.server-addr";
    private static final String NAMESPACE_PROPERTY_KEY = "spring.cloud.nacos.config.namespace";

    @Value("spring.application.name")
    private String applicationName;

    private String serverAddr;
    private String namespace;

    @Resource
    private MessagesConfig messagesConfig;

    @Resource
    private ConfigurableApplicationContext applicationContext;

    @Autowired
    public void init() {
        serverAddr = applicationContext.getEnvironment().getProperty(SERVER_ADDR_PROPERTY_KEY);
        namespace = applicationContext.getEnvironment().getProperty(NAMESPACE_PROPERTY_KEY);
        if (StringUtils.isEmpty(namespace)) {
            namespace = DEFAULT_NAMESPACE;
        }

        initI18n(null);
        initI18n(Locale.CHINA);
        initI18n(Locale.US);
        initI18n(Locale.TAIWAN);

        log.debug("init system parameters successfully. application name {}, nacos addr {}, namespace {}",
                applicationName, serverAddr, namespace);
    }

    /**
     * 初始化对应的 Locale
     * 配置拉取，保存文件
     * 设置监听，配置更新
     *
     * @param locale Locale
     */
    private void initI18n(Locale locale) {
        String content;
        String dataId;
        ConfigService configService;
        if (locale == null) {
            dataId = messagesConfig.getBasename() + SUFFIX_PROPERTIES;
        } else {
            dataId = messagesConfig.getBasename()
                    + UNDERSCORE
                    + locale.getLanguage()
                    + UNDERSCORE
                    + locale.getCountry()
                    + SUFFIX_PROPERTIES;
        }
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        properties.put(PropertyKeyConst.NAMESPACE, namespace);
        try {
            configService = NacosFactory.createConfigService(properties);
            content = configService.getConfig(dataId, DEFAULT_GROUP, 5000);
            if (StringUtils.isEmpty(content)) {
                log.warn("i18n config content is empty, skip init. dataId : {}", dataId);
                return;
            }
            log.info("init i18n config. content : {}", content);

            saveLocalFile(dataId, content);
            setListener(configService, dataId, locale);
        } catch (NacosException e) {
            e.printStackTrace();
        }

    }

    /**
     * 将拉取来的配置保存本地文件
     *
     * @param fileName 文件名称
     * @param content  配置文件内容
     */
    private void saveLocalFile(String fileName, String content) {
        String path = System.getProperty("user.dir") + File.separator + messagesConfig.getBaseDir();
        fileName = path + File.separator + fileName;
        File file = new File(fileName);
        try {
            FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8);
            log.debug("i18n config content updated. local file path : {}", fileName);
        } catch (IOException e) {
            log.error("i18n config error, local file path : {}, error message : {}", fileName, e);
        }
    }

    /**
     * 设置监听
     *
     * @param configService ConfigService
     * @param dataId        dataId
     * @param locale        Locale
     */
    private void setListener(ConfigService configService, String dataId, Locale locale) {
        try {
            configService.addListener(dataId, DEFAULT_GROUP, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.debug("received new i18n config. config info : {}", configInfo);
                    initI18n(locale);
                }
            });
        } catch (NacosException e) {
            log.error("add listener error", e);
        }
    }

}
