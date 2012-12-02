/*
 * Copyright 2012 david gonzalez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.activecq.api.plugins;

import com.day.cq.i18n.I18n;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.sling.api.SlingHttpServletRequest;

/**
 *
 * @author david
 */
public class I18nPlugin extends BasePlugin {

    private I18n i18n;

    /**
     * Constructor
     *
     * @param exposed
     */
    public I18nPlugin(SlingHttpServletRequest request) {
        super(request);
        this.i18n = new I18n(request);
        this.enable();
    }

    /**
     * Set i18n resource bundle; by default set to the Request
     *
     * @param resourceBundle
     */
    public void setI18n(String language) {
        Locale locale = new Locale(language);
        this.setI18n(locale);
    }

    public void setI18n(String language, String country) {
        Locale locale = new Locale(language, country);
        this.setI18n(locale);
    }

    public void setI18n(String language, String country, String variant) {
        Locale locale = new Locale(language, country, variant);
        this.setI18n(locale);
    }

    public void setI18n(Locale locale) {
        this.i18n = new I18n(this.request.getResourceBundle(locale));
    }

    /**
     * Set i18n resource bundle; by default set to the Request
     *
     * @param request
     */
    public void setI18n(SlingHttpServletRequest request) {
        this.i18n = new I18n(request);
    }

    /**
     * Translate the supplied string
     *
     * @param str
     * @return
     */
    public String translate(String str) {
        if (!isEnabled()) {
            return str;
        }

        return this.i18n.get(str);
    }
}