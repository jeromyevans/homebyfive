package com.blueskyminds.enterprise.pricing;

import com.blueskyminds.enterprise.Schedule;
import com.blueskyminds.homebyfive.framework.framework.journal.Journal;
import com.blueskyminds.enterprise.Enterprise;

import java.util.*;

/**
 * Pre-approved automatic payment for an AccountHolder against a fixed set of Invoices
 *
 * Date Started: 14/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
// todo: this needs to fit into the framework of a scheduler that runs planned tasks
public class AutomaticPaymentPlan extends Schedule {

    /**
     * A map of all of the dates that payment will be made and the amount of money to make.
     */
    private Map<Date, Money> paymentSchedule;

    // ------------------------------------------------------------------------------------------------------

    public AutomaticPaymentPlan(Enterprise enterprise, Journal journal) {
        super(enterprise, journal);
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the payment plan with default attributes */
    private void init() {
        paymentSchedule = new HashMap<Date, Money>();
    }

    // ------------------------------------------------------------------------------------------------------

   // public void addPayment
}
