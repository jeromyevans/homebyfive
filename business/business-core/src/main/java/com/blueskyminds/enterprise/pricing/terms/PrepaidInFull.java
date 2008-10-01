package com.blueskyminds.enterprise.pricing.terms;

import com.blueskyminds.enterprise.pricing.PaymentTerms;
import com.blueskyminds.homebyfive.framework.core.recurrence.Recurrence;
import com.blueskyminds.homebyfive.framework.core.recurrence.RecurrenceFactory;
import com.blueskyminds.homebyfive.framework.core.datetime.PeriodTypes;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Terms for a CommercialDocument where payment is due in advance in full.
 *
 * Date Started: 12/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity(name="TermsPrepaidInFull")
@PrimaryKeyJoinColumn(name="TermsId")
public class PrepaidInFull extends Terms {

    // ------------------------------------------------------------------------------------------------------

    /** Create pre-paid recurring terms */
    public PrepaidInFull() {
        super(PaymentTerms.PrepaidInFull);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Calculates the 'Date Due' for the commercial document based on the terms, derived from the
     * date applied.
     *
     * For the PrepaidInFull terms, it's due on the date applied.
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
        return RecurrenceFactory.create(dateApplied, PeriodTypes.OnceOff);
    }

    // ------------------------------------------------------------------------------------------------------
}
