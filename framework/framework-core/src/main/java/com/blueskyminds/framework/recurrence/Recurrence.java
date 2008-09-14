package com.blueskyminds.framework.recurrence;

import com.blueskyminds.framework.datetime.Interval;
import com.blueskyminds.framework.memento.CaretakerDomainObject;
import com.blueskyminds.framework.recurrence.constraint.RecurrenceConstraint;
import com.blueskyminds.framework.recurrence.strategy.RecurrenceStrategy;
import com.blueskyminds.framework.recurrence.strategy.memento.RecurrenceMemento;

import javax.persistence.*;
import java.io.PrintStream;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Generalised representation of a recurring event
 *
 * Date Started: 6/05/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "RecurrenceType", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("Abstract")
public class Recurrence extends CaretakerDomainObject {

    /**
     * List of constraints applied to this recurrence
     */
    private List<RecurrenceConstraint> constraints;

    // ------------------------------------------------------------------------------------------------------

    /** Default constructor for ORM */
    protected Recurrence(RecurrenceStrategy recurrenceStrategy) {
        super(recurrenceStrategy, recurrenceStrategy.getMemento());
        init();
    }

    /** Default constructor for ORM */
    public Recurrence(RecurrenceStrategy recurrenceStrategy, RecurrenceMemento memento) {
        super(recurrenceStrategy, memento);
        init();
    }

    /**
     * Default constructor for ORM
     */
    protected Recurrence() {
    }

    // ------------------------------------------------------------------------------------------------------

