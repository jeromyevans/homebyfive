package com.blueskyminds.framework.recurrence;

import com.blueskyminds.framework.AbstractDomainObject;

import java.util.Date;

/**
 * A simple class for reporting the occurrence of an event
 *
 * Date Started: 6/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class Occurrence extends AbstractDomainObject {

    // ------------------------------------------------------------------------------------------------------

    /**
     * The date of the occurrence
     **/
    private Date dateOfOccurrence;       

    // ------------------------------------------------------------------------------------------------------

    /** Create a new occurrence on the specified date */
    public Occurrence(Date dateOfOccurrence) {
        this.dateOfOccurrence = dateOfOccurrence;
    }

    // ------------------------------------------------------------------------------------------------------
}
