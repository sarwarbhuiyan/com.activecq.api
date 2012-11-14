/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.activecq.api.plugins.options;

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
public class TextOptionsTest {

    public TextOptionsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of values method, of class TextOptions.
     */
    @Test
    public void testValues() {
        System.out.println("values");
        TextOptions[] expResult = {TextOptions.DIFF, TextOptions.RAW, TextOptions.XSS, TextOptions.I18N};
        TextOptions[] result = TextOptions.values();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of valueOf method, of class TextOptions.
     */
    @Test
    public void testValueOf() {
        System.out.println("valueOf");
        String name = "XSS";
        TextOptions expResult = TextOptions.XSS;
        TextOptions result = TextOptions.valueOf(name);
        assertEquals(expResult, result);
    }

    /**
     * Test of isIn method, of class TextOptions.
     */
    @Test
    public void testIsIn() {
        System.out.println("isIn");
        TextOptions[] options = {TextOptions.RAW, TextOptions.I18N};
        TextOptions instance = TextOptions.I18N;
        boolean expResult = true;
        boolean result = instance.isIn(options);
        assertEquals(expResult, result);
    }

    @Test
    public void testIsIn_False() {
        System.out.println("isIn (False)");
        TextOptions[] options = {TextOptions.RAW, TextOptions.I18N};
        TextOptions instance = TextOptions.DIFF;
        boolean expResult = false;
        boolean result = instance.isIn(options);
        assertEquals(expResult, result);
    }
}
