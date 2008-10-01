package com.blueskyminds.enterprise.taxpolicy;

import com.blueskyminds.enterprise.Enterprise;
import com.blueskyminds.enterprise.Schedule;
import com.blueskyminds.homebyfive.framework.core.journal.Journal;

import javax.persistence.*;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;

/**
 * Defines all of the taxes in an Enterprise
 *
 * Date Started: 23/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class ScheduleOfTaxes extends Schedule<TaxPolicy> {

    /** A map between the name of a tax policy and the instance */
//    private Map<String, TaxPolicy> taxPolicyMap;

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    public ScheduleOfTaxes(Enterprise enterprise, Journal journal) {
        super(enterprise,  journal);
        init();
    }

    /** Default constructor for ORM */
    protected ScheduleOfTaxes() {

    }

    // -----------------------------------------------------------------------------------------------------

    private void init() {
        //taxPolicyMap = new HashMap<String, TaxPolicy>();
    }

    // -----------------------------------------------------------------------------------------------------

    /** Defines the tax policy in this schedule */
    public void defineTaxPolicy(TaxPolicy taxPolicy) {
        super.create(taxPolicy);
        //taxPolicyMap.put(taxPolicy.getClassName(), taxPolicy);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Gets the tax policy defined by the given name that is applicable at the moment.
     *
     * This is currently a slow iterative search implementation.
     *
     */
    @Transient
    public TaxPolicy getByName(String name) {
        TaxPolicy policyFound = null;

        for (TaxPolicy taxPolicy : getDomainObjects()) {
            if (taxPolicy.getName().equals(name)) {
                if (taxPolicy.isApplicable(new Date())) {
                    policyFound = taxPolicy;
                    break;
                }
            }
        }

        return policyFound;
    }

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="ScheduleOfTaxesEntry",
            joinColumns=@JoinColumn(name="ScheduleOfTaxesId"),
            inverseJoinColumns = @JoinColumn(name="TaxPolicyId")
    )
    protected List<TaxPolicy> getTaxes() {
        return super.getDomainObjects();
    }

    protected void setTaxes(List<TaxPolicy> taxes) {
        super.setDomainObjects(taxes);
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

    public void print(PrintStream out) {
        out.println("--- Schedule of Taxes ---");
        for (TaxPolicy taxPolicy : getTaxes()) {
            taxPolicy.print(out);
        }
    }
}
