/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.activecq.api.utils.comparators;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.osgi.framework.ServiceReference;
import static org.mockito.Mockito.*;
import org.osgi.framework.Constants;

/**
 *
 * @author david
 */
public class OsgiServiceRankingComparatorTest {

    public OsgiServiceRankingComparatorTest() {
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
     * Test of compare method, of class OsgiServiceRankingComparator.
     */
    @Test
    public void testCompare_lessThan() {
        System.out.println("compare - less than");
        ServiceReference ref1 = mock(ServiceReference.class);
        ServiceReference ref2 = mock(ServiceReference.class);

        when(ref1.getProperty(Constants.SERVICE_RANKING)).thenReturn(1);
        when(ref2.getProperty(Constants.SERVICE_RANKING)).thenReturn(2);

        OsgiServiceRankingComparator instance = new OsgiServiceRankingComparator();
        int expResult = -1;
        int result = instance.compare(ref1, ref2);
        assertEquals(expResult, result);
    }

    @Test
    public void testCompare_equals() {
        System.out.println("compare - equals");
        ServiceReference ref1 = mock(ServiceReference.class);
        ServiceReference ref2 = mock(ServiceReference.class);

        when(ref1.getProperty(Constants.SERVICE_RANKING)).thenReturn(5);
        when(ref2.getProperty(Constants.SERVICE_RANKING)).thenReturn(5);

        OsgiServiceRankingComparator instance = new OsgiServiceRankingComparator();
        int expResult = 0;
        int result = instance.compare(ref1, ref2);
        assertEquals(expResult, result);
    }

    public void testCompare_greaterThan() {
        System.out.println("compare - greater than");
        ServiceReference ref1 = mock(ServiceReference.class);
        ServiceReference ref2 = mock(ServiceReference.class);

        when(ref1.getProperty(Constants.SERVICE_RANKING)).thenReturn(2);
        when(ref2.getProperty(Constants.SERVICE_RANKING)).thenReturn(1);

        OsgiServiceRankingComparator instance = new OsgiServiceRankingComparator();
        int expResult = 1;
        int result = instance.compare(ref1, ref2);
        assertEquals(expResult, result);
    }

    public void testCompare_bothNulls() {
        System.out.println("compare - both null refs");
        ServiceReference ref1 = mock(ServiceReference.class);
        ServiceReference ref2 = mock(ServiceReference.class);

        when(ref1.getProperty(Constants.SERVICE_RANKING)).thenReturn(null);
        when(ref2.getProperty(Constants.SERVICE_RANKING)).thenReturn(null);

        OsgiServiceRankingComparator instance = new OsgiServiceRankingComparator();
        int expResult = 0;
        int result = instance.compare(ref1, ref2);
        assertEquals(expResult, result);
    }

    public void testCompare_ref1Null() {
        System.out.println("compare - ref1 null");
        ServiceReference ref1 = mock(ServiceReference.class);
        ServiceReference ref2 = mock(ServiceReference.class);

        when(ref1.getProperty(Constants.SERVICE_RANKING)).thenReturn(null);
        when(ref2.getProperty(Constants.SERVICE_RANKING)).thenReturn(20);

        OsgiServiceRankingComparator instance = new OsgiServiceRankingComparator();
        int expResult = -1;
        int result = instance.compare(ref1, ref2);
        assertEquals(expResult, result);
    }

    public void testCompare_ref2Null() {
        System.out.println("compare - ref2 null");
        ServiceReference ref1 = mock(ServiceReference.class);
        ServiceReference ref2 = mock(ServiceReference.class);

        when(ref1.getProperty(Constants.SERVICE_RANKING)).thenReturn(21);
        when(ref2.getProperty(Constants.SERVICE_RANKING)).thenReturn(null);

        OsgiServiceRankingComparator instance = new OsgiServiceRankingComparator();
        int expResult = 1;
        int result = instance.compare(ref1, ref2);
        assertEquals(expResult, result);
    }
}
