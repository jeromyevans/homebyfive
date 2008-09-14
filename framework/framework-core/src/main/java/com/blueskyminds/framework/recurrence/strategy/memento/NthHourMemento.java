package com.blueskyminds.framework.recurrence.strategy.memento;

import java.util.Date;

/**
 * Records setings for the NthHour strategy
 *
 * Date Started: 18/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class NthHourMemento extends RecurrenceMemento {

    private Date epoch;
    private Integer hours;
    private Integer minutes;

    public NthHourMemento(Date epoch, Integer hours, Integer mintes) {
        this.epoch = epoch;
        this.hours = hours;
        this.minutes = mintes;
    }

    public NthHourMemento() {
    }

    // ------------------------------------------------------------------------------------------------------

    public Date getEpoch() {
        return epoch;
    }

    public void setEpoch(Date epoch) {
        this.epoch = epoch;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }
}
