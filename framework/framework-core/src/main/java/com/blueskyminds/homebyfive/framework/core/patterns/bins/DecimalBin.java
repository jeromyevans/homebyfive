package com.blueskyminds.homebyfive.framework.core.patterns.bins;

import com.blueskyminds.homebyfive.framework.core.patterns.Bin;
import com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherInitialisationException;

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

    /** decimal pattern: optional minus sign, optional single decimal point, optional commas */
    public static final String DECIMAL_PATTERN = "^(\\d|-)?(\\d|,)*\\.?\\d*$";

    public DecimalBin() throws PatternMatcherInitialisationException {
        addRegExPattern(DECIMAL_PATTERN, true, -1, null);
    }

}
