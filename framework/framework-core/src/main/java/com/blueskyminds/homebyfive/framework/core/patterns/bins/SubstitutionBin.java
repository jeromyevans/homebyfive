package com.blueskyminds.homebyfive.framework.core.patterns.bins;

import com.blueskyminds.homebyfive.framework.core.patterns.Bin;
import com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.homebyfive.framework.core.patterns.comparison.StringComparator;
import com.blueskyminds.homebyfive.framework.core.patterns.comparison.IgnoreCaseComparator;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.Substitution;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.service.SubstitutionService;

import java.util.List;

/**
 * A generic bin that contains anything that fits in the Substitution groupName
 * It registers one pattern for each substitution
 *
 * A SubstitutionBin substitutes the matched value with predefined value
 *  eg. "St" is matched and substituted with "Street"
 *
 * Uses a SubstitutionDAO
 *
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class SubstitutionBin extends Bin {

    private String groupName;

    public SubstitutionBin(String groupName, SubstitutionService substitutionService) throws PatternMatcherInitialisationException {
        this.groupName = groupName;
        List<Substitution> substitutions;
        StringComparator<String> stringComparator = new IgnoreCaseComparator();

        substitutions = substitutionService.listSubstitutionsForGroup(groupName);

        for (Substitution substitution : substitutions) {
            addPattern(substitution.getPattern(), stringComparator, substitution.getSubstitution(), substitution.isExclusive(), substitution.getGroupNo(), substitution.getMetadata());
        }
    }

    public String getGroupName() {
        return groupName;
    }
}
