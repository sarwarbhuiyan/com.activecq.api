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
package com.activecq.api.utils;

import com.day.cq.commons.PathInfo;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;

/**
 *
 * @author david
 */
public class HttpRequestUtil {

    private HttpRequestUtil() { }

    /**
     * <p>
     * Gets the Attribute or Parameter value from the Request.
     * </p><p>
     * Attribute takes precedence
     * </p><p>
     * If neither a Parameter or Attribute value exists @ key, return null
     * </p>
     *
     * @param request
     * @param key
     * @return
     */
    public static String getParameterOrAttribute(HttpServletRequest request,String key) {
        return getParameterOrAttribute(request, key, null);
    }

    /**
     * <p>
     * Gets the Attribute or Parameter value from the Request.
     * </p><p>
     * Attribute takes precedence
     * </p><p>
     * If neither a Parameter or Attribute value exists @ key, return the dfault
     * </p>
     *
     * @param request
     * @param key
     * @param dfault
     * @return
     */
    public static String getParameterOrAttribute(HttpServletRequest request,String key, String dfault) {
        String value = null;
        if(request == null) { return value; }

        if(hasParameter(request, key)) {
            value = request.getParameter(key);
        } else if (hasAttribute(request, key)) {
            value = (String) request.getAttribute(key);
        }

        if(StringUtils.isBlank(value)) {
            value =  dfault;
        }

        return value;
    }

    /**
     * <p>
     * Gets the Attribute or Parameter value from the Request.
     * </p><p>
     * Attribute takes precedence
     * </p><p>
     * If neither an Attribute or Parameter value exists @ key, return the null
     * </p>
     *
     * @param request
     * @param key
     * @return
     */
    public static String getAttributeOrParameter(HttpServletRequest request, String key) {
        return getAttributeOrParameter(request, key, null);
    }

    /**
     * <p>
     * Gets the Attribute or Parameter value from the Request.
     * </p><p>
     * Attribute takes precedence
     * </p><p>
     * If neither an Attribute or Parameter value exists @ key, return the dfault
     * </p>
     *
     * @param request
     * @param key
     * @param dfault
     * @return
     */
    public static String getAttributeOrParameter(HttpServletRequest request, String key, String dfault) {
        String value = null;
        if(request == null) { return value; }

        if (hasAttribute(request, key)) {
            value = (String) request.getAttribute(key);
        } else if(hasParameter(request, key)) {
            value = request.getParameter(key);
        }

        if(StringUtils.isBlank(value)) {
            value =  dfault;
        }

        return value;
    }

    /**
     * Checks if the Request has non-Blank Parameter value @ key
     *
     * @param request
     * @param key
     * @return
     */
    public static boolean hasParameter(HttpServletRequest request, String key) {
        if(request == null) { return false; }
        String tmp = (String) request.getParameter(key);
        return StringUtils.isNotBlank(tmp);
    }

    /**
     * Checks if the Request has non-Blank Attribute value @ key
     *
     * @param request
     * @param key
     * @return
     */
    public static boolean hasAttribute(HttpServletRequest request,String key) {
        if(request == null) { return false; }
        String tmp = (String) request.getAttribute(key);
        return StringUtils.isNotBlank(tmp);
    }

    /**
     * Creates a URI from the Sling PathInfo
     *
     * @param pathInfo
     * @return
     */
    public static String toString(PathInfo pathInfo) {
        if(pathInfo == null) { return ""; }

        List<String> tmp = new ArrayList<String>();
        List<String> tmp2 = new ArrayList<String>();

        tmp.add(pathInfo.getResourcePath());
        tmp.add(pathInfo.getSelectorString());
        tmp.add(pathInfo.getExtension());

        tmp2.add(StringUtils.join(tmp, '.'));
        tmp2.add(pathInfo.getSuffix());

        return StringUtils.join(tmp2, '/');
    }

    /**
     * Get the SlingScriptHelper from the SlingHttpServletRequest object
     *
     * @param request
     * @return
     */
    public static SlingScriptHelper getSlingScriptHelper(SlingHttpServletRequest request) {
        // Get SlingScriptHelper from SlingBindings included as an attribute on the Sling HTTP Request
        SlingBindings slingBindings = (SlingBindings) request.getAttribute(SlingBindings.class.getName());
        if (slingBindings == null) {
            throw new IllegalArgumentException(
                    "SlingHttpServletRequest most have contain a SlingBindings object at key: " + SlingBindings.class.getName());
        }

        return slingBindings.getSling();
    }
}
