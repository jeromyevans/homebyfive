package com.blueskyminds.enterprise.pricing.terms;

import com.blueskyminds.enterprise.pricing.PaymentTerms;
import com.blueskyminds.framework.recurrence.Recurrence;
import com.blueskyminds.framework.recurrence.RecurrenceFactory;
import com.blueskyminds.framework.datetime.PeriodTypes;

import javax.persistence.*;
import java.util.Date;

/**
 *
 * Implementation of Terms for Perpaid periodic terms.
 *
 * eg. prepaid monthly
 *
 * Date Started: 11/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity(name="TermsPrepaidRecurring")
@PrimaryKeyJoinColumn(name="TermsId")
public class PrepaidRecurring extends Terms {

    /** The period of the recurring payment terms */
    private PeriodTypes periodType;

    // ------------------------------------------------------------------------------------------------------

    /** Create pre-paid recurring terms */
    public PrepaidRecurring(PeriodTypes periodTypes) {
        super(PaymentTerms.PrepaidPeriodic);
        this.periodType = periodTypes;
    }

    /** Default constructor for ORM */
    protected PrepaidRecurring() {

    }

    // ------------------------------------------------------------------------------------------------------

    public String toString() {
        return getType()+"/"+periodType;
    }

    // ------------------------------------------------------------------------------------------------------

    @Enumerated
    @Column(name="PeriodType")
    public PeriodTypes getPeriodType() {
        return periodType;
    }

    protected void setPeriodType(PeriodTypes periodType) {
        this.periodType = periodType;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Calculates the 'Date Due' for the commercial document based on the terms, derived from the
     * date applied.
     *
     * For the PrepaidRecurring terms, it's due on the date applied.
     **/
    @Transient
    public Date getDateDue(Date dateApplied) {
        return dateApplied;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns a Recurrence representative of the dates for the Terms.
     * eg. If terms are prepaid in full, then the recurrence contains only the dateApplied, but
     * Terms with a recurring pattern will return that pattern from the dateApplied*/
    public Recurrence recurrence(Date dateApplied) {
        return RecurrenceFactory.create(dateApplied, periodType);
    }

    // ------------------------------------------------------------------------------------------------------
}
