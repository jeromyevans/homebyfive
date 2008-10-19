package com.blueskyminds.homebyfive.framework.core.tools.substitutions;

import java.util.Comparator;

/**
 * Order substitutions by their group and sequence number
 *
 * Date Started: 7/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class SubstitutionComparator implements Comparator<Substitution> {

    public int compare(Substitution o1, Substitution o2) {
        int result;
        if (o1.getGroupName() != null) {
            if (o2.getGroupName() != null) {
                result = 0;
            } else {
                result = 1;
            }
        } else {
            if (o2.getGroupName() != null) {
                result = -1;
            } else {
                result = 0;
            }
        }

        if (result == 0) {
            if (o1.getSequenceNo() != null) {
                if (o2.getSequenceNo() != null) {
                    result = o1.getSequenceNo().compareTo(o2.getSequenceNo());
                } else {
                    result = 1;
                }
            } else {
                if (o2.getSequenceNo() != null) {
                    result = -1;
                } else {
                    result = 0;
                }
            }
        }
        return result;
    }
}
