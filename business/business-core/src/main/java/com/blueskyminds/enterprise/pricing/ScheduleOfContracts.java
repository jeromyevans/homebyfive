package com.blueskyminds.enterprise.pricing;

import com.blueskyminds.enterprise.Enterprise;
import com.blueskyminds.enterprise.Schedule;
import com.blueskyminds.framework.journal.Journal;

import javax.persistence.*;
import java.io.PrintStream;
import java.util.List;

/**
 * A schedule of Contracts
 *
 * Date Started: 14/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class ScheduleOfContracts extends Schedule<Contract> {

    // ------------------------------------------------------------------------------------------------------

    /** Create a new schedule of Contracts */
    public ScheduleOfContracts(Enterprise enterprise, Journal journal) {
        super(enterprise, journal);
    }

    protected ScheduleOfContracts() {

    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create the specified contract
     * @return the contract if added successfully, otherwise null
     */
    public Contract createContract(Contract contract) {
        return super.create(contract);
    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="ScheduleOfContractsEntry",
            joinColumns=@JoinColumn(name="ScheduleOfContractsId"),
            inverseJoinColumns = @JoinColumn(name="ContractId")
    )
    protected List<Contract> getContracts() {
        return super.getDomainObjects();
    }

    protected void setContracts(List<Contract> contracts) {
        super.setDomainObjects(contracts);
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
        out.println("--- Schedule of Contracts ---");

        for (Contract contract : getContracts()) {
            contract.print(out);
        }
    }
}
