package com.blueskyminds.enterprise.accounting;

import com.blueskyminds.enterprise.pricing.Money;
import com.blueskyminds.homebyfive.framework.framework.AbstractDomainObject;
import com.blueskyminds.enterprise.party.Party;

import javax.persistence.*;
import java.util.Date;

/**
 * An entry is a party of a transaction that changes the balance of an account.
 * An entry can only be instantiated by a FinancialTransaction
 *
 * Date Started: 16/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class AccountEntry extends AbstractDomainObject {

    private FinancialTransaction financialTransaction;

    /** The party associated with the entry */
    private Party party;

    /** The account that this entry belongs to */
    private Account account;

    /** The datetime for this entry */
    private Date date;

    /** Whether this is a credit or debit */
    private AccountEntryTypes type;

    /** The amount of money applied by this entry */
    private Money amount;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create an instance of an AccountEntry ready for inclusion in a financial transaction
     *
     * @param financialTransaction
     * @param party
     * @param account
     * @param date
     * @param type
     * @param amount
     */
    public AccountEntry(FinancialTransaction financialTransaction, Party party, Account account, Date date, AccountEntryTypes type, Money amount) throws FinancialTransactionException {
        this.financialTransaction = financialTransaction;
        this.party = party;
        this.date = date;
        this.account = account;
        this.type = type;
        this.amount = amount;
        validateEntry();
    }

    /** Default constructor for ORM */
    public AccountEntry() {

    }

    // ------------------------------------------------------------------------------------------------------

    /** Validates the values included in this entry */
    private void validateEntry() throws FinancialTransactionException {
        if (financialTransaction == null) {
            throw new FinancialTransactionException("Attempted to create an AccountEntry without a FinancialTransaction");
        }
        if (party == null) {
            throw new FinancialTransactionException("Attempted to create an AccountEntry without an associated Party");
        }
        if (date == null) {
            throw new FinancialTransactionException("Attempted to create an AccountEntry without a timestamp");
        }
        if (account == null) {
            throw new FinancialTransactionException("Attempted to create an AccountEntry without an associated Account");
        }
        if (type == null) {
            throw new FinancialTransactionException("Attempted to create an AccountEntry without a Type");
        }
        if (amount == null) {
            throw new FinancialTransactionException("Attempted to create an AccountEntry without an Amount");
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Attempts to update the amount of this entry by adding the specified amount of money.
     *
     * @param money
     * @return the new amount
     */
    public Money addToAmount(Money money) {
        return amount.sum(money);
    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name="FinancialTransactionId")
    public FinancialTransaction getFinancialTransaction() {
        return financialTransaction;
    }

    protected void setFinancialTransaction(FinancialTransaction financialTransaction) {
        this.financialTransaction = financialTransaction;
    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name="PartyId")
    public Party getParty() {
        return party;
    }

    protected void setParty(Party party) {
        this.party = party;
    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name="AccountId")
    public Account getAccount() {
        return account;
    }

    protected void setAccount(Account account) {
        this.account = account;
    }

    // ------------------------------------------------------------------------------------------------------

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Date")
    public Date getDate() {
        return date;
    }

    protected void setDate(Date date) {
        this.date = date;
    }

    // ------------------------------------------------------------------------------------------------------

    @Enumerated
    @Column(name="Type")
    public AccountEntryTypes getType() {
        return type;
    }

    protected void setType(AccountEntryTypes type) {
        this.type = type;
    }

    // ------------------------------------------------------------------------------------------------------

    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name="amount", column = @Column(name="Amount")),
            @AttributeOverride(name="currencyCode", column = @Column(name="CurrencyCode"))
    })
    public Money getAmount() {
        return amount;
    }

    protected void setAmount(Money amount) {
        this.amount = amount;
    }

    // ------------------------------------------------------------------------------------------------------
}
