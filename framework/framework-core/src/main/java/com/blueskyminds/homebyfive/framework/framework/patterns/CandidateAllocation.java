package com.blueskyminds.homebyfive.framework.framework.patterns;

import com.blueskyminds.homebyfive.framework.framework.patterns.scoring.ScoringStrategy;
import com.blueskyminds.homebyfive.framework.framework.patterns.scoring.Score;

import java.util.Collection;
import java.util.List;

/**
 * Represents a single sequence of allocations of phrases to bins that forms a candidate
 *
 * Date Started: 18/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */

public class CandidateAllocation {

    /** The list of allocations of phrases to bins for this candidate */
    //private List<PhraseToBinAllocation> allocations;
    private PhraseToBinAllocationSequence allocationSequence;

    /** The strategy to use to getScore the candidate */
    private ScoringStrategy scoringStrategy;

    private Score score;

    // ------------------------------------------------------------------------------------------------------

    public CandidateAllocation(List<PhraseToBinAllocation> allocations, ScoringStrategy scoringStrategy) {
        this.allocationSequence = new PhraseToBinAllocationSequence(allocations);
        this.scoringStrategy = scoringStrategy;
        this.score = null;
    }

    public CandidateAllocation(PhraseToBinAllocationSequence sequence, ScoringStrategy scoringStrategy) {
        this.allocationSequence = sequence;
        this.scoringStrategy = scoringStrategy;
        this.score = null;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns the last-calculated score for this candidate */
    public Score getScore() {
        return score;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Derives a getScore for this candidate - currently counts the number of allocations made */
    public Score calculateScore(int totalWords) {
        this.score = scoringStrategy.score(allocationSequence, totalWords);
        return score;
    }

    // ------------------------------------------------------------------------------------------------------

    public Collection<PhraseToBinAllocation> getAllocations() {
        return allocationSequence.getAllocations();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Gets the allocation to the bin of the given class, if defined */
    public <T extends Bin> PhraseToBinAllocation getAllocationForBin(Class<T> clazz) {
        return allocationSequence.getAllocationTo(clazz);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * This CandidateAllocation matches another CandidateAllocation if the sequence of phrase to bin allocations match
     *
     * @param o     the other CandidateAllocation
     * @return true if they are identical (by value)
     */
    public boolean equals(Object o) {
        boolean matches = false;
        if (CandidateAllocation.class.equals(o.getClass())) {
            CandidateAllocation other = (CandidateAllocation) o;

            matches = this.allocationSequence.equals(other.allocationSequence);
        }
        return matches;
    }

    public String toString() {        
        return allocationSequence.toString()+(score != null ? " ["+score+"]" : "");
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Extract the value for a parameter from the given allocation.
     *   If a substituttion is defined, the substituion is returned, otherwise the input phrase is used verbotim
     *
     * The substition is requested from the bin.
     *
     * @param clazz         bin type
     * @return the value
     */
    public <T extends Bin> String extractValue(Class<T> clazz) {
        PhraseToBinAllocation allocation = getAllocationForBin(clazz);
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
     * @param clazz
     * @return null if not a valid float
     */
    public <T extends Bin> Float extractFloat(Class<T> clazz) {
        Float amount = null;
        try {
            String value = extractValue(clazz);
            if (value != null) {
                amount = Float.parseFloat(value);
            }
        } catch (NumberFormatException e) {
            // ignore invalid
        }
        return amount;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Extract the metadata underlying the given allocation.
     *
     * @param clazz
     * @return the metadata, or null if the allocation is not valid or there's no underlying metadata of the specified class
     */
    @SuppressWarnings({"unchecked"})
    public <T> T extractMetadata(Class<T> metadataClazz, Class<? extends Bin> clazz) {
        PhraseToBinAllocation allocation = getAllocationForBin(clazz);
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
}
