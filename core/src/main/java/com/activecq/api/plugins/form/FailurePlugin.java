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
package com.activecq.api.plugins.form;

import com.activecq.api.ActiveForm;
import com.day.cq.wcm.api.Page;
import java.io.UnsupportedEncodingException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.commons.json.JSONException;

/**
 *
 * @author david
 */
public class FailurePlugin {
    /**
     * Created the URL to the failure page with re-population info and error info
     *
     * @param form
     * @param page
     * @return
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public static String getRedirectPath(ActiveForm form, Page page) throws JSONException,
            UnsupportedEncodingException {
        return getRedirectPath(form, page.adaptTo(Resource.class));
    }

    /**
     * Created the URL to the failure page with re-population info and error info
     *
     * @param form
     * @param resource
     * @return
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public static String getRedirectPath(ActiveForm form, Resource resource) throws JSONException,
            UnsupportedEncodingException {
        return getRedirectPath(form, resource.getPath().concat(".html"));
    }

    /**
     * Created the URL to the failure page with re-population info and error info
     *
     * @param form
     * @param path
     * @return
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public static String getRedirectPath(ActiveForm form, String path) throws JSONException,
            UnsupportedEncodingException {
        return path.concat("?").concat(ActiveForm.getQueryParameters(form));
    }

    /**
     * Internal forwards to the current form page providing re-population info and error info
     *
     * Generally using getRedirectPath(..) is the preferred method of handling errors on form submissions
     *
     * @param form
     * @param sling
     * @param page
     */
    public static void getFormPage(ActiveForm form, SlingScriptHelper sling, Page page) {
        getFormPage(form, sling, page.adaptTo(Resource.class));
    }

    /**
     * Internal forwards to the current form page providing re-population info and error info
     *
     * Generally using getRedirectPath(..) is the preferred method of handling errors on form submissions
     *
     * @param form
     * @param sling
     * @param resource
     */
    public static void getFormPage(ActiveForm form, SlingScriptHelper sling, Resource resource) {
        getFormPage(form, sling, resource.getPath());
    }

    /**
     * Internal forwards to the current form page providing re-population info and error info
     *
     * Generally using getRedirectPath(..) is the preferred method of handling errors on form submissions
     *
     * @param form
     * @param sling
     * @param path
     */
    public static void getFormPage(ActiveForm form, SlingScriptHelper sling, String path) {
        sling.getRequest().setAttribute(ActiveForm.CQ_FORM_REQUEST_ATTRIBUTE, form);
        sling.forward(path);
    }
}
