package com.blueskyminds.landmine.core.property;

import com.blueskyminds.enterprise.address.Address;
import com.blueskyminds.framework.AbstractDomainObject;
import com.blueskyminds.framework.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Maps an address to a property.
 * The mapping is a typed relationship - it defines when the address was applied to the property.
 *
 * This is used to distinguish when addresses change for a property.
 *
 * Date Started: 8/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class PremiseAddressMap extends AbstractEntity {

    private Premise premise;
    private Address address;
    private Date dateApplied;

    // ------------------------------------------------------------------------------------------------------

    /**
     * @param premise
     * @param address
     * @param dateApplied - this may be null, now or a date in the past.  Null means unknown, so assume it's
     * been applied always
     */
    public PremiseAddressMap(Premise premise, Address address, Date dateApplied) {
        this.premise = premise;
        this.address = address;
        this.dateApplied = dateApplied;
    }

        // ------------------------------------------------------------------------------------------------------

    /**
     * @param premise
     * @param address
     *
     * Does not set the DateApplied for the relationship
     */
    public PremiseAddressMap(Premise premise, Address address) {
        this.premise = premise;
        this.address = address;
        this.dateApplied = null;
    }

    /** Default constructor for ORM */
    protected PremiseAddressMap() {

    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name="PremiseId")
    public Premise getPremise() {
        return premise;
    }

    public void setPremise(Premise premise) {
        this.premise = premise;
    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="AddressId")
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    // ------------------------------------------------------------------------------------------------------

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DateApplied")
    public Date getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(Date dateApplied) {
        this.dateApplied = dateApplied;
    }

    // ------------------------------------------------------------------------------------------------------
}
