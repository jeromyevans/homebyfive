package com.blueskyminds.homebyfive.business.address.patterns;

import com.blueskyminds.homebyfive.framework.core.patterns.scoring.ScoringStrategy;
import com.blueskyminds.homebyfive.framework.core.patterns.scoring.Score;
import com.blueskyminds.homebyfive.framework.core.patterns.scoring.AbstractScoringStrategy;
import com.blueskyminds.homebyfive.framework.core.patterns.PhraseToBinAllocationSequence;
import com.blueskyminds.homebyfive.framework.core.patterns.PhraseSequence;

/**
 * Date Started: 31/05/2008
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class SuburbScoringStrategy extends AbstractScoringStrategy implements ScoringStrategy {

    public Score score(PhraseToBinAllocationSequence allocationSequence, int totalWords) {
        int score = scoreSuburb(allocationSequence);
        return new Score(score);
    }

    /** Calculates a Score for the suburb */
    private int scoreSuburb(PhraseToBinAllocationSequence allocationSequence) {
        PhraseSequence result;
        int firstScore = -1;

        // suburb before state
        result = testSequence(allocationSequence, SuburbNameBin.class, StateBin.class);

        switch (result) {
            case FIRST_BEFORE_SECOND:
                firstScore = GOOD;
                break;
            case FIRST_ADJACENT_TO_SECOND:
                firstScore = VERY_GOOD;
                break;
            case FIRST_ONLY:
                firstScore = GOOD;
                break;
            case NONE:
                firstScore = VERY_BAD;
                break;
            case SECOND_BEFORE_FIRST:
                firstScore = VERY_BAD;
                break;
            case SECOND_ONLY:
                firstScore = BAD;
                break;
        }
        return firstScore;
    }
}
