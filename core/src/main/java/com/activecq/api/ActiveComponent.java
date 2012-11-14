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

import com.activecq.api.plugins.wrappers.ComponentPluginsWrapper;
import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.search.QueryBuilder;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.components.Component;
import com.day.cq.wcm.api.components.ComponentContext;
import com.day.cq.wcm.api.components.EditContext;
import com.day.cq.wcm.api.designer.Design;
import com.day.cq.wcm.api.designer.Designer;
import com.day.cq.wcm.api.designer.Style;
import com.day.cq.wcm.commons.WCMUtils;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This SlingHttpServletRequest must target a Resource whose resourceType
 * resolves to a CQ Component.
 * </p><p>
 * The ActiveComponent class is abstract as the intent is each Component
 * implementation will subclass it. The methods in this class are protected,
 * encouraging moving component domain logic into the subclass, instead of
 * keeping it in the JSP.
 * </p><p>
 * If provided methods need to be exposed to the JSP, the subclass must override
 * them as public methods, and return the super.method(..) call.
 * </p><p>
 * ActiveComponent is not thread safe and (at this time) is only meant to be
 * instantiated in a JSP.
 * </p><p>
 * The method signatures for subclasses are encouraged to follow the get/set
 * bean pattern for heightened compatability with JSTL.
 * </p>
 *
 * @author david
 */
public abstract class ActiveComponent {
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(ActiveComponent.class);

    /*
     * Universal Fields
     */
    private SlingScriptHelper slingScriptHelper;
    private SlingHttpServletRequest request;
    private SlingHttpServletResponse response;
    private Component component;
    private Node node;
    private Resource resource;
    private ComponentContext componentContext;
    private EditContext editContext;

    /*
     * Request Fields
     */
    private Page requestPage;
    private Design requestDesign;
    private Style requestStyle;
    private ValueMap requestPageProperties;
    private ValueMap requestDesignProperties;

    /*
     * Resource Fields
     */
    private ValueMap resourceProperties;
    private Page resourcePage;
    private Design resourceDesign;
    private Style resourceStyle;
    private ValueMap resourcePageProperties;
    private ValueMap resourceDesignProperties;

    /*
     * Utility Fields
     */
    private Designer designer;
    private ResourceResolver resourceResolver;
    private QueryBuilder queryBuilder;
    private PageManager pageManager;

    /**
     * <p>Wrapper that contains standard ActiveComponent Plug-ins for supporting
     * Diff, XSS protection, I18n, and WCMMode switching.
     * </p><p>
     * The plug-ins act as a convenient methods of access for common, and often
     * overlooked functionality.
     * </p>
     */
    public final ComponentPluginsWrapper Plugins;

    /**
     * <p>
     * ActiveComponent constructor which takes a SlingHttpServletRequest object.
     * </p><p>
     * This SlingHttpServletRequest must target a Resource whose resourceType
     * resolves to a CQ Component.
     * </p>
     *
     *
     * @param request
     * @throws RepositoryException
     * @throws LoginException
     */
    protected ActiveComponent(SlingHttpServletRequest request)
            throws RepositoryException, LoginException {

        // HTTP Request
        this.request = request;
        if (this.request == null) {
            throw new IllegalArgumentException(
                    "Sling HTTP Request must NOT be null.");
        }

        // Get SlingScriptHelper from SlingBindings included as an attribute on the Sling HTTP Request
        SlingBindings slingBindings = (SlingBindings) this.request.getAttribute(SlingBindings.class.getName());
        if (slingBindings == null) {
            throw new IllegalArgumentException(
                    "SlingHttpServletRequest most have contain a SlingBindings object at key: " + SlingBindings.class.getName());
        }

        // Sling Script Helper
        this.slingScriptHelper = slingBindings.getSling();
        if (this.slingScriptHelper == null) {
            throw new IllegalArgumentException(
                    "SlingScriptHelper must NOT be null.");
        }

        // HTTP Response
        this.response = this.slingScriptHelper.getResponse();
        if (this.response == null) {
            throw new IllegalArgumentException(
                    "SlingScriptHelper's Sling HTTP Response must NOT be null.");
        }

        // Resource of component (Regardless of which page/resource it lives under)
        this.resource = this.request.getResource();
        if (this.resource == null) {
            throw new IllegalArgumentException("Resource must NOT be null.");
        }

        // CQ Component object
        this.component = WCMUtils.getComponent(this.resource);
        if (this.component == null) {
            throw new IllegalArgumentException(
                    "Resource must have a resourceType of a CQ Component.");
        }

        // JCR Node
        this.node = WCMUtils.getNode(this.resource);

        // Initialize Plugins
        this.Plugins = new ComponentPluginsWrapper(this.request);
    }


