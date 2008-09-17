package com.blueskyminds.landmine.core.property.events;

import java.util.Comparator;

/**
 * Compares PremiseEvent by the date applied in descending order
 *
 * Date Started: 15/04/2008
 */

public class PremiseEventDateComparator implements Comparator<PremiseEvent> {

    public int compare(PremiseEvent o1, PremiseEvent o2) {
        if (o1.getDateApplied() != null) {
            if (o2.getDateApplied() != null) {
                return o1.getDateApplied().compareTo(o2.getDateApplied()) * -1;
            } else {
                return 1;
            }
        } else {
            if (o2.getDateApplied() != null) {
                return -1;
            } else {
                return 0;
            }
        }

    }
}