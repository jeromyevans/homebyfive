package com.blueskyminds.enterprise.pricing.terms;

import com.blueskyminds.enterprise.pricing.PaymentTerms;
import com.blueskyminds.homebyfive.framework.core.recurrence.Recurrence;
import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;

import javax.persistence.*;
import java.util.Date;

/**
 *
 * Terms represents the terms for an order or invoice.
 *  eg. To be prepaid, Net 30 days, etc
 *
 * Date Started: 11/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Terms extends AbstractDomainObject {

    /** The payment terms enumeration */
    private PaymentTerms type;

    // ------------------------------------------------------------------------------------------------------

    /** Create a new instance of the terms */
    public Terms(PaymentTerms type) {
        this.type = type;
    }

    /** Default constructor for ORM */
    protected Terms() {

    }

    // ------------------------------------------------------------------------------------------------------

    @Enumerated
    @Column(name="Type")
    public PaymentTerms getType() {
        return type;
    }

    protected void setType(PaymentTerms type) {
        this.type = type;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Calculates the 'Date Due' for the commercial document based on the terms, derived from the
     * date applied */
    @Transient
    public abstract Date getDateDue(Date dateApplied);

    /** Returns a Recurrence representative of the dates for the Terms.
     * eg. If terms are prepaid in full, then the recurrence contains only the dateApplied, but
     * Terms with a recurring pattern will return that pattern from the dateApplied*/
    public abstract Recurrence recurrence(Date dateApplied);

    // ------------------------------------------------------------------------------------------------------

    public String toString() {
        return type.toString();
    }
}
