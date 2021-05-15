package com.transformers.i18n.config;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * 自定义解析器
 *
 * @author daniel
 * @date 2021-05-15
 */
public class CustomLocaleResolver implements LocaleResolver {

    private static final String LANG = "lang";
    private static final String LANG_SESSION = "lang_session";
    private static final String UNDERSCORE = "_";

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String lang = request.getHeader(LANG);
        Locale result = Locale.getDefault();
        HttpSession session = request.getSession();
        if (!StringUtils.isEmpty(lang) && lang.contains(UNDERSCORE)) {
            String[] split = lang.split(UNDERSCORE);
            result = new Locale(split[0], split[1]);

            session.setAttribute(LANG_SESSION, result);
        } else {
            Locale localeInSession = (Locale) session.getAttribute(LANG_SESSION);
            if (localeInSession != null) {
                result = localeInSession;
            }
        }
        return result;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}
