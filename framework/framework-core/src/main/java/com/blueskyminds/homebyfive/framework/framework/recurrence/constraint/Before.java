package com.blueskyminds.homebyfive.framework.framework.recurrence.constraint;

import com.blueskyminds.homebyfive.framework.framework.recurrence.Recurrence;

import javax.persistence.*;
import java.util.Date;
import java.util.Calendar;

/**
 * A constraint placed on a recurrence that defines that is it can only occur BEFORE a specified date (not
 * inclusive of the date)
 *
 * Date Started: 7/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("Before")
public class Before extends RecurrenceConstraint {

    /**
     * The date that the recurrence must occur before
     **/
    private Date dateBefore;

    // ------------------------------------------------------------------------------------------------------

    /** Create a constraint that the recurrence must occur before the specified date*/
    public Before(Date dateBefore, Recurrence recurrence) {
        super(recurrence);
        this.dateBefore = dateBefore;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a constraint that the recurrence must occur before the specified date*/
    public Before(Calendar dateBefore, Recurrence recurrence) {
        super(recurrence);
        this.dateBefore = dateBefore.getTime();
    }

    /** Default constructor for ORM */
    protected Before() {

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
        // if date is before the dateBefore constraint
        return date.before(dateBefore);
    }

    // ------------------------------------------------------------------------------------------------------

    @Temporal(TemporalType.DATE)
    @Column(name="DateBefore")
    public Date getDateBefore() {
        return dateBefore;
    }

    protected void setDateBefore(Date dateBefore) {
        this.dateBefore = dateBefore;
    }
}
