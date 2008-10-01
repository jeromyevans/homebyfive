package com.blueskyminds.homebyfive.framework.framework.patterns.scoring;

import com.blueskyminds.homebyfive.framework.framework.patterns.PhraseToBinAllocationSequence;

/**
 *
 * An interface to scrore a sequence of phrase to bin allocations.  The getScore is used to find the best
 * candidate sequence.
 *
 * Date Started: 23/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public interface ScoringStrategy {

    /** Calculate a score for the given sequence of phrases.
     * @param allocationSequence
     * @param totalWords - the total number of words in the orginal input string
     * @return the score
     */
    Score score(PhraseToBinAllocationSequence allocationSequence, int totalWords);
    
}
