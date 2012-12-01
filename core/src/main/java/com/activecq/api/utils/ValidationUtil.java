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

import com.activecq.api.ActiveForm;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.NonExistingResource;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;

/**
 * Provides common validations for ActiveForms
 *
 * @author david
 */
public class ValidationUtil {

    public enum Comparisons {
        LESS_THAN,
        LESS_THAN_OR_EQUAL_TO,
        EQUAL_TO,
        GREATER_THAN,
        GREATER_THAN_OR_EQUAL_TO
    }

    private ValidationUtil() { }

    /**
     * Checks if the value @ key exists and is not null and not white space
     *
     * @param form
     * @param key
     * @return
     */
    public static boolean isPresent(ActiveForm form, String key) {
        if (!form.has(key)) {
            return false;
        }

        Object val = form.get(key, Object.class);
        if (val instanceof String) {
            return StringUtils.isNotBlank((String) val);
        } else if (val != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if the value @ key is a path that exists in the JCR
     *
     * @param form
     * @param key
     * @param resourceResolver
     * @return
     * @throws NullPointerException
     */
    public static boolean isValidPath(ActiveForm form, String key, ResourceResolver resourceResolver)
            throws NullPointerException {
        if (!isPresent(form, key)) {
            return false;
        }

        if (resourceResolver == null) {
            throw new NullPointerException("ResourceResolver cannot be null.");
        }

        String val = (String) form.get(key);
        Resource r = resourceResolver.resolve(val);
        if (r == null) {
            return false;
        } else if (ResourceUtil.isA(r, NonExistingResource.RESOURCE_TYPE_NON_EXISTING)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks to make sure the field (key) represents a confirmed input
     *
     * @param form
     * @param key
     * @return
     */
    public static boolean isConfirmed(ActiveForm form, String key) {
        if (!isPresent(form, key)) {
            return false;
        }

        String val = form.get(key);
        return StringUtils.equalsIgnoreCase(val, "true");
    }

    /**
     * Checks if the value @ key is NOT in supplied blacklist
     *
     * @param <T>
     * @param form
     * @param key
     * @param blacklist
     * @return
     */
    public  static <T> boolean isNotIn(ActiveForm form, String key, T... blacklist) {
        if (!isPresent(form, key)) {
            return false;
        }

        List<T> blist = Arrays.asList(blacklist);
        T val = (T) form.get(key);

        if (val == null) {
            return false;
        }

        for (T item : blist) {
            if (item instanceof Object) {
                if (val.equals(item)) {
                    return false;
                }
            } else {
                if (val == item) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Checks if the value @ key IS in supplied blacklist
     *
     * @param <T>
     * @param form
     * @param key
     * @param whitelist
     * @return
     */
    public static <T> boolean isIn(ActiveForm form, String key, T... whitelist) {
        if (!isPresent(form, key)) {
            return false;
        }

        List<T> wlist = Arrays.asList(whitelist);
        T val = (T) form.get(key);

        for (T item : wlist) {
            if (item instanceof Object) {
                if (val.equals(item)) {
                    return true;
                }
            } else {
                if (val == item) {
                    return true;
                }
            }
        }

        return false;
    }



    /**
     * Checks if the value @ key meets the length conditions
     *
     * @param form
     * @param key
     * @param comparison
     * @param length
     * @return
     */
    public static boolean isLength(ActiveForm form, String key, Comparisons comparison, int length) {
        if (!isPresent(form, key)) {
            return false;
        }

        int dataLength = 0;
        Object val = form.get(key);

        if (val instanceof String) {
            dataLength = StringUtils.stripToEmpty((String) val).length();
        } else {
            return false;
        }

        if (Comparisons.EQUAL_TO.equals(comparison)) {
            return (dataLength == length);
        } else if (Comparisons.GREATER_THAN.equals(comparison)) {
            return (dataLength > length);
        } else if (Comparisons.GREATER_THAN_OR_EQUAL_TO.equals(comparison)) {
            return (dataLength >= length);
        } else if (Comparisons.LESS_THAN.equals(comparison)) {
            return (dataLength < length);
        } else if (Comparisons.LESS_THAN_OR_EQUAL_TO.equals(comparison)) {
            return (dataLength <= length);
        } else {
            return false;
        }
    }

    /**
     * Checks if the value @ key matches regex
     *
     * @param form
     * @param key
     * @param regex
     * @return
     */
    public static boolean isOfFormat(ActiveForm form, String key, String regex) {
        if (!isPresent(form, key)) {
            return false;
        }

        String val = StringUtils.stripToEmpty(form.get(key));

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(val);

        return m.matches();
    }
}
