package com.blueskyminds.homebyfive.framework.core.patterns;

import com.blueskyminds.homebyfive.framework.core.patterns.bins.EnumSubstitutionBin;
import com.blueskyminds.homebyfive.framework.core.patterns.scoring.ScoringStrategy;
import com.blueskyminds.homebyfive.framework.core.patterns.scoring.FirstWinsScoringStrategy;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.dao.SubstitutionDAO;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.service.SubstitutionServiceImpl;

/**
 * An extension of the EnumPatternMatcher that also supports substitution groups
 *
 * You may supply either:
 *   a Bin used to detect the enumeration; or
 *   the groupName of the Substitution defined for the enumeration
 *
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */

import javax.persistence.EntityManager;

public class EnumSubstitutionPatternMatcher<E extends Enum> extends EnumPatternMatcher<E> {

    /**
     *
     * @param substitutionGroupName uses a SubstitutionGroup to match the enum
     */
    public EnumSubstitutionPatternMatcher(Class<E> enumClass, String substitutionGroupName, EntityManager em) throws PatternMatcherInitialisationException {
        this(enumClass, substitutionGroupName, new FirstWinsScoringStrategy(), em);
    }

    /**
     *
     * @param substitutionGroupName uses a SubstitutionGroup to match the enum
     */
    public EnumSubstitutionPatternMatcher(Class<E> enumClass, String substitutionGroupName, ScoringStrategy scoringStrategy, EntityManager em) throws PatternMatcherInitialisationException {
        super(enumClass, new EnumSubstitutionBin(substitutionGroupName, enumClass, new SubstitutionServiceImpl(new SubstitutionDAO(em))), scoringStrategy);
    }

}


