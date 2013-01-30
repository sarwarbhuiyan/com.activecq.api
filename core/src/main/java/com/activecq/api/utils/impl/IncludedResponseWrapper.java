package com.activecq.api.utils.impl;

import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.wrappers.SlingHttpServletResponseWrapper;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * User: david
 */
public class IncludedResponseWrapper extends SlingHttpServletResponseWrapper {
    private StringWriter stringWriter = new StringWriter();
    private PrintWriter printWriter = new PrintWriter(stringWriter);

    public IncludedResponseWrapper(SlingHttpServletResponse slingHttpServletResponse) {
        super(slingHttpServletResponse);
    }

    public PrintWriter getWriter() {
        return printWriter;
    }

    public String getString() {
        return stringWriter.toString();
    }

    public void clearWriter() {
        printWriter.close();
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }
}