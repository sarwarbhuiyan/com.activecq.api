/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.activecq.api.plugins;

import com.activecq.api.plugins.options.TextOptions;
import com.day.cq.i18n.I18n;
import com.day.cq.xss.ProtectionContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;

/**
 *
 * @author david
 */
public class TextPlugin extends BasePlugin {
    private boolean enabled;
    private I18nPlugin i18n;
    private XSSPlugin xss;

    /**
     * Constructor
     *
     * @param exposed
     */
    public TextPlugin(SlingHttpServletRequest request) {
        super(request);
        this.i18n = new I18nPlugin(request);
        this.xss = new XSSPlugin(request);

        if(this.i18n.isEnabled() || this.xss.isEnabled()) {
            this.enable();
        } else {
            this.disable();
        }
    }

    // i18n
    /**
     * Set i18n resource bundle; by default set to the Request
     *
     * @param resourceBundle
     */
    public void setI18n(Locale locale) {
        this.i18n.setI18n(locale);
    }

    /**
     * Set i18n resource bundle; by default set to the Request
     *
     * @param request
     */
    public void setI18n(SlingHttpServletRequest request) {
        this.i18n.setI18n(request);
    }

    // XSS
    /**
     * Enabled XSS Protection for specified fields
     *
     * @param whitelist
     */
    public void set(String... whitelist) {
        this.xss.set(whitelist);
    }

    /**
     * Enable XSS Protection using the supplied ProtectionContext
     *
     * http://dev.day.com/docs/en/cq/current/javadoc/com/day/cq/xss/ProtectionContext.html
     *
     * @param context
     */
    public void set(ProtectionContext context) {
        this.xss.set(context);
    }

    /**
     * Enable XSS protection on the component
     *
     * @param context type of XSS protection
     * @param whitelist list of fields that do not require XSS protection
     */
    public void set(ProtectionContext context, String... whitelist) {
        this.xss.set(context, whitelist);
    }

    // process
    public String text(String str, String key, TextOptions... options) {
        if (!isEnabled()) {
            return str;
        } else if(TextOptions.RAW.isIn(options)) {
            return str;
        }

        if(TextOptions.I18N.isIn(options)) {
            if(this.i18n.isEnabled()) {
                str = this.i18n.translate(str);
            }
        }

        if(TextOptions.XSS.isIn(options)) {
            if(StringUtils.isBlank(key)) {
                str = this.xss.protect(str);
            } else {
                str = this.xss.protect(str, key);
            }
        }

        return str;
    }

    /**
     * Translate the supplied string
     *
     * @param str data to process
     * @return
     */
    public String text(String str) {
        return text(str, null, TextOptions.I18N, TextOptions.XSS);
    }

    /**
     * Translate the supplied string
     *
     * @param str data to process
     * @param key use for XSS whitelist
     * @return
     */
    public String text(String str, String key) {
        return text(str, key, TextOptions.I18N, TextOptions.XSS);
    }
}
