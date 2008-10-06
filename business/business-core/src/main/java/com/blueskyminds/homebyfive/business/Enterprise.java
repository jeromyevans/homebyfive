package com.blueskyminds.homebyfive.business;

import com.blueskyminds.homebyfive.business.pricing.*;
import com.blueskyminds.homebyfive.business.taxpolicy.ScheduleOfTaxes;
import com.blueskyminds.homebyfive.business.taxpolicy.TaxPolicy;
import com.blueskyminds.homebyfive.business.party.ScheduleOfParties;
import com.blueskyminds.homebyfive.business.party.SystemParty;
import com.blueskyminds.homebyfive.business.party.Party;
import com.blueskyminds.homebyfive.framework.core.journal.Journal;
import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;
import com.blueskyminds.homebyfive.business.accounting.FinancialJournal;
import com.blueskyminds.homebyfive.business.accounting.ChartOfAccounts;
import com.blueskyminds.homebyfive.business.accounting.Account;
import com.blueskyminds.homebyfive.business.accounting.DetailAccount;

import javax.persistence.*;
import java.util.Currency;
import java.io.PrintStream;

/**
 * An Enterprise manages all of the operations for a business enterprise
 *
 * An enterprise has:
 *   ScheduleOfParties
 *   ScheduleOfContracts
 *   ProductList
 *
 * An Enterprise subclass should implement:
 *   a Schedule of Products particular to that Enterprise
 *   a Schedule of Accounts particular to that Enterprise
 *
 * Date Started: 14/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
//@Inheritance(strategy = InheritanceType.JOINED)
public class Enterprise extends AbstractDomainObject {

    /** A human-readable name given to this enterprise */
    private String name;

    /** The schedule of parties defined in this Enterprise */
    private ScheduleOfParties scheduleOfParties;

    /** The schedule of Contracts defined in this Enterprise */
    private ScheduleOfContracts scheduleOfContracts;

    /** The list of products defined in this Enterprise */
    private ProductList productList;

    /** The financial journal for this Enterprise */
    private FinancialJournal financialJournal;

    /** The chart of accounts for this Enterprise */
    private ChartOfAccounts chartOfAccounts;

     /** The AccountsReceiveable account */
    private DetailAccount accountsReceivable;

    private DetailAccount taxLiabilityAccount;

    private DetailAccount chequeAccount;

    /** The default currency for this enterprise */
    private Currency defaultCurrency;

    /** The special-purpose party for system */
    private Party systemParty;

    /** A mapping between products and their income/expense relationships in this enterprise's chart of relationships */
    private ProductAccountMap productAccountMap;

    /** A schedule of the taxes defined in this Enterprise */
    private ScheduleOfTaxes scheduleOfTaxes;

    /** A mapping between tax policies and their liability/expense relationships in this enterprise's chart of relationships */
    private TaxAccountMap taxAccountMap;

    /** Orders placed with the enterprise */
    private ScheduleOfOrders scheduleOfOrders;

    /** Invoices entered by the enterprise */
    private ScheduleOfInvoices scheduleOfInvoices;

    /** Receipts issued by the enterprise */
    private ScheduleOfReceipts scheduleOfReceipts;

    /**
     * The journal that's used for record keeping
     */
    private Journal journal;

    // ------------------------------------------------------------------------------------------------------

    public Enterprise(String name, Currency defaultCurrency) {
        init(name, defaultCurrency);
    }

    /** Default constructor for ORM */
    protected Enterprise() {
    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the enterprise with default attributes */
    private void init(String name, Currency defaultCurrency) {
        this.name = name;
        this.defaultCurrency = defaultCurrency;
        journal = new Journal();

        financialJournal = new FinancialJournal(this, journal);
        chartOfAccounts = new ChartOfAccounts(this, defaultCurrency, journal);

        scheduleOfParties = new ScheduleOfParties(this, journal);
        scheduleOfContracts = new ScheduleOfContracts(this, journal);
        productList = new ProductList(this, journal);

        productAccountMap = new ProductAccountMap(this);
        taxAccountMap = new TaxAccountMap(this);
        scheduleOfTaxes = new ScheduleOfTaxes(this, journal);

        systemParty = new SystemParty("System");

        scheduleOfOrders = new ScheduleOfOrders(this, journal);
        scheduleOfInvoices = new ScheduleOfInvoices(this, journal);
        scheduleOfReceipts = new ScheduleOfReceipts(this, journal);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the name of this enterprise */
    @Basic
    @Column(name="Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // ------------------------------------------------------------------------------------------------------
    /** Get the default currency for this enterprise */
    @Transient  // mapped by CurrencyCode
    public Currency getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(Currency defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    /** Get the currency code for the enterprise */
    @Basic
    @Column(name="CurrencyCode")   
    public String getCurrencyCode() {
        return defaultCurrency.getCurrencyCode();
    }

    public void setCurrencyCode(String currencyCode) {
        this.defaultCurrency = Currency.getInstance(currencyCode);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the Party used to represent 'system' */
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="SystemPartyId")
    public Party getSystemParty() {
        return systemParty;
    }

    public void setSystemParty(Party systemParty) {
        this.systemParty = systemParty;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Map the specified product to a revenue account (for sales) in the chart of relationships */
    public boolean mapProductToRevenueAccount(Product product, Account account) {
        return productAccountMap.mapRevenueAccount(product, account);
    }


    // ------------------------------------------------------------------------------------------------------

    /** Map the specified TaxPolicy to a liability account in the chart of relationships */
    public boolean mapTaxPolicyToLiabilityAccount(TaxPolicy taxPolicy, Account account) {
        return taxAccountMap.mapLiabilityAccount(taxPolicy, account);
    }

    // ------------------------------------------------------------------------------------------------------
    /** Get the map between Products and Accounts for different types of transactions */
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="ProductAccountMapId")
    public ProductAccountMap getProductAccountMap() {
        return productAccountMap;
    }

    protected void setProductAccountMap(ProductAccountMap productAccountMap) {
        this.productAccountMap = productAccountMap;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the map between taxes and Accounts */
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="TaxAccountMapId")
    public TaxAccountMap getTaxAccountMap() {
        return taxAccountMap;
    }

    protected void setTaxAccountMap(TaxAccountMap taxAccountMap) {
        this.taxAccountMap = taxAccountMap;
    }

    // ------------------------------------------------------------------------------------------------------

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="ScheduleOfPartiesId")
    public ScheduleOfParties getScheduleOfParties() {
        return scheduleOfParties;
    }

    protected void setScheduleOfParties(ScheduleOfParties scheduleOfParties) {
        this.scheduleOfParties = scheduleOfParties;
    }

    // ------------------------------------------------------------------------------------------------------

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="ScheduleOfContractsId")
    public ScheduleOfContracts getScheduleOfContracts() {
        return scheduleOfContracts;
    }

    public void setScheduleOfContracts(ScheduleOfContracts scheduleOfContracts) {
        this.scheduleOfContracts = scheduleOfContracts;
    }

    // ------------------------------------------------------------------------------------------------------

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="ProductListId")
    public ProductList getProductList() {
        return productList;
    }

    public void setProductList(ProductList productList) {
        this.productList = productList;
    }

    // ------------------------------------------------------------------------------------------------------
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="FinancialJournalId")
    public FinancialJournal getFinancialJournal() {
        return financialJournal;
    }

    public void setFinancialJournal(FinancialJournal financialJournal) {
        this.financialJournal = financialJournal;
    }

    // ------------------------------------------------------------------------------------------------------

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="ChartOfAccountsId")
    public ChartOfAccounts getChartOfAccounts() {
        return chartOfAccounts;
    }

    public void setChartOfAccounts(ChartOfAccounts chartOfAccounts) {
        this.chartOfAccounts = chartOfAccounts;
    }

    // ------------------------------------------------------------------------------------------------------

    @Transient
    public Journal getJournal() {
        return journal;
    }

    // ------------------------------------------------------------------------------------------------------

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="ScheduleOfTaxesId")
    public ScheduleOfTaxes getScheduleOfTaxes() {
        return scheduleOfTaxes;
    }

    public void setScheduleOfTaxes(ScheduleOfTaxes scheduleOfTaxes) {
        this.scheduleOfTaxes = scheduleOfTaxes;
    }

    // ------------------------------------------------------------------------------------------------------

    @Transient
    public DetailAccount getAccountsReceivable() {
        return accountsReceivable;
    }

    public void setAccountsReceivable(DetailAccount accountsReceivable) {
        this.accountsReceivable = accountsReceivable;
    }

    // ------------------------------------------------------------------------------------------------------

    @Transient
    public DetailAccount getTaxLiabilityAccount() {
        return taxLiabilityAccount;
    }

    public void setTaxLiabilityAccount(DetailAccount taxLiabilityAccount) {
        this.taxLiabilityAccount = taxLiabilityAccount;
    }

    // ------------------------------------------------------------------------------------------------------

    @Transient
    public DetailAccount getChequeAccount() {
        return chequeAccount;
    }

    public void setChequeAccount(DetailAccount chequeAccount) {
        this.chequeAccount = chequeAccount;
    }

    // ------------------------------------------------------------------------------------------------------

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="ScheduleOfOrdersId")
    public ScheduleOfOrders getScheduleOfOrders() {
        return scheduleOfOrders;
    }

    protected void setScheduleOfOrders(ScheduleOfOrders scheduleOfOrders) {
        this.scheduleOfOrders = scheduleOfOrders;
    }

    // ------------------------------------------------------------------------------------------------------

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="ScheduleOfInvoicesId")
    public ScheduleOfInvoices getScheduleOfInvoices() {
        return scheduleOfInvoices;
    }

    protected void setScheduleOfInvoices(ScheduleOfInvoices scheduleOfInvoices) {
        this.scheduleOfInvoices = scheduleOfInvoices;
    }

    // ------------------------------------------------------------------------------------------------------

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="ScheduleOfReceiptsId")
    public ScheduleOfReceipts getScheduleOfReceipts() {
        return scheduleOfReceipts;
    }

    protected void setScheduleOfReceipts(ScheduleOfReceipts scheduleOfReceipts) {
        this.scheduleOfReceipts = scheduleOfReceipts;
    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.print(getIdentityName()+"("+getName()+")");
    }
}
