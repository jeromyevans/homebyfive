package com.blueskyminds.homebyfive.business.taxpolicy;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * A placeholder for GST-free tax (no tax)
 *
 * Date Started: 23/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */

public class GSTFree  {

    private static final String TAX_NAME = "FRE";
    private static final String TAX_PERCENT = "0.0";

    // ------------------------------------------------------------------------------------------------------

    /** Initialise a GST free tax policy */
    public static TaxPolicy getInstance() {
        return new FixedTax(TAX_NAME, new BigDecimal(TAX_PERCENT), Currency.getInstance("AUD"), null, null);
    }

    // ------------------------------------------------------------------------------------------------------

}
