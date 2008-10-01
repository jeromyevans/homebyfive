package com.blueskyminds.homebyfive.framework.framework.patterns.bins;

import com.blueskyminds.homebyfive.framework.framework.patterns.Bin;
import com.blueskyminds.homebyfive.framework.framework.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.homebyfive.framework.framework.patterns.comparison.IgnoreCaseComparator;
import com.blueskyminds.homebyfive.framework.framework.tools.substitutions.Substitution;
import com.blueskyminds.homebyfive.framework.framework.tools.substitutions.service.SubstitutionService;

import java.util.Collection;

import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A generic bin that contains the values of an Enumeration
 *
 * Uses a SubstitutionSuburb to load the substitutions for the groupName
 * IMPORTANT: Each substitution should include the enum ordinal value name in its metadata (exact match)
 *
 * Then for each substitution, if an enumeration value is defined its added as a pattern for this bin
 *
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
public class EnumSubstitutionBin<E extends Enum> extends Bin {

    private static final Log LOG = LogFactory.getLog(EnumSubstitutionBin.class);

    private String groupName;

    public EnumSubstitutionBin(String groupName, Class<E> enumClass, SubstitutionService substitutionService) throws PatternMatcherInitialisationException {
        this.groupName = groupName;

        Collection<Substitution> substitutions;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        substitutions = substitutionService.getSubstitutionsForGroup(groupName);

        stopWatch.split();
//        LOG.info("   enumSub1: "+stopWatch.toString());
        stopWatch.unsplit();

        for (Substitution substitution : substitutions) {
            // lookup the enumeration using the metadata in the substitution
            E instance = (E) Enum.valueOf(enumClass, substitution.getMetadata());

            // add the pattern to the bin with or without the substituted value
            if (substitution.getSubstitution() != null) {
                addPattern(substitution.getPattern(), new IgnoreCaseComparator(), substitution.getSubstitution().toLowerCase(), substitution.isExclusive(), substitution.getGroupNo(), instance);
            } else {
                addPattern(substitution.getPattern(), new IgnoreCaseComparator(), null, substitution.isExclusive(), substitution.getGroupNo(), instance);
            }

        }
        stopWatch.stop();
//        LOG.info("   enumSubTotal: "+stopWatch.toString());
    }

}
