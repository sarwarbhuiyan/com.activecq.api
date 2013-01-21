package com.activecq.api.utils;

import com.activecq.api.utils.impl.IncludedResponseWrapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: david
 */
public class ResourceDataUtil {
    private static final Logger log = LoggerFactory.getLogger(ResourceDataUtil.class);

    public static final String ENCODING_UTF_8 = "UTF-8";

    public static String getIncludeAsString(String path, SlingHttpServletRequest slingRequest,
                                  SlingHttpServletResponse slingResponse) {
        IncludedResponseWrapper responseWrapper = null;
        try {
            responseWrapper = new IncludedResponseWrapper(slingResponse);
            final RequestDispatcher requestDispatcher = slingRequest.getRequestDispatcher(path);

            requestDispatcher.include(slingRequest, responseWrapper);

            return StringUtils.stripToNull(responseWrapper.getString());
        } catch (Exception ex) {
            log.error("Error creating the String representation for: {}", path ,ex);
        } finally {
            if(responseWrapper != null) {
                responseWrapper.clearWriter();
            }
        }

        return null;
    }

    public static InputStream getFileAsInputStream(String path, ResourceResolver resourceResolver) throws RepositoryException {
        return getFileAsInputStream(resourceResolver.resolve(path));
    }

    public static InputStream getFileAsInputStream(Resource resource) throws RepositoryException {
        final Node node = resource.adaptTo(Node.class);
        final Node jcrContent = node.getNode(JcrConstants.JCR_CONTENT);
        return jcrContent.getProperty(JcrConstants.JCR_DATA).getBinary().getStream();
    }

    public static String getFileAsString(String path, ResourceResolver resourceResolver) throws RepositoryException, IOException {
        return getFileAsString(path, resourceResolver, ENCODING_UTF_8);
    }

    public static String getFileAsString(String path, ResourceResolver resourceResolver, String encoding) throws RepositoryException, IOException {
        return getFileAsString(resourceResolver.resolve(path));
    }

    public static String getFileAsString(Resource resource) throws RepositoryException, IOException {
        return getFileAsString(resource, ENCODING_UTF_8);
    }

    public static String getFileAsString(Resource resource, String encoding) throws RepositoryException, IOException {
        final InputStream inputStream = getFileAsInputStream(resource);
        return IOUtils.toString(inputStream, encoding);
    }
}