package com.blueskyminds.enterprise.pricing;

import com.blueskyminds.enterprise.pricing.policy.PricingPolicy;
import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;
import com.blueskyminds.homebyfive.framework.core.journal.Journal;

import javax.persistence.*;
import java.io.PrintStream;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * A Contract specifies the pricing policy for each available License
 *
 * A Contract may reference a BaseContract from which PricingPolicy's are sought when not defined in this
 * Contract.
 *
 * Date Started: 7/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class Contract extends AbstractDomainObject {

    /** A mapping between products that are priced by this contract */
    //private Map<Product, PricingPolicy> priceMap;

    Collection<ContractProductPriceMap> contractProductPriceMaps;

    /** The journal for recording events */
    private Journal journal;

    /** The name given to this contract */
    private String name;

    /**
     * A reference contract upon which this one is based, and from which PricingPolicy's are sought
     * if not defined in this Contract.
     */
    private Contract baseContract;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise a new contract
     * @param name
     * @param journal
     */
    public Contract(String name, Journal journal) {
        init();
        this.name = name;
        this.journal = journal;
        this.baseContract = null;
    }

    /** Default constructor for ORM */
    protected Contract() {

    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the contract with default attributes */
    private void init() {
        //priceMap = new HashMap<Product, PricingPolicy>();
         contractProductPriceMaps = new LinkedList<ContractProductPriceMap>();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Sets a pricing policy for the specified product under this contract
     *
     * Replaces existing price
     *
     * @param product
     * @param policy
     * @return true if set ok
     */
    public boolean mapPricingPolicy(Product product, PricingPolicy policy) {
        //PricingPolicy oldPolicy = priceMap.put(product, policy);

        boolean found = false;
        for (ContractProductPriceMap contractProductPriceMap : contractProductPriceMaps) {
            if (contractProductPriceMap.getProduct().equals(product)) {
                contractProductPriceMap.setPrice(policy);
                found = true;
                break;
            }
        }

        if (!found) {
            contractProductPriceMaps.add(new ContractProductPriceMap(this, product, policy));
        }

        //journal.changed(this, product, "PricingPolicy", oldPolicy, policy, null);

        return true;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Gets the pricing policy for the specified product.
     *
     * If there is no pricing policy in this contract, recurses into the base contract to check it for a
     * match (and it's base and so on). Returns null if there is no policy found for the specified
     * product (the Product is not priced and therefore not allowed under this contract).
     *
     * @param product
     * @return policy if defined
     */
    public PricingPolicy lookupPricingPolicy(Product product) {
        PricingPolicy policy = null;

//        if (priceMap.containsKey(product)) {
//            policy = priceMap.get(product);
//        }
//        else {
//            if (baseContract != null) {
//                // recurse into the base contract
//                policy = baseContract.lookupPricingPolicy(product);
//            }
//        }

        boolean found = false;
        for (ContractProductPriceMap contractProductPriceMap : contractProductPriceMaps) {
            if (contractProductPriceMap.getProduct().equals(product)) {
                policy = contractProductPriceMap.getPrice();
                found = true;
                break;
            }
        }

        if (!found) {
            // recurse into the base contract
            policy = baseContract.lookupPricingPolicy(product);
        }
        return policy;
    }
    // ------------------------------------------------------------------------------------------------------

    /**
     * Determines if the specified product is available under this contract.
     * A product is only available if it has a PricingPolicy defined by this contract, or under the
     * base contract(s).
     *
     * @param product
     * @return true if the product is available under this contract (not that the product is available
     *  to purchase though)
     */
    public boolean isProductAvailable(Product product) {
        return lookupPricingPolicy(product) != null;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the base contract for this Contract, if one is defined
     * The base contract is a parent
     * @return Contract - the contract upon which this contract is based
     */
    @ManyToOne
    @JoinColumn(name="BaseContractId")
    public Contract getBaseContract() {
        return baseContract;
    }

    private void setBaseContract(Contract contract) {
        this.baseContract = contract;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Set the base contract for this Contract.  If this contract doesn't contain a requested pricing policy
     * then the base contract is checked (and it's base, recursively)
     *
     * Changing the base contract is a journaled event.
     *
     * @param newBaseContract
     */
    public void changeBaseContract(Contract newBaseContract) {
        Contract oldBaseContract = baseContract;
        setBaseContract(newBaseContract);
        journal.changed(this, this, "baseContract", oldBaseContract, newBaseContract, null);
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    @Basic
    @Column(name="Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Converts the pricemap for this contract (which is a hash for fast lookup) into a simple collection
     * for persistence.
     * The collection is cached in memory foreach each subsequent request - the cache is cleared if the priceMap is
     * altered.
     * @return collection of mapping classes
     */
    @OneToMany(mappedBy="contract", cascade = CascadeType.ALL)
    public Collection<ContractProductPriceMap> getContractProductPriceMap() {

//        Collection<ContractProductPriceMap> contractProductPriceMaps = new LinkedList<ContractProductPriceMap>();
//
//        if (priceMap != null) {
//            // iterate through each product priced in this contract
//            for (Product product : priceMap.keySet()) {
//                contractProductPriceMaps.add(new ContractProductPriceMap(this, product, priceMap.get(product)));
//            }
//        }

        return contractProductPriceMaps;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Loads a collection of product price maps into this contract, converting them into the priceMap hash
     * @param contractProductPriceMaps
     */
    public void setContractProductPriceMap(Collection<ContractProductPriceMap> contractProductPriceMaps) {
//        if (priceMap != null) {
//            priceMap.clear();
//        }
//        for (ContractProductPriceMap mapEntry : contractProductPriceMaps) {
//           priceMap.put(mapEntry.getProduct(),  mapEntry.getPrice());
//        }
        this.contractProductPriceMaps = contractProductPriceMaps;
    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println(getIdentityName()+" ("+getName()+"):");
//        if (priceMap != null) {
//            for (Product product : priceMap.keySet()) {
//                out.println("   "+product+": "+priceMap.get(product));
//            }
//        }
//        else {
//            out.println("   no prices in this contract");
//        }
        for (ContractProductPriceMap contractProductPriceMap : contractProductPriceMaps) {
            out.println("   "+contractProductPriceMap.getProduct().getIdentityName()+": "+contractProductPriceMap.getPrice());
        }
    }
}
