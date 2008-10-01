package com.blueskyminds.homebyfive.framework.core.recurrence.constraint;

import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;
import com.blueskyminds.homebyfive.framework.core.recurrence.Recurrence;

import javax.persistence.*;
import java.util.Date;

/**
 * A hard constraint that can be applied to a recurrence to limit the period that it's valid.
 *
 * Simple examples are start and end dates.
 *
 * Date Started: 7/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ConstraintType", discriminatorType = DiscriminatorType.STRING)
public abstract class RecurrenceConstraint extends AbstractDomainObject {

    /** The recurrence that this constraint instance is applied to */
    private Recurrence recurrence;

    // ------------------------------------------------------------------------------------------------------

    protected RecurrenceConstraint(Recurrence recurrence) {
        this.recurrence = recurrence;
    }

    /** Default constructor for ORM */
    protected RecurrenceConstraint() {

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
    public abstract boolean isAcceptable(Date date);

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    /** Get the recurrence instance that this constraint is applied to.  This is necessary for ORM */
    @OneToOne
    @JoinColumn(name="RecurrenceId")
    public Recurrence getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(Recurrence recurrence) {
        this.recurrence = recurrence;
    }

    // ------------------------------------------------------------------------------------------------------
}
