package com.blueskyminds.homebyfive.business.accounting.cashflow;

import com.blueskyminds.homebyfive.business.pricing.Money;

import javax.persistence.Transient;
import java.util.Set;

/**
 * Date Started: 17/03/2009
 */
public class ProfitLossStatement {

    private Set<ProfitLossStatementEntry> income;
    private Set<ProfitLossStatementEntry> costOfSales;

    private Set<ProfitLossStatementEntry> expense;


    private void calculateTotalIncome() {
        Money total = new Money();
//        for (ProfitLossStatementEntry entry : income) {
//            total.add(entry.getAmount());
//        }
    }

//    @Transient
//    public Money getTotalIncome() {
//
//    }
}
