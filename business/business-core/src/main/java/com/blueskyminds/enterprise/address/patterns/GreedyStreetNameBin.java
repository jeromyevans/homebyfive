package com.blueskyminds.enterprise.address.patterns;

import com.blueskyminds.homebyfive.framework.framework.patterns.*;

/**
 * Uses simple patterns to match a street name
 *
 * Date Started: 16/07/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class GreedyStreetNameBin extends Bin implements OrderedBin {

    /** The types of bins allowed to the left of this position */
    private static final BinType[] ALLOW_LEFT = {
        BinType.UnitNumberBin, BinType.LotNumberBin, BinType.StreetNumberBin
    };

    /** The types of bins allowed to the right of this position */
    private static final BinType[] ALLOW_RIGHT = {
        BinType.StreetTypeBin, BinType.StreetSectionBin, BinType.SuburbNameBin, BinType.StateBin, BinType.PostCodeBin
    };

    public BinType[] getBinTypesAllowedLeft() {
        return ALLOW_LEFT;
    }

    public BinType[] getBinTypesAllowedRight() {
        return ALLOW_RIGHT;
    }

    public GreedyStreetNameBin() throws PatternMatcherInitialisationException {
        addRegExPattern("^\\w+$", null, true, 0, null);// allow numbers
    }
   
}