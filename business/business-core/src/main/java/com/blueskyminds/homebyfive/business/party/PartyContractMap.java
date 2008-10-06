package com.blueskyminds.homebyfive.business.party;

import com.blueskyminds.homebyfive.business.pricing.Contract;

import javax.persistence.Entity;

/**
 * Maping between a Party and their Contract
 *
 * This has been created to remove the Contract reference from Party and may not be completely thought through
 *
 * Date Started: 18/03/2008
 * <p/>
 * History:
 */
@Entity
public class PartyContractMap {

    private Party party;
    private Contract contract;

    public PartyContractMap(Party party, Contract contract) {
        this.party = party;
        this.contract = contract;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

     /**
     * Indicates whether this Party has a current contract
     * @return true if the party has a contract
     */
//    public boolean hasContract() {
//        return (getCurrentContract() != null);
//    }

    // -------------------------------------------------------------------------------------------------------

    /**
     * Get a reference to the current contract for this account
     * @return Contract
     */
//    @ManyToOne
//    public Contract getCurrentContract() {
//        return currentContract;
//    }
//
//    private void setCurrentContract(Contract contract) {
//        this.currentContract = contract;
//    }

    // -------------------------------------------------------------------------------------------------------

    /**
     * Change the current contract for this account.
     * This event is recorded in the journal.
     *
     * @param newContract
     * @param journal to record the event
     */
    // todo: this doesn't belong here
//     public void changeCurrentContract(Contract newContract, Journal journal) {
//        Contract previousContract = currentContract;
//        setCurrentContract(newContract);
//        journal.changed(this, this, "currentContract", previousContract, newContract, null);
//    }
}