    /**
     * *************************************************************************
     * Request & Response
     *************************************************************************
     */
    /**
     * Get the Sling Request that initiated the request for this component
     *
     * @return the Sling Request that initiated the request for this component
     */
    protected SlingHttpServletRequest getRequest() {
        return this.request;
    }

    /**
     * Get the Sling Response that will be used to transport this component
     * rendition to the client
     *
     * @return the Sling Response that will be used to transport this component
     * rendition to the client
     */
    protected SlingHttpServletResponse getResponse() {
        return this.response;
    }

    /**
     * *************************************************************************
     * Getters for Component Resource
     *
     * These will deal directly with the Resource node instance that represents
     * a particular instance of a Component and does care which Page the
     * component actually resides on.
     *************************************************************************
     */
    /**
     * <p>
     * Getter for Resource object that represents the Component Instance
     * </p><p>
     * http://dev.day.com/docs/en/cq/current/javadoc/org/apache/sling/api/resource/Resource.html
     * </p>
     * @return Resource representing the component
     */
    protected Resource getResource() {
        return this.resource;
    }

    /**
     * <p>
     * Getter for the resource's Component object.
     * </p><p>
     * This object represents the Component implementation under /apps or /libs.
     * </p><p>
     * http://dev.day.com/docs/en/cq/current/javadoc/com/day/cq/wcm/api/components/ComponentContext.html
     * </p>
     * @return CQ Component representing the component
     */
    protected Component getComponent() {
        return this.component;
    }

    /**
     * Check this component has a valid node
     *
     * @return true is Node exists
     */
    protected boolean hasNode() {
        return this.node != null;
    }

    /**
     * <p>
     * Getter for Resource's JCR Node. JCR Node should be used only for modify
     * the Node. Sling APIs should be used to read from the resources.
     * </p><p>
     * http://www.day.com/maven/jsr170/javadocs/jcr-1.0/javax/jcr/Node.html
     * </p>
     * @return Node representing the component
     */
    protected Node getNode() {
        return this.node;
    }

    /**
     * <p>
     * Get for the Component's ComponentContext for the Request
     * </p><p>
     * The Component Context's context is that of the Request, not necessarily
     * the page that contains the component.
     * </p><p>
     * http://dev.day.com/docs/en/cq/current/javadoc/com/day/cq/wcm/api/components/ComponentContext.html
     * </p>
     * @return the Component Content of the Request
     */
    protected ComponentContext getComponentContext() {
        if (this.componentContext == null) {
            this.componentContext = WCMUtils.getComponentContext(this.getRequest());
        }

        return this.componentContext;
    }

    /**
     * <p>
     * Get the Component's Edit Context
     * </p><p>
     * http://dev.day.com/docs/en/cq/current/javadoc/com/day/cq/wcm/api/components/EditContext.html
     * </p>
     *
     * @return the CQ Component's Edit Context
     */
    protected EditContext getEditContext() {
        if (this.editContext == null) {
            if (this.getComponentContext() != null) {
                this.editContext = this.getComponentContext().getEditContext();
            }
        }

        return this.editContext;
    }

    /**
     * *************************************************************************
     * Lazy-loading Getters for Request-oriented Attributes
     *************************************************************************
     */
    /**
     * <p>
     * Getter for the Request Resource's Page object.
     * <p></p>
     * This represents the page resource that was requested, and sling includes
     * the component (regardless of where the include's path points).
     * <p></p>
     * http://dev.day.com/docs/en/cq/current/javadoc/com/day/cq/wcm/api/Page.html
     * <p>
     *
     * @return the Request's CQ Page object
     */
    protected Page getRequestPage() {
        if (this.requestPage == null) {
            if (this.getComponentContext() != null) {
                this.requestPage = this.getComponentContext().getPage();

                if (this.requestPage == null) {
                    this.requestPage = this.getResourcePage();
                }
            }
        }

        return this.requestPage;
    }

