/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.activecq.api.plugins;

import com.activecq.api.stubs.BasePluginStub;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 *
 * @author david
 */
public class BasePluginTest {
    private SlingHttpServletRequest request;
    private SlingScriptHelper sling;

    public BasePluginTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        request = mock(SlingHttpServletRequest.class);
        sling = mock(SlingScriptHelper.class);
        SlingBindings bindings = mock(SlingBindings.class);

        when(request.getAttribute(SlingBindings.class.getName())).thenReturn(bindings);
        when(bindings.getSling()).thenReturn(sling);
        when(request.getResource()).thenReturn(mock(Resource.class));
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of enable method, of class BasePlugin.
     */
    @Test
    public void testEnable() {
        System.out.println("enable");
        BasePluginStub instance = new BasePluginStub(request);
        instance.enable();
        assertEquals(instance.isEnabled(), true);
    }

    /**
     * Test of disable method, of class BasePlugin.
     */
    @Test
    public void testDisable() {
        System.out.println("disable");
        BasePluginStub instance = new BasePluginStub(request);
        instance.disable();
        assertEquals(instance.isEnabled(), false);
    }

    /**
     * Test of isEnabled method, of class BasePlugin.
     */
    @Test
    public void testIsEnabled_Default() {
        System.out.println("isEnabled_Default");
        BasePluginStub instance = new BasePluginStub(request);
        boolean expResult = false;
        boolean result = instance.isEnabled();
        assertEquals(expResult, result);
    }

    /**
     * Test of getService method, of class BasePlugin.
     */
    /*@Test
    public void testGetService() {
        System.out.println("getService");
        BasePlugin instance = null;
        Object expResult = null;
        Object result = instance.getService(null);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
}
