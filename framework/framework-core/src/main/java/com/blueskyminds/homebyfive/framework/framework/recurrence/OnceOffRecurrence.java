package com.blueskyminds.homebyfive.framework.framework.recurrence;

import com.blueskyminds.homebyfive.framework.framework.recurrence.strategy.OnSpecificDate;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;
import java.util.Date;

/**
 * Implementation of a Once-Off recurrence - the event occurs only once at a given date/time
 *
 * Date Started: 14/05/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
@DiscriminatorValue("OnceOff")
public class OnceOffRecurrence extends Recurrence {

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a once-only recurrence on the specified date
     *
     * @param date
     */
    public OnceOffRecurrence(Date date) {
        super(new OnSpecificDate(date));
    }

    /** Default constructor for ORM */
    protected OnceOffRecurrence() {

    }

    // ------------------------------------------------------------------------------------------------------
}
