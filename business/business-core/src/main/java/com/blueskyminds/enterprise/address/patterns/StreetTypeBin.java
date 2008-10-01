package com.blueskyminds.enterprise.address.patterns;

import com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.homebyfive.framework.core.patterns.bins.EnumSubstitutionBin;
import com.blueskyminds.enterprise.address.StreetType;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.service.SubstitutionService;

/**
 * Matches words to street types
 *
 * Uses the street types loaded from persistence.
 * Patterns are matched exactly.
 * The metadata for each pattern is used to identify the name of the corresponding StreetType enumeration.
 *
 * Date Started: 18/06/2006
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
public class StreetTypeBin extends EnumSubstitutionBin<StreetType> implements OrderedBin {

    private static final String GROUP_NAME = "StreetType";

    /** The types of bins allowed to the left of this position */
    private static final BinType[] ALLOW_LEFT = {
        BinType.UnitNumberBin, BinType.LotNumberBin, BinType.StreetNumberBin, BinType.StreetNameBin
    };

    /** The types of bins allowed to the right of this position */
    private static final BinType[] ALLOW_RIGHT = {
        BinType.StreetSectionBin, BinType.SuburbNameBin, BinType.StateBin, BinType.PostCodeBin
    };

    public BinType[] getBinTypesAllowedLeft() {
        return ALLOW_LEFT;
    }

    public BinType[] getBinTypesAllowedRight() {
        return ALLOW_RIGHT;
    }

    public StreetTypeBin(SubstitutionService substitutionDAO) throws PatternMatcherInitialisationException {
        super(GROUP_NAME, StreetType.class, substitutionDAO);
    }

    /**
     * Extends the default implementation to ensure that street type names start with a letter
     *
     * @param word
     * @return true if an exclusion was matched
     */
    protected boolean wordMatchesExclusion(String word) {
        if (!Character.isLetter(word.charAt(0))) {
            return true;
        } else {
            return super.wordMatchesExclusion(word);
        }
    }
}