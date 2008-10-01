package com.blueskyminds.homebyfive.framework.framework.patterns.bins;

import com.blueskyminds.homebyfive.framework.framework.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.homebyfive.framework.framework.patterns.Bin;
import com.blueskyminds.homebyfive.framework.framework.tools.substitutions.service.SubstitutionService;
import com.blueskyminds.homebyfive.framework.framework.tools.substitutions.Substitution;

import java.util.List;

/**
 * A SubstitutionBin that uses Regular Expression matching instead of exact matching
 *
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
public class RegExSubstitutionBin extends Bin {

    private String groupName;

    public RegExSubstitutionBin(String groupName, SubstitutionService substitutionService) throws PatternMatcherInitialisationException {
        this.groupName = groupName;
        List<Substitution> substitutions;        

        substitutions = substitutionService.getSubstitutionsForGroup(groupName);

        for (Substitution substitution : substitutions) {
            addRegExPattern(substitution.getPattern(), substitution.getSubstitution(), substitution.isExclusive(), substitution.getGroupNo(), substitution.getMetadata());
        }
    }

    public String getGroupName() {
        return groupName;
    }
    
}
