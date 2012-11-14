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
package com.activecq.api.plugins.form;

import com.activecq.api.ActiveForm;
import com.day.cq.wcm.api.Page;
import java.io.UnsupportedEncodingException;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONException;

/**
 *
 * @author david
 */
public class SuccessPlugin {

    /**
     * Returns a URL (no scheme) to the success landing page (Page obj) with
     * success message
     *
     * @param page
     * @param message
     * @return
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public static String getRedirectPath(Page page, String message) throws UnsupportedEncodingException {
        return getRedirectPath(page.adaptTo(Resource.class), message);
    }

    /**
     * Returns a URL (no scheme) to the success landing page (Resource) with
     * success message
     *
     * @param resource
     * @param message
     * @return
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public static String getRedirectPath(Resource resource, String message) throws UnsupportedEncodingException {
        return getRedirectPath(resource.getPath().concat(".html"), message);
    }

    /**
     * Returns a URL (no scheme) to the success landing page (String path) with
     * success message
     *
     * @param path
     * @param message
     * @return
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public static String getRedirectPath(String path, String message) throws UnsupportedEncodingException {
        if (StringUtils.isBlank(message)) {
            return path;
        } else {
            message = StringEscapeUtils.escapeJavaScript(message);
            return path.concat("?").concat(ActiveForm.CQ_FORM_SUCCESS).concat("=").concat(message);
        }
    }
}
