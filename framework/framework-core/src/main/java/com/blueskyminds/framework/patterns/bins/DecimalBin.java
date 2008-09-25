package com.blueskyminds.framework.patterns.bins;

import com.blueskyminds.framework.patterns.Bin;
import com.blueskyminds.framework.patterns.PatternMatch;
import com.blueskyminds.framework.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.framework.patterns.comparison.PassComparator;

import java.util.Set;

/**
 * Can contain any decimal number
 *
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
public class DecimalBin extends Bin {

    private static final String DECIMAL_PATTERN = "^\\-?\\d+(\\.?\\d+)?$";  // note doesn't allow leading or trailing chars

    public DecimalBin() throws PatternMatcherInitialisationException {
        addRegExPattern(DECIMAL_PATTERN, true, -1, null);
    }

}
