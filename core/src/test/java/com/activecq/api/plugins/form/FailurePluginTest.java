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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author david
 */
public class FailurePluginTest {

    private ActiveFormStub form;

    public FailurePluginTest() {
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
     * Test of getRedirectPath method, of class FailurePlugin.
     */
    @Test
    public void testGetRedirectPath_ActiveForm_Page() throws Exception {
        System.out.println("getRedirectPath");

        form.setError("key-1", "this is a test error");

        Page page = mock(Page.class);
        Resource resource = mock(Resource.class);
        when(page.adaptTo(Resource.class)).thenReturn(resource);
        when(resource.getPath()).thenReturn("/content/testing/page");

        String expResult = "/content/testing/page.html?_cq_f=eyJrZXktMSI6InZhbHVlLTEiLCJrZXktMiI6InZhbHVlLTIifQ&_cq_e=eyJrZXktMSI6InRoaXMgaXMgYSB0ZXN0IGVycm9yIn0";
        String result = FailurePlugin.getRedirectPath(form, page);
        assertEquals(expResult, result);
    }

    /**
     * Test of getRedirectPath method, of class FailurePlugin.
     */
    @Test
    public void testGetRedirectPath_ActiveForm_Resource() throws Exception {
        System.out.println("getRedirectPath");
        form.setError("key-1", "this is a test error");

        Resource resource = mock(Resource.class);
        when(resource.getPath()).thenReturn("/content/testing/page");

        String expResult = "/content/testing/page.html?_cq_f=eyJrZXktMSI6InZhbHVlLTEiLCJrZXktMiI6InZhbHVlLTIifQ&_cq_e=eyJrZXktMSI6InRoaXMgaXMgYSB0ZXN0IGVycm9yIn0";
        String result = FailurePlugin.getRedirectPath(form, resource);
        assertEquals(expResult, result);
    }

    /**
     * Test of getRedirectPath method, of class FailurePlugin.
     */
    @Test
    public void testGetRedirectPath_ActiveForm_String() throws Exception {
        System.out.println("getRedirectPath");
        form.setError("key-1", "this is a test error");

        String path = "/content/testing/page.html";
        String expResult = "/content/testing/page.html?_cq_f=eyJrZXktMSI6InZhbHVlLTEiLCJrZXktMiI6InZhbHVlLTIifQ&_cq_e=eyJrZXktMSI6InRoaXMgaXMgYSB0ZXN0IGVycm9yIn0";
        String result = FailurePlugin.getRedirectPath(form, path);
        assertEquals(expResult, result);
    }

    /**
     * Test of getFormPage method, of class FailurePlugin.
     */
    /*@Test
    public void testGetFormPage_3args_1() {
        System.out.println("getFormPage");
        ActiveForm form = null;
        SlingScriptHelper sling = null;
        Page page = null;
        FailurePlugin.getFormPage(form, sling, page);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of getFormPage method, of class FailurePlugin.
     */
    /*
     * @Test
    public void testGetFormPage_3args_2() {
        System.out.println("getFormPage");
        ActiveForm form = null;
        SlingScriptHelper sling = null;
        Resource resource = null;
        FailurePlugin.getFormPage(form, sling, resource);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of getFormPage method, of class FailurePlugin.
     */
    /*
    @Test
    public void testGetFormPage_3args_3() {
        System.out.println("getFormPage");
        ActiveForm form = null;
        SlingScriptHelper sling = null;
        String path = "";
        FailurePlugin.getFormPage(form, sling, path);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
}
