package com.blueskyminds.homebyfive.framework.core.patterns;

import com.blueskyminds.homebyfive.framework.core.tools.text.StringTools;
import com.blueskyminds.homebyfive.framework.core.patterns.scoring.ScoringStrategy;
import com.blueskyminds.homebyfive.framework.core.patterns.scoring.Score;

import java.util.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A text pattern matching implementation.
 *
 * Commence processing using the given input string:
 *       1. decomposes the text into phrases (every series of consecuative words in the text)
 *       2. allocates all of the phrases to the bins added to this pattern matcher
 *       3. uses the allocations to derive candidates - valid combinations of allocations to bins
 *       4. scores all of the candidates
 *       5. returns the candidate with the best score
 *
 * Date Started: 18/06/2006
 *
 * Parameters:
 *   T cam be any class that the PatternMatcher has been setup to detect
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public abstract class PatternMatcher<T> {

    private static final Log LOG = LogFactory.getLog(PatternMatcher.class);

    /**
     * Optimization that limits the number of words permitted in a phrase.  This can make a significant
     * performance improvement for large text but means it won't match any pattern containing more than this
     * many parts
     */
    private static final int MAX_WORDS_IN_A_PHRASE = 4;

    /** List of the bins that are matched by each candidate */
    private List<Bin> bins;

    /** The number of words in the input string */
    private int noOfWords;

    /** the phrases in the input string */
    private List<Phrase> phraseList;

    /** List of all the candidate allocations of sequences of phrases allocated bins */
    private Collection<CandidateAllocation> candidateAllocations;

    /** The list of one or more candidates with the top scrore */
    private Collection<CandidateAllocation> bestCandidates;

    /** The strategy used to getScore the candidates */
    private ScoringStrategy scoringStrategy;

    // ------------------------------------------------------------------------------------------------------

    public PatternMatcher(ScoringStrategy scoringStategy) throws PatternMatcherInitialisationException {
        this.scoringStrategy = scoringStategy;
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the PatternMatcher with default attributes
     */
    private void init() {
        bins = new ArrayList<Bin>(10);
        candidateAllocations = new HashSet<CandidateAllocation>();
        bestCandidates  = new HashSet<CandidateAllocation>();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Include an optional bin to match with
     **/
    public PatternMatcher addBin(Bin newBin) {                
        bins.add(newBin);
        return this;
    }


    // ------------------------------------------------------------------------------------------------------

    /** Remove the specified bin */
    public boolean removeBin(Bin bin) {
        return bins.remove(bin);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Reset all the allocations to bins used by this matcher as well as all the candidateAllocations */
    public void reset() {
        if (phraseList != null) {
            phraseList.clear();
        }
        if (candidateAllocations != null) {
            candidateAllocations.clear();
        }
        if (bestCandidates != null) {
            bestCandidates.clear();
        }
        if (bins != null) {
            for (Bin bin : bins) {
                bin.resetAllocations();
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Decomposes the input string into phrases.
     *   A phrase is generated for every series of consecutive words defined in the input string, from single words
     *  up to a phrase containing every word, and everything in between.
     *
     *  eg.  "Fat dog runs" will generate the phrases:
     *           "fat", "dog", "runs", "fat dog", "dog runs" and "fat dog runs"
     *
     * Words are separated by white space.
     * If a word contains tailing or leading punctuation it is added both with and without the punctuation.
     * eg. "\"Fat dog runs\"" is added as "\"Fat dog runs\"" and "Fat dog runs"
     *
     * @param inputString
     * @return list of phrases
     */
    private List<Phrase> extractPhrases(String inputString) {
        // split on whitespace
        String[] wordList = inputString.split("\\s");
        LinkedList<Phrase> phraseList = new LinkedList<Phrase>();

        // remember how many words there are
        noOfWords = wordList.length;

        // setup the initial phrase list
        for (int index = 0; index < wordList.length; index++) {
            String nonPunctuated = StringTools.stripPunctuation(wordList[index]);
            if (nonPunctuated.equals(wordList[index])) {
                phraseList.add(new Phrase(wordList[index], index, index));
            } else {
                // add the punctuated and non-punctuated versions for matching
                phraseList.add(new Phrase(wordList[index], index, index));
                phraseList.add(new Phrase(nonPunctuated, index, index));
            }
        }

        // extract all the phrases (sequences of words) from the string - from two words per sequence through to MAX words
        for (int length = 1; length <= Math.min(noOfWords, MAX_WORDS_IN_A_PHRASE); length++) {
            for (int firstWordIndex = 0; firstWordIndex < noOfWords-length; firstWordIndex++) {
                String segment = StringTools.joinWords(wordList, firstWordIndex, firstWordIndex + length);
                String nonPunctuated = StringTools.stripPunctuation(segment);
                if (nonPunctuated.equals(segment)) {
                    phraseList.add(new Phrase(segment, firstWordIndex, firstWordIndex+length));
                } else {
                    // add the punctuated and non-punctuated versions for matching
                    phraseList.add(new Phrase(segment, firstWordIndex, firstWordIndex+length));
                    phraseList.add(new Phrase(nonPunctuated, firstWordIndex, firstWordIndex+length));
                }
            }
        }

        return phraseList;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Allocate the phrases to the respective bins using the matching function defined in each bin
     **/
    private void allocateToBins(List<Phrase> phraseList) {
        for (Bin bin : bins) {
            bin.allocateMatchingPhrases(phraseList);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Reorder an allocation sequence based on the order of the usages of phrases from the input string */
    private List<PhraseToBinAllocation> sortAllocationSequence(List<PhraseToBinAllocation> allocations) {

        // order the sequence
        PhraseToBinAllocation[] orderedSequence = new PhraseToBinAllocation[allocations.size()];
        allocations.toArray(orderedSequence);

        /** Sort the allocations based on the order of the use of phrases - first to last */
        Arrays.sort(orderedSequence, new Comparator<PhraseToBinAllocation>() {
            public int compare(PhraseToBinAllocation o1, PhraseToBinAllocation o2) {
                Integer firstIndex = o1.getPhrase().getFirstIndex();
                Integer secondIndex = o2.getPhrase().getFirstIndex();
                return firstIndex.compareTo(secondIndex);
            }
        });

        // update the list of allocations
        return Arrays.asList(orderedSequence);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Starting at the specified start word number, iterates looking for a phrase in any of the bins
     * that continues from the word position.  If one or more is found, then recurses back into this
     * method to find one or more phrases that continue from that word position.
     *
     * @param allocationsSoFar collection of allocations that have already been made
     * @param availableBins of bins that are available for allocation - a bin is available if is isn't sed in the allocationsSoFar
     */
    private void allocatePhraseSequencesToBins(String path, List<PhraseToBinAllocation> allocationsSoFar, Collection<Bin> availableBins) {

        Set<Bin> binsAttempted = new HashSet<Bin>();
        List<List<PhraseToBinAllocation>> listOfAllocationsSoFar = new LinkedList<List<PhraseToBinAllocation>>();

        // loop through all of the available bins
        for (Bin bin : availableBins) {
            // track which bins that have been attempted as there's no reason to try them again
            binsAttempted.add(bin);

            // get all of the allocations in this bin that are compatible with the allocations so far - that is, they
            // do not overlap with existing phrases
            Collection<PhraseToBinAllocation> allocationsToAttempt = bin.getCompatibleAllocations(allocationsSoFar);

            // there are phrases in this bin that can be used...
            if (allocationsToAttempt.size() > 0) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(path+" "+bin.getClass().getSimpleName()+": "+allocationsToAttempt.size()+" paths available:");
                }
                int pathNo = 0;
                for (PhraseToBinAllocation allocation : allocationsToAttempt) {

                    // create a subset of available bins that excludes this one
                    Collection<Bin> nextAvailableBins = new HashSet<Bin>(availableBins);
                    nextAvailableBins.removeAll(binsAttempted);

                    // set up the list of allocations so far, including this new one
                    List<PhraseToBinAllocation> nextAllocationsSoFar = new LinkedList<PhraseToBinAllocation>(allocationsSoFar);
                    nextAllocationsSoFar.add(allocation);
                    listOfAllocationsSoFar.add(nextAllocationsSoFar);

                    // remember this sequence of candidate allocations if we haven't used it before
                    CandidateAllocation candidate = new CandidateAllocation(sortAllocationSequence(nextAllocationsSoFar), scoringStrategy);
                    if (!candidateExists(candidate)) {
                        candidateAllocations.add(candidate);

                        if (nextAvailableBins.size() > 0) {
                            // recurse into the method again for new set of bins and allocations so far
                            allocatePhraseSequencesToBins(path+"."+bin.getClass().getSimpleName()+"["+pathNo+"]", nextAllocationsSoFar, nextAvailableBins);
                        }
                        pathNo++;
                    }                    
                }
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Determines whether a candidate allocation already exists (by value)
     * @param candidate
     * @return
     */
    private boolean candidateExists(CandidateAllocation candidate) {
        boolean exists = false;

        for (CandidateAllocation existingCandidate : candidateAllocations) {
            if (existingCandidate.equals(candidate)) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Order the bins from the most number of allocations to the least */
    private Bin[] getBinsOrderedByAllocation() {
        // as an optimisation, order the bins by the number of allocations to them, from least to most.  This allows
        // the combinations to be minimised quickly really???? 
        Bin[] orderedBinList = new Bin[bins.size()];
        bins.toArray(orderedBinList);
        Arrays.sort(orderedBinList, new Comparator<Bin>() {
            public int compare(Bin b1, Bin b2) {
                return (b1.getAllocationCount()).compareTo(b2.getAllocationCount());
            }
        });

        return orderedBinList;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Commence processing using the given input string:
     *   1. splits the text on whitespace
     *   2. decomposes into phrases (every series of consecutive words in the text)
     *   3. allocates all of the phrases to the bins added to this pattern matcher
     *   4. uses the allocations to derive candidates - valid combinations of allocations to bins
     *   5. scores all of the candidates */

    public final void process(String inputString) throws PatternMatcherException  {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        reset();

        phraseList = extractPhrases(inputString);
//        LOG.info("parseTime0: "+stopWatch.toString());
        stopWatch.reset();
        stopWatch.start();

        allocateToBins(phraseList);

        stopWatch.stop();
//        LOG.info("parseTime1: "+stopWatch.toString());
        stopWatch.reset();
        stopWatch.start();

        postProcessAllocations();

        calculateCandidates();

        stopWatch.stop();
//        LOG.info("parseTime2: "+stopWatch.toString());
        stopWatch.reset();
        stopWatch.start();

        postProcessCandidates(candidateAllocations);

        scoreCandidates();

        stopWatch.stop();
//        LOG.info("parseTime3: "+stopWatch.toString());
        stopWatch.reset();
        stopWatch.start();

        postProcessBestCandidates(bestCandidates);

        stopWatch.stop();
//        LOG.info("parseTime3: "+stopWatch.toString());
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * If an extending implementation chooses to perform any additional processing after the allocation to bins
     * has been completed then it may override this implementation.
     *
     * This method is called prior to evaluating candidates.
     */
    protected void postProcessAllocations() throws PatternMatcherException {
    }

    /**
     * If an extending implementation chooses to perform any additional processing after candidates have been
     * calculated they may override this implementation.
     *
     * This method is called prior to scoring.  The collection may be altered.
     */
    protected void postProcessCandidates(Collection<CandidateAllocation> candidateAllocations) throws PatternMatcherException {
    }

    /**
     * If an extending implementation chooses to perform any additional processing after candidates have been
     * scored they may override this implementation.
     *
     * The collection may be altered.
     */
    protected void postProcessBestCandidates(Collection<CandidateAllocation> candidateAllocations) throws PatternMatcherException {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Iterates through all the phrase to bin allocations that have been completed and extracts out
     * all of the valid sequences as CandidateAllocations
     */
    private void calculateCandidates() {
         List<PhraseToBinAllocation> allocationsSoFar = new LinkedList<PhraseToBinAllocation>();
         Collection<Bin> binsAvailable = Arrays.asList(getBinsOrderedByAllocation());
         // list of all the candidate allocations
         allocatePhraseSequencesToBins(".", allocationsSoFar, binsAvailable);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Iterates through all of the candidates and scores them using the scoring strategy */
    private void scoreCandidates() {
        // getScore and sort the candidates.
        Score bestScore = new Score(Integer.MIN_VALUE);
        Score thisScore;

        bestCandidates = new LinkedList<CandidateAllocation>();

        // calculate the scores for all candidates and remember those with the best score
        for (CandidateAllocation allocation : candidateAllocations) {
            thisScore = allocation.calculateScore(noOfWords);
            if (thisScore.compareTo(bestScore) > 0) {
                // new best score - create a new list of the best candidates
                bestCandidates.clear();
                bestCandidates.add(allocation);
                // remember the best score
                bestScore = thisScore;
            } else {
                if (thisScore.compareTo(bestScore) == 0) {
                    // remember this is one of the equal-best
                    bestCandidates.add(allocation);
                }
            }
        }


        if (LOG.isDebugEnabled()) {
            CandidateAllocation[] sortedCandidates = new CandidateAllocation[candidateAllocations.size()];
            int candidateNo = 0;

            // sort the candidates by score and list the results
            candidateAllocations.toArray(sortedCandidates);
            Arrays.sort(sortedCandidates, new Comparator<CandidateAllocation>() {
            // sort the candidates based on getScore
            public int compare(CandidateAllocation o1, CandidateAllocation o2) {
                    return (o2.getScore().compareTo(o1.getScore()));
                }
            });

            LOG.debug("--- Top 100 candidates:---");
            for (CandidateAllocation candidate: sortedCandidates) {
                LOG.debug("Candidate["+candidateNo+"]: score:" + candidate.getScore()+(candidate.getScore() == bestScore ? " BEST CANDIDATE" : ""));

                for (PhraseToBinAllocation allocation : candidate.getAllocations()) {
                    LOG.debug("   " + allocation.getBin().getClass().getSimpleName() + ":" + allocation.getPhrase().getFirstIndex() + "-" + allocation.getPhrase().getLastIndex() + ":" + allocation.getPhrase().getStringLiteral());
                }
                candidateNo++;
                if (candidateNo > 99) {
                    break;
                }
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Extract the value for a parameter from the given allocation.
     *   If a substituttion is defined, the substituion is returned, otherwise the input phrase is used verbotim
     *
     * The substition is requested from the bin.
     *
     * @param allocation
     * @return the value
     */
    protected String extractValue(PhraseToBinAllocation allocation) {
        String value = null;
        if (allocation != null) {
            // request the bin to provide a substitution for the pattern
            String substitution = allocation.getBin().getSubstitution(allocation.getPattern(), allocation.getGroupMatches());
            if (substitution != null) {
                // use the substitution defined by the pattern
                value = substitution;
            } else {
                // use the string literal from the input phrase
                value = allocation.getPhrase().getStringLiteral();
            }
        }

        return value;
    }

    // ------------------------------------------------------------------------------------------------------

    /**      
     * @param allocation
     * @return null if not a valid float
     */
    protected Number extractNumber(PhraseToBinAllocation allocation) {
        Number amount = null;
        try {
            String value = extractValue(allocation);
            if (value != null) {
                amount = NumberFormat.getNumberInstance().parse(value);
            }
        } catch (NumberFormatException e) {
            // ignore invalid
        } catch (ParseException e) {
            //ignore invalid
        }
        return amount;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Extract the metadata underlying the given allocation.
     *
     * @param allocation
     * @return the metadata, or null if the allocation is not valid or there's no underlying metadata of the specified class
     */
    @SuppressWarnings({"unchecked"})
    protected <T> T extractMetadata(Class<T> metadataClazz, PhraseToBinAllocation allocation) {
        T metadata = null;
        if (allocation != null) {
            try {
                metadata = (T) allocation.getPattern().getMetadata();
            } catch(ClassCastException e) {
                // the metadata isn't the requested class
            }
        }

        return metadata;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the list of phrases created by the pattern matcher */
    protected List<Phrase> getPhraseList() {
        return phraseList;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the list of candidate allocations - one or more candidates with the best getScore */
    protected Collection<CandidateAllocation> getBestCandidates() {
        return bestCandidates;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Parses the input and extracts the based-candidate result */
    public T extractBest(String inputString) throws PatternMatcherException {
        if (inputString != null) {
            List<T> matches = extractMatches(inputString, 1);
            if (matches.size() > 0) {
                return matches.iterator().next();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /** Parses the input and extracts the based maxMatches results */
    public List<T> extractMatches(String inputString, int maxMatches) throws PatternMatcherException {
        List<T> matches = new LinkedList<T>();
        if (inputString != null) {
            boolean created = false;
            CandidateAllocation candidate;

            process(inputString);

            Collection<CandidateAllocation> bestCandidates = getBestCandidates();

            // try all of the best candidates until the first one that generates a valid result
            Iterator<CandidateAllocation> iterator = bestCandidates.iterator();
            while ((!created) && (iterator.hasNext())) {

                candidate = iterator.next();

                // use the visitor to extract the result
                T result = extractCandidate(candidate);
                if (result != null) {
                    matches.add(result);
                    if (matches.size() >= maxMatches) {
                        created = true;
                    }
                }
            }
        }

        return matches;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * This method is called for each candidate allocation made by the PatternMatcher, ordered by Score until a
     *  non-null result is returned.
     */
    protected abstract T extractCandidate(CandidateAllocation candidateAllocation) throws PatternMatcherException;
}
