package com.blueskyminds.landmine.core.property.patterns;

import com.blueskyminds.framework.patterns.bins.EnumSubstitutionBin;
import com.blueskyminds.framework.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.framework.tools.substitutions.dao.SubstitutionDAO;
import com.blueskyminds.framework.tools.substitutions.service.SubstitutionService;
import com.blueskyminds.landmine.core.property.PropertyTypes;

/**
 * Contains text identifying a PropertyType
 *
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class PropertyTypesBin extends EnumSubstitutionBin<PropertyTypes> {

    private static final String GROUP_NAME = "PropertyTypes";

    public PropertyTypesBin(SubstitutionService substitutionService) throws PatternMatcherInitialisationException {
        super(GROUP_NAME, PropertyTypes.class, substitutionService);
    }

    // ------------------------------------------------------------------------------------------------------
}
