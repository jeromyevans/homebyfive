package com.blueskyminds.landmine.core.property.patterns;

import com.blueskyminds.framework.patterns.bins.EnumSubstitutionBin;
import com.blueskyminds.framework.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.framework.tools.substitutions.dao.SubstitutionDAO;
import com.blueskyminds.framework.tools.substitutions.service.SubstitutionService;
import com.blueskyminds.framework.datetime.PeriodTypes;

/**
 * Bin that contains PeriodTypes for prices
 *
 * Date Started: 10/04/2008
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
public class PeriodTypesBin extends EnumSubstitutionBin<PeriodTypes> {

    private static final String GROUP_NAME = "PeriodTypes";

    // ------------------------------------------------------------------------------------------------------

    public PeriodTypesBin(SubstitutionService substitutionService) throws PatternMatcherInitialisationException {
        super(GROUP_NAME, PeriodTypes.class, substitutionService);
    }

    // ------------------------------------------------------------------------------------------------------
}