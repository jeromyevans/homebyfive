package com.blueskyminds.homebyfive.business.pricing;

/**
 * This exception indicates that pricing wasn't possible because the inputs were inconsistent.
 *   eg. two order items with different currencies
 *   eg. an order item where the quanity is different from the pricing quantity
 *
 * Date Started: 11/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class InconsistentPricingException extends Exception {

    public InconsistentPricingException() {
    }

    public InconsistentPricingException(String message) {
        super(message);
    }

    public InconsistentPricingException(String message, Throwable cause) {
        super(message, cause);
    }

    public InconsistentPricingException(Throwable cause) {
        super(cause);
    }
}
