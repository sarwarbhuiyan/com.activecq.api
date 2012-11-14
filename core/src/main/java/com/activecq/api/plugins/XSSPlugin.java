/*
 * Copyright 2012 david.
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

import com.day.cq.xss.ProtectionContext;
import com.day.cq.xss.XSSProtectionException;
import com.day.cq.xss.XSSProtectionService;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.sling.api.SlingHttpServletRequest;

/**
 *
 * @author david
 */
public class XSSPlugin extends BasePlugin {

    private ProtectionContext protectionContext;
    private ArrayList<String> whitelist;
    private XSSProtectionService service;

    public XSSPlugin(SlingHttpServletRequest request) {
        super(request);
        this.service = (XSSProtectionService) this.getService(XSSProtectionService.class);
        this.whitelist = new ArrayList<String>();
        if (this.service != null) {
            this.enable();
        } else {
            this.disable();
        }
    }

    /**
     * Enabled XSS Protection for specified fields
     *
     * @param whitelist
     */
    public void set(String... whitelist) {
        this.protectionContext = null;

        if (whitelist == null || whitelist.length < 1) {
            this.whitelist = new ArrayList<String>();
        } else {
            this.whitelist = new ArrayList<String>(Arrays.asList(whitelist));
        }
    }

    /**
     * Enable XSS Protection using the supplied ProtectionContext
     *
     * http://dev.day.com/docs/en/cq/current/javadoc/com/day/cq/xss/ProtectionContext.html
     *
     * @param context
     */
    public void set(ProtectionContext context) {
        set(context, new String[0]);
    }

    /**
     * Enable XSS protection on the component
     *
     * @param context type of XSS protection
     * @param whitelist list of fields that do not require XSS protection
     */
    public void set(ProtectionContext context, String... whitelist) {
        this.protectionContext = context;

        if (whitelist == null || whitelist.length < 1) {
            this.whitelist = new ArrayList<String>();
        } else {
            this.whitelist = new ArrayList<String>(Arrays.asList(whitelist));
        }
    }

    /**
     * XSS Protect a specific string
     *
     * @param str
     * @return
     */
    public String protect(String str) {
        if (!isEnabled()) {
            return str;
        }

        try {
            if (this.hasContext()) {
                return this.getXSSProtectionService().protectForContext(this.getProtectionContext(), str);
            } else {
                return this.getXSSProtectionService().protectFromXSS(str);
            }
        } catch (XSSProtectionException e) {
            return "<!-- XSS Protection Error -->";
        }
    }

    public String protect(String str, String key) {
        if (!isEnabled() || isWhitelisted(key)) {
            return str;
        }

        try {
            if (this.hasContext()) {
                return this.getXSSProtectionService().protectForContext(this.getProtectionContext(), str);
            } else {
                return this.getXSSProtectionService().protectFromXSS(str);
            }
        } catch (XSSProtectionException e) {
            return "<!-- XSS Protection Error -->";
        }
    }

    /**
     * Checks if the field is whitelisted (does not require XSS protection)
     *
     * @param field
     * @return
     */
    protected boolean isWhitelisted(String field) {
        return this.whitelist.contains(field);
    }

    protected boolean hasContext() {
        return this.protectionContext != null;
    }

    protected ProtectionContext getProtectionContext() {
        return this.protectionContext;
    }

    private XSSProtectionService getXSSProtectionService() {
        return this.service;
    }
}