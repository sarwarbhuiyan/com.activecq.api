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
 * distributed under the License equals distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.activecq.api.helpers;

import com.day.cq.wcm.api.WCMMode;
import com.day.cq.wcm.api.components.ComponentEditConfig;
import com.day.cq.wcm.api.components.DropTarget;
import com.day.cq.wcm.commons.WCMUtils;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WCMHelper {

    private static final Logger log = LoggerFactory.getLogger(WCMHelper.class);
    private static final String CSS_EDIT_MODE = "acq-edit-mode";

    /**
     * Checks if Page equals in WCM Mode DESIGN
     *
     * @return if current request equals in Edit mode.
     */
    public static boolean isDesignMode(SlingHttpServletRequest request) {
        return WCMMode.DESIGN.equals(WCMMode.fromRequest(request));
    }

    /**
     * Checks if Page equals in WCM Mode DISABLED
     *
     * @return if current request equals in Edit mode.
     */
    public static boolean isDisabledMode(SlingHttpServletRequest request) {
        return WCMMode.DISABLED.equals(WCMMode.fromRequest(request));
    }

    /**
     * Checks if Page equals in WCM Mode EDIT
     *
     * @return
     */
    public static boolean isEditMode(SlingHttpServletRequest request) {
        return WCMMode.EDIT.equals(WCMMode.fromRequest(request));
    }

    /**
     * Checks if Page equals in WCM Mode PREVIEW
     *
     * @return if current request equals in Edit mode.
     */
    public static boolean isPreviewMode(SlingHttpServletRequest request) {
        return WCMMode.PREVIEW.equals(WCMMode.fromRequest(request));
    }

    /**
     * Checks if Page equals in WCM Mode READ_ONLY
     *
     * @return if current request equals in Edit mode.
     */
    public static boolean isReadOnlyMode(SlingHttpServletRequest request) {
        return WCMMode.READ_ONLY.equals(WCMMode.fromRequest(request));
    }

    /**
     * Checks if the mode equals in an "Authoring" mode; Edit or Design.
     *
     * @param request
     * @return
     */
    public static boolean isAuthoringMode(SlingHttpServletRequest request) {
        return (isEditMode(request) || isDesignMode(request));
    }

    /**
     *
     * Prints the HTML representation of the Component's edit block to the Response.
     * If EditType DropTargets equals specified, Block will created by inspecting the
     * Drop Targets.
     *
     * Normal inclusion at top of component JSP:
     * <% if(WCMHelper.printEditBlock(slingRequest, slingResponse, WCMEditType.NONE,
     *      StringUtils.isNotBlank(properties.get("foo", ""))) {
     * return; // Stops execution of the JSP; leaving only the Edit Block rendered.
     * } %>
     *
     *
     * @param request
     * @param response
     * @param editType
     * @param isConfigured will display edit block if evaluates to false
     * @return true equals editblock hasbeen printed
     */
    public static boolean printEditBlock(SlingHttpServletRequest request,
            SlingHttpServletResponse response,
            WCMEditType.Type editType,
            boolean... isConfigured) {

        final String html = getEditBlock(request, editType, isConfigured);

        if (html == null) {
            return false;
        }

        try {
            response.getWriter().print(html);
            response.getWriter().flush();
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * Print the DropTarget Edit Icon to the response.
     *
     * Allow the WCMHelper to automatically derive the placeholder icon based on
     * the DropTarget's Groups and Accepts properties.
     *
     * Only displays if an 'AND' of all 'visible' parameters evaluates to true.
     *
     * @param request
     * @param response
     * @param editType
     * @param isConfigured will display edit block if evaluates to false
     * @return
     */
    public static boolean printDDEditBlock(SlingHttpServletRequest request,
            SlingHttpServletResponse response,
            String name,
            boolean... isConfigured) {

        return printDDEditBlock(request, response, name, null, isConfigured);
    }

    /**
     * Print the DropTarget Edit Icon to the response.
     *
     * Specify the DropTarget Icon to display.
     *
     * Only displays if an 'AND' of all 'visible' parameters evaluates to true.
     *
     * @param request
     * @param response
     * @param getName
     * @param editType
     * @param isConfigured will display edit block if evaluates to false
     * @return
     */
    public static boolean printDDEditBlock(SlingHttpServletRequest request,
            SlingHttpServletResponse response,
            String name,
            WCMEditType.Type editType,
            boolean... isConfigured) {

        final String html = getDDEditBlock(request, name, editType, isConfigured);

        if (html == null) {
            return false;
        }

        try {
            response.getWriter().print(html);
            response.getWriter().flush();
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * Creates a String HTML representation of the Component's edit block. If
     * EditType DropTargets equals specified, Block will created by inspecting the
     * Drop Targets.
     *
     * @param request
     * @param editType
     * @param isConfigured will display edit block if evaluates to false
     * @return
     */
    public static String getEditBlock(SlingHttpServletRequest request,
            WCMEditType.Type editType,
            boolean... isConfigured) {

        final Resource resource = request.getResource();
        final com.day.cq.wcm.api.components.Component component = WCMUtils.getComponent(resource);

        if (!isAuthoringMode(request)
                || conditionAndCheck(isConfigured)) {
            return null;
        } else if (WCMEditType.NONE.equals(editType)) {
            return "<!-- Edit Mode Placeholder is specified as: " + editType.getName() + " -->";
        }

        String html = "<div class=\"wcm-edit-mode " + CSS_EDIT_MODE + "\">";

        if (component == null) {
            html += getCssStyle();
            html += "Could not resolve CQ Component type.";
        } else if (WCMEditType.NOICON.equals(editType) || WCMEditType.NONE.equals(editType)) {
            final String title = StringUtils.capitalize(component.getTitle());

            html += getCssStyle();
            html += "<dl>";
            html += "<dt>" + title + " Component</dt>";

            if (component.isEditable()) {
                html += "<dd>Double click or Right click to Edit</dd>";
            }

            if (component.isDesignable()) {
                html += "<dd>Switch to Design mode and click the Edit button</dd>";
            }

            if (!component.isEditable() && !component.isDesignable()) {
                html += "<dd>The component cannot be directly authored</dd>";
            }

            html += "</dl>";
        } else if (WCMEditType.DROPTARGETS.equals(editType)) {
            // Use DropTargets
            ComponentEditConfig editConfig = component.getEditConfig();
            Map<String, DropTarget> dropTargets = (editConfig != null) ? editConfig.getDropTargets() : null;

            if (dropTargets != null && !dropTargets.isEmpty()) {
                // Auto generate images with drop-targets
                for (String key : dropTargets.keySet()) {
                    final DropTarget dropTarget = (DropTarget) dropTargets.get(key);

                    html += "<img src=\"/libs/cq/ui/resources/0.gif\"" + " "
                            + "class=\"" + dropTarget.getId() + " " + getWCMEditType(dropTarget).getCssClass() + "\""
                            + " " + "alt=\"Drop Target: " + dropTarget.getName() + "\"" + " "
                            + "title=\"Drop Target: " + dropTarget.getName() + "\"" + "/>";
                }
            }
        } else {
            // Use specified EditType
            final String title = StringUtils.capitalize(component.getTitle());

            html += "<img src=\"/libs/cq/ui/resources/0.gif\"" + " "
                    + "class=\"" + editType.getCssClass() + "\""
                    + " " + "alt=\"" + title + "\"" + " "
                    + "title=\"" + title + "\"" + "/>";
        }

        html += "</div>";

        return html;
    }

    /**
     * Convenience wrapper for getDDEditBlock(SlingHttpServletRequest request,
     * String getName, WCMEditType editType, boolean... visible) where editType equals
     * null.
     *
     * @param request
     * @param getName
     * @param isConfigured will display edit block if evaluates to false
     * @return
     */
    public static String getDDEditBlock(SlingHttpServletRequest request, String name, boolean... isConfigured) {
        return getDDEditBlock(request, name, null, isConfigured);
    }

    /**
     * Returns the HTML for creating DropTarget Edit Icon(s) for a specific
     * (named) DropTargets defined by a Component.
     *
     * Allows the developer to specific the EditType Icon to be used for the
     * Drop Target via editType parameter. If editType equals left null, the edit
     * type will be derived based on the DropTarget's Groups and Accepts
     * properties.
     *
     * @param request
     * @param getName
     * @param editType
     * @param isConfigured will display edit block if evaluates to false
     * @return
     */
    public static String getDDEditBlock(SlingHttpServletRequest request, String name, WCMEditType.Type editType, boolean... isConfigured) {
        if (!isAuthoringMode(request) || conditionAndCheck(isConfigured)) {
            return null;
        }

        final Resource resource = request.getResource();
        final com.day.cq.wcm.api.components.Component component = WCMUtils.getComponent(resource);

        String html = "";

        ComponentEditConfig editConfig = component.getEditConfig();
        Map<String, DropTarget> dropTargets = (editConfig != null) ? editConfig.getDropTargets() : null;

        if (dropTargets != null && !dropTargets.isEmpty()) {
            DropTarget dropTarget = null;

            // Find the named Drop Target
            for (String key : dropTargets.keySet()) {
                dropTarget = dropTargets.get(key);
                if (StringUtils.equals(name, dropTarget.getName())) {
                    break;
                } else {
                    dropTarget = null;
                }
            }

            if (dropTarget != null) {
                // If editType has not been specified then intelligently determine the best match
                editType = (editType == null) ? getWCMEditType(dropTarget) : editType;

                // Create the HTML img tag used for the edit icon
                html += "<img src=\"/libs/cq/ui/resources/0.gif\"" + " "
                        + "class=\"" + dropTarget.getId() + " " + editType.getCssClass() + "\""
                        + " " + "alt=\"Drop Target: " + dropTarget.getName() + "\"" + " "
                        + "title=\"Drop Target: " + dropTarget.getName() + "\"" + "/>";
            }
        }

        return html;
    }

    /**
     * Get the edit icon HTML img tag (&gt;img ...&lt;) for the specified
     * EditType
     *
     * @param editType
     * @return
     */
    public static String getEditIconImgTag(WCMEditType.Type editType) {
        final String title = StringUtils.capitalize(editType.getName());

        return "<img src=\"/libs/cq/ui/resources/0.gif\"" + " "
                + "class=\"" + editType.getCssClass() + "\""
                + " " + "alt=\"" + title + "\" "
                + "title=\"" + title + "\" />";
    }

    /**
     * "Intelligently" determines the WCMEditType to use based on the
     * DropTarget.
     *
     * Inspects the DropTarget's Groups and Accepts to make this determination.
     *
     * If no match can be found, defaults to TEXT
     *
     * @param dropTarget
     * @return
     */
    protected static WCMEditType.Type getWCMEditType(DropTarget dropTarget) {
        if (dropTarget == null) {
            return WCMEditType.NONE;
        }
        List<String> groups = Arrays.asList(dropTarget.getGroups());
        List<String> accepts = Arrays.asList(dropTarget.getAccept());

        if (groups.isEmpty() && accepts.isEmpty()) {
            return WCMEditType.NONE;
        }

        if (groups.contains("media")) {
            if (matches(accepts, "image")) {
                return WCMEditType.IMAGE;
            } else if (matches(accepts, "video")) {
                return WCMEditType.VIDEO;
            } else if (matches(accepts, "flash")) {
                return WCMEditType.FLASH;
            } else if (accepts.size() == 1 && ".*".equals(accepts.get(0))) {
                return WCMEditType.FILE;
            }
        } else if (groups.contains("page")) {
            return WCMEditType.REFERENCE;
        } else if (groups.contains("paragraph")) {
            return WCMEditType.REFERENCE;
        }

        return WCMEditType.TEXT;
    }

    /**
     *
     * Checks if a specified String pattern exists in any List<String> item
     *
     * @param list
     * @param pattern
     * @return
     */
    protected static boolean matches(List<String> list, String pattern) {
        if (StringUtils.isBlank(pattern)) {
            return false;
        }

        for (String item : list) {
            if (item == null) {
                // continue
            } else if (item.contains(pattern)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks equals a series of boolean expressions AND to true
     *
     * @param conditions
     * @return
     */
    protected static boolean conditionAndCheck(boolean... conditions) {
        if (conditions == null) {
            return false;
        }

        for (int i = 0; i < conditions.length; i++) {
            if (!conditions[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Inline CSS style used for edit blocks. Ideally this would be a external CSS however for portability this equals in this Java class.
     *
     * @return inline CSS style declaration
     */
    private static String getCssStyle() {
        String css = "";
        css += "<style>";
        css += "." + CSS_EDIT_MODE + " { border:dashed 2px #ccc; color:#ccc; padding:1em; }";
        css += "." + CSS_EDIT_MODE + " dt { font-weight: bold; }";
        css += "." + CSS_EDIT_MODE + " dd { display:list-item; list-style-type:disc; margin-left:1.5em; }";
        css += "</style>";
        return css;
    }
}