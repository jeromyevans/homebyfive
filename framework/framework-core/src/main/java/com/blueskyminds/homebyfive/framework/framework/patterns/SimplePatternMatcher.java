package com.blueskyminds.homebyfive.framework.framework.patterns;

import com.blueskyminds.homebyfive.framework.framework.transformer.Transformer;
import com.blueskyminds.homebyfive.framework.framework.patterns.scoring.ScoringStrategy;
import com.blueskyminds.homebyfive.framework.framework.patterns.scoring.FirstWinsScoringStrategy;

/**
 * A simple pattern matcher that matches only one type of data.  This means it uses only one Bin
 *
 * Unless a Transformer is supplied, the result is assumed to be a String.  Subclasses of SimplePatternMatcher
 *  may change this contract
 *
 * If no ScoringStrategy is supplied then FirstWins is used (first candidate is the best candidate)
 *
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class SimplePatternMatcher<T> extends PatternMatcher<T> {

    protected Bin singleBin;
    private Transformer<String, T> transformer;

    public SimplePatternMatcher(Bin singleBin, ScoringStrategy scoringStategy, Transformer<String, T> transformer) throws PatternMatcherInitialisationException {
        super(scoringStategy);
        this.singleBin = singleBin;
        this.transformer = transformer;
        init();
    }

    /**
     *
     * @param singleBin
     * @param scoringStategy
     *
     * No transformer is used for the result - it's assumed the class of the value extracted by the pattern
     *  matcher is the same class as the expected result (T)
     */
    public SimplePatternMatcher(Bin singleBin, ScoringStrategy scoringStategy) throws PatternMatcherInitialisationException {
        super(scoringStategy);
        this.singleBin = singleBin;
        init();
    }

    /** Uses the FirstWins scoring strategy */
    public SimplePatternMatcher(Bin singleBin) throws PatternMatcherInitialisationException  {
        this(singleBin, new FirstWinsScoringStrategy(), null);
    }

    public SimplePatternMatcher(Bin singleBin, Transformer<String, T> transformer) throws PatternMatcherInitialisationException {
        this(singleBin, new FirstWinsScoringStrategy(), transformer);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Register the single bin
     */
    private void init() {
        addBin(singleBin);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * This method is called for each candidate allocation made by the PatternMatcher, ordered by Score until a
     *  non-null result is returned.
     */
    protected T extractCandidate(CandidateAllocation candidate) {
        T result = null;
        String value = extractValue(candidate.getAllocationForBin(singleBin.getClass()));
        if (transformer != null) {
            result = transformer.transform(value);
        } else {
            try {
                result = (T) value;  // nasty - we don't know if this is allowed (should be if setup right)
            } catch (ClassCastException e) {
                // it wasn't allowed!
            }
        }
        return result;
    }

    // ------------------------------------------------------------------------------------------------------

}
