package com.blueskyminds.business.address.patterns;

import com.blueskyminds.homebyfive.framework.core.patterns.*;

/**
 * Matches words to australian postcodes using a basic battern "dddd"
 *
 * Date Started: 16/07/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class GreedyPostCodeBin extends Bin implements OrderedBin {

    /** The types of bins allowed to the left of this position */
    private static final BinType[] ALLOW_LEFT = {
        BinType.UnitNumberBin, BinType.LotNumberBin, BinType.StreetNumberBin, BinType.StreetNameBin, BinType.StreetTypeBin, BinType.StreetSectionBin, BinType.SuburbNameBin, BinType.StateBin
    };

    /** The types of bins allowed to the right of this position */
    private static final BinType[] ALLOW_RIGHT = {
        BinType.StateBin
    };

    public BinType[] getBinTypesAllowedLeft() {
        return ALLOW_LEFT;
    }

    public BinType[] getBinTypesAllowedRight() {
        return ALLOW_RIGHT;
    }

    public GreedyPostCodeBin() throws PatternMatcherInitialisationException {
        addRegExPattern("^\\d\\d\\d\\d$", null, true, 0, null);
    }

    /** Must be 3 or 4 digits */
    protected boolean wordMatchesExclusion(String word) {
        if (word.length() < 3 || word.length() > 4) {
            return true;
        } else {
            return super.wordMatchesExclusion(word);
        }
    }

    
}