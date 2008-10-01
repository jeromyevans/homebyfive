package com.blueskyminds.enterprise.pricing;

import com.blueskyminds.enterprise.Schedule;
import com.blueskyminds.enterprise.Enterprise;
import com.blueskyminds.homebyfive.framework.framework.journal.Journal;

import javax.persistence.*;
import java.util.List;

/**
 * The list of products in the enterprise
 *
 * Date Started: 12/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class ProductList extends Schedule<Product> {

    /** Create a new product list */
    public ProductList(Enterprise enterprise, Journal journal) {
        super(enterprise, journal);
    }

    /** Default constructor for ORM */
    protected ProductList() {

    }

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a product in the product list
     * @return the product if added successfully, otherwise null
     */
    public Product createProduct(Product product) {
        return super.create(product);
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="ProductListEntry",
            joinColumns=@JoinColumn(name="ProductListId"),
            inverseJoinColumns = @JoinColumn(name="ProductId")
    )
    protected List<Product> getProducts() {
        return super.getDomainObjects();
    }

    protected void setProducts(List<Product> products) {
        super.setDomainObjects(products);
    }

    // ------------------------------------------------------------------------------------------------------

    @Override
    @OneToOne()
    @JoinColumn(name="EnterpriseId")
    public Enterprise getEnterprise() {
        return super.getEnterprise();
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
}
