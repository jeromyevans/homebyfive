package com.blueskyminds.enterprise.address.patterns;

import com.blueskyminds.framework.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.framework.patterns.bins.RegExSubstitutionBin;
import com.blueskyminds.framework.tools.substitutions.service.SubstitutionService;

/**
 * Matches words to unit number patterns
 *
 * Uses regular expressions loaded from persistence to match the unit number.
 * A group within the pattern is used to extract the exact unit number value.
 * The group number is defined in the metadata.
 *
 * eg. For the phrase 1/22, this bin will extract the value '1' and return this as the substitution
 *
 * IMPORTANT NOTE: The allocation for this match is NON-EXCLUSIVE, meaning the phrase is permitted to be
 *   used in other bins within the same result.  This is because the phrase may be split into two parts,
 *    one for the unit number and one for the street number
 *
 * Date Started: 22/06/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/> 
 */
public class UnitNumberBin extends RegExSubstitutionBin implements OrderedBin {

    private static final String GROUP_NAME = "UnitNumber";

    /** The types of bins allowed to the left of this position */
    private static final BinType[] ALLOW_LEFT = {
    };

    /** The types of bins allowed to the right of this position */
    private static final BinType[] ALLOW_RIGHT = {
        BinType.StreetNumberBin, BinType.StreetNameBin, BinType.StreetTypeBin, BinType.StreetSectionBin, BinType.SuburbNameBin, BinType.StateBin, BinType.PostCodeBin
    };

    public BinType[] getBinTypesAllowedLeft() {
        return ALLOW_LEFT;
    }

    public BinType[] getBinTypesAllowedRight() {
        return ALLOW_RIGHT;
    }

    public UnitNumberBin(SubstitutionService substitutionDAO) throws PatternMatcherInitialisationException {
        super(GROUP_NAME, substitutionDAO);
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
}
