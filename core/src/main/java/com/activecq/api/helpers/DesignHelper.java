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
package com.activecq.api.helpers;

import com.activecq.api.utils.TypeUtil;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Design;
import com.day.cq.wcm.api.designer.Designer;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DesignHelper {
    private static final Logger log = LoggerFactory.getLogger(DesignHelper.class);

    private static final String DEFAULT_CSS = "css";
    private static final String DEFAULT_IMAGES = "images";
    private static final String DEFAULT_SCRIPTS = "js";
    private static final String DEFAULT_CSS_MEDIA_TYPE = "all";

    /**
     * * CSS Methods **
     */
    /**
     * Convenience method for calling css without media indicator
     *
     * @param path
     * @param page
     * @return
     */
    public static String cssTag(String path, Page page) {
        return cssTag(path, page, DEFAULT_CSS_MEDIA_TYPE);
    }

    /**
     * Returns an link tag for the css path supplied in the media indicator.
     *
     * @param path
     * @param page
     * @param attrs
     * @return
     */
    public static String cssTag(String path, Page page, String media) {
        String src = cssSrc(path, page);

        if (StringUtils.isBlank(src)) {
            return "<!-- Missing CSS : " + path + ".css -->";
        }

        if (StringUtils.isBlank(media)) {
            media = DEFAULT_CSS_MEDIA_TYPE;
        }

        media = StringEscapeUtils.escapeHtml(media);
        src = StringEscapeUtils.escapeHtml(src);

        // Begin writing link tag
        return "<link rel=\"stylesheet\" media=\"" + media + "\" href=\"" + src + "\" />";
    }

    /**
     * Returns the src path for the CSS file
     *
     * @param path
     * @param page
     * @return
     */
    public static String cssSrc(String path, Page page) {
        return designSrc(page, DEFAULT_CSS, path);
    }

    /**
     * * Image Methods **
     */
    /**
     * Convenience method for calling designImage without extra args
     *
     * @param path
     * @param page
     * @return
     */
    public static String imgTag(String path, Page page) {
        return imgTag(path, page, new HashMap<String, String>());
    }

    /**
     * Returns an img tag for the image path supplied in the path parameter.
     *
     * @param path
     * @param page
     * @param attrs
     * @return
     */
    public static String imgTag(String path, Page page, String[] attrs) {
        Map<String, String> map = new HashMap<String, String>();

        try {
            map = TypeUtil.ArrayToMap(attrs);
        } catch (IllegalArgumentException ex) {
            // Pass in empty map
        }

        return imgTag(path, page, map);
    }

    /**
     * Returns an img tag for the image path supplied in the path parameter.
     *
     * @param path
     * @param page
     * @param attrs
     * @return
     */
    public static String imgTag(String path, Page page, Map<String, String> attrs) {

        String src = imgSrc(path, page);

        if (StringUtils.isBlank(src)) {
            return "<!-- Missing Image : " + path + " -->";
        }

        if (attrs == null) {
            attrs = new HashMap<String, String>();
        }

        // Image alt
        if (!attrs.containsKey("alt")) {
            attrs.put("alt", "");
        }

        // Begin writing img tag
        String html = "<img src=\"" + StringEscapeUtils.escapeHtml(src) + "\"";

        for (String key : attrs.keySet()) {
            final String attr = StringEscapeUtils.escapeHtml(StringUtils.stripToEmpty(attrs.get(key)));
            html += " " + key + "=\""
                    + attr + "\"";
        }

        html += "/>";

        return html;
    }

    /**
     * Generates the path for the Image resource
     *
     * @param path
     * @param page
     * @return
     */
    public static String imgSrc(String path, Page page) {
        return designSrc(page, DEFAULT_IMAGES, path);
    }

    /**
     * * Script Methods **
     */
    /**
     * Returns an script tag for the script path.
     *
     * @param path
     * @param page
     * @return
     */
    public String scriptTag(String path, Page page) {
        String src = scriptSrc(path, page);

        if (StringUtils.isBlank(src)) {
            return "<!-- Missing Script : " + path + ".js -->";
        }

        // Begin writing script tag
        src = StringEscapeUtils.escapeHtml(src);
        return "<script src=\"" + src + "\" ></script>";
    }

    /**
     *
     * Generate the path to the Script resource
     *
     * @param path
     * @param page
     * @return
     */
    public static String scriptSrc(String path, Page page) {
        return designSrc(page, DEFAULT_SCRIPTS, path);
    }

    /**
     * Generic src selector for a generic Design resource (htc, etc.)
     *
     * @param path
     * @param page
     * @return
     */
    public static String src(String path, Page page) {
        return designSrc(page, null, path);
    }

    /**
     * * SRC Method **
     */
    protected static String designSrc(Page page, String pathPrefix, String path) {
        if (page == null || StringUtils.isBlank(path)) {
            return "";
        }

        ResourceResolver resourceResolver = page.adaptTo(Resource.class).getResourceResolver();
        Designer designer = resourceResolver.adaptTo(Designer.class);

        if (designer == null) {
            return "";
        }

        final Design design = designer.getDesign(page);
        final Resource designResource = resourceResolver.getResource(design.getPath());

        if (design == null) {
            return "";
        }

        String src = makePath(designResource, pathPrefix, path);
        if (!exists(resourceResolver, src)) {
            // Search up the tree
            src = searchUp(designResource, pathPrefix, path);
        }

        return StringUtils.isBlank(src) ? "" : StringEscapeUtils.escapeHtml(src);
    }

    /**
     * Internal recursive helper method which searches up the tree for Design
     * nodes which may be candidates for holding the requested Design resource.
     *
     * @param resource
     * @param prefix
     * @param path
     * @return
     */
    protected static String searchUp(Resource resource, String pathPrefix, String path) {
        if (resource == null || StringUtils.equals(resource.getPath(), "/")) {
            return null;
        }

        final Design design = resource.adaptTo(Design.class);

        if (design != null) {
            String designResourcePath = makePath(resource, pathPrefix, path);

            Resource designResource = resource.getResourceResolver().getResource(designResourcePath);
            if (designResource != null) {
                return designResource.getPath();
            }
        }

        return searchUp(resource.getParent(), pathPrefix, path);
    }

    /**
     * Used to create path from Design resource, asset type prefix (css, images,
     * js, etc) and path (usually file name)
     *
     * @param resource
     * @param pathPrefix
     * @param path
     * @return
     */
    protected static String makePath(Resource resource, String pathPrefix, String path) {
        if (StringUtils.startsWith(path, "/")) {
            // Absolute paths ignore the pathPrefix
            return resource.getPath() + path;
        } else {
            return resource.getPath()
                    + "/"
                    + StringUtils.stripStart(pathPrefix, "/")
                    + "/"
                    + StringUtils.stripStart(path, "/");
        }
    }

    /**
     * Checks if the resource exists/is accessible to the current user session
     *
     * @param resourceResolver
     * @param path
     * @return
     */
    protected static boolean exists(ResourceResolver resourceResolver, String path) {
        return resourceResolver.getResource(path) != null;
    }
}
