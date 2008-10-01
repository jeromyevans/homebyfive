package com.blueskyminds.homebyfive.framework.core.patterns;

import com.blueskyminds.homebyfive.framework.core.patterns.PhraseToBinAllocation;
import com.blueskyminds.homebyfive.framework.core.patterns.Bin;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.commons.lang.StringUtils;

/**
 * A class to store the sequence of bins in a series of Phrase to Bin allocations
 *
 * Date Started: 25/10/2007
 * <p/>
 * History:
 */
public class PhraseToBinAllocationSequence {

    private static final int MAX_FUZZ = 2;

    private List<PhraseToBinAllocation> allocationSequence;

    /** Identifies the index of the specified bin in the list for fast lookup*/
    private Map<Class<? extends Bin>, Integer> indexLookup;

    /** Identifies the position of the first word for the bin in the list for fast lookup*/
    private Map<Class<? extends Bin>, Integer> wordPosition;

    /** A value between 0 and 1 indicating how fuzzy the matches over the entire sequence were.
     * 0 is all exact, 1 is all by loose patterns */
    private float fuzziness;

    public PhraseToBinAllocationSequence(List<PhraseToBinAllocation> allocationSequence) {
        this.allocationSequence = allocationSequence;
//        calculatePositions();
        indexLookup = new HashMap<Class<? extends Bin>, Integer>();
        wordPosition = new HashMap<Class<? extends Bin>, Integer>();
    }

    public PhraseToBinAllocationSequence(PhraseToBinAllocation initialAllocation) {
        this.allocationSequence = new LinkedList<PhraseToBinAllocation>();
        if (initialAllocation != null) {
            allocationSequence.add(initialAllocation);
        }
        indexLookup = new HashMap<Class<? extends Bin>, Integer>();
        wordPosition = new HashMap<Class<? extends Bin>, Integer>();
    }

    protected PhraseToBinAllocationSequence(PhraseToBinAllocationSequence source, PhraseToBinAllocation another) {
        this.allocationSequence = new LinkedList<PhraseToBinAllocation>();
        for (PhraseToBinAllocation allocation : source.getAllocations()) {
            allocationSequence.add(allocation);
        }
        allocationSequence.add(another);
        indexLookup = new HashMap<Class<? extends Bin>, Integer>();
        wordPosition = new HashMap<Class<? extends Bin>, Integer>();
    }

    /**
     * Clone the sequence and include another allocation
     *
     * @param anotherAllocation
     * @return
     */
    public PhraseToBinAllocationSequence cloneAndExtend(PhraseToBinAllocation anotherAllocation) {
        return new PhraseToBinAllocationSequence(this, anotherAllocation);
    }

/*
    private void calculatePositions() {
        if ((positions == null) || (positions.size() != allocationSequence.size())) {
            int position = 0;
            positions = new HashMap<Class<? extends Bin>, Integer>();
            float fuzzTotal = 0.0f;

            // todo: this actually doesn't work - it's wrong.  the position in the sequence is not
            // loop through the sequence to find the index of the first and last bins (by class)
            for (PhraseToBinAllocation allocation : allocationSequence) {
                Class<? extends Bin> aClass = allocation.getBin().getClass();
                positions.put(aClass, position);
                switch (allocation.getPatternMatchType()) {
                    case Exact:
                        break;
                    case Fuzzy:
                        fuzzTotal+=1;
                        break;
                    case Pattern:
                        fuzzTotal+=MAX_FUZZ;
                        break;
                }
                position++;
            }
            fuzziness = fuzzTotal / (MAX_FUZZ * positions.size());
        }
    }
*/

    /**
     * Get the index of the specified bin in the list
     *
     * @param bin
     * @return the index if included, -1 if not included in the sequence
     */
    public <F extends Bin> int indexOf(Class<F> bin) {
        Integer index = indexLookup.get(bin);
        if (index == null) {
            index = 0;
            boolean found = false;
            for (PhraseToBinAllocation allocation : allocationSequence) {
                if (bin.equals(allocation.getBin().getClass())) {
                    indexLookup.put(bin, index);
                    found = true;
                    break;
                } else {
                    index++;
                }
            }
            if (!found) {
                index = null;
            }
        }

        if (index != null) {
            return index;
        } else {
            return -1;
        }
    }

    /**
     * Get the word index of the specified bin in the sequence (index of the first word).
     *
     * @param bin
     * @return the index if included, -1 if not included in the sequence
     */
    public <F extends Bin> int wordIndex(Class<F> bin) {
        Integer firsIndex = wordPosition.get(bin);
        if (firsIndex == null) {
            for (PhraseToBinAllocation allocation : allocationSequence) {
                if (bin.equals(allocation.getBin().getClass())) {
                    firsIndex = allocation.getPhrase().getFirstIndex();
                    wordPosition.put(bin, firsIndex);
                    break;
                }
            }
        }

        if (firsIndex != null) {
            return firsIndex;
        } else {
            return -1;
        }
    }
    
    /** Determine if the sequence contains a bin of the specified class */
    public boolean contains(Class<? extends Bin> binClass) {
        return indexOf(binClass) >= 0;
    }

    /** 
     * True if both bins are included and the first occurs before the second
     * */
    public boolean before(Class<? extends Bin> firstBin, Class<? extends Bin> secondBin) {
        int firstIndex = wordIndex(firstBin);
        int secondIndex = wordIndex(secondBin);
        return (firstIndex >= 0) && (secondIndex >= 0) && (firstIndex < secondIndex);
    }

    /**
     * True if both bins are included and the first occurs after the second
     * */
    public boolean after(Class<? extends Bin> firstBin, Class<? extends Bin> secondBin) {
        int firstIndex = wordIndex(firstBin);
        int secondIndex = wordIndex(secondBin);
        return (firstIndex >= 0) && (secondIndex >= 0) && (firstIndex > secondIndex);
    }

    /**
     * Returns the allocation to the specified bin
     *
     * @param binClass
     * @return null if there's no matching bin
     */
    public PhraseToBinAllocation getAllocationTo(Class<? extends Bin> binClass) {
        int index = indexOf(binClass);
        if (index >= 0) {
            return allocationSequence.get(index);
        } else {
            return null;
        }
    }

    /**
     * Examine the relative order of two bins in the sequence
     *
     * @param firstBinClass
     * @param secondBinClass
     * @return
     */
    public <F extends Bin, S extends Bin> PhraseSequence testSequence(Class<F> firstBinClass, Class<S> secondBinClass) {
        int firstIndex = -1;
        int secondIndex = -1;
        PhraseSequence result;
        int index = 0;

        firstIndex = wordIndex(firstBinClass);
        secondIndex = wordIndex(secondBinClass);

        // categorise the search result
        if (firstIndex >= 0) {
            if (secondIndex >= 0) {
                if (secondIndex > firstIndex) {
                    if (secondIndex == firstIndex+1) {
                        result = PhraseSequence.FIRST_ADJACENT_TO_SECOND;
                    } else {
                        result = PhraseSequence.FIRST_BEFORE_SECOND;
                    }
                } else {
                    result = PhraseSequence.SECOND_BEFORE_FIRST;
                }
            } else {
                result = PhraseSequence.FIRST_ONLY;
            }
        } else {
            if (secondIndex >= 0) {
                result = PhraseSequence.SECOND_ONLY;
            } else {
                result = PhraseSequence.NONE;
            }
        }

        return result;
    }

     /** Count the number of words from the original input string used in the allocation sequence */
    public int getWordCount() {
        int totalWords = 0;

        for (PhraseToBinAllocation allocation : allocationSequence) {
            totalWords += allocation.getPhrase().getNoOfWords();
        }

        return totalWords;
    }


    /** Get the ordered list of allocations */
    public List<PhraseToBinAllocation> getAllocations() {
        return allocationSequence;
    }

     public String toString() {
        return StringUtils.join(allocationSequence.iterator(), ",");
    }

     /**
     * This CandidateAllocation matches another CandidateAllocation if the sequence of phrase to bin allocations match
     *
     * @param o     the other CandidateAllocation
     * @return true if they are identical (by value)
     */
    public boolean equals(Object o) {
        boolean matches = false;
        if (PhraseToBinAllocationSequence.class.equals(o.getClass())) {
            PhraseToBinAllocationSequence other = (PhraseToBinAllocationSequence) o;

            if (allocationSequence.size() == other.allocationSequence.size()) {
                matches = true;
                for (int index = 0; index < allocationSequence.size(); index++) {
                   if (!this.allocationSequence.get(index).equals(other.allocationSequence.get(index))) {
                       matches = false;
                       break;
                   }
                }
            }
        }
        return matches;
    }

    /**
     * Return the right-most allocation to any one of the specified bins
     *  (The allocation for the bin that has the highest index in the sequence) 
     *
     * @param bins
     * @return
     */
    public PhraseToBinAllocation rightMost(Class<Bin>[] bins) {
        int index = -1;
        PhraseToBinAllocation rightMost = null;
        for (Class<Bin> bin : bins) {
            if (wordIndex(bin) > index) {
                index = wordIndex(bin);
                rightMost = getAllocationTo(bin);
            }
        }        
        return rightMost;
    }

    /**
     * A value between 0 and 1 that's indicative of how 'fuzzy' the overall sequence is.
     * 0 means every match was exact, 1 means every match as against a patern
     **/
    public float getFuzziness() {
        return fuzziness;
    }

    /**
     * Determines whether the allocation is compatible with this sequence.  A compatibile allocation means the
     * allocation can be added to the sequence without conflicting within another allocation.
     *
     * eg. uses a word that's already allocated in this sequence
     *
     * @param allocation
     * @return
     */
    public boolean isCompatible(PhraseToBinAllocation allocation) {
        boolean conflict = false;

        if (allocation.isExclusive()) {
            for (PhraseToBinAllocation allocationUsed : allocationSequence) {
                // remove the allocation because it conflicts with an existing allocation
                if (allocation.getPhrase().overlaps(allocationUsed.getPhrase())) {
                    conflict = true;
                    break;
                }
            }
        } else {
            // we don't have an exclusive allocation - but if someone else does we still can't use it -
            // it can only be shared with other non-exclusive allocations
            for (PhraseToBinAllocation allocationUsed : allocationSequence) {
                if (allocationUsed.isExclusive()) {
                    // remove the allocation because it conflicts with an existing allocation
                    if (allocation.getPhrase().overlaps(allocationUsed.getPhrase())) {
                        conflict = true;
                        break;
                    }
                }
            }
        }
        return !conflict;
    }

    public int size() {
        return allocationSequence.size();
    }
}
