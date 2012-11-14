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

import com.activecq.api.utils.HttpRequestUtil;
import com.activecq.api.utils.TextUtil;
import com.day.cq.commons.DiffInfo;
import com.day.cq.commons.DiffService;
import com.day.cq.wcm.api.WCMMode;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

/**
 *
 * @author david
 */
public class DiffPlugin extends BasePlugin {
    private static final String DIFF_REQUEST_PARAM = "cq_diffTo";
    private final DiffService diffService;

    public DiffPlugin(SlingHttpServletRequest request) {
        super(request);

        this.diffService = (DiffService) this.getService(DiffService.class);

        // Must be in Diff Mode and Preview Mode to be enabled
        if(HttpRequestUtil.hasParameter(this.request, DIFF_REQUEST_PARAM) &&
                WCMMode.PREVIEW.equals(WCMMode.fromRequest(this.request))) {
            if (this.diffService != null) { this.enable(); } else { this.disable(); }

        } else {
            this.disable();
        }
    }

    /**
     * Gets the Diff text for the current resource for the provided key
     *
     * @param key
     * @return
     */
    public String getDiff(String key) {
       return getDiff(this.resource, key, null);
    }

    /**
     * Gets the Diff text for the current resource for the provided key assuming the provided current text
     *
     * @param key
     * @param current
     * @return
     */
    public String getDiff(String key, String current) {
       return getDiff(this.resource, key, current);
    }

    /**
     * Gets the Diff text for the provided resource for the provided key
     *
     * @param resource
     * @param key
     * @return
     */
    public String getDiff(Resource resource, String key) {
        return getDiff(resource, key, null);
    }

    /**
     * Gets the Diff text for the provided resource for the provided key assuming the provided current text
     *
     * @param key
     * @param current
     * @return
     */
    public String getDiff(Resource resource, String key, String current) {
        if (this.isEnabled()) {

            if (current == null) {
                current = this.getCurrentText(resource, key, current);
            }

            String previous = this.getPreviousText(resource, key);

            return this.getDiff(resource, current, previous,
                    (TextUtil.isRichText(current) || TextUtil.isRichText(previous)));
        } else {
            return current;
        }
    }

    /* Protected */

    /**
     * Protected method which calls the diff service which formats the current and previous text to the diff'd HTML output string
     *
     * @param resource
     * @param current
     * @param previous
     * @param isRichText
     * @return
     */
    protected String getDiff(Resource resource, String current, String previous, boolean isRichText) {
        if(!this.isEnabled()) { return current; }

        DiffInfo diffInfo = resource.adaptTo(DiffInfo.class);
        return diffInfo.getDiffOutput(this.diffService, current, previous, isRichText);
    }

    /**
     * Wrapper method for getDiff(Resource resource, String current, String previous, boolean isRichText)
     *
     * @param current
     * @param previous
     * @param isRichText
     * @return
     */
    protected String getDiff(String current, String previous, boolean isRichText) {
        if(!this.isEnabled()) { return current; }
        return getDiff(this.resource, current, previous, isRichText);
    }

    /**
     * Returns the current text for a particular key, unless overridden by "current" param
     *
     * @param resource
     * @param key
     * @param current
     * @return
     */
    protected String getCurrentText(Resource resource, String key, String current) {
        if(!this.isEnabled()) {
            return current;
        } else if(resource == null || StringUtils.isBlank(key)) {
            return current;
        }

        // Get "current" text
        if(StringUtils.isBlank(current)) {
            ValueMap valueMap = resource.adaptTo(ValueMap.class);
            current = valueMap.get(key, String.class);
        }

        return current;
    }

    /**
     * Gets Previous text
     *
     * @param resource
     * @param key
     * @return
     */
    protected String getPreviousText(Resource resource, String key) {
        if(!this.isEnabled()) { return null; }

        DiffInfo diffInfo = resource.adaptTo(DiffInfo.class);
        ValueMap diffValueMap = diffInfo.getContent().adaptTo(ValueMap.class);
        return diffValueMap.get(key, String.class);
    }
}