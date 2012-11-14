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

import com.activecq.api.utils.HttpRequestUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;

/**
 *
 * @author david
 */
public abstract class BasePlugin {
    protected final SlingHttpServletRequest request;
    protected final SlingScriptHelper slingScriptHelper;
    protected final Resource resource;
    private boolean enabled = false;

    public BasePlugin(SlingHttpServletRequest request) {
        this.request = request;

        // Sling Script Helper
        this.slingScriptHelper = HttpRequestUtil.getSlingScriptHelper(this.request);
        if (this.slingScriptHelper == null) {
            throw new IllegalArgumentException(
                    "SlingScriptHelper must NOT be null.");
        }

        // Resource
        this.resource = this.request.getResource();
        if (this.resource == null) {
            throw new IllegalArgumentException("Resource must NOT be null.");
        }
    }

    /**
     * Enable/Disbled I18n processing when getting properties
     *
     * @param enable
     */
    public void enable() {
        this.enabled = true;
    }

    /**
     * Marks plugin as disabled
     */
    public void disable() {
        this.enabled = false;
    }

    /**
     * Checks if i18n is enabled
     *
     * @return
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Exposes ability to get services from within this class and subclasses
     *
     * @param <ServiceType>
     * @param type
     * @return the requested service or null.
     */
    protected <ServiceType> ServiceType getService(Class<ServiceType> type) {
        return slingScriptHelper.getService(type);
    }
}