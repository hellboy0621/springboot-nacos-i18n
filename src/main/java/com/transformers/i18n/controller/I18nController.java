package com.transformers.i18n.controller;

import com.transformers.i18n.util.MessageUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author daniel
 * @date 2021-05-15
 */
@RestController
@RequestMapping("/i18n")
public class I18nController {

    /**
     * 获取单个 key 对应的国际化 value
     * 如果不存在 key，返回这个 key
     *
     * @param key 国际化 key
     * @return 国际化 value
     */
    @GetMapping("")
    public String get(@RequestParam String key) {
        return MessageUtils.getPropertie(key);
    }

    /**
     * 获取所有国际化配置文件
     *
     * @return Map<String, String>
     */
    @GetMapping("/properties")
    public Map<String, String> properties() {
        return MessageUtils.getProperties();
    }
}
