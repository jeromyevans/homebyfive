package com.blueskyminds.business;

import com.blueskyminds.business.Schedule;
import com.blueskyminds.business.Enterprise;
import com.blueskyminds.homebyfive.framework.core.journal.Journal;
import com.blueskyminds.homebyfive.framework.core.recurrence.Occurrence;
import com.blueskyminds.homebyfive.framework.core.recurrence.OccurrenceTypes;

import java.util.Date;

/**
 * A schedule of occurences is used to track the occurrences of a recurring event.
 *
 * Date Started: 6/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
// todo: this entire class needs to be revisited
@Deprecated
public class ScheduleOfOccurrences extends Schedule<Occurrence> {

    private static final String NAME = "ScheduleOfOccurences";

    /**
     * The type of this ScheduleOfOccurrences.
     */
    private OccurrenceTypes type;

    /**
     * If set, and type is FixedCount, this is the target number of occurrences
     */
    private Integer targetCount;

    /**
     * If set and type is EndDate, this is the last date to track occurrences
     */
    private Date expiryDate;

    // ------------------------------------------------------------------------------------------------------

    /** Create a ScheduleOfOccurrences without specifying an enddate or target count */
    public ScheduleOfOccurrences(Enterprise enterprise, OccurrenceTypes type, Journal journal) {
        super(enterprise, journal);
        this.type = type;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a ScheduleOfOccurrences with a specified enddate */
    public ScheduleOfOccurrences(Enterprise enterprise, Date expiryDate, OccurrenceTypes type, Journal journal) {
        super(enterprise, journal);
        this.expiryDate = expiryDate;
        this.type = type;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a ScheduleOfOccurrences with a specified number of occurrences */
    public ScheduleOfOccurrences(Enterprise enterprise, int targetCount, OccurrenceTypes type, Journal journal) {
        super(enterprise, journal);
        this.targetCount = targetCount;
        this.type = type;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the target count */
    public Integer getTargetCount() {
        return targetCount;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Set the target count
     * @param targetCount
     */
    public void setTargetCount(Integer targetCount) {
        this.targetCount = targetCount;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Reset the schedule of occurreneces back to zero/none
     */
    public void reset() {
        super.clear();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the type of this occurance.  eg. if it's perpetial or has an endDate
     * @return the occurenceType
     */
    public OccurrenceTypes getType() {
        return type;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Records an occurrence for the specified date
     *
     * @param date
     * @return true if added ok
     */
    public boolean recordOccurrence(Date date) {
        Occurrence occurrence = new Occurrence(date);
        return super.create(occurrence) != null;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * get the count of the number of occurrences in this schedule
     * @return the count
     */
    public int count() {
        return super.size();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Determines if there's any occurrences expected after the specified date
     *
     * @param date
     * @return true if there's no more occurrences expected after the specified date
     */
    public boolean isCompleteBy(Date date) {
        boolean expired = false;
        switch (type) {
            case EndDate:
                if (date.after(expiryDate)) {
                    expired = true;
                }
                break;
            case FixedCount:
                if (count() >= targetCount) {
                    expired = true;
                }
                break;
            case Perpetual:
                break;
        }
        return expired;
    }

    // ------------------------------------------------------------------------------------------------------
}
