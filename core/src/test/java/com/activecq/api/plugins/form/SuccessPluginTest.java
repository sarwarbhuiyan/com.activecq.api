/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.activecq.api.plugins.form;

import com.activecq.api.stubs.ActiveFormStub;
import com.day.cq.wcm.api.Page;
import java.util.HashMap;
import java.util.Map;
import org.apache.sling.api.resource.Resource;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.*;


/**
 *
 * @author david
 */
public class SuccessPluginTest {
    private static final String message = "You are successful";

    private ActiveFormStub form;

    public SuccessPluginTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key-1", "value-1");
        map.put("key-2", "value-2");
        form = new ActiveFormStub(map);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getRedirectPath method, of class SuccessPlugin.
     */
    @Test
    public void testGetRedirectPath_3args_1() throws Exception {
        System.out.println("getRedirectPath");

        Page page = mock(Page.class);
        Resource resource = mock(Resource.class);
        when(page.adaptTo(Resource.class)).thenReturn(resource);
        when(resource.getPath()).thenReturn("/content/testing/success");

        String expResult = "/content/testing/success.html?_cq_fs=You are successful";
        String result = SuccessPlugin.getRedirectPath(page, message);
        assertEquals(expResult, result);
    }

    /**
     * Test of getRedirectPath method, of class SuccessPlugin.
     */
    @Test
    public void testGetRedirectPath_3args_2() throws Exception {
        System.out.println("getRedirectPath");

        Resource resource = mock(Resource.class);
        when(resource.getPath()).thenReturn("/content/testing/success");

        String expResult = "/content/testing/success.html?_cq_fs=You are successful";
        String result = SuccessPlugin.getRedirectPath(resource, message);
        assertEquals(expResult, result);
    }

    /**
     * Test of getRedirectPath method, of class SuccessPlugin.
     */
    @Test
    public void testGetRedirectPath_3args_3() throws Exception {
        System.out.println("getRedirectPath");

        String path = "/content/testing/success.html";
        String expResult = "/content/testing/success.html?_cq_fs=You are successful";
        String result = SuccessPlugin.getRedirectPath(path, message);
        assertEquals(expResult, result);
    }
}
