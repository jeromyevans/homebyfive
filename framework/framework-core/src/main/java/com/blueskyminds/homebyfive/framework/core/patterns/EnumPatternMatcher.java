package com.blueskyminds.homebyfive.framework.core.patterns;

import com.blueskyminds.homebyfive.framework.core.patterns.scoring.ScoringStrategy;
import com.blueskyminds.homebyfive.framework.core.patterns.scoring.FirstWinsScoringStrategy;

/**
 * A special-case pattern matcher that can instantiate an enumeration detected in the input string
 *
 * You beed to supply the bin to use to detect the enumeration
 *
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class EnumPatternMatcher<E extends Enum> extends SimplePatternMatcher<E> {

    private Class<E> enumClass;

    public EnumPatternMatcher(Bin singleBin, ScoringStrategy scoringStategy) throws PatternMatcherInitialisationException  {
        super(singleBin, scoringStategy);
    }

    /**
     * Uses the FirstWinsScoringStrategy
     * @param singleBin the Bin to use to match the enum
     */
    public EnumPatternMatcher(Bin singleBin) throws PatternMatcherInitialisationException {
        this(singleBin, new FirstWinsScoringStrategy());
    }

    protected EnumPatternMatcher(Class<E> enumClass, Bin singleBin, ScoringStrategy scoringStategy) throws PatternMatcherInitialisationException {
        super(singleBin, scoringStategy);
        this.singleBin = singleBin;
        this.enumClass = enumClass;
    }

    /** Instead of extracting the value from the substition, we extract the metadata from the
     * Bin which will be an instance of the enumeration
     *
     * @param candidate
     * @return
     */
    @Override
    protected E extractCandidate(CandidateAllocation candidate) {
        return extractMetadata(enumClass, candidate.getAllocationForBin(singleBin.getClass()));
    }
}
