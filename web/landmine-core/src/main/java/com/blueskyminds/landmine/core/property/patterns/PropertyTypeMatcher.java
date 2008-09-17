package com.blueskyminds.landmine.core.property.patterns;

import com.blueskyminds.framework.tools.substitutions.dao.SubstitutionDAO;
import com.blueskyminds.framework.tools.substitutions.service.SubstitutionService;
import com.blueskyminds.framework.patterns.EnumPatternMatcher;
import com.blueskyminds.framework.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.landmine.core.property.PropertyTypes;
import com.google.inject.Inject;

import javax.persistence.EntityManager;

/**
 * A pattern matcher that detects the value of a PropertyType enumeration
 *
 * (as defined by patterns in the the PropertyTypesBin)
 *
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
public class PropertyTypeMatcher extends EnumPatternMatcher<PropertyTypes> {

    @Inject
    public PropertyTypeMatcher(SubstitutionService substitutionService) throws PatternMatcherInitialisationException {
        super(new PropertyTypesBin(substitutionService));
    }
}
