package com.blueskyminds.enterprise.address.patterns;

import com.blueskyminds.homebyfive.framework.framework.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.homebyfive.framework.framework.patterns.bins.RegExSubstitutionBin;
import com.blueskyminds.homebyfive.framework.framework.tools.substitutions.service.SubstitutionService;

/**
 * Matches words to street number patterns
 *
 * Uses regular expressions loaded from persistence to match the street number.
 * A group within the pattern is used to extract the exact street number value.
 * The group number is defined in the metadata.
 *
 * eg. For the phrase 1/22, this bin will extract the value '22' and return this as the substitution
 *
 * IMPORTANT NOTE: The allocation for this match is NON-EXCLUSIVE, meaning the phrase is permitted to be
 *   used in other bins within the same result.  This is because the phrase may be split into two parts,
 *   one for the unit number and one for the street number
 *
 * Date Started: 18/06/2006
 * 
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
public class StreetNumberBin extends RegExSubstitutionBin implements OrderedBin {

    private static final String GROUP_NAME = "StreetNumber";

    /** The types of bins allowed to the left of this position */
    private static final BinType[] ALLOW_LEFT = {
        BinType.UnitNumberBin, BinType.LotNumberBin
    };

    /** The types of bins allowed to the right of this position */
    private static final BinType[] ALLOW_RIGHT = {
        BinType.StreetNameBin, BinType.StreetTypeBin, BinType.StreetSectionBin, BinType.SuburbNameBin, BinType.StateBin, BinType.PostCodeBin
    };

    public BinType[] getBinTypesAllowedLeft() {
        return ALLOW_LEFT;
    }

    public BinType[] getBinTypesAllowedRight() {
        return ALLOW_RIGHT;
    }

    public StreetNumberBin(SubstitutionService substitutionDAO) throws PatternMatcherInitialisationException {
        super(GROUP_NAME, substitutionDAO);
    }

    // ------------------------------------------------------------------------------------------------------

}