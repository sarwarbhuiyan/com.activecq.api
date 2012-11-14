/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.activecq.api.utils;

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
public class EncodingUtilTest {

    public EncodingUtilTest() {
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
     * Test of decode method, of class EncodingUtil.
     */
    @Test
    public void testDecode() throws Exception {
        System.out.println("decode");
        final String expResult = "this is base64 and URL encoded data";
        String encodedData = EncodingUtil.encode(expResult);
        String result = EncodingUtil.decode(encodedData);
        assertEquals(expResult, result);
    }

    /**
     * Test of encode method, of class EncodingUtil.
     */
    @Test
    public void testEncode() throws Exception {
        System.out.println("encode");
        final String unencodedData = "this is base64 and URL encoded data";
        String expResult = "dGhpcyBpcyBiYXNlNjQgYW5kIFVSTCBlbmNvZGVkIGRhdGE";
        String result = EncodingUtil.encode(unencodedData);
        assertEquals(expResult, result);
    }
}
