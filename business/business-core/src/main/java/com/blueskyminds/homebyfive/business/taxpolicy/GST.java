package com.blueskyminds.homebyfive.business.taxpolicy;

import java.math.BigDecimal;
import java.util.Currency;


/**
 * Convenience implementation of the GST tax policy
 *
 * Date Started: 22/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class GST {

    public static final String TAX_NAME = "GST";
    private static final String TAX_PERCENT = "0.1";

    // ------------------------------------------------------------------------------------------------------

    /** Initialise a new GST tax policy instance. */
    public static TaxPolicy newInstance() {
        return new FixedTax(TAX_NAME, new BigDecimal(TAX_PERCENT), Currency.getInstance("AUD"), null, null);
    }

    // ------------------------------------------------------------------------------------------------------
}
