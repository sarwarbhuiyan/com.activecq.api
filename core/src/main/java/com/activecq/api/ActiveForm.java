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
package com.activecq.api;

import com.activecq.api.plugins.form.FailurePlugin;
import com.activecq.api.plugins.form.SuccessPlugin;
import com.activecq.api.utils.EncodingUtil;
import com.activecq.api.utils.TypeUtil;
import com.day.cq.commons.jcr.JcrConstants;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

/**
 * <p>
 * The ActiveForm class is abstract as the intent is each Form
 * implementation will subclass it.
 * </p><p>
 * The sub-classed ActiveForm object models an HTML form and facilitates
 * collecting data from the Request, input validation, and responding to
 * the client with form errors.
 * </p>
 *
 * @author david
 */
public class ActiveForm {

    public static final String CQ_FORM = "_cq_f";
    public static final String CQ_FORM_SUCCESS = "_cq_fs";
    public static final String CQ_FORM_REQUEST_ATTRIBUTE = "_cq_f_req_attr";
    public static final FailurePlugin Failure = new FailurePlugin();
    public static final SuccessPlugin Success = new SuccessPlugin();

    /**
     * ActiveProperties representing system JCR/Sling node properties
     */
    public static class SystemProperties extends ActiveProperties {

        public static final String CREATED_AT = JcrConstants.JCR_CREATED;
        public static final String LAST_MODIFIED_AT = JcrConstants.JCR_LASTMODIFIED;
        public static final String PRIMARY_TYPE = JcrConstants.JCR_PRIMARYTYPE;
        public static final String RESOURCE_TYPE = SlingConstants.PROPERTY_RESOURCE_TYPE;
        public static final String RESOURCE_SUPER_TYPE = SlingConstants.PROPERTY_RESOURCE_SUPER_TYPE;
        public static final String DISTRIBUTE = "cq:distribute";
    }
    public static final SystemProperties SystemProperties = new SystemProperties();

    /**
     * Empty ActiveProperty; ActiveResource objects should extend this
     * Properties (ActiveResource.Properties)
     */
    protected static class Properties extends ActiveProperties {
    }
    public final ActiveProperties Properties = new Properties();
    /**
     * *************************************************************************
     * ActiveForm Data setting and retrieval
     * ************************************************************************
     */
    /**
     * Private data modeling attributes
     */
    private Map<String, Object> data = new HashMap<String, Object>();
    private ActiveErrors errors = new ActiveErrors();

    protected ActiveForm() {
    }

    /**
     *
     * @param key
     * @return
     */
    public boolean has(String key) {
        return this.data.containsKey(key);
    }

    /**
     * Checks of the value of for a key is empty (null or empty string)
     *
     * @param key
     * @return
     */
    public boolean isEmpty(String key) {
        if (this.has(key)) {
            return StringUtils.isBlank(this.get(key));
        }

        return true;
    }

    /**
     * Gets a String value from this.data
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return this.get(key, "");
    }

    /**
     * Get value from this.data
     *
     * @param <T>
     * @param key
     * @param defaultValue
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, T defaultValue) {
        if (this.has(key)) {
            return (T) this.data.get(key);
        }

        return defaultValue;
    }

    /**
     * Get value from this.data
     *
     * @param <T>
     * @param key
     * @param type
     * @return
     */
    public <T> T get(String key, Class<T> type) {
        if (this.has(key)) {
            if (type != null) {
                return type.cast(this.data.get(key));
            }
        }

        return null;
    }

    /**
     * Stores a key/value into this.data
     *
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        this.data.put(key, value);
    }

    public void set(String key, boolean value) {
        this.data.put(key, Boolean.valueOf(value));
    }

    public void set(String key, double value) {
        this.data.put(key, Double.valueOf(value));
    }

    public void set(String key, long value) {
        this.data.put(key, Long.valueOf(value));
    }

    public void set(String key, int value) {
        this.data.put(key, Integer.valueOf(value));
    }

    /**
     * Takes all the key/values pairs in the parameter map and merges them into
     * this.data.
     *
     * @param map
     */
    public void setAll(Map<String, ? extends Object> map) {
        if (map == null) {
            return;
        }
        for (String key : map.keySet()) {
            this.set(key, map.get(key));
        }
    }

    /**
     * Reset the contents of the ActiveForm to the supplied map. If the map is
     * null, reset the ActiveForm to be empty.
     *
     * @param map
     */
    public void resetTo(Map<String, ? extends Object> map) {
        if (map == null) {
            map = new HashMap<String, String>();
        }

        this.data = (Map<String, Object>) map;
        this.errors = new ActiveErrors();
    }

    /**
     * Reset contents of ActiveForm to be empty
     */
    public void reset() {
        this.data = new HashMap<String, Object>();
        this.errors = new ActiveErrors();
    }

    /**
     * Checks if any keys have been set
     *
     * @return
     */
    public boolean hasData() {
        return !this.data.isEmpty();
    }

    // Conversion methods
    /**
     * Convert the object data in JSON format
     *
     * @return JSONObject representation of this.data
     */
    public JSONObject toJSON() {
        return new JSONObject(this.data);
    }

    /**
     * Returns this.data at Map object
     *
     * @return Map object representation of this.data (which is already its
     * native data type)
     */
    public Map<String, Object> toMap() {
        return new HashMap<String, Object>(this.data);
    }

