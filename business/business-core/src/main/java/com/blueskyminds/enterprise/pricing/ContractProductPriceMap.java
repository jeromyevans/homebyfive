package com.blueskyminds.enterprise.pricing;

import com.blueskyminds.enterprise.pricing.policy.PricingPolicy;
import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;

import javax.persistence.*;
import java.io.PrintStream;

/**
 * This is an Association Class used to map products and prices within a contract.  It's role is purely
 * to simply the persistence of a Contract.  The ContractProductPricePk is a related class used to
 * generate a compound primary key for this class.
 *
 * Date Started: 28/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class ContractProductPriceMap extends AbstractDomainObject {

    private Contract contract;

    private Product product;

    private PricingPolicy price;

    // ------------------------------------------------------------------------------------------------------

    public ContractProductPriceMap(Contract contract, Product product, PricingPolicy price) {
        this.contract = contract;
        this.product = product;
        this.price = price;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Default constructor for use by ORM */
    protected ContractProductPriceMap() {

    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name="ContractId")
    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="PricingPolicyId")
    public PricingPolicy getPrice() {
        return price;
    }

    public void setPrice(PricingPolicy price) {
        this.price = price;
    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println(contract.getIdentityName()+": "+product.getIdentityName()+" : "+price.getIdentityName()+" "+price.toString());
    }
}
