/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.activecq.api.utils.comparators;

import java.util.Comparator;
import org.apache.sling.commons.osgi.OsgiUtil;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

/**
 * Comparator that sorts OSGi ServiceReferences by Ranking, Ascending
 *
 * @author david
 */
public class OsgiServiceRankingComparator implements Comparator<ServiceReference> {

    @Override
    public int compare(ServiceReference ref1, ServiceReference ref2) {
        if (ref1 == null && ref2 == null) {
            return 0;
        }
        if (ref1 != null && ref2 == null) {
            // a null ref is considered to be smaller than any non-null ref
            return 1;
        }
        if (ref1 == null && ref2 != null) {
            // a null ref is considered to be smaller than any non-null ref
            return -1;
        }

        final int ranking1 = OsgiUtil.toInteger(ref1.getProperty(Constants.SERVICE_RANKING), 0);
        final int ranking2 = OsgiUtil.toInteger(ref2.getProperty(Constants.SERVICE_RANKING), 0);

        if (ranking1 < ranking2) {
            return -1;
        } else if (ranking1 > ranking2) {
            return 1;
        }

        return 0;
    }
}