    // ActiveError Wrapper/Helper Methods
    /**
     * Gets this' ActiveError object
     *
     * @return ActiveError object
     */
    public ActiveErrors getErrors() {
        return this.errors;
    }

    /**
     * Checks if this' ActiveError object contains any errors
     *
     * @return true if ActiveError object has errors
     */
    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }

    /**
     * Checks if an error has been associated with a specific field (key)
     *
     * @param key
     * @return
     */
    public boolean hasError(String key) {
        return this.errors.has(key);
    }

    /**
     * Gets the error message (String) associated with a specific field (key)
     *
     * @param key
     * @return
     */
    public String getError(String key) {
        return this.hasError(key) ? this.errors.get(key) : null;
    }

    /**
     * Adds an error entry and message to this's ActiveError object
     *
     * @param key
     * @param message
     */
    public void setError(String key, String message) {
        this.errors.set(key, message);
    }

    /**
     * Adds an error entry to this's ActiveError object
     *
     * @param key
     */
    public void setError(String key) {
        this.errors.set(key);
    }

    /**
     * *************************************************************************
     * ActiveForm Builder
     * ************************************************************************
     */
    /**
     * Build an ActiveForm object from the provided resource
     *
     * @param resource Resource to populate the ActiveForm from
     * @return populated ActiveForm object
     */
    public ActiveForm(Resource resource) {
        this(new HashMap<String, Object>(resource.adaptTo(ValueMap.class)));
    }

    /**
     * Build an ActiveForm object from the provided Map
     *
     * @param map populated ActiveForm object
     */
    public ActiveForm(Map<String, Object> map) {
        for (String key : map.keySet()) {
            this.set(key, map.get(key));
        }
    }

    /**
     * Build and ActiveForm object from the provided SlingHttpServletRequest
     * - Inspects Request Attributes and Query Parameters
     *
     * @param <T>
     * @param request
     */
    public ActiveForm (SlingHttpServletRequest request) {
        this(request, new String[0]);
    }

    /**
     * Build and ActiveForm object from the provided SlingHttpServletRequest
     * - Inspects Request Attributes and Query Parameters
     *
     * @param <T>
     * @param request
     * @param properties This ActiveForms properties will be used to pull data from the Request
     */
    public <T extends ActiveProperties> ActiveForm (SlingHttpServletRequest request, T properties) {
        this(request, properties.getKeys().toArray(new String[properties.size()]));
    }

    /**
     * Build and ActiveForm object from the provided SlingHttpServletRequest
     * - Inspects Request Attributes and Query Parameters
     *
     * @param <T>
     * @param request
     * @param properties A list of keys used to pull data from the Request
     */
    public <T extends ActiveProperties> ActiveForm (SlingHttpServletRequest request, String... properties) {
        if(properties == null || properties.length < 1) {
            for (Object okey : request.getParameterMap().keySet()) {
                if(okey instanceof String) {
                    String key = (String) okey;
                    this.data.put(key, StringUtils.stripToEmpty(request.getParameter(key)));
                }
            }
        } else {
            for (String key : properties) {
                this.data.put(key, StringUtils.stripToEmpty(request.getParameter(key)));
            }
        }

        if (hasIncomingRequestAttributeData(request)) {
            // Get Form and Errors from Request Attr
            // This is to handle when the "Forward" method is used
            final ActiveForm incomingForm = (ActiveForm) request.getAttribute(ActiveForm.CQ_FORM_REQUEST_ATTRIBUTE);
            this.data = incomingForm.toMap();
            this.errors = new ActiveErrors(request);
        } else if (hasIncomingQueryParamData(request)) {
            // Get Form and Errors from Query Params
            // This is to handle the "Redirect" method
            String formData = request.getRequestParameter(ActiveForm.CQ_FORM).getString();
            this.errors = new ActiveErrors(request);

            if (StringUtils.isNotBlank(formData)) {
                try {
                    JSONObject jsonForm = new JSONObject(EncodingUtil.decode(formData));
                    this.data = TypeUtil.toMap(jsonForm);
                } catch (UnsupportedEncodingException e) {
                } catch (JSONException e) {
                }
            }
        }
    }

    private static boolean hasIncomingRequestAttributeData(SlingHttpServletRequest request) {
        if (request.getAttribute(ActiveForm.CQ_FORM_REQUEST_ATTRIBUTE) != null) {
            return (request.getAttribute(ActiveForm.CQ_FORM_REQUEST_ATTRIBUTE) instanceof ActiveForm);
        }

        return false;
    }

    private static boolean hasIncomingQueryParamData(SlingHttpServletRequest request) {
        RequestParameter param = request.getRequestParameter(ActiveForm.CQ_FORM);
        if (param == null) {
            return false;
        }
        return StringUtils.isNotBlank(param.getString());
    }

    /**
     * *
     * Returns the a string of query parameters that hold Form and Form Error
     * data
     *
     * @return
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public static String getQueryParameters(ActiveForm form) throws UnsupportedEncodingException {

        final String formData = form.toJSON().toString();
        final String errorData = form.getErrors().toJSON().toString();

        String params = ActiveForm.CQ_FORM;
        params += "=";
        params += EncodingUtil.encode(formData);
        params += "&";
        params += ActiveErrors.CQ_ERRORS;
        params += "=";
        params += EncodingUtil.encode(errorData);

        return params;
    }
}