    /**
     * <p>
     * Convenience method.
     * </p><p>
     * Handles the "normal" case of using the Request Page's Page object.
     * </p>
     *
     * @return the Request's CQ Page object
     */
    protected Page getPage() {
        return this.getRequestPage();
    }

    /**
     * <p>
     * Getter for the Requested Page's Design
     * </p><p>
     * http://dev.day.com/docs/en/cq/current/javadoc/index.html?com/day/cq/wcm/api/designer/class-use/Design.html
     *</p>
     *
     * @return the Requested Page's Design
     */
    protected Design getRequestDesign() {
        if (this.requestDesign == null) {
            if (this.getDesigner() != null) {
                if (this.getRequestPage() != null) {
                    this.requestDesign = this.getDesigner().getDesign(this.getRequestPage());
                }
            }
        }

        return this.requestDesign;
    }

    /**
     * <p>
     * Convenience method.
     * </p><p>
     * Handles the "normal" case of using the Request Page's Design
     * </p>
     *
     * @returnThe Requested Page's Design
     */
    protected Design getDesign() {
        return this.getRequestDesign();
    }

    /**
     * Gets the Page Properties for the Page associated with Request
     *
     * @return the Page Properties as a HierarchyNodeInheritanceValueMap
     */
    protected ValueMap getRequestPageProperties() {
        if (this.requestPageProperties == null) {
            if (this.getRequestPage() != null) {
                this.requestPageProperties = new HierarchyNodeInheritanceValueMap(this.getRequestPage().getContentResource());
            }
        }

        return this.requestPageProperties;
    }

    /**
     * Alias for getPageProperties()
     *
     * @return the Page Properties as a HierarchyNodeInheritanceValueMap
     */
    protected ValueMap getPageProperties() {
        return this.getRequestPageProperties();
    }

    /**
     * Gets the Page Properties for the Page associated with the Resource
     *
     * @return the Page Properties as a HierarchyNodeInheritanceValueMap
     */
    protected ValueMap getResourcePageProperties() {
        if (this.resourcePageProperties == null) {
            if (this.getResourcePage() != null) {
                this.resourcePageProperties = new HierarchyNodeInheritanceValueMap(this.getResourcePage().getContentResource());
            }
        }

        return this.resourcePageProperties;
    }

    /**
     * Getter for the Component's Page's Style
     *
     * @return Style associated with the Request
     */
    protected Style getRequestStyle() {
        if (this.requestStyle == null) {
            if (this.getRequestDesign() != null && this.getComponentContext() != null) {
                this.requestStyle = this.getRequestDesign().getStyle(this.getComponentContext().getCell());
            }
        }

        return this.requestStyle;
    }

    /**
     * <p>
     * Convenience method.
     * </p><p>
     * Alias for getRequestStyle()
     * </p>
     * @return Style associated with the Request
     */
    protected Style getStyle() {
        return this.getRequestStyle();
    }

    /**
     * Get a ValueMap of the properties for the content Resource's Design
     *
     * @return Style ValueMap
     */
    protected ValueMap getRequestDesignProperties() {
        if (this.requestDesignProperties == null) {
            this.requestDesignProperties = this.getRequestStyle();
        }

        return this.requestDesignProperties;
    }

    /**
     * <p>
     * Convenience method.
     * </p><p>*
     * Get a ValueMap of the properties for the content Resource's Design
     * </p>
     *
     * @return Style ValueMap
     */
    protected ValueMap getDesignProperties() {
        return this.getRequestDesignProperties();
    }

    /**
     * *************************************************************************
     * Lazy-loading Getters for Resource-oriented Data
     *************************************************************************
     */
    /**
     * <p>
     * Getter for the Component instance's Page object.
     * <p></p>
     * This represents the page resource who is the parent of the component
     * instance. This may not be the same as the getRequetsPage() Page.
     * <p></p>
     * http://dev.day.com/docs/en/cq/current/javadoc/com/day/cq/wcm/api/Page.html
     * <p>
     *
     * @return CQ Page the resource lives under
     */
    protected Page getResourcePage() {
        if (this.resourcePage == null) {
            if (this.getPageManager() != null) {
                this.resourcePage = this.getPageManager().getContainingPage(
                        this.getResource());
            }
        }

        return this.resourcePage;
    }

