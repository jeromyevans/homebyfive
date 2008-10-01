package com.blueskyminds.enterprise.accounting;

import com.blueskyminds.enterprise.Enterprise;
import com.blueskyminds.enterprise.Schedule;
import com.blueskyminds.enterprise.party.Party;
import com.blueskyminds.enterprise.pricing.Money;
import com.blueskyminds.homebyfive.framework.core.journal.Journal;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A FinancialJournal is a schedule of FinancialTransaction's.
 *
 * A FinacialJournal records all financial transactions
 *
 * Date Started: 19/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class FinancialJournal extends Schedule<FinancialTransaction> {

    /** List of transactions created by the journal that haven't been committed yet.  Used to ensure
     * the journal doesn't commit a transaction it created itself
     */
    private Collection<FinancialTransaction> uncommittedTransactions;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise a new FinancialJournal
     */
    public FinancialJournal(Enterprise enterprise, Journal journal) {
        super(enterprise, journal);
        init();
    }

    /** Default constructor for ORM */
    protected FinancialJournal() {

    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the journal with default attributes */
    private void init() {
        uncommittedTransactions = new LinkedList<FinancialTransaction>();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a simple transaction in the journal consisting of a single debit and credit between two
     * relationships for a single party.
     *
     * @param dateTime
     * @return the FinancialTransaction instance that was created, or null if it wasn't added
     */
    public FinancialTransaction createSimpleTransaction(Date dateTime, Account debitAccount, Account creditAccount, Money amount, String note, Party party) throws FinancialTransactionException {
        FinancialTransaction financialTransaction = null;
        try {
            financialTransaction = new FinancialTransaction(this, dateTime, debitAccount, creditAccount, amount, note, party);

            // add the financial transaction to this journal
            financialTransaction = create(financialTransaction);

        } catch (FinancialTransactionException e) {
            throw new FinancialTransactionException("Failed to create a FinancialJournal transaction", e);
        }

        return financialTransaction;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create an uncommitted multi-legged transaction in the journal
     *
     * This uncommitted transaction is stored by the journal to use with the commitTransaction method
     *
     * @param dateTime
     * @return the FinancialTransaction instance that was created, or null if it wasn't added
     */
    public FinancialTransaction createMultileggedTransaction(Date dateTime, String note, Party party) throws FinancialTransactionException {
        FinancialTransaction financialTransaction = null;
        try {
            financialTransaction = new FinancialTransaction(this, dateTime, note, party);

            // add to the list of uncommitted transactions
            uncommittedTransactions.add(financialTransaction);

        } catch (FinancialTransactionException e) {
            throw new FinancialTransactionException("Failed to create a FinancialJournal transaction", e);
        }

        return financialTransaction;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Commits the FinanicalTransaction that was created by this Journal.
     * The FinancialTransaction must not be committed already
     * Cannot be used on FinancialTransactions created outside this journal.
     *
     * @param financialTransaction
     * @return financialTransaction after committing to the journal, or null if commit failed
     */
    public FinancialTransaction commitTransaction(FinancialTransaction financialTransaction) {

        boolean commited = false;

        if (uncommittedTransactions.contains(financialTransaction)) {
            if (!financialTransaction.isCommitted()) {
                // remove from the uncommittted list
                uncommittedTransactions.remove(financialTransaction);

                try {
                    financialTransaction.commitTransaction();

                    // add the financial transaction to this journal properly
                    financialTransaction = create(financialTransaction);
                    commited = true;
                }
                catch (FinancialTransactionException e) {
                    // could not commit the transaction
                }
            }
        }

        if (commited) {
            return financialTransaction;
        }
        else {
            return null;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    @OneToMany(mappedBy="financialJournal", cascade = CascadeType.ALL)
    protected List<FinancialTransaction> getTransactions() {
        return super.getDomainObjects();
    }

    public void setTransactions(List<FinancialTransaction> transactions) {
        super.setDomainObjects(transactions);
    }

    // ------------------------------------------------------------------------------------------------------

    @Override
    @OneToOne()
    @JoinColumn(name="EnterpriseId")
    public Enterprise getEnterprise() {
        return super.getEnterprise();
    }
    
    // ------------------------------------------------------------------------------------------------------

     /**
     * Print the journal
     */
    public void print(PrintStream out) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        out.println("--- Journal ---");
        out.println(" Date      | Party             |  Debit  |  Credit |");

        Collection<FinancialTransaction> unsortedTransactions = getTransactions();
        Object[] sortedTransactions = unsortedTransactions.toArray();
        Arrays.sort(sortedTransactions, new Comparator() {

            public int compare(Object o1, Object o2) {
                if ((o1 instanceof FinancialTransaction) && (o2 instanceof FinancialTransaction)) {
                    FinancialTransaction a1 = (FinancialTransaction) o1;
                    FinancialTransaction a2 = (FinancialTransaction ) o2;

                    return (a1.getDate().compareTo(a2.getDate()));
                }
                else {
                    return -1;
                }
            }
        }
        );

        for (Object sortedTransaction : sortedTransactions) {
            FinancialTransaction transaction = (FinancialTransaction) sortedTransaction;

            out.println(dateFormat.format(transaction.getDate()) + " "+transaction.getParty().getIdentityName()+": "+transaction.getNote());
            for (AccountEntry entry : transaction.getEntries()) {
                out.print("          ");
                if (entry.getType().equals(AccountEntryTypes.Debit)) {
                    out.print(StringUtils.rightPad(entry.getAccount().getName(), 20));
                }
                else {
                    out.print(StringUtils.rightPad("   "+entry.getAccount().getName(), 20));
                }

                if (entry.getType().equals(AccountEntryTypes.Debit)) {
                    out.print(entry.getAmount().toString());
                } else {
                    out.print(StringUtils.leftPad(entry.getAmount().toString(), 20));
                }
                out.println();
            }
            out.println();
        }

    }

    // ------------------------------------------------------------------------------------------------------
}
