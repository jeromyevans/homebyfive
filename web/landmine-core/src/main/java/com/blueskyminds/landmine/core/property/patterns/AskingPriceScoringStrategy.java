package com.blueskyminds.landmine.core.property.patterns;

import com.blueskyminds.framework.patterns.scoring.AbstractScoringStrategy;
import com.blueskyminds.framework.patterns.scoring.Score;
import com.blueskyminds.framework.patterns.PhraseToBinAllocation;
import com.blueskyminds.framework.patterns.PhraseToBinAllocationSequence;

import java.util.List;

/**
 * Calculates a Score for a candidate AskingPrice allocation
 *
 * good scores:
 *   lower price only (no upper price)
 *   lower price and an asking price type (no upper price)
 *   lower price before upper price
 *   asking price type only
 *
 * Date Started: 13/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class AskingPriceScoringStrategy extends AbstractScoringStrategy {

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AskingPriceScoringStrategy with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------


    public Score score(PhraseToBinAllocationSequence allocationSequence, int totalWords) {

        int score = 0;

        if ((testIncludes(allocationSequence, LowerPriceBin.class)) && (testExcludes(allocationSequence, UpperPriceBin.class, AskingPriceTypesBin.class))) {
            score += 1;
        }
        if ((testIncludes(allocationSequence, LowerPriceBin.class, AskingPriceTypesBin.class)) && (testExcludes(allocationSequence, UpperPriceBin.class))) {
            score += 1;
        }

        switch (testSequence(allocationSequence, LowerPriceBin.class, UpperPriceBin.class)) {
            case FIRST_ADJACENT_TO_SECOND:
                score += 1;
                break;
            case FIRST_BEFORE_SECOND:
                score += 1;
                break;
            case FIRST_ONLY:
                score += 1;
                break;
            case NONE:
                break;
            case SECOND_BEFORE_FIRST:
                break;
            case SECOND_ONLY:
                break;
        }
        if ((testIncludes(allocationSequence, AskingPriceTypesBin.class)) & (testExcludes(allocationSequence, LowerPriceBin.class, UpperPriceBin.class))) {
            score += 1;
        }

        // todo: finish and test this scoring algorithm - the rules are non-exclusive so need to think about best approach for this
        
        return new Score(score);
    }
}
