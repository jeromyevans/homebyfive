package com.blueskyminds.enterprise.address.patterns;

import com.blueskyminds.framework.patterns.*;
import com.blueskyminds.framework.patterns.comparison.NamedStringComparator;
import com.blueskyminds.framework.patterns.comparison.IgnoreCaseComparator;
import com.blueskyminds.framework.patterns.comparison.ExactComparator;
import com.blueskyminds.framework.patterns.comparison.PassComparator;
import com.blueskyminds.enterprise.address.dao.AddressDAO;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.enterprise.region.country.CountryHandle;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

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