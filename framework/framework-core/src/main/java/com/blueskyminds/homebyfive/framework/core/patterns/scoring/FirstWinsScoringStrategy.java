package com.blueskyminds.homebyfive.framework.core.patterns.scoring;

import com.blueskyminds.homebyfive.framework.core.patterns.PhraseToBinAllocationSequence;

/**
 * Scores 1 to the first allocation and 0 to every other allocation
 *
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class FirstWinsScoringStrategy extends AbstractScoringStrategy {

    private static final int SCORE = 1;
    private boolean firstScore;

    public FirstWinsScoringStrategy() {
        reset();
    }

    public Score score(PhraseToBinAllocationSequence allocationSequence, int totalWords) {
        if (firstScore) {
            firstScore = false;
            return new Score(SCORE);
        } else {
            return new Score(0);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    public void reset() {
        firstScore = true;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
}
