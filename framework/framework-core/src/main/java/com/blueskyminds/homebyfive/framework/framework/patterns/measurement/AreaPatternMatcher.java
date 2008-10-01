package com.blueskyminds.homebyfive.framework.framework.patterns.measurement;

import com.blueskyminds.homebyfive.framework.framework.patterns.PatternMatcher;
import com.blueskyminds.homebyfive.framework.framework.patterns.scoring.ScoringStrategy;
import com.blueskyminds.homebyfive.framework.framework.patterns.CandidateAllocation;
import com.blueskyminds.homebyfive.framework.framework.patterns.PhraseToBinAllocation;
import com.blueskyminds.homebyfive.framework.framework.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.homebyfive.framework.framework.patterns.measurement.bins.UnitsOfAreaBin;
import com.blueskyminds.homebyfive.framework.framework.patterns.bins.DecimalBin;
import com.blueskyminds.homebyfive.framework.framework.tools.substitutions.service.SubstitutionService;
import com.blueskyminds.homebyfive.framework.framework.measurement.Area;
import com.blueskyminds.homebyfive.framework.framework.measurement.UnitsOfArea;

/**
 * A pattern matcher that can decompose an area string down into its components
 * <p/>
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class AreaPatternMatcher extends PatternMatcher<Area> {

    // ------------------------------------------------------------------------------------------------------
    private UnitsOfAreaBin unitsOfAreaBin;
    private DecimalBin amountBin;
    private SubstitutionService substitutionService;

    public AreaPatternMatcher(SubstitutionService substitutionService) throws PatternMatcherInitialisationException {
        this(new AreaScoringStrategy(), substitutionService);
    }

    public AreaPatternMatcher(ScoringStrategy scoringStategy, SubstitutionService substitutionService) throws PatternMatcherInitialisationException  {
        super(scoringStategy);
        this.substitutionService = substitutionService;
        init();
    }

    /**
     * Initialise the AreaPatternMatcher with default attributes
     */
    private void init() throws PatternMatcherInitialisationException {
        amountBin = new DecimalBin();
        unitsOfAreaBin = new UnitsOfAreaBin(substitutionService);
        addBin(amountBin);
        addBin(unitsOfAreaBin);
    }

    // ------------------------------------------------------------------------------------------------------


    // ------------------------------------------------------------------------------------------------------

    private Float extractAmount(PhraseToBinAllocation allocation) {
        Float amount = Float.NaN;
        try {
            amount = Float.parseFloat(extractValue(allocation));
        } catch (NumberFormatException e) {
            // ignore invalid
        }
        return amount;
    }

    private UnitsOfArea extractUnitsOfArea(PhraseToBinAllocation allocation) {
        return extractMetadata(UnitsOfArea.class, allocation);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * This method is called for each candidate allocation made by the PatternMatcher, ordered by Score until a
     *  non-null result is returned.
     */
    protected Area extractCandidate(CandidateAllocation candidate) {
        Area area ;

        float amount = extractAmount(candidate.getAllocationForBin(DecimalBin.class));
        UnitsOfArea units = extractUnitsOfArea(candidate.getAllocationForBin(UnitsOfAreaBin.class));

        area = new Area(amount, units);

        return area;
    }


    // ------------------------------------------------------------------------------------------------------


}