    /**
     * <p>
     * Getter for the Component Page's Design
     * <p></p>
     * This represents the Design for the page who is the parent of the resource.
     * This may not be the same as the getRequetsPage() Page.
     * <p></p>
     * http://dev.day.com/docs/en/cq/current/javadoc/index.html?com/day/cq/wcm/api/designer/class-use/Design.html
     * <p>
     *
     * @return Design for the
     */
    protected Design getResourceDesign() {
        if (this.resourceDesign == null) {
            if (this.getDesigner() != null) {
                if (this.getRequestPage() != null) {
                    this.resourceDesign = this.getDesigner().getDesign(this.getResourcePage());
                }
            }
        }

        return this.resourceDesign;
    }

    /**
     * Get the style associated with the Resource's Page
     *
     * @return
     */
    protected Style getResourceStyle() {
        if (this.resourceStyle == null) {
            if (this.getResourceDesign() != null && this.getComponentContext() != null) {
                this.resourceStyle = this.getResourceDesign().getStyle(this.getComponentContext().getCell());
            }
        }

        return this.resourceStyle;
    }

    /**
     * Get a ValueMap of properties for the content Resource
     *
     * @return
     */
    protected ValueMap getProperties() {
        if (this.resourceProperties == null) {
            this.resourceProperties = ResourceUtil.getValueMap(this.getResource());
        }

        return this.resourceProperties;
    }

    /**
     * Get the Design properties associated with the Resource's style (rather
     * than the Requests')
     *
     * @return
     */
    protected ValueMap getResourceDesignProperties() {
        if (this.resourceDesignProperties == null) {
            this.resourceDesignProperties = this.getResourceStyle();
        }

        return this.resourceDesignProperties;
    }

    /**
     * *************************************************************************
     * Lazy-loading Getters for Universal Helper and Utility objects
     *************************************************************************
     */
    /**
     * Getter for a CQ Designer for the Sling Request's user
     *
     * @return
     */
    protected Designer getDesigner() {
        if (this.designer == null) {
            this.designer = this.getResourceResolver().adaptTo(Designer.class);
        }

        return this.designer;
    }

    /**
     * Getter for a CQ PageManager for the Sling Request's user
     *
     * @return
     */
    protected PageManager getPageManager() {
        if (this.pageManager == null) {
            if (this.getResourceResolver() != null) {
                this.pageManager = this.getResourceResolver().adaptTo(
                        PageManager.class);
            }
        }

        return this.pageManager;
    }

    /**
     * Getter for a CQ Resource Resolver for the Sling Request's user
     *
     * @return
     */
    protected ResourceResolver getResourceResolver() {
        if (this.resourceResolver == null) {
            this.resourceResolver = this.getRequest().getResourceResolver();
        }

        return this.resourceResolver;
    }

    /**
     * Getter for a CQ Query Builder for the Sling Request's user
     *
     * @return
     */
    protected QueryBuilder getQueryBuilder() {
        if (this.queryBuilder == null) {
            this.queryBuilder = this.getResourceResolver().adaptTo(
                    QueryBuilder.class);
        }

        return this.queryBuilder;
    }

    /**
     * *************************************************************************
     * OSGi Service Exposure Methods
     *************************************************************************
     */
    /**
     * Exposes ability to get services from within the ActiveComponent class,
     * which itself is not a Sling Service
     *
     * @param <ServiceType>
     * @param type
     * @return the requested service or null.
     */
    protected <ServiceType> ServiceType getService(Class<ServiceType> type) {
        if (this.slingScriptHelper != null) {
            return slingScriptHelper.getService(type);
        } else {
            throw new IllegalStateException(
                    "SlingScriptHelper is NULL");
        }
    }

    /**
     * *************************************************************************
     * Property Getter Methods
     *************************************************************************
     */
    // Resource (Dialog) Properties

