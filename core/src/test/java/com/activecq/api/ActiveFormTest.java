/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.activecq.api;

import com.activecq.api.stubs.ActiveFormStub;
import java.util.HashMap;
import java.util.Map;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author david
 */
public class ActiveFormTest {

    private ActiveFormStub instance;
    private Map<String, Object> map;

    public ActiveFormTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        map = new HashMap<String, Object>();
        map.put("text", "this is text");
        map.put("richtext", "<strong>this is rich text</strong>");
        map.put("long", new Long(999999999));
        map.put("double", new Double(9.99));
        map.put("boolean", true);
        map.put("empty1", null);
        map.put("empty2", "");

        instance = new ActiveFormStub(map);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of has method, of class ActiveForm.
     */
    @Test
    public void testHas_true() {
        System.out.println("has");

        String key = "richtext";
        boolean expResult = true;
        boolean result = instance.has(key);
        assertEquals(expResult, result);
    }

    @Test
    public void testHas_false() {
        System.out.println("has");

        String key = "doesnt-exist";
        boolean expResult = false;
        boolean result = instance.has(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of isEmpty method, of class ActiveForm.
     */
    @Test
    public void testIsEmpty_false() {
        System.out.println("isEmpty");

        String key = "richtext";
        boolean expResult = false;
        boolean result = instance.isEmpty(key);
        assertEquals(expResult, result);
    }


    @Test
    public void testIsEmpty_true1() {
        System.out.println("isEmpty");

        String key = "empty1";
        boolean expResult = true;
        boolean result = instance.isEmpty(key);
        assertEquals(expResult, result);
    }

    @Test
    public void testIsEmpty_true2() {
        System.out.println("isEmpty");

        String key = "empty2";
        boolean expResult = true;
        boolean result = instance.isEmpty(key);
        assertEquals(expResult, result);
    }

    @Test
    public void testIsEmpty_true3() {
        System.out.println("isEmpty");

        String key = "doesnt-exist";
        boolean expResult = true;
        boolean result = instance.isEmpty(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of get method, of class ActiveForm.
     */
    @Test
    public void testGet_String() {
        System.out.println("get");
        String key = "text";
        String expResult = "this is text";
        String result = instance.get(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of get method, of class ActiveForm.
     */
    @Test
    public void testGet_String_GenericType() {
        System.out.println("get");
        String key = "text";
        Object defaultValue = "default text";

        Object expResult = "this is text";
        Object result = instance.get(key, defaultValue);
        assertEquals(expResult, result);
    }


    /**
     * Test of get method, of class ActiveForm.
     */
    @Test
    public void testGet_String_GenericType_2() {
        System.out.println("get");
        String key = "doesnt-exist";
        Object defaultValue = "default text";

        Object expResult = defaultValue;
        Object result = instance.get(key, defaultValue);
        assertEquals(expResult, result);
    }

    /**
     * Test of get method, of class ActiveForm.
     */
    @Test
    public void testGet_String_Class() {
        System.out.println("get");
        String key = "text";
        Object expResult = "this is text";
        Object result = instance.get(key, String.class);
        assertEquals(expResult, result);
    }

    /**
     * Test of set method, of class ActiveForm.
     */
    @Test
    public void testSet_String_Object() {
        System.out.println("set");
        String key = "text";
        String value = "this is NEW text";
        instance.set(key, value);

        assertEquals(value, instance.get(key, String.class));
    }

    /**
     * Test of set method, of class ActiveForm.
     */
    @Test
    public void testSet_String_boolean() {
        System.out.println("set");
        String key = "boolean";
        boolean value = true;
        instance.set(key, value);

        assertEquals(value, instance.get(key, Boolean.class));
    }

    /**
     * Test of set method, of class ActiveForm.
     */
    @Test
    public void testSet_String_double() {
        System.out.println("set");
        String key = "double";
        Double value = new Double(8.88);
        instance.set(key, value);

        assertEquals(value, instance.get(key, Double.class));
    }

    /**
     * Test of set method, of class ActiveForm.
     */
    @Test
    public void testSet_String_long() {
        System.out.println("set");
        String key = "new-long";
        Long expValue = new Long(5555);

        instance.set(key, expValue);
        assertEquals(expValue, instance.get(key, Long.class));
    }

    /**
     * Test of set method, of class ActiveForm.
     */
    @Test
    public void testSet_String_int() {
        System.out.println("set");
        String key = "new-int";
        int value = 101;
        instance.set(key, value);
        assertEquals(new Integer(value), instance.get(key, value));
    }

    /**
     * Test of setAll method, of class ActiveForm.
     */
    @Test
    public void testSetAll() {
        System.out.println("setAll");
        Map<String, String> newMap = new HashMap<String, String>();
        newMap.put("newkey1", "new value 1");
        newMap.put("new key 2", "new value 2");
        instance.setAll(newMap);

        assertEquals("this is text", instance.get("text", String.class));
        assertEquals("<strong>this is rich text</strong>", instance.get("richtext", String.class));
        assertEquals(new Long(999999999), instance.get("long", Long.class));
        assertEquals(new Double(9.99), instance.get("double", Double.class));
        assertEquals(true, instance.get("boolean", Boolean.class));

        assertEquals("new value 1", instance.get("newkey1", String.class));
        assertEquals("new value 2", instance.get("new key 2", String.class));
    }

    /**
     * Test of resetTo method, of class ActiveForm.
     */
    @Test
    public void testResetTo() {
        System.out.println("resetTo");
        Map<String, String> newMap = new HashMap<String, String>();
        newMap.put("newkey1", "new value 1");
        newMap.put("new key 2", "new value 2");

        instance.resetTo(newMap);

        assertFalse(instance.has("text"));
        assertFalse(instance.has("boolean"));
        assertEquals("new value 1", instance.get("newkey1", String.class));
        assertEquals("new value 2", instance.get("new key 2", String.class));
    }

    /**
     * Test of reset method, of class ActiveForm.
     */
    @Test
    public void testReset() {
        System.out.println("reset");
        assertTrue(instance.hasData());
        instance.reset();
        assertFalse(instance.hasData());
    }

    /**
     * Test of hasData method, of class ActiveForm.
     */
    @Test
    public void testHasData() {
        System.out.println("hasData");
        boolean expResult = true;
        boolean result = instance.hasData();
        assertEquals(expResult, result);
    }

    /**
     * Test of toJSON method, of class ActiveForm.
     */
    @Test
    public void testToJSON() throws JSONException {
        System.out.println("toJSON");
        JSONObject expResult = new JSONObject(this.map);
        JSONObject result = instance.toJSON();

        assertEquals(expResult.get("text"), result.get("text"));
        assertEquals(expResult.get("richtext"), result.get("richtext"));
        assertEquals(expResult.get("long"), result.get("long"));
        assertEquals(expResult.get("boolean"), result.get("boolean"));
        assertEquals(expResult.has("empty1"), result.has("empty1"));
        assertEquals(expResult.get("empty2"), result.get("empty2"));
    }

    /**
     * Test of toMap method, of class ActiveForm.
     */
    @Test
    public void testToMap() {
        System.out.println("toMap");
        Map expResult = this.map;
        Map result = instance.toMap();
        assertEquals(expResult, result);
    }

    /**
     * Test of getErrors method, of class ActiveForm.
     */
    @Test
    public void testGetErrors() {
        System.out.println("getErrors");

        ActiveErrors result = instance.getErrors();
        assertTrue(result.isEmpty());
    }

    /**
     * Test of hasErrors method, of class ActiveForm.
     */
    @Test
    public void testHasErrors() {
        System.out.println("hasErrors");

        assertFalse(instance.hasErrors());
        instance.setError("text", "error!");
        assertTrue(instance.hasErrors());
    }

    /**
     * Test of hasError method, of class ActiveForm.
     */
    @Test
    public void testHasError() {
        System.out.println("hasError");
        assertFalse(instance.hasError("text"));
        instance.setError("text", "error!");
        assertTrue(instance.hasError("text"));
    }

    /**
     * Test of getError method, of class ActiveForm.
     */
    @Test
    public void testGetError() {
        System.out.println("getError");
        String expResult = "error!";
        instance.setError("text", expResult);
        assertEquals(expResult, instance.getError("text"));
    }

    /**
     * Test of setError method, of class ActiveForm.
     */
    @Test
    public void testSetError_String_String() {
        System.out.println("setError");
        String key = "text";
        String message = "error message";
        instance.setError(key, message);
        assertEquals(message, instance.getError(key));
    }

    /**
     * Test of setError method, of class ActiveForm.
     */
    @Test
    public void testSetError_String() {
        System.out.println("setError");
        String key = "new-key";

        assertFalse(instance.hasError(key));
        instance.setError(key);
        assertTrue(instance.hasError(key));
    }

    /**
     * Test of getQueryParameters method, of class ActiveForm.
     */
    @Test
    public void testGetQueryParameters() throws Exception {
        System.out.println("getQueryParameters");
        String expResult = "_cq_f=eyJ0ZXh0IjoidGhpcyBpcyB0ZXh0IiwiZW1wdHkxIjpudWxsLCJlbXB0eTIiOiIiLCJyaWNodGV4dCI6IjxzdHJvbmc-dGhpcyBpcyByaWNoIHRleHQ8XC9zdHJvbmc-IiwiYm9vbGVhbiI6dHJ1ZSwibG9uZyI6OTk5OTk5OTk5LCJkb3VibGUiOjkuOTl9&_cq_e=e30";
        String result = ActiveForm.getQueryParameters(this.instance);
        assertEquals(expResult, result);
    }
}