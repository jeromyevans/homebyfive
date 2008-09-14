package com.blueskyminds.framework.recurrence.constraint;

import com.blueskyminds.framework.recurrence.Recurrence;

import javax.persistence.*;
import java.util.Date;
import java.util.Calendar;

/**
 * A constraint placed on a recurrence that defines that is does not occur after a specified date
 *
 * Date Started: 7/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("NoLaterThan")
public class NoLaterThan extends RecurrenceConstraint {

    /**
     * The latest date that the recurrence is allowed to occur on
     **/
    private Date dateToComplete;

    // ------------------------------------------------------------------------------------------------------

    /** Create a constraint that the recurrence is not occur before the specified date */
    public NoLaterThan(Date dateToComplete, Recurrence recurrence) {
        super(recurrence);
        this.dateToComplete = dateToComplete;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a constraint that the recurrence is not occur before the specified date */
    public NoLaterThan(Calendar dateToComplete, Recurrence recurrence) {
        super(recurrence);
        this.dateToComplete = dateToComplete.getTime();
    }

    /** Default constructor for ORM */
    protected NoLaterThan() {

    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Determines if the contraint affects the specified date and returns false if the date is outside
     * the range permitted by the constraint.
     *
     * @param date
     * @return true if the date is okay
     */
    @Transient
    public boolean isAcceptable(Date date) {
        // if date to complete is after or on the date specified (ie. !before)
        return !(dateToComplete.before(date));
    }

    // ------------------------------------------------------------------------------------------------------

    @Temporal(TemporalType.DATE)
    public Date getDateToComplete() {
        return dateToComplete;
    }

    protected void setDateToComplete(Date dateToComplete) {
        this.dateToComplete = dateToComplete;
    }

    // ------------------------------------------------------------------------------------------------------
}
