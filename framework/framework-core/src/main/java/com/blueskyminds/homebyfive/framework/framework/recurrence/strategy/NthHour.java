package com.blueskyminds.homebyfive.framework.framework.recurrence.strategy;

import com.blueskyminds.homebyfive.framework.framework.recurrence.strategy.memento.NthHourMemento;
import com.blueskyminds.homebyfive.framework.framework.datetime.DateTools;

import java.util.Date;

/**
 * A RecurrenceStrategy for an event that occurs every Nth hour
 *
 * Date Started: 18/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class NthHour extends RecurrenceStrategy {

    public NthHour(Date epoch, int hours, int minutes) {
        super(new NthHourMemento(epoch, hours, minutes));
    }

    public NthHour() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the NthHour with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns the next occurrence for this strategy AFTER the given TIMESTAMP
     */
    public Date nextOccurrence(Date timestamp) {
        long firstOccurrence = getEpoch().getTime();
        long period = DateTools.toMilliseconds(getHours(), getMinutes(), 0);
        long seeking = timestamp.getTime();
        long current = firstOccurrence;
        boolean found = false;
        Date result = null;
        
        if (seeking >= firstOccurrence) {

            int expectedPeriods = (int) ((seeking - firstOccurrence) / period);

            current = firstOccurrence + (expectedPeriods * period);

            // iterate from this point until we reach the timestamp (1 or 2 iterations)
            while (!found) {
                if (current <= seeking) {
                    current += period;
                } else {
                    found = true;
                }

            }
        }

        if (found) {
            result = new Date(current);
        }

        return result;
    }

    private Date getEpoch() {
        return ((NthHourMemento) getMemento()).getEpoch();
    }

    private int getHours() {
        return ((NthHourMemento) getMemento()).getHours();
    }

    private int getMinutes() {
        return ((NthHourMemento) getMemento()).getMinutes();
    }

    /**
     * Always returns true
     */
    public boolean occursOn(Date date) {
        return true;
    }
}
