package com.blueskyminds.business.pricing;

import com.blueskyminds.business.Enterprise;
import com.blueskyminds.business.Schedule;
import com.blueskyminds.homebyfive.framework.core.journal.Journal;

import javax.persistence.*;
import java.io.PrintStream;
import java.util.List;

/**
 *
 * A list of receipts in an enterprise
 *
 * Date Started: 14/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class ScheduleOfReceipts extends Schedule<Receipt>  {

    /** Create a new schedule of receipts */
    public ScheduleOfReceipts(Enterprise enterprise, Journal journal) {
        super(enterprise, journal);
    }

    /** Default constructor for ORM */
    protected ScheduleOfReceipts() {

    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create the specified receipt
     * @return the receipt if added successfully, otherwise null
     */
    public Receipt createReceipt(Receipt receipt) {
        return super.create(receipt);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Deletes the specified order.
     * The order can only be deleted if it has not been actioned.
     *
     * @param receipt
     * @return true if deleted successfully
     */
    public boolean deleteReceipt(Receipt receipt) {

        boolean deleted;

        if (!receipt.isActioned()) {
            // mark as deleted
            receipt.delete();
        }

        deleted = super.delete(receipt);

        return deleted;
    }

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="ScheduleOfReceiptsEntry",
            joinColumns=@JoinColumn(name="ScheduleOfReceiptsId"),
            inverseJoinColumns = @JoinColumn(name="ReceiptId")
    )
    protected List<Receipt> getRecipts() {
        return super.getDomainObjects();
    }

    protected void setRecipts(List<Receipt> receipts) {
        super.setDomainObjects(receipts);
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
        out.println("--- "+getIdentityName()+" ---");
        for (Receipt receipt : getRecipts()) {
            receipt.print(out);
        }
    }
}
