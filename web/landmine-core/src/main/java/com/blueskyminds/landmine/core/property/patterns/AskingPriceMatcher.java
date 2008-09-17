package com.blueskyminds.landmine.core.property.patterns;

import com.blueskyminds.enterprise.pricing.Money;
import com.blueskyminds.framework.patterns.*;
import com.blueskyminds.framework.tools.substitutions.service.SubstitutionService;
import com.blueskyminds.framework.datetime.PeriodTypes;
import com.blueskyminds.landmine.core.property.advertisement.AskingPrice;
import com.blueskyminds.landmine.core.property.advertisement.AskingPriceTypes;
import com.google.inject.Inject;

import java.util.Currency;

/**
 * Extracts the AskingPrice from a string
 *
 * Looks for:
 *    AsingPriceType using the AskingPriceTypeBin
 *    One or Two Decimal values
 *
 * The best batch is selected by the AskingPriceScoringStrategy
 *
 * Date Started: 13/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class AskingPriceMatcher extends PatternMatcher<AskingPrice> {

    private SubstitutionService substitutionService;

    // ------------------------------------------------------------------------------------------------------

    @Inject
    public AskingPriceMatcher(SubstitutionService substitutionService) throws PatternMatcherInitialisationException {
        super(new AskingPriceScoringStrategy());
        this.substitutionService = substitutionService;
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AskingPriceMatcher with default attributes - creates all of the standard pattern
     * matching bins for processing an asking price
     */
    private void init() throws PatternMatcherInitialisationException {

        LowerPriceBin lowerValue = new LowerPriceBin();
        UpperPriceBin upperValue = new UpperPriceBin();
        
        AskingPriceTypesBin askingPriceType = new AskingPriceTypesBin(substitutionService);
        PeriodTypesBin periodTypesBin = new PeriodTypesBin(substitutionService);

        addBin(lowerValue);
        addBin(upperValue);
        addBin(askingPriceType);
        addBin(periodTypesBin);
    }

    // ------------------------------------------------------------------------------------------------------

    private Money extractMoney(PhraseToBinAllocation allocation, Currency currency) {
        Float amount = extractFloat(allocation);
        Money money = null;
        if (amount != null) {
            money = new Money(amount, currency);
        }

        return money;
    }

    private Money extractLowerPrice(PhraseToBinAllocation allocation) {
        return extractMoney(allocation, Currency.getInstance("AUD"));  // todo: AUD assumption
    }

    private Money extractUpperPrice(PhraseToBinAllocation allocation) {
        return extractMoney(allocation, Currency.getInstance("AUD"));  // todo: AUD assumption
    }
    
    private AskingPriceTypes extractAskingPriceType(PhraseToBinAllocation allocation) {
        return extractMetadata(AskingPriceTypes.class, allocation);
    }

    private PeriodTypes extractPeriodType(PhraseToBinAllocation allocation) {
        return extractMetadata(PeriodTypes.class, allocation);
    }

    // ------------------------------------------------------------------------------------------------------

    protected AskingPrice extractCandidate(CandidateAllocation candidateAllocation) throws PatternMatcherException {
        AskingPrice askingPrice = null;

        Money lowerPrice = extractLowerPrice(candidateAllocation.getAllocationForBin(LowerPriceBin.class));
        Money upperPrice = extractUpperPrice(candidateAllocation.getAllocationForBin(UpperPriceBin.class));
        AskingPriceTypes askingPriceType = extractAskingPriceType(candidateAllocation.getAllocationForBin(AskingPriceTypesBin.class));
        PeriodTypes periodType = extractPeriodType(candidateAllocation.getAllocationForBin(PeriodTypesBin.class));

        if (lowerPrice == null) {
            if (upperPrice != null) {
                // switch upper price for lowerprice to simplify the logic
                lowerPrice = upperPrice;
                upperPrice = null;
            }
        }

        if (lowerPrice != null) {
            if (upperPrice != null) {
                // a price range (type is irrelevant - it's a range)
                if (periodType != null) {
                    askingPrice = new AskingPrice(lowerPrice, upperPrice, periodType);
                } else {
                    askingPrice = new AskingPrice(lowerPrice, upperPrice);
                }
            } else {
                if (askingPriceType != null) {
                    if (periodType != null) {
                        askingPrice = new AskingPrice(lowerPrice, periodType, askingPriceType);
                    } else {
                        askingPrice = new AskingPrice(lowerPrice, askingPriceType);
                    }
                } else {
                    if (periodType != null) {
                        askingPrice = new AskingPrice(lowerPrice, periodType);
                    } else {
                        askingPrice = new AskingPrice(lowerPrice);
                    }
                }
            }
        }

        return askingPrice;
    }
}
