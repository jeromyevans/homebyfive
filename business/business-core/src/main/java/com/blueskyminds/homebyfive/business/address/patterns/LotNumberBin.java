package com.blueskyminds.homebyfive.business.address.patterns;

import com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.homebyfive.framework.core.patterns.bins.RegExSubstitutionBin;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.service.SubstitutionService;

/**
 * Matches words to lot number patterns
 *
 * Uses regular expressions loaded from persistence to match the lot number.
 * A group within the pattern is used to extract the exact lot number value.
 * The group number is defined in the metadata.
 *
 * eg. For the phrase Lot 24, this bin will extract the value '24' and return this as the substitution
 *
 * Date Started: 24/06/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
public class LotNumberBin extends RegExSubstitutionBin implements OrderedBin {

    private static final String GROUP_NAME = "LotNumber";

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

    public LotNumberBin(SubstitutionService substitutionDAO) throws PatternMatcherInitialisationException {
        super(GROUP_NAME, substitutionDAO);
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
}