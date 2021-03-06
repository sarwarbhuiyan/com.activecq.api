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
package com.activecq.api.utils;

import org.apache.sling.commons.testing.sling.MockSlingHttpServletRequest;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author david
 */
public class PathInfoUtilTest {
        
    public PathInfoUtilTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getQueryParam method, of class PathInfoUtil.
     */
    @Test
    public void testGetQueryParam_HttpServletRequest_String() {
        System.out.println("getQueryParam");
        MockSlingHttpServletRequest request = new MockSlingHttpServletRequest("/apple/macbookair", "show", "html", "simple", "ghz=2.4");        

        String key = "ghz";
        String expResult = "2.4";
        String result = PathInfoUtil.getQueryParam(request, key);
        //assertEquals(expResult, result);
    }

    /**
     * Test of getQueryParam method, of class PathInfoUtil.
     */
    @Test
    public void testGetQueryParam_withDefault() {
        System.out.println("getQueryParam");
        MockSlingHttpServletRequest request = new MockSlingHttpServletRequest("/apple/macbookair", "show", "html", "simple", "cpu=i7&ghz=2.4");        

        String key = "ghz";
        String expResult = "2.4";
        String result = PathInfoUtil.getQueryParam(request, key, "3");
        //assertEquals(expResult, result);

        key = "doesnt-exist";
        expResult = "3";
        result = PathInfoUtil.getQueryParam(request, key, "3");  
        //assertEquals(expResult, result);
        
    }

    /**
     * Test of getSelector method, of class PathInfoUtil.
     */
    @Test
    public void testGetSelector() {
        System.out.println("getSelector");
        MockSlingHttpServletRequest request = new MockSlingHttpServletRequest("/apple/macbookair", "show.test", "html", "simple", "cpu=i7&ghz=2.4");
        
        String expResult = "show";
        String result = PathInfoUtil.getSelector(request, 0);
        assertEquals(expResult, result);
        
        expResult = "test";
        result = PathInfoUtil.getSelector(request, 1);
        assertEquals(expResult, result);    
        
        result = PathInfoUtil.getSelector(request, -1);
        assertNull(result);    

        result = PathInfoUtil.getSelector(request, 10);
        assertNull(result);    
    }

    /**
     * Test of getSuffixSegment method, of class PathInfoUtil.
     */
    @Test
    public void testGetSuffixSegment() {
        System.out.println("getSuffixSegment");
        MockSlingHttpServletRequest request = new MockSlingHttpServletRequest("/apple/macbookair", "show.test", "html", "super/simple", "cpu=i7&ghz=2.4");

        String expResult = "super";
        String result = PathInfoUtil.getSuffixSegment(request, 0);
        assertEquals(expResult, result);
        
        expResult = "simple";
        result = PathInfoUtil.getSuffixSegment(request, 1);
        assertEquals(expResult, result);

        result = PathInfoUtil.getSuffixSegment(request, -1);
        assertNull(result);

        result = PathInfoUtil.getSuffixSegment(request, 10);
        assertNull(result);
    }

    /**
     * Test of getSuffix method, of class PathInfoUtil.
     */
    @Test
    public void testGetSuffix() {
        System.out.println("getSuffix");
        MockSlingHttpServletRequest request = new MockSlingHttpServletRequest("/apple/macbookair", "show.test", "html", "super/simple", "cpu=i7&ghz=2.4");
        
        String expResult = "super/simple";
        String result = PathInfoUtil.getSuffix(request);
        assertEquals(expResult, result);
    }
}
