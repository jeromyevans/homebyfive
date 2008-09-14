package com.blueskyminds.framework.recurrence.constraint;

import com.blueskyminds.framework.recurrence.Recurrence;

import javax.persistence.*;
import java.util.Date;
import java.util.Calendar;

/**
 * A constraint placed on a recurrence that defines that is does not occur before a specified start
 *
 * Date Started: 7/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("NotEarlierThan")
public class NoEarlierThan extends RecurrenceConstraint {

    /**
     * The date that the recurrence is allowed to commence from
     **/
    private Date dateToCommence;

    // ------------------------------------------------------------------------------------------------------

    /** Create a constraint that the recurrence is not occur before the specified date */
    public NoEarlierThan(Date dateToCommence, Recurrence recurrence) {
        super(recurrence);
        this.dateToCommence = dateToCommence;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a constraint that the recurrence is not occur before the specified date */
    public NoEarlierThan(Calendar dateToCommence, Recurrence recurrence) {
        super(recurrence);
        this.dateToCommence = dateToCommence.getTime();
    }

    /** Default constructor for ORM */
    protected NoEarlierThan() {

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
        // date to commence is before or on the date specified (ie. !after)
        return !(dateToCommence.after(date));
    }

    // ------------------------------------------------------------------------------------------------------

    @Temporal(TemporalType.DATE)
    @Column(name="DateToCommence")
    public Date getDateToCommence() {
        return dateToCommence;
    }

    protected void setDateToCommence(Date dateToCommence) {
        this.dateToCommence = dateToCommence;
    }
}
