package com.blueskyminds.framework.patterns.scoring;

import com.blueskyminds.framework.patterns.*;

import java.util.List;

/**
 * Provides some helpful methods for scoring the pattern matches
 *
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public abstract class AbstractScoringStrategy implements ScoringStrategy {

    // ------------------------------------------------------------------------------------------------------

    protected static final int VERY_GOOD = 2;
    protected static final int GOOD = 1;
    protected static final int INDIFFERENT = 0;
    protected static final int BAD = -1;
    protected static final int VERY_BAD= -5;
    protected static final int EXTREMELY_BAD= -50;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AbstractScoringStrategy with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns an enumeration depending on the relative position of two bin allocations in a sequence.
     *   eg. First before second
     *       second only
     *       First adjacent to second (on the left) etc
     *
     * The enumeration can be used to score the sequence
     **/
    protected <F extends Bin, S extends Bin> PhraseSequence testSequence(PhraseToBinAllocationSequence allocationSequence, Class<F> firstBinClass, Class<S> secondBinClass) {
        return allocationSequence.testSequence(firstBinClass, secondBinClass);
    }

    /** Determine if the sequence contains a bin of the specified class */
    protected boolean contains(PhraseToBinAllocationSequence allocationSequence, Class<? extends Bin> binClass) {
        return allocationSequence.contains(binClass);
    }

    /**
     * Returns the pattern for the bin of the specified class.
     *
     * @param allocationSequence
     * @param binClass
     * @return null if there's no matching bin
     */
    protected Pattern getPattern(PhraseToBinAllocationSequence allocationSequence, Class<? extends Bin> binClass) {
        PhraseToBinAllocation allocation = allocationSequence.getAllocationTo(binClass);
        if (allocation != null) {
           return allocation.getPattern();
        } else {
            return null;
        }
    }

    /**
     * Assert that the sequence includes all the toIncludes
     *
     * @param allocationSequence
     * @param toInclude
     * @return
     */
    protected boolean testIncludes(PhraseToBinAllocationSequence allocationSequence, Class<? extends Bin>... toInclude) {
        boolean okay = true;

        for (Class<? extends Bin> bin : toInclude) {
            okay &= contains(allocationSequence, bin);
        }

        return okay;
    }

    /**
     * Assert that the sequence excludes all toExclude
     * @param allocationSequence  
     * @param toExclude
     * @return true if all excluded
     */
    protected boolean testExcludes(PhraseToBinAllocationSequence allocationSequence, Class<? extends Bin>... toExclude) {
        boolean okay = true;

        for (Class<? extends Bin> bin : toExclude) {
            okay &= !contains(allocationSequence, bin);
        }

        return okay;
    }

}
