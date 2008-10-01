package com.blueskyminds.homebyfive.framework.framework.patterns.measurement.bins;

import com.blueskyminds.homebyfive.framework.framework.patterns.bins.EnumSubstitutionBin;
import com.blueskyminds.homebyfive.framework.framework.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.homebyfive.framework.framework.tools.substitutions.service.SubstitutionService;
import com.blueskyminds.homebyfive.framework.framework.measurement.UnitsOfArea;

/**
 *
 * Matches text to units of area
 *
 * Date Started: 12/01/2007
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class UnitsOfAreaBin extends EnumSubstitutionBin<UnitsOfArea> {

    private static final String AREA_UNITS_GROUP = "UnitsOfArea";

    public UnitsOfAreaBin(SubstitutionService substitutionService) throws PatternMatcherInitialisationException {
        super(AREA_UNITS_GROUP, UnitsOfArea.class, substitutionService);
    }

}
