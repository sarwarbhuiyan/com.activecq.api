/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.activecq.api.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

/**
 * Util class to help get OSGi Services
 *
 * @author david
 */
public class ServiceUtil {

    /**
     * <p>
     * Returns a List of Service objects
     * <p></p>
     * Example: ServiceUtil.getServices(com.day.cq.dam.api.handler.AssetHandler.class, sling)
     * </p>
     *
     * @param <T> Type of Services to return
     * @param serviceType Type of Service to discover
     * @param sling SlingScriptHelper which provides access to Services
     * @return a List of Service objects
     */
    public static <T> List<T> getServices(Class<T> serviceType, SlingScriptHelper sling) {
        return getServices(serviceType, sling, null);
    }

    /**
     * <p>
     * Returns a List of filtered Service objects
     * <p></p>
     * Example: ServiceUtil.getServices(com.day.cq.dam.api.handler.AssetHandler.class, sling, "(service.pid=com.day.cq.dam.handler.standard.ooxml.OpenOfficeHandler)")
     * </p>
     *
     * @param <T> Type of Services to return
     * @param serviceType Type of Service to discover
     * @param sling SlingScriptHelper which provides access to Services
     * @param filter Filter expression to filter services on "(service.property=value)"
     * @return a List of Service objects
     */
    public static <T> List<T> getServices(Class<T> serviceType, SlingScriptHelper sling, String filter) {
        return Arrays.asList(sling.getServices(serviceType, filter));
    }

    /**
     * <p>
     * Returns a List of Service objects
     * <p></p>
     * Example: ServiceUtil.getServices(com.day.cq.dam.api.handler.AssetHandler.class, sling)
     * </p>
     *
     * @param <T> Type of Services to return
     * @param serviceType Type of Service to discover
     * @param request SlingHttpServletRequest which provides context to get OSGi Services
     * @return a List of Service objects
     */
    public static <T> List<T> getServices(Class<T> serviceType, SlingHttpServletRequest request) {
        return getServices(serviceType, request, null);
    }

    /**
     * <p>
     * Returns a List of filtered Service objects
     * <p></p>
     * Example: ServiceUtil.getServices(com.day.cq.dam.api.handler.AssetHandler.class, sling, "(service.pid=com.day.cq.dam.handler.standard.ooxml.OpenOfficeHandler)")
     * </p>
     *
     * @param <T> Type of Services to return
     * @param serviceType Type of Service to discover
     * @param request SlingHttpServletRequest which provides context to get OSGi Services
     * @param filter Filter expression to filter services on "(service.property=value)"
     * @return a List of Service objects
     */
    public static <T> List<T> getServices(Class<T> serviceType, SlingHttpServletRequest request, String filter) {
        SlingScriptHelper sling = HttpRequestUtil.getSlingScriptHelper(request);
        if(sling == null) { return new ArrayList<T>(); }
        return getServices(serviceType, sling, filter);
    }
}
