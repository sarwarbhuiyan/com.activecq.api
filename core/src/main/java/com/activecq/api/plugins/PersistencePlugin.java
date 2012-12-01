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

import com.day.cq.wcm.api.components.Component;
import com.day.cq.wcm.commons.WCMUtils;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;

/**
 *
 * @author david
 */
public class PersistencePlugin extends BasePlugin {

    private String componentKey;
    private Component component;

    public PersistencePlugin(SlingHttpServletRequest request) {
        super(request);
        this.component = WCMUtils.getComponent(this.resource);
        this.componentKey = "__acq_persistence_plugin-" + this.component.getPath();
        this.enable();
    }

    /**
     * Used to persist data between Components on the same HTTP request.
     *
     * @param key
     * @param value
     * @returns
     */
    @SuppressWarnings("unchecked")
    public boolean set(String key, Object value) {
        boolean overwrite = false;
        Map<String, Object> map = getMap();

        if (map.containsKey(key)) {
            overwrite = true;
        }

        map.put(key, value);
        request.setAttribute(this.componentKey, map);

        return overwrite;
    }

    /**
     * Used to retrieve persisted data between Components on the same HTTP
     * request.
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object get(String key) {
        Map<String, Object> map = getMap();

        if (map.containsKey(key)) {
            return map.get(key);
        }

        return null;
    }

    /**
     * Gets the persistence map for the current component
     *
     * @return
     */
    public Map<String, Object> getMap() {
        Map<String, Object> map;

        map = (HashMap<String, Object>) this.request.getAttribute(this.componentKey);
        if (map == null) {
            return new HashMap<String, Object>();
        }

        return map;
    }

    /**
     * Checks if any persisted data exists for the current component
     *
     * @return
     */
    public boolean isEmpty() {
        if (StringUtils.isBlank(this.componentKey)) {
            return true;
        }

        HashMap<String, Object> hashMap = (HashMap<String, Object>) this.request.getAttribute(this.componentKey);
        if (hashMap == null) {
            return true;
        }

        return hashMap.isEmpty();
    }

    /**
     * Disable method does not apply to this Plugin; it is always enabled.
     * Override to prevent inaccurate state representation via isEnabled()
     */
    @Override
    public void disable() {
        // Do nothing; Enable/Disable does not apply to this Plugin
    }
}