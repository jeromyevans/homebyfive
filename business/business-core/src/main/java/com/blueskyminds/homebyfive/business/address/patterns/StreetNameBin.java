package com.blueskyminds.homebyfive.business.address.patterns;

import com.blueskyminds.homebyfive.framework.core.patterns.*;
import com.blueskyminds.homebyfive.framework.core.patterns.bins.RegExSubstitutionBin;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.service.SubstitutionService;
import com.blueskyminds.homebyfive.business.address.dao.AddressDAO;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.graph.Street;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.service.StreetService;

import java.util.Set;
import java.util.HashSet;
import java.util.List;

/**
 *
 * Matches words to street names.
 *
 * The current implementation looks up known street names with fuzzy matching and also accepts any non-numeric word
 *
 * Date Started: 18/06/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
public class StreetNameBin extends RegExSubstitutionBin {

    private static final String GROUP_NAME = "StreetName";
    private Country country;
    private Suburb suburb;
    private StreetService streetService;

    public StreetNameBin(Country country, StreetService streetService, SubstitutionService substitutionDAO) throws PatternMatcherInitialisationException {
        super(GROUP_NAME, substitutionDAO);
        this.streetService = streetService;
        this.country = country;
    }

    public StreetNameBin(Suburb suburb, StreetService streetService, SubstitutionService substitutionDAO) throws PatternMatcherInitialisationException {
        super(GROUP_NAME, substitutionDAO);
        this.streetService = streetService;
        this.suburb = suburb;
    }

    /**
     * Extends the default implementation to ensure that street names start with a letter
     *
     * @param word
     * @return true if an exclusion was matched
     */
    protected boolean wordMatchesExclusion(String word) {
        if (!Character.isLetter(word.charAt(0))) {
            return true;
        } else {
            return super.wordMatchesExclusion(word);
        }
    }

    /**
     * First attempts to match one of the known street names.
     * If that's unsuccessful, uses the RegEx patterns
     *
     * @param word
     * @return
     */
    @Override
    protected Set<PatternMatch> wordMatchesPattern(String word) {
        Set<PatternMatch> matches = new HashSet<PatternMatch>();
        matches.addAll(wordMatchesKnownStreet(word));
        matches.addAll(super.wordMatchesPattern(word));
        return matches;
    }

    /**
     * Determines if the given word matches any of the known streets using a fuzzy matching algorithm
     *
     * @param word
     * @return All of the named objects that were matched (may be zero, one or more)
     */
    protected Set<PatternMatch> wordMatchesKnownStreet(String word) {
        boolean exclusive = true;
        Set<PatternMatch> matches = new HashSet<PatternMatch>();
        Set<Street> streets;
                
        if (country != null) {
            streets = streetService.listStreets(country);
        } else {
            streets = streetService.listStreets(suburb);
        }

        if (streets.size() > 0) {
            List<Street> matchingStreets = LevensteinDistanceTools.matchName(word, streets);

            for (Street match : matchingStreets) {
                if (word.equalsIgnoreCase(match.getName())) {
                    matches.add(new PatternMatch(new NamedPatternDecorator(match), exclusive, null, PatternMatchType.Exact));
                } else {
                    matches.add(new PatternMatch(new NamedPatternDecorator(match), exclusive, null, PatternMatchType.Fuzzy));
                }
            }
        }

        return matches;
    }


    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
}