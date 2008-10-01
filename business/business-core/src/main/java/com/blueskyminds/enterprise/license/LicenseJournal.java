package com.blueskyminds.enterprise.license;

import com.blueskyminds.enterprise.Enterprise;
import com.blueskyminds.enterprise.Schedule;
import com.blueskyminds.enterprise.party.Party;
import com.blueskyminds.homebyfive.framework.core.journal.Journal;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A journal recording all changes to licenses within the enterpreise.
 * Records transfer of License's between LicenseAccount's
 *
 * Date Started: 1/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class LicenseJournal extends Schedule<LicenseTransaction> {

    // ------------------------------------------------------------------------------------------------------

    public LicenseJournal(Enterprise enterprise, Journal journal) {
        super(enterprise, journal);
        init();
    }

    /** Default constructor for ORM */
    protected LicenseJournal() {

    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the journal with default attributes */
    private void init() {

    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

     /** Create a new transaction for reserving a license and commits it */
    public LicenseTransaction createReserveTransaction(Date dateTime, LicenseAccount fromAccount, LicenseAccount toAccount, License license, String note, Party party) throws IllegalTransactionException {
        LicenseTransaction licenseTransaction = new LicenseTransaction(this, dateTime, note, party);

        licenseTransaction.addReleaseEntry(fromAccount, license);
        licenseTransaction.addReserveEntry(toAccount,  license);
        licenseTransaction.commit();

        // add the financial transaction to this journal
        licenseTransaction = create(licenseTransaction);

        return licenseTransaction;
    }

// ------------------------------------------------------------------------------------------------------

    /** Create a new transaction for unreserving a license and commits it.
     * This method requires that the toAccount is a special-case UnallocatedAccount */
   public LicenseTransaction createUnreserveTransaction(Date dateTime, LicenseAccount fromAccount, UnallocatedAccount toAccount, License license, String note, Party party) throws IllegalTransactionException {
       LicenseTransaction licenseTransaction = new LicenseTransaction(this, dateTime, note, party);

       licenseTransaction.addReleaseEntry(fromAccount, license);
       licenseTransaction.addAllocateEntry(toAccount,  license);
       licenseTransaction.commit();

       // add the financial transaction to this journal
       licenseTransaction = create(licenseTransaction);

       return licenseTransaction;
   }

    // ------------------------------------------------------------------------------------------------------

    /** Create a new transaction for unreserving a license and commits it */
   public LicenseTransaction createTransferTransaction(Date dateTime, LicenseAccount fromAccount, LicenseAccount toAccount, License license, String note, Party party) throws IllegalTransactionException {
       LicenseTransaction licenseTransaction = new LicenseTransaction(this, dateTime, note, party);

       licenseTransaction.addReleaseEntry(fromAccount, license);
       licenseTransaction.addAllocateEntry(toAccount,  license);
       licenseTransaction.commit();

       // add the financial transaction to this journal
       licenseTransaction = create(licenseTransaction);

       return licenseTransaction;
   }

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    @OneToMany(mappedBy="licenseJournal", cascade = CascadeType.ALL)
    protected List<LicenseTransaction> getTransactions() {
        return super.getDomainObjects();
    }

    public void setTransactions(List<LicenseTransaction> transactions) {
        super.setDomainObjects(transactions);
    }

    // ------------------------------------------------------------------------------------------------------

    @Override
    @OneToOne()
    @JoinColumn(name="EnterpriseId")
    public Enterprise getEnterprise() {
        return super.getEnterprise();
    }

    public void print(PrintStream out) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        out.println("--- License Journal ---");
        out.println(" Date      |");

        Collection<LicenseTransaction> unsortedTransactions = getTransactions();
        Object[] sortedTransactions = unsortedTransactions.toArray();
        Arrays.sort(sortedTransactions, new Comparator() {

            public int compare(Object o1, Object o2) {
                if ((o1 instanceof LicenseTransaction) && (o2 instanceof LicenseTransaction)) {
                    LicenseTransaction a1 = (LicenseTransaction) o1;
                    LicenseTransaction a2 = (LicenseTransaction ) o2;

                    return (a1.getDate().compareTo(a2.getDate()));
                }
                else {
                    return -1;
                }
            }
        }
        );

        for (Object sortedTransaction : sortedTransactions) {
            LicenseTransaction transaction = (LicenseTransaction) sortedTransaction;

            out.println(dateFormat.format(transaction.getDate()) + " "+transaction.getParty().getIdentityName()+": "+transaction.getNote());
            for (LicenseEntry entry : transaction.getEntries()) {
                out.print("          ");

                if ((entry.getType().equals(LicenseEntryTypes.Unreserve)) || (entry.getType().equals(LicenseEntryTypes.Release))) {
                    out.print(entry.getAccount().getIdentityName()+" "+entry.getType());
                }
                else {
                    out.print(StringUtils.leftPad(entry.getAccount().getIdentityName()+" "+entry.getType(), 30));
                }

                out.println();
            }
            out.println();
        }

    }

    // ------------------------------------------------------------------------------------------------------
}
