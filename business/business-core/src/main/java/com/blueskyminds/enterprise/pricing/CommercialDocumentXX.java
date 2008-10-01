package com.blueskyminds.enterprise.pricing;

import com.blueskyminds.homebyfive.framework.core.measurement.Quantity;
import com.blueskyminds.enterprise.party.Party;

import java.util.Date;

/**
 *
 * A generic interface to the following commerical documents:
 *   quote
 *   order
 *   invoice
 *   receipt
 *
 * This needs a better name.
 *
 * Date Started: 11/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public interface CommercialDocumentXX {

    /**
     * Get the party that this CommercialDocument was created for
     * @return Party
     */
    Party getParty();

    /**
     * Attempts to add the specified product to this CommercialDocument
     *
     * There is no check to see if the product is actually available.
     *
     * Does check that the product has a PricingPolicy under the current contract.
     *
     * @param product
     * @return true if added to this order
     */
    boolean addProduct(Product product, Quantity quantity);

  /**
     * Adds the specified item to this document
     *
     * @param item
     * @return true if added ok
     */
    boolean addItem(CommercialDocumentItem item);

     /**
      * Copy the specified item to this CommercialDocument
      **/
    boolean copyItem(CommercialDocumentItem item);

     /**
     * Returns the total value of this CommercialDocument
     * @return the total value of this CommercialDocument
     */
    Money grossValue() throws InconsistentPricingException;

    /**
     * Returns the due date for this commercial document.  This is derived from the Terms.
     * If it's not applicable, then it will be equal to the dateApplied
     *
     * @return the date due for this commercial document.
     */
    Date getDateDue();

    /**
     * Returns the quantity of the specified item in this CommercialDocument.
     * The quantity is obtained directly from the item itself, but may be updated with in the context
     * of this CommercialDocument.
     *
     * eg. if the item is charged only per transaction, then the quantity is derived from the estimated
     *  number of transactions in this timespan (based on the terms of payment)
     *
     * @param item
     * @return quantity
     */
    public Quantity getItemQuantity(CommercialDocumentItem item);

    /**
     * Returns the gross value of the specified item in the context of this CommercialDocument.
     *
     * The price is obtained directly from the item itself but for certain types of items may be updated
     *  in the context of this commercial document.
     *
     * eg. an item who's price depends on timespan or the number of transactions.
     *
     * @param item
     * @return quantity
     */
    public Money getItemGrossValue(CommercialDocumentItem item) throws InconsistentPricingException;

     /**
     * Convenience method that passes straight through to the items to determine if this CommercialDocument contains
     * any items with a recurring component
     *
     * @return true if there is a recurring component
     */
    boolean hasRecurringComponent();

    /**
     * Determines if this CommercialDocument is open.
     *
     * An OPEN state means no action has been taken.
     *
     * @return true if action has been taken
     */
    boolean isOpen();

    /**
     * Determines if any actions have been taken with the CommercialDocument.
     * If an action has been taken then it's probable that it can't be deleted.
     *
     * The document has been actionied if it's state is anything other then OPEN or DELETED
     *
     * @return true if action has been taken
     */
    boolean isActioned();

    /**
     * Determines if this CommercialDocument is closed.
     *
     * @return true if action has been taken
     */
    boolean isClosed();

    /**
     * Changes the state of this CommercialDocument to Closed
     */
    void close();

    /**
     * Changes the state of this CommercialDocument to Actioned, indicating work is in-progress
     * based on this document
     */
    void action();

    /**
     * Changes the state of this CommercialDocument to Deleted
     */
    void delete();

}
