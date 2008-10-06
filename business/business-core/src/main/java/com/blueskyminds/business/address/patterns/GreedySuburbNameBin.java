package com.blueskyminds.business.address.patterns;

import com.blueskyminds.homebyfive.framework.core.patterns.*;

/**
 * Uses simple patterns to match a suburb name
 *
 * Date Started: 16/07/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class GreedySuburbNameBin extends Bin implements OrderedBin {

    /** The types of bins allowed to the left of this position */
    private static final BinType[] ALLOW_LEFT = {
        BinType.UnitNumberBin, BinType.LotNumberBin, BinType.StreetNumberBin, BinType.StreetTypeBin, BinType.StreetSectionBin,  
    };

    /** The types of bins allowed to the right of this position */
    private static final BinType[] ALLOW_RIGHT = {
        BinType.StateBin, BinType.PostCodeBin
    };

    public BinType[] getBinTypesAllowedLeft() {
        return ALLOW_LEFT;
    }

    public BinType[] getBinTypesAllowedRight() {
        return ALLOW_RIGHT;
    }

    public GreedySuburbNameBin() throws PatternMatcherInitialisationException {
        addRegExPattern("^\\D+$", null, true, 0, null);
    }

}