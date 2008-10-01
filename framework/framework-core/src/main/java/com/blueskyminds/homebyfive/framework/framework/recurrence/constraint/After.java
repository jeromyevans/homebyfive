package com.blueskyminds.homebyfive.framework.framework.recurrence.constraint;

import com.blueskyminds.homebyfive.framework.framework.recurrence.Recurrence;

import javax.persistence.*;
import java.util.Date;
import java.util.Calendar;

/**
 * A constraint placed on a recurrence that defines that is it can only occur AFTER a specified date (not
 * inclusive of the date)
 *
 * Date Started: 7/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("After")
public class After extends RecurrenceConstraint {

    /**
     * The date that the recurrence must occur after
     **/
    private Date dateAfter;

    // ------------------------------------------------------------------------------------------------------

    /** Create a constraint that the recurrence must occur after the specified date (non inclusive)*/
    public After(Date dateAfter, Recurrence recurrence) {
        super(recurrence);
        this.dateAfter = dateAfter;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a constraint that the recurrence must occur after the specified date (non inclusive)*/
    public After(Calendar dateAfter, Recurrence recurrence) {
        super(recurrence);
        this.dateAfter = dateAfter.getTime();
    }

    /** Default constructor for ORM */
    protected After() {

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
        // if date is after the dateAfter constraint
        return date.after(dateAfter);
    }

    // ------------------------------------------------------------------------------------------------------

    @Temporal(TemporalType.DATE)
    @Column(name="DateAfter")
    public Date getDateAfter() {
        return dateAfter;
    }

    protected void setDateAfter(Date dateAfter) {
        this.dateAfter = dateAfter;
    }
}
