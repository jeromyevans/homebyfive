package com.blueskyminds.enterprise.license;

import com.blueskyminds.enterprise.party.Party;
import com.blueskyminds.framework.AbstractDomainObject;

import javax.persistence.*;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract implemention of a LicenseAccount that provides standard implementations of the methods
 * for adding and removing licenses
 *
 * Each License is wrapped with a LicenseAllocation value object indicating the type of allocation
 *
 * Date Started: 2/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="AccountClass", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("Normal")
public class LicenseAccount extends AbstractDomainObject {

    /** The schedule of license accounts in this enterprise */
    private ScheduleOfLicenseAccounts scheduleOfLicenseAccounts;

    /** The party that this licenseAccount is owned by */
    private Party party;

    /** The underlying list of licenses, each wrapped in a LicenseAllocation to identify the
    * allocation type */
    private List<LicenseAllocation> licenses;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new account initialised with the default parameters
     */
    public LicenseAccount(ScheduleOfLicenseAccounts scheduleOfLicenseAccounts, Party party) {
        this.scheduleOfLicenseAccounts = scheduleOfLicenseAccounts;
        this.party = party;
        init();
    }

    /** Default constructor for ORM */
    protected LicenseAccount() {

    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the account with default memento
     **/
    private void init() {
        licenses = new LinkedList<LicenseAllocation>();

    }

    // -------------------------------------------------------------------------------------------------------

    /** Adds the license, wrapped with allocation type information, to the list */
    private boolean addLicense(LicenseAllocation license) {
        return licenses.add(license);
    }

    // -------------------------------------------------------------------------------------------------------

    /** Removes the license, wrapped with allocation type information, to the list */
    private boolean removeLicense(LicenseAllocation license) {
        return licenses.remove(license);
    }

    // -------------------------------------------------------------------------------------------------------
    /** Determines if the license, wrapped with allocation type information, is in the list */
    private boolean hasLicense(LicenseAllocation license) {
        return licenses.contains(license);
    }

    // -------------------------------------------------------------------------------------------------------

    /**
     * Adds the specified license to this account
     * @param license
     * @return true if added okay
     */
    public boolean addLicense(License license) {
        LicenseAllocation wrapper = new LicenseAllocation(this, license,  LicenseAllocationTypes.Allocated);
        return addLicense(wrapper);
    }

    // -------------------------------------------------------------------------------------------------------

     /**
     * Removes the specified license from this account
     * @param license
     * @return true if removed okay
     */
    public boolean removeLicense(License license) {
        LicenseAllocation wrapper = new LicenseAllocation(this, license,  LicenseAllocationTypes.Allocated);
        return removeLicense(wrapper);
    }

    // -------------------------------------------------------------------------------------------------------

     /**
     * Removes the specified license reservation from this account
     * @param license
     * @return true if removed okay
     */
    public boolean addReservedLicense(License license) {
        LicenseAllocation wrapper = new LicenseAllocation(this, license,  LicenseAllocationTypes.Reserved);
        return addLicense(wrapper);
    }

    // -------------------------------------------------------------------------------------------------------

    /**
     * Removes the specified license reservation from this account
     * @param license
     * @return true if removed okay
     */
    public boolean removeReservedLicense(License license) {
        LicenseAllocation wrapper = new LicenseAllocation(this, license,  LicenseAllocationTypes.Reserved);
        return removeLicense(wrapper);
    }

    // -------------------------------------------------------------------------------------------------------

    /**
     * Determines if the specified license has been reserved by this account
     *
     * @param license to check for
     * @return true if the license is reserved
     */
    public boolean hasLicenseReserved(License license) {
        // create a wrapper value-object
        LicenseAllocation wrapper = new LicenseAllocation(this, license, LicenseAllocationTypes.Reserved);

        // check if the list contais the VO
        return hasLicense(wrapper);
    }

    // -------------------------------------------------------------------------------------------------------

    /** Determines if this account has a license allocation of the specified type.
     * This is an iterative search.
     *
     * @param type
     * @return true if it does
     */
    private boolean hasAllocationOfType(LicenseAllocationTypes type) {
        boolean found = false;

        for (LicenseAllocation wrapper : licenses)  {
            if (wrapper.getType().equals(type)) {
                found = true;
                break;
            }
        }
        return found;
    }

    // -------------------------------------------------------------------------------------------------------

    /**
     * Determines if licenses are reserved by this account
     *
     * @return true if any licenses are reserved
     */
    public boolean hasAnyLicenseReserved() {
        return hasAllocationOfType(LicenseAllocationTypes.Reserved);
    }

    // -------------------------------------------------------------------------------------------------------

    /**
     * Returns true if the account has the specified license
     * @param license
     * @return true if the account has this license
     */
    public boolean hasLicenseAllocated(License license) {
        // create a wrapper value-object
        LicenseAllocation wrapper = new LicenseAllocation(this, license, LicenseAllocationTypes.Allocated);

        // check if the list contais the VO
        return hasLicense(wrapper);
    }

    // -------------------------------------------------------------------------------------------------------

    /**
     * Determines if any licenses are allocated to this account
     *
     * @return true if any licenses are allocated
     */
    public boolean hasAnyLicenseAllocated() {
        return hasAllocationOfType(LicenseAllocationTypes.Allocated);
    }

    // -------------------------------------------------------------------------------------------------------

    /**
     * The default implementation is that licenses in this account are not available for transfer
     * Only special-case relationships would normally override this
     * @param license
     * @return true if the license can be transferred from this account
     */
    @Transient
    public boolean isLicenseAvailable(License license) {
        return false;
    }

    // -------------------------------------------------------------------------------------------------------

    /**
     * List the licenses allocated to this account with the allocation type specified
     * @return List<License>
     */
    @Transient
    private List<License> getLicensesByAllocationType(LicenseAllocationTypes type) {
        List<License> list = new LinkedList<License>();

        for (LicenseAllocation wrapper : licenses)  {
            if (wrapper.getType().equals(type)) {
                list.add(wrapper.getLicense());
            }
        }
        return list;
    }

    // -------------------------------------------------------------------------------------------------------

    /**
     * List the licenses allocated to this account
     * @return List<License>
     */
    @Transient
    public List<License> getAllocatedLicenses() {
        return getLicensesByAllocationType(LicenseAllocationTypes.Allocated);
    }

    // -------------------------------------------------------------------------------------------------------

    /**
     * List the licenses reserved by this account
     * @return List<License>
     */
    @Transient
    public List<License> getReservedLicenses() {
        return getLicensesByAllocationType(LicenseAllocationTypes.Reserved);
    }

    // -------------------------------------------------------------------------------------------------------

    /** Get the list of license allocations for this account */
    @OneToMany(mappedBy = "licenseAccount", cascade=CascadeType.ALL)
    protected List<LicenseAllocation> getLicenses() {
        return licenses;
    }

    protected void setLicenses(List<LicenseAllocation> licenses) {
        this.licenses = licenses;
    }

    // -------------------------------------------------------------------------------------------------------

    /** Get the party that this LicenseAccount belongs to */
    @OneToOne
    @JoinColumn(name="PartyId")
    public Party getParty() {
        return party;
    }

    protected void setParty(Party party) {
        this.party = party;
    }

    // -------------------------------------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name="ScheduleOfLicenseAccountsId")
    public ScheduleOfLicenseAccounts getScheduleOfLicenseAccounts() {
        return scheduleOfLicenseAccounts;
    }

    public void setScheduleOfLicenseAccounts(ScheduleOfLicenseAccounts scheduleOfLicenseAccounts) {
        this.scheduleOfLicenseAccounts = scheduleOfLicenseAccounts;
    }

    // -------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        boolean first = true;
        out.println(getIdentityName()+" ("+getParty().getIdentityName()+"):");
        out.print("   ");
        printReservedLicenses(out);
        out.print("   ");
        printAllocatedLicenses(out);
    }

    // -------------------------------------------------------------------------------------------------------

    public void printReservedLicenses(PrintStream out) {
        boolean first = true;
        out.print("ReservedLicenses: ");
        for (License license : getReservedLicenses()) {
            if (!first) {
                out.print(",");
            }
            else {
                first = false;
            }
            out.print(license.getIdentityName());
        }
        out.println();
    }

    // -------------------------------------------------------------------------------------------------------

     public void printAllocatedLicenses(PrintStream out) {
        boolean first = true;
        out.print("AllocatedLicenses: ");
        for (License license : getAllocatedLicenses()) {
            if (!first) {
                out.print(",");
            }
            else {
                first = false;
            }
            out.print(license.getIdentityName());
        }
         out.println();
    }

    // -------------------------------------------------------------------------------------------------------
}
