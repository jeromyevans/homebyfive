package com.blueskyminds.homebyfive.framework.framework.recurrence;

import com.blueskyminds.homebyfive.framework.framework.recurrence.strategy.NthHour;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;
import java.util.Date;

/**
 * A recurrence that occurs every n number of hours and x minutes
 *
 * Date Started: 18/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
@DiscriminatorValue("Hourly")
public class HourlyRecurrence extends Recurrence {

    /**
     * An event that recurs every N hours and X minutes
     */
    public HourlyRecurrence(Date epoch, int hours, int minutes) {
        super(new NthHour(epoch, hours, minutes));
    }

    /** Default constructor for ORM */
    protected HourlyRecurrence() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the HourlyRecurrence with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------
}
