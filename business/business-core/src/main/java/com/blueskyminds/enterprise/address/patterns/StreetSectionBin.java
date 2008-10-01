package com.blueskyminds.enterprise.address.patterns;

import com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.homebyfive.framework.core.patterns.bins.EnumSubstitutionBin;
import com.blueskyminds.enterprise.address.StreetSection;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.service.SubstitutionService;

/**
 *
 * Matches words to street section types
 *
 * Uses the street section types loaded from persistence.
 * Patterns are matched exactly.
 * The metadata for each pattern is used to identify the corresponding StreetSection enumeration. 
 *
 * Date Started: 22/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class StreetSectionBin extends EnumSubstitutionBin<StreetSection> implements OrderedBin {

    private static String GROUP_NAME = "StreetSection";

    /** The types of bins allowed to the left of this position */
    private static final BinType[] ALLOW_LEFT = {
        BinType.UnitNumberBin, BinType.LotNumberBin, BinType.StreetNumberBin, BinType.StreetNameBin, BinType.StreetTypeBin
    };

    /** The types of bins allowed to the right of this position */
    private static final BinType[] ALLOW_RIGHT = {
        BinType.SuburbNameBin, BinType.StateBin, BinType.PostCodeBin
    };

    public BinType[] getBinTypesAllowedLeft() {
        return ALLOW_LEFT;
    }

    public BinType[] getBinTypesAllowedRight() {
        return ALLOW_RIGHT;
    }

    public StreetSectionBin(SubstitutionService substitutionDAO) throws PatternMatcherInitialisationException {
        super(GROUP_NAME, StreetSection.class, substitutionDAO);
    }

    /**
     * Extends the default implementation to ensure that section names start with a letter
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
