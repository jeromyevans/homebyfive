package com.blueskyminds.landmine.core.property.patterns;

import com.blueskyminds.landmine.core.property.advertisement.AskingPriceTypes;
import com.blueskyminds.framework.patterns.bins.EnumSubstitutionBin;
import com.blueskyminds.framework.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.framework.tools.substitutions.dao.SubstitutionDAO;
import com.blueskyminds.framework.tools.substitutions.service.SubstitutionService;

/**
 * Bin that contains AskingPriceTypes
 *
 * Date Started: 13/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class AskingPriceTypesBin extends EnumSubstitutionBin<AskingPriceTypes> {

    private static final String GROUP_NAME = "AskingPriceTypes";

    // ------------------------------------------------------------------------------------------------------

    public AskingPriceTypesBin(SubstitutionService substitutionService) throws PatternMatcherInitialisationException {
        super(GROUP_NAME, AskingPriceTypes.class, substitutionService);
    }

    // ------------------------------------------------------------------------------------------------------
}
