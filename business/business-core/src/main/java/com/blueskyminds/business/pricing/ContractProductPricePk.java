package com.blueskyminds.business.pricing;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * This class implements a composite primary key for a ContractProductPriceMap for ORM
 * The composite primary key is derived from:
 *    Contract.id
 *    Product.id
 *
 * Date Started: 28/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Embeddable
public class ContractProductPricePk implements Serializable {

    private Contract contract;

    private Product product;

    // ------------------------------------------------------------------------------------------------------

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    // ------------------------------------------------------------------------------------------------------

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // ------------------------------------------------------------------------------------------------------

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ContractProductPricePk that = (ContractProductPricePk) o;

        if (!contract.equals(that.contract)) return false;
        if (!product.equals(that.product)) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = contract.hashCode();
        result = 29 * result + product.hashCode();
        return result;
    }
}
