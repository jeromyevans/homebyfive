package com.blueskyminds.homebyfive.framework.framework.patterns.measurement;

import com.blueskyminds.homebyfive.framework.framework.patterns.scoring.AbstractScoringStrategy;
import com.blueskyminds.homebyfive.framework.framework.patterns.scoring.Score;
import com.blueskyminds.homebyfive.framework.framework.patterns.PhraseToBinAllocationSequence;
import com.blueskyminds.homebyfive.framework.framework.patterns.PhraseSequence;
import com.blueskyminds.homebyfive.framework.framework.patterns.measurement.bins.UnitsOfAreaBin;
import com.blueskyminds.homebyfive.framework.framework.patterns.bins.DecimalBin;

/**
 * Scores pattern matcher results for Area
 * Basicically
 *   if it contains an amount, followed by unts, it's good
 *
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class AreaScoringStrategy extends AbstractScoringStrategy {

     // ------------------------------------------------------------------------------------------------------

    /**  If it contains an amount, followed by unts, it's very good, it it's just an amount it's good */
    public Score score(PhraseToBinAllocationSequence allocationSequence, int totalWords) {
        PhraseSequence sequence = testSequence(allocationSequence, DecimalBin.class, UnitsOfAreaBin.class);
        int score = -1;

        switch (sequence) {
            case FIRST_ADJACENT_TO_SECOND:
                score = VERY_GOOD;
                break;
            case FIRST_BEFORE_SECOND:
                score = BAD;
                break;
            case FIRST_ONLY:
                score = GOOD;
                break;
            case NONE:
                score = BAD;
                break;
            case SECOND_BEFORE_FIRST:
                score = EXTREMELY_BAD;
                break;
            case SECOND_ONLY:
                score = EXTREMELY_BAD;
                break;
        }
        return new Score(score);
    }
}
