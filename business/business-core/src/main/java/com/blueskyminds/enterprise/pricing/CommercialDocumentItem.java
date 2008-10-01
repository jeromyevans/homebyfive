package com.blueskyminds.enterprise.pricing;

import com.blueskyminds.enterprise.pricing.policy.PricingPolicy;
import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;
import com.blueskyminds.homebyfive.framework.core.datetime.Interval;
import com.blueskyminds.homebyfive.framework.core.measurement.Quantity;

import javax.persistence.*;

/**
 * An item occuring in a CommercialDocument
 *
 * Date Started: 11/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class CommercialDocumentItem extends AbstractDomainObject {

    /** The commercial document that this item occurs in */
    private CommercialDocument document;

    /** Reference to the product supplied/to be supplied for this item */
    public Product product;

    /** The quantity of this item */
    private Quantity quantity;

    /** The pricing policy that's been applied to this item */
    private PricingPolicy price;

    // ------------------------------------------------------------------------------------------------------

    /** Initialise a new item for this CommercialDocument */
    public CommercialDocumentItem(CommercialDocument document) {
        this.document = document;
    }

    /** Default constructor for ORM */
    protected CommercialDocumentItem() {

    }

    // ------------------------------------------------------------------------------------------------------

    /** The gross value of this item.
     * Accounts only for a:
     *    fixed pricing component; and
     *    recurrung pricing component
     *
     * If the price has other components then an InconsistentPricingException may be thrown
     *
     * @param interval
     * @return
     * @throws InconsistentPricingException
     */
    public Money grossValue(Interval interval) throws InconsistentPricingException {
        return price.calculateGrossPrice(quantity, interval);
    }

    // ------------------------------------------------------------------------------------------------------

     /** The gross value of this item.
      *  Accounts only for a:
      *    fixed pricing component
      *
      * If the price has other components then an InconsistentPricingException may be thrown
      * */
    public Money grossValue() throws InconsistentPricingException {
        return price.calculateGrossPrice(quantity);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Calculates the gross tax on the value of this item given the number of transactions and interval
     *
     * Accounts all of  the supported pricing components :
     *   Fixed pricing component
     *   Recurring pricing component and
     *   Per-transaction pricing component
     *
     * @param noOfTransactions
     * @param interval
     * @return the gross tax mapped by tax policy
     */
    public TaxMap grossTax(int noOfTransactions, Interval interval) throws InconsistentPricingException {
        return price.calculateGrossTax(quantity, noOfTransactions, interval);
    }

    // ------------------------------------------------------------------------------------------------------

    /** The gross tax on the value this item.
     * Accounts only for a:
     *    fixed pricing component; and
     *    recurrung pricing component
     *
     * If the price has other components then an InconsistentPricingException may be thrown
     *
     * @param interval
     * @return
     * @throws InconsistentPricingException
     */
    public TaxMap grossTax(Interval interval) throws InconsistentPricingException {
        return price.calculateGrossTax(quantity, interval);
    }

    // ------------------------------------------------------------------------------------------------------

     /** The gross tax on the value of this item.
      *  Accounts only for a:
      *    fixed pricing component
      *
      * If the price has other components then an InconsistentPricingException may be thrown
      * */
    public TaxMap grossTax() throws InconsistentPricingException {
        return price.calculateGrossTax(quantity);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Calculates the gross value of this item given the number of transactions and interval
     *
     * Accounts all of  the supported pricing components :
     *   Fixed pricing component
     *   Recurring pricing component and
     *   Per-transaction pricing component
     *
     * @param noOfTransactions
     * @param interval
     * @return the gross value
     */
    public Money grossValue(int noOfTransactions, Interval interval) throws InconsistentPricingException {
        return price.calculateGrossPrice(quantity, noOfTransactions, interval);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Convenience method that passes straight through to the pricing policy to determine if this item's
     * price has a recurring component
     *
     * @return true if there is a recurring component
     */
    public boolean hasRecurringComponent() {
        return price.hasRecurringComponent();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a clone of this item.
     * NOTE: The clone has references to the same product, quantity and price
     */
    public CommercialDocumentItem copy() {
        CommercialDocumentItem newItem = new CommercialDocumentItem(document);
        newItem.setProduct(product);
        newItem.setQuantity(quantity);
        newItem.setPrice(price);
        return newItem;
    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name="CommercialDocumentId")
    public CommercialDocument getDocument() {
        return document;
    }

    public void setDocument(CommercialDocument document) {
        this.document = document;
    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name="ProductId")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }


    // ------------------------------------------------------------------------------------------------------

    @Embedded
    public Quantity getQuantity() {
        return quantity;
    }

    public void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    // ------------------------------------------------------------------------------------------------------

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="PricingPolicyId")    
    public PricingPolicy getPrice() {
        return price;
    }

    public void setPrice(PricingPolicy price) {
        this.price = price;
    }

    // ------------------------------------------------------------------------------------------------------


}