    public static Recurrence newInstance(RecurrenceStrategy recurrenceStrategy) {
        return new Recurrence(recurrenceStrategy);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the recurrence with default paramaters */
    private void init() {
        constraints = new LinkedList<RecurrenceConstraint>();
        //exceptions = new LinkedList<RecurrenceException();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Add a constraint to this recurrence pattern
     * @param constraint
     */
    public void addConstraint(RecurrenceConstraint constraint) {
        if (!constraints.contains(constraint)) {
            constraints.add(constraint);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Determine if this recurring activity has already expired by the specified date.  That is, it
     * has no occurances left by that date
     *
     * Uses the Occurance object to perform the determination.
     *
     * @param date
     * @return true if this recurring activity has expired by the specified date
     */
    @Transient
    public boolean isExpired(Date date) {
        //return occurrences.isCompleteBy(date);
        return false;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Tests whether the specified date falls within the constraints defined for this recurrence
     *
     * @param date
     * @return true if the date is acceptable
     */
    private boolean withinConstraints(Date date) {
        boolean acceptable = true;

        // loop through the constraints in order
        for (RecurrenceConstraint constraint : constraints) {
            if (!constraint.isAcceptable(date)) {
                acceptable = false;
                break;
            }
        }

        return acceptable;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Determines if this event occurs on the specified date
     *   1. Checks the date against the constraints
     *   2. If ok, uses the RecurrenceStrategy to test if the date is ok
     *
     * @param date
     * @return true if it is due to occur
     */
    @Transient
    public boolean isDueOn(Date date) {
        if (withinConstraints(date)) {
            return getRecurrenceStrategy().occursOn(date);
        }
        else {
            return false;
        }
    }

    // ------------------------------------------------------------------------------------------------------
    
    /**
     * Determines the next occurrence of this recurrence AFTER the specified datetime
     * @param date
     * @return the next occurrence, if defined
     */
    @Transient
    public Date getNextOccurence(Date date) {
        Date nextDate = getRecurrenceStrategy().nextOccurrence(date);
        if (withinConstraints(nextDate)) {
            return nextDate;
        }
        else {
            // result is out of the constraints
            return null;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Provides an iterator over all of the dates that have an event in this recurrence pattern
     *
     * Note: If there's no constraints on the recurrence, then the iteration maybe infinite.
     *
     * @return Iterator<Date> for each Date that has an event in this recurrence pattern
     *
     */
    @Transient
    public Iterator<Date> getDateIterator(Date startDate) {
        return new DateItr(startDate);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Gets the instance of the recurrence stategy
     *
     * The RecurenceStategy is the MementoOriginator and an instance was instantiated when this Recurrence
     *  instance was loaded from persistence (or it was simply set through the constructor)
     *
     * @return RecurrenceStategy
     */
    @Transient
    public RecurrenceStrategy getRecurrenceStrategy() {
        return (RecurrenceStrategy) getOriginator();
    }

    public void setRecurrenceStrategy(RecurrenceStrategy recurrenceStrategy) {
        setOriginator(recurrenceStrategy);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Implementation of an iterator that retrieves the dates in this recurrence
     */
    private class DateItr implements Iterator<Date> {

        private Date lastDate = null;
        private boolean useStartDate = false;

        // --------------------------------------------------------------------------------------------------

        /**
         * Initialise an iterator starting from the specified date
         * @param startDate
         */
        public DateItr(Date startDate) {
            if (isDueOn(startDate)) {
                useStartDate = true;
            }

            lastDate = startDate;
        }

        // --------------------------------------------------------------------------------------------------

        /**
         * Determine if there's another date in the recurrence iterator
         * @return true if there is
         */
        public boolean hasNext() {
            if (lastDate != null) {
                if (useStartDate) {
                    // the start date is the first result
                    return true;
                }
                else {
                    return getNextOccurence(lastDate) != null;
                }
            }
            else {
                return false;
            }
        }

        // --------------------------------------------------------------------------------------------------

        /**
         * Return the date of the next occurence in this recurrence iterator
         * @return true if there is
         */
        public Date next() {
            if (useStartDate) {
                // special case to use the starting date
                useStartDate = false;
                return lastDate;
            }
            else {
                if (lastDate != null) {
                    this.lastDate = getNextOccurence(lastDate);
                    return this.lastDate;
                }
                else {
                    return null;
                }
            }
        }

        // --------------------------------------------------------------------------------------------------

        /** remove is not supported */
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported on a recurrence iterator");
        }
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Calculates the number of occurrences of this recurrence in the specified interval
     *
     * This implementation iterates through the occurrences using the DateIterator.  A subclass
     * may override this method with a faster implementation.
     *
     * The maximum value must be specified as the number of occurences may be infinity.
     *
     * @param startDate
     * @param interval
     * @param maxOccurrences - if reached then the method exits with the value -1
     * @return the number of occurrences in the interval, or -1 if the number of occurrences
     *  exceeds the maximum number specified
     */
    public int occurrences(Date startDate, Interval interval, int maxOccurrences) {
        Iterator<Date> dates = getDateIterator(startDate);
        int occurrences = 0;
        Date endDate = interval.lastSecond(startDate);
        boolean pastEndDate = false;

        while ((dates.hasNext()) && (occurrences <= maxOccurrences) && (!pastEndDate)) {
            Date date = dates.next();

            if (date != null) {
                //  if the date is not after the enddate
                if ((date.equals(endDate)) || (date.before(endDate))) {
                    if (interval.contains(startDate, date)) {
                        occurrences++;
                    }
                }
                else {
                    pastEndDate = true;
                }
            }
            else {
                pastEndDate = true;
            }
        }

        if (occurrences > maxOccurrences) {
            // reset the number of occurrences, indicating the maximum has been exceeded
            occurrences = -1;
        }

        return occurrences;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Get the constraints applied to this recurrence instance */
    @OneToMany(mappedBy="recurrence", cascade = CascadeType.ALL)
    public List<RecurrenceConstraint> getConstraints() {
        return constraints;
    }

    protected void setConstraints(List<RecurrenceConstraint> constraints) {
        this.constraints = constraints;
    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println(getIdentityName()+":");
        out.print("   Strategy: ");
        if (getOriginator() != null) {
            out.println(getOriginatorName());
        } else {
            out.println("null");
        }
        out.println("   Constraints: ");
        if (constraints != null) {
            for (RecurrenceConstraint constraint : getConstraints()) {
                out.print("   ");
                constraint.print(out);
            }
        } else {
            out.println("null");
        }
    }

    // ------------------------------------------------------------------------------------------------------
}
