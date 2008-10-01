package com.blueskyminds.homebyfive.framework.framework.persistence.query;

import javax.persistence.TemporalType;
import java.util.Calendar;
import java.util.Date;

/**
 * A generic temporal parameter for a AbstractQuery
 *
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class QueryTemporalParameter extends QueryParameter {

    private Date date;
    private Calendar calendar;
    private TemporalType temporalType;
    private boolean isDate;

    // ------------------------------------------------------------------------------------------------------

    public QueryTemporalParameter(String name, Date date, TemporalType temporalType) {
        super(name);
        this.date = date;
        this.temporalType = temporalType;
        isDate = true;
    }

    public QueryTemporalParameter(String name, Calendar calendar, TemporalType temporalType) {
        super(name);
        this.calendar = calendar;
        this.temporalType = temporalType;
        isDate = false;
    }

    // ------------------------------------------------------------------------------------------------------

    public QueryTemporalParameter(int index, Date date, TemporalType temporalType) {
        super(index);
        this.date = date;
        this.temporalType = temporalType;
        isDate = true;
    }

    public QueryTemporalParameter(int index, Calendar calendar, TemporalType temporalType) {
        super(index);
        this.calendar = calendar;
        this.temporalType = temporalType;
        isDate = false;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the QueryTemporalParameter with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    public Date getDate() {
        return date;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public TemporalType getTemporalType() {
        return temporalType;
    }

    public boolean isDate() {
        return isDate;
    }

}