    /**
     *
     * @param <T> Data type of klass
     * @param key Property key
     * @param klass Expected return data type
     * @return The resource property
     */
    public <T> T getProperty(String key, Class<T> klass) {
        if (String.class.equals(klass)) {
            final String current = (String) getPropertyGeneric(this.getResource(), key, klass);
            return (T) this.Plugins.Diff.getDiff(this.getResource(), key, current);
        } else {
            return getPropertyGeneric(this.getResource(), key, klass);
        }
    }

    /**
     *
     * @param <T> Data type of defaultValue
     * @param key Property key
     * @param defaultValue
     * @return The resource property
     */
    public <T> T getProperty(String key, T defaultValue) {
        if (defaultValue instanceof String) {
            final String current = (String) getPropertyGeneric(this.getResource(), key, defaultValue);
            return (T) this.Plugins.Diff.getDiff(this.getResource(), key, current);
        } else {
            return getPropertyGeneric(this.getResource(), key, defaultValue);
        }
    }

    // Design Dialog Properties
    /**
     * Getter for Design associated with the Request
     *
     * @param <T> Data type of klass
     * @param key Property key
     * @param klass Expected return data type
     * @return The resource property
     */
    public <T> T getDesignProperty(String key, Class<T> klass) {
        return getPropertyGeneric(this.getRequestDesignProperties(), key, klass);
    }

    /**
     * Getter for Design associated with the Request
     *
     * @param <T> Data type
     * @param key Property key
     * @param defaultValue Default value if node property is blank
     * @return The resource property
     */
    public <T> T getDesignProperty(String key, T defaultValue) {
        return getPropertyGeneric(this.getRequestDesignProperties(), key, defaultValue);
    }

    // Any Resource
    /**
     * Getter for resource property
     *
     * @param <T> Data type of klass
     * @param resource The resource to get the value from
     * @param key Property key
     * @param klass Expected return data type
     * @return The resource property
     */
    public <T> T getProperty(Resource resource, String key, Class<T> klass) {
        if (resource == null) {
            return null;
        }
        return getPropertyGeneric(resource, key, klass);
    }

    /**
     *
     * @param <T> Data type of klass
     * @param resource The resource to get the value from
     * @param key Property key
     * @param defaultValue Default value if node property is blank
     * @return The resource property
     */
    public <T> T getProperty(Resource resource, String key, T defaultValue) {
        if (resource == null) {
            return null;
        }
        return getPropertyGeneric(resource, key, defaultValue);
    }

    /**
     *
     * @param <T> Data type of klass
     * @param resource The resource to get the value from
     * @param key Property key
     * @param klass Expected return data type
     * @return The resource property
     */
    private <T> T getPropertyGeneric(Resource resource, String key, Class<T> klass) {
        if (resource == null) {
            return null;
        }

        final ValueMap valueMap = resource.adaptTo(ValueMap.class);
        return this.getPropertyGeneric(valueMap, key, klass);
    }


    @SuppressWarnings("unchecked")
    private <T> T getPropertyGeneric(ValueMap valueMap, String key, Class<T> klass) {
        if (valueMap == null || klass == null) {
            return null;
        }

        if (!valueMap.containsKey(key)) {
            return null;
        }

        boolean isString = String.class.equals(klass);

        if (isString) {
            // Strip leading and trailing whitespace
            String strValue = StringUtils.strip((String) valueMap.get(key, klass));
            return (T) strValue;
        } else {
            return valueMap.get(key, klass);
        }
    }

    private <T> T getPropertyGeneric(Resource resource, String key, T defaultValue) {
        if (resource == null) {
            return null;
        }

        final ValueMap valueMap = resource.adaptTo(ValueMap.class);
        return this.getPropertyGeneric(valueMap, key, defaultValue);
    }

    @SuppressWarnings("unchecked")
    private <T> T getPropertyGeneric(ValueMap valueMap, String key, T defaultValue) {
        if (valueMap == null) {
            return defaultValue;
        }

        if (!valueMap.containsKey(key)) {
            return defaultValue;
        }

        boolean isString = false;
        if (defaultValue != null) {
            isString = String.class.equals(defaultValue.getClass());
        }

        if (isString) {
            // Strip leading and trailing whitespace
            String strValue = StringUtils.strip((String) valueMap.get(key, defaultValue));
            return (T) strValue;
        } else {
            return valueMap.get(key, defaultValue);
        }
    }
}