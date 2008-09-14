package com.blueskyminds.enterprise.accounting;

import com.blueskyminds.framework.journal.Journal;
import com.blueskyminds.enterprise.Enterprise;
import com.blueskyminds.enterprise.Schedule;

import java.util.*;
import java.io.PrintStream;

import org.apache.commons.lang.StringUtils;

import javax.persistence.*;

/**
 * Maintains a hierarchical list of Accounts for double-entry book-keeping
 *
 * Date Started: 15/05/2006
 *
 * History:
 * 
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class ChartOfAccounts extends Schedule<Account> {

   /** The default currency for this chart of relationships */
    private Currency defaultCurrency;

    /** A map of the root accounts in this chart of account, separated by type, derived from the schedule of
     * accounts. */
    private Map<AccountTypes, Account> rootAccounts;

    // ------------------------------------------------------------------------------------------------------

    /** Create a new chart of relationships */
    public ChartOfAccounts(Enterprise enterprise, Currency defaultCurrency, Journal journal) {
        super(enterprise, journal);
        this.defaultCurrency = defaultCurrency;
        init();
    }

    /** Default constructor for ORM */
    protected ChartOfAccounts() {

    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the chart of relationships with default attributes */
    private void init() {
        rootAccounts = new HashMap<AccountTypes, Account>();

        rootAccounts.put(AccountTypes.Asset, createRootSummaryAccount(AccountTypes.Asset, "Assets"));
        rootAccounts.put(AccountTypes.Liability, createRootSummaryAccount(AccountTypes.Liability, "Liabilities"));
        rootAccounts.put(AccountTypes.Capital, createRootSummaryAccount(AccountTypes.Capital,  "Capital"));
        rootAccounts.put(AccountTypes.Drawings, createRootSummaryAccount(AccountTypes.Drawings, "Drawings"));
        rootAccounts.put(AccountTypes.Revenue, createRootSummaryAccount(AccountTypes.Revenue, "Revenue"));
        rootAccounts.put(AccountTypes.Expense, createRootSummaryAccount(AccountTypes.Expense, "Expenses"));
    }

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new summary account in this chart of relationships at the root level of the specified type
     *
     * @param name
     * @return an instance of the new summary account
     */
    private SummaryAccount createRootSummaryAccount(AccountTypes type, String name) {
        SummaryAccount summaryAccount = new SummaryAccount(this, name, type);
        super.create(summaryAccount);

        return summaryAccount;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new summary account in this chart of relationships.
     *
     * The account is added to the specified parent account
     * The account type is derived from the parent
     *
     * @param name
     * @return an instance of the new summary account
     */
    public SummaryAccount createSummaryAccount(Account parentAccount, String name) {
        SummaryAccount summaryAccount = new SummaryAccount(this, name, parentAccount.getType());
        if (super.create(summaryAccount) != null) {
            parentAccount.addChild(summaryAccount);
        }

        return summaryAccount;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new detail account in this chart of relationships.
     *
     * The account is added to the specified parent account
     * The account inherits the type of the parent
     *
     * @param name
     * @return an instance of the new summary account
     */
    public DetailAccount createDetailAccount(Account parentAccount, String name) {

        DetailAccount detailAccount = new DetailAccount(this, name, parentAccount.getType());
        if (super.create(detailAccount) != null) {
            parentAccount.addChild(detailAccount);
        }

        return detailAccount;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new detail account in this chart of relationships
     * The account will be created below the parent of the corresonding type
     *
     * @param name
     * @return an instance of the new summary account
     */
    public DetailAccount createAssetAccount(String name) {
        return createDetailAccount(rootAccounts.get(AccountTypes.Asset), name);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new detail account in this chart of relationships
     * The account will be created below the parent of the corresonding type
     *
     * @param name
     * @return an instance of the new detail account
     */
    public DetailAccount createLiabilityAccount(String name) {
        return createDetailAccount(rootAccounts.get(AccountTypes.Liability), name);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new detail account in this chart of relationships
     * The account will be created below the parent of the corresonding type
     * @param name
     * @return an instance of the new detail account
     */
    public DetailAccount createCapitalAccount(String name) {
        return createDetailAccount(rootAccounts.get(AccountTypes.Capital), name);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new detail account in this chart of relationships
     * The account will be created below the parent of the corresonding type
     * @param name
     * @return an instance of the new detail account
     */
    public DetailAccount createDrawingsAccount(String name) {
        return createDetailAccount(rootAccounts.get(AccountTypes.Drawings), name);
    }


    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new detail account in this chart of relationships
     * The account will be created below the parent of the corresonding type
     * @param name
     * @return an instance of the new detail account
     */
    public DetailAccount createRevenueAccount(String name) {
        return createDetailAccount(rootAccounts.get(AccountTypes.Revenue), name);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new detail account in this chart of relationships
     * The account will be created below the parent of the corresonding type
     * @param name
     * @return an instance of the new summary account
     */
    public DetailAccount createExpenseAccount(String name) {
        return createDetailAccount(rootAccounts.get(AccountTypes.Expense), name);
    }

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new summary account in this chart of relationships
     *
     * @param name
     * @return an instance of the new summary account
     */
//    public SummaryAccount createAssetSummaryAccount(String name) {
//        return createSummaryAccount(rootAccounts.get(AccountTypes.Asset), name);
//    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new detail account in this chart of relationships
     *
     * @param name
     * @return an instance of the new detail account
     */
//    public SummaryAccount createLiabilitySummaryAccount(String name) {
//        return createSummaryAccount(rootAccounts.get(AccountTypes.Liability), name);
//    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new detail account in this chart of relationships
     *
     * @param name
     * @return an instance of the new detail account
     */
//    public SummaryAccount createCapitalSummaryAccount(String name) {
//        return createSummaryAccount(rootAccounts.get(AccountTypes.Capital), name);
//    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new detail account in this chart of relationships
     *
     * @param name
     * @return an instance of the new detail account
     */
//    public SummaryAccount createDrawingsSummaryAccount(String name) {
//        return createSummaryAccount(rootAccounts.get(AccountTypes.Drawings), name);
//    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new detail account in this chart of relationships
     *
     * @param name
     * @return an instance of the new detail account
     */
//    public SummaryAccount createRevenueSummaryAccount(String name) {
//        return createSummaryAccount(rootAccounts.get(AccountTypes.Revenue), name);
//    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new detail account in this chart of relationships
     *
     * @param name
     * @return an instance of the new summary account
     */
//    public SummaryAccount createExpenseSummaryAccount(String name) {
//        return createSummaryAccount(rootAccounts.get(AccountTypes.Expense), name);
//    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the default currency for this chart of accounts */
    @Transient
    public Currency getDefaultCurrency() {
        return defaultCurrency;
    }

    /** Get the currency code for the default currency */
    @Basic
    @Column(name="CurrencyCode")
    public String getDefaultCurrencyCode() {
        return defaultCurrency.getCurrencyCode();
    }

    protected void setDefaultCurrencyCode(String currencyCode) {
        defaultCurrency = Currency.getInstance(currencyCode);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get a reference to the financial journal associated with this ChartOfAccounts.
     * The FinancialJournal is obtained from the Enterprise.
     *
     * @return FinancialJournal
     */
    @Transient
    public FinancialJournal getFinancialJournal() {
        return getEnterprise().getFinancialJournal();
    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println("--- Chart of Accounts---");

        for (AccountTypes type : rootAccounts.keySet()) {
            printAccount(out, rootAccounts.get(type));
        }
    }

    // ------------------------------------------------------------------------------------------------------

    private void printAccount(PrintStream out, Account account) {

        if (account instanceof SummaryAccount) {
            out.print("Summary: "+ StringUtils.rightPad(account.getName(), 20));
        }
        else {
            out.print("Detail : "+StringUtils.rightPad(account.getName(), 20));
        }

        if (account.getBalanceType().equals(AccountBalanceTypes.Debit)) {
            out.print("    "+StringUtils.leftPad(account.getBalance().toString(), 8));
        }
        else {
            out.print("                        "+StringUtils.leftPad(account.getBalance().toString(), 8));
        }

        out.println();

        if (account.getChildren() != null) {
            // recurse into children
            for (Account child: account.getChildren()) {
                printAccount(out, child);
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------

    @Transient
    public SummaryAccount getAssets() {
        return (SummaryAccount) rootAccounts.get(AccountTypes.Asset);
    }

    @Transient
    public SummaryAccount getLiabilities() {
        return (SummaryAccount) rootAccounts.get(AccountTypes.Liability);
    }

    @Transient
    public SummaryAccount getCapital() {
        return (SummaryAccount) rootAccounts.get(AccountTypes.Capital);
    }

    @Transient
    public SummaryAccount getDrawings() {
        return (SummaryAccount) rootAccounts.get(AccountTypes.Drawings);
    }

    @Transient
    public SummaryAccount getRevenue() {
        return (SummaryAccount) rootAccounts.get(AccountTypes.Revenue);
    }

    @Transient
    public SummaryAccount getExpenses() {
        return (SummaryAccount) rootAccounts.get(AccountTypes.Expense);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the top-level list of accounts in this chart of accounts */
    @OneToMany(mappedBy="chartOfAccounts", cascade=CascadeType.ALL)
    protected List<Account> getAccounts() {
        return super.getDomainObjects();
    }

    protected void setAccounts(List<Account> accounts) {
        super.setDomainObjects(accounts);

        rootAccounts = new HashMap<AccountTypes, Account>();

        /** Transfer the root nodes into the list back to the hashmap for fast retrieval*/
        for (Account account : accounts) {
            if (account.getParent() == null) {
                rootAccounts.put(account.getType(), account);
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    @Override
    @OneToOne()
    @JoinColumn(name="EnterpriseId")
    public Enterprise getEnterprise() {
        return super.getEnterprise();
    }
        
    // ------------------------------------------------------------------------------------------------------

}
