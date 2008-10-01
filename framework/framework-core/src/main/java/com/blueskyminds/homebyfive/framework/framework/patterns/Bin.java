package com.blueskyminds.homebyfive.framework.framework.patterns;

import com.blueskyminds.homebyfive.framework.framework.patterns.comparison.IgnoreCaseComparator;
import com.blueskyminds.homebyfive.framework.framework.patterns.comparison.StringComparator;
import com.blueskyminds.homebyfive.framework.framework.tools.Named;

import java.io.PrintStream;
import java.util.*;

/**
 *
 * Phrases are matched to bins.  A phrase belongs to zero or more bins.  A bin is allocated zero or more phrases
 *
 * The bin defines patterns, constraints, exclusions, algorithms for matching a phrase
 *
 * Date Started: 16/06/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public abstract class Bin {

    /**
     * The algorithm used to compare two strings
     */
    private StringComparator comparator;

    /** All of the patterns defined in this bin */
    private List<BinPattern> patterns;

    // todo: allocations should be separated from the bin instance so the bin can be reused
    /** Allocation of phrases to this bin (because the phrase matches a pattern) */
    private List<PhraseToBinAllocation> allocations;

    private List<Object> aPrioriAllocations;

    /** All of the patterns defined in this bin that are pattern exclusions (not to match).*/
    private List<BinPattern> exclusions;

    // --------------------------------------------------------------------------------------------------

    /**
     * Create a new bin using the default "Ignore case" comparator
     *
     * @throws PatternMatcherInitialisationException
     */
    public Bin() throws PatternMatcherInitialisationException {
        this.comparator = new IgnoreCaseComparator();
        init();
    }

    public Bin(StringComparator comparator) throws PatternMatcherInitialisationException {
        this.comparator = comparator;
        init();
    }


    // --------------------------------------------------------------------------------------------------

    public void init() {
        allocations = new LinkedList<PhraseToBinAllocation>();
        patterns = new LinkedList<BinPattern>();
        aPrioriAllocations = new LinkedList<Object>();
        exclusions = new LinkedList<BinPattern>();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Determines if the given word matches any of the given exception patterns.
     *
     * @param word
     * @return true if an exclusion was matched
     */
    protected boolean wordMatchesExclusion(String word) {
        boolean wordMatches = false;

        for (BinPattern pattern : exclusions) {
            if (pattern.matches(word)) {
                wordMatches = true;
                break;
            }
        }

        return wordMatches;
    }

    /**
     *
     * Determines if the word matches any of the patterns in this bin.
     *
     * @param word
     * @return set of patterns that were matched
     */
    protected Set<PatternMatch> wordMatchesPattern(String word) {

        Set<PatternMatch> matches = new HashSet<PatternMatch>();

        for (BinPattern pattern : patterns) {
            matches = pattern.wordMatchesPattern(word);
            if (matches.size() > 0) {
                break;
            }
        }
        
        return matches;
    }

    // --------------------------------------------------------------------------------------------------

    protected String[] asArray() {
        ArrayList<String> patternArray = new ArrayList(patterns.size());
        for (BinPattern pattern : patterns) {
            patternArray.add(pattern.getPattern());
        }
        return (String[]) patternArray.toArray();
    }

    // --------------------------------------------------------------------------------------------------

    /** Reset all of the allocations made to this bin back to an empty list*/
    public void resetAllocations() {
        allocations.clear();
        aPrioriAllocations.clear();
    }

    // --------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------

    /** Adds a pattern to this bin, with optional substitution and metadata */
    public void addPattern(String pattern, StringComparator<String> comparator, String substitution, boolean exclusive, int groupNo, Object metadata) {
        patterns.add(new SimpleBinPattern(pattern, comparator, substitution, exclusive, groupNo, metadata));
    }

    /** Adds a pattern to this bin, with optional substitution and metadata */
    public void addPattern(java.util.regex.Pattern pattern, String substitution, boolean exclusive, int groupNo, Object metadata) {
        patterns.add(new RegExBinPattern(pattern, substitution, exclusive, groupNo, metadata));
    }

    /** Adds a pattern to this bin, with optional metadata (no substitution) */
    public void addPattern(String pattern, StringComparator<String> comparator, boolean exclusive, int groupNo, Object metadata) {
        patterns.add(new SimpleBinPattern(pattern, comparator, exclusive, groupNo, metadata));
    }

    /** Adds a pattern to this bin, with optional metadata (no substitution) */
    public void addRegExPattern(String pattern, String substitution, boolean exclusive, int groupNo, Object metadata) {
        patterns.add(new RegExBinPattern(java.util.regex.Pattern.compile(pattern), substitution, exclusive, groupNo, metadata));
    }

    /** Adds a pattern to this bin, with optional metadata (no substitution) */
    public void addRegExPattern(String pattern,  boolean exclusive, int groupNo, Object metadata) {
        patterns.add(new RegExBinPattern(java.util.regex.Pattern.compile(pattern), exclusive, groupNo, metadata));
    }

    /** Adds a Named object as a pattern for this bin, with optional metadata (no substitution) */
    public void addPattern(Named pattern, StringComparator<Named> comparator, boolean exclusive, int groupNo, Object metadata) {
        patterns.add(new NamedBinPattern(pattern, comparator, exclusive, groupNo, metadata));
    }

    /** Add an exclusion to this bin */
    public void addExclusion(String pattern, StringComparator<String> comparator) {
        exclusions.add(new SimpleBinPattern(pattern, comparator, null, true, 0, null));
    }

    /** Add an exclusion to this bin */
    public void addExclusion(String[] patterns, StringComparator<String> comparator) {
        for (String pattern : patterns) {
            addExclusion(pattern, comparator);
        }
    }


    // --------------------------------------------------------------------------------------------------

    /** Get the pattern idenfitied by the pattern number */
    protected BinPattern getPattern(int patternNo) {
        return patterns.get(patternNo);
    }

    // --------------------------------------------------------------------------------------------------

    /**
     * If defined, gets the substition to use for the specified pattern.
     *
     * If the substitution has a GroupNo defined, then the specified group result is returned, otherwise it returns
     * the substitution defined within the pattern itself,
     *
     * Implementations may override this method to return an alternative substitution
     *
     * A null value means no substution is defined.
     **/
    public String getSubstitution(Pattern pattern, String[] groupMatches) {
        if (pattern.getGroupNo() >= 0) {
            return extractGroupMatch(pattern.getGroupNo(), groupMatches);
        } else {
            return pattern.getSubstitution();
        }
    }

    // --------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------

    /** Allocate a phrase to this bin */
    private void allocatePhrase(Phrase phrase, PatternMatch patternMatch) {
        allocations.add(new PhraseToBinAllocation(this, phrase, patternMatch));
    }

    // --------------------------------------------------------------------------------------------------

    /**
     * Iterates through the given phrase list and allocates any of the phrases that match this bin
     * @return true if there is at least one match
     **/
    public boolean allocateMatchingPhrases(PhraseList phraseList) {
        return allocateMatchingPhrases(phraseList.asList());
    }

    /** Iterates through the given phrase list and allocates any of the phrases that match this bin
     * @return true if there is at least one match */
    public boolean allocateMatchingPhrases(List<Phrase> phraseList) {
        boolean matchOccurred = false;
        for (Phrase phrase : phraseList) {
            String literalString = phrase.getStringLiteral();

            if (literalString.length() > 0) {
                if (!wordMatchesExclusion(literalString)) {
                    // check if there's a match
                    Set<PatternMatch> matches = wordMatchesPattern(literalString);

                    for (PatternMatch match : matches) {
                        // allocate the phrase to this bin, recording the pattern that was matched
                        allocatePhrase(phrase, match);
                        matchOccurred = true;
                    }
                }
            }
        }

        return matchOccurred;
    }

     /**
     * Iterates through the given phrase list and allocates any of the phrases that match this bin and are
      * compatible with the sequence.  The state of the is not affected.
      *
     * @return the list of allocations
     **/
    public List<PhraseToBinAllocation> matchPhrasesCompatibleWith(PhraseList phraseList, PhraseToBinAllocationSequence sequence) {
        return matchPhrasesCompatibleWith(phraseList.asList(), sequence);
    }

    /**
     * Iterates through the given phrase list and allocates any of the phrases that match this bin and are compatible with the
     * sequence.  The state of the bin is not updated.
     *
     * @return the list of allocations
     * */
    public List<PhraseToBinAllocation> matchPhrasesCompatibleWith(List<Phrase> phraseList, PhraseToBinAllocationSequence sequence) {

        List<PhraseToBinAllocation> allocations = new LinkedList<PhraseToBinAllocation>();

        for (Phrase phrase : phraseList) {
            String literalString = phrase.getStringLiteral();

            if (literalString.length() > 0) {
                if (!wordMatchesExclusion(literalString)) {
                    // check if there's a match
                    Set<PatternMatch> matches = wordMatchesPattern(literalString);

                    for (PatternMatch match : matches) {
                        // allocate the phrase to this bin, recording the pattern that was matched
                        PhraseToBinAllocation candidateAllocation = new PhraseToBinAllocation(this, phrase, match);
                        if (sequence.isCompatible(candidateAllocation)) {
                            allocations.add(candidateAllocation);
                        }
                    }
                }
            }
        }

        return allocations;
    }

    // --------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------

    /**
     * Initialise apriori data for this bin.  This is a match that is automatically true even if there
     * is never a phrase allocation made.
     *
      * @param metadata
     */
    public void addAPrioriAllocation(Object metadata) {
        aPrioriAllocations.add(metadata);
    }

    /** Get the list of phrases allocated to this bin */
    public List<PhraseToBinAllocation> getAllocations() {
        return allocations;
    }

    /** Get the list of patterns defined in this bin */
    protected List<BinPattern> getPatterns() {
        return patterns;
    }

    /** Return the number of allocations in the bin */
    public Integer getAllocationCount() {
        return allocations.size();
    }

    public void print(PrintStream out) {
        out.println(getClass().getSimpleName()+":");
        for (PhraseToBinAllocation allocation : allocations) {
            out.println("   "+allocation.getPhrase().getFirstIndex()+" to "+allocation.getPhrase().getLastIndex()+":"+allocation.getPhrase().getStringLiteral());
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Iterates through all the allocations in this bin to see if there's at least one phrase that starts with
     * the word at the specified index
     * @param wordIndex
     * @return true if found
     */
    public boolean containsPhraseStartingAtPosition(int wordIndex) {
        boolean found = false;
        for (PhraseToBinAllocation allocation : allocations) {
            Phrase phrase = allocation.getPhrase();
            if (phrase.getFirstIndex() == wordIndex) {
                // found a phase starting at that position
                found = true;
                break;
            }
        }

        return found;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns all the phrases allocated to this bin that start at the specified word index
     * @param wordIndex
     * @return true if found
     */
    public List<Phrase> getPhrasesStartingAtPosition(int wordIndex) {
        List<Phrase> phrases = new LinkedList<Phrase>();

        for (PhraseToBinAllocation allocation : allocations) {
            Phrase phrase = allocation.getPhrase();
            if (phrase.getFirstIndex() == wordIndex) {
                // found a phase starting at that position - add it to the list
                phrases.add(phrase);
            }
        }

        return phrases;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Iterates through the allocations provided to find available positions in the underlying phrases.
     * ie. any unallocated words
     * Returns any of the allocations in this bin that fit within any of these gaps
     *
     * IMPORTANT NOTE: If a phrase is used in a non-exclusive allocation, then it remains available for re-use in
     *  other non-exclusive allocations
     *
     * @return true if found
     */
    public Collection<PhraseToBinAllocation> getCompatibleAllocations(Collection<PhraseToBinAllocation> allocationsUsed) {
        // start with a list of all the allocations
        List<PhraseToBinAllocation> nonConflictingAllocations = new LinkedList<PhraseToBinAllocation>(allocations);

        // remove any of the allocations that conflict with allocations in the allocationsUsed
        for (PhraseToBinAllocation allocation : allocations) {
            if (allocation.isExclusive()) {
                for (PhraseToBinAllocation allocationUsed : allocationsUsed) {
                    // remove the allocation because it conflicts with an existing allocation
                    if (allocation.getPhrase().overlaps(allocationUsed.getPhrase())) {
                        nonConflictingAllocations.remove(allocation);
                    }
                }
            } else {
                // we don't have an exclusive allocation - but if someone else does we still can't use it -
                // it can only be shared with other non-exclusive allocations
                for (PhraseToBinAllocation allocationUsed : allocationsUsed) {
                    if (allocationUsed.isExclusive()) {
                        // remove the allocation because it conflicts with an existing allocation
                        if (allocation.getPhrase().overlaps(allocationUsed.getPhrase())) {
                            nonConflictingAllocations.remove(allocation);
                        }
                    }
                }
            }
        }

        return nonConflictingAllocations;
    }

    /**
     * Iterates through the allocations provided to find available positions in the underlying phrases.
     * ie. any unallocated words
     * Returns any of the allocations in this bin that fit within any of these gaps
     *
     * IMPORTANT NOTE: If a phrase is used in a non-exclusive allocation, then it remains available for re-use in
     *  other non-exclusive allocations
     *
     * @return true if found
     */
    public Collection<PhraseToBinAllocation> getCompatibleAllocations(PhraseToBinAllocationSequence currentSequence) {
        if (currentSequence != null) {
            return getCompatibleAllocations(currentSequence.getAllocations());
        } else {
            return getCompatibleAllocations(new LinkedList<PhraseToBinAllocation>());
        }
    }

    // --------------------------------------------------------------------------------------------------

    /**
     * Helper method to extract the value from the last-used regular expression matcher for the group specified.
     * This method is only useful if:
     *   a. the regular expression matcher was used
     *   b. a match was made
     *   c. the regular expression contained groups (although group zero is always the entire match if regex is used)
     *
     * @param groupNo
     * @return the value matched for the specified group, if defined
     */
    protected String extractGroupMatch(int groupNo, String[] groupMatches) {
        String value = null;

        if (groupMatches != null) {
            if (groupNo < groupMatches.length) {
                try {
                    value = groupMatches[groupNo];
                } catch(ArrayIndexOutOfBoundsException e) {
                    // no corresponding group
                }
            }
        }
        return value;
    }

    // ------------------------------------------------------------------------------------------------------

}
