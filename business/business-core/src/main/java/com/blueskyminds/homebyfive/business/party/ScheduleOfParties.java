package com.blueskyminds.homebyfive.business.party;

import com.blueskyminds.homebyfive.business.Enterprise;
import com.blueskyminds.homebyfive.business.Schedule;
import com.blueskyminds.homebyfive.framework.core.journal.Journal;

import javax.persistence.*;
import java.io.PrintStream;
import java.util.List;

/**
 * Manages a list of Parties
 *
 * Date Started: 6/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class ScheduleOfParties extends Schedule<Party> {

    // ------------------------------------------------------------------------------------------------------

    /** Initialise a new schedule of parties */
    public ScheduleOfParties(Enterprise enterprise, Journal journal) {
        super(enterprise, journal);
    }

    /** Default constructor for ORM */
    protected ScheduleOfParties() {

    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Create the party specified by including it in this schedule
     *
     * @param party to create by adding to ths schedule
     * @return the new Party, or null if it failed
     */
    public Party createParty(Party party) {
        return super.create(party);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Deletes the specified party.
     *
     * @param party
     * @return true if deleted successfully
     */
    public boolean deleteParty(Party party) {

        boolean deleted;

        // todo: delete related parties?
//        if (account.hasAnyLicenseReserved()) {
//            // if the account has any licenses reserved, unreserve them
//            List<License> licenses = account.getReservedLicenses();
//            for (License license : licenses) {
//                schedule.unreserveLicense(license, account, getDefaultUnallocatedAccount());
//            }
//        }

        deleted = super.delete(party);

        return deleted;
    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="ScheduleOfPartiesEntry",
            joinColumns=@JoinColumn(name="ScheduleOfPartiesId"),
            inverseJoinColumns = @JoinColumn(name="PartyId")            
    )
    protected List<Party> getParties() {
        return super.getDomainObjects();
    }

    protected void setParties(List<Party> parties) {
        super.setDomainObjects(parties);
    }

    // ------------------------------------------------------------------------------------------------------

    @Override
    @OneToOne()
    @JoinColumn(name="EnterpriseId")
    public Enterprise getEnterprise() {
        return super.getEnterprise();
    }
        
    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println("--- Schedule of Parties ---");

        for (Party party : getParties()) {
            party.print(out);
        }
    }

    // ------------------------------------------------------------------------------------------------------
}
