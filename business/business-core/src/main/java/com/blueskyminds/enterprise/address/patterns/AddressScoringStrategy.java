package com.blueskyminds.enterprise.address.patterns;

import com.blueskyminds.homebyfive.framework.core.patterns.Phrase;
import com.blueskyminds.homebyfive.framework.core.patterns.PhraseSequence;
import com.blueskyminds.homebyfive.framework.core.patterns.PhraseToBinAllocation;
import com.blueskyminds.homebyfive.framework.core.patterns.PhraseToBinAllocationSequence;
import com.blueskyminds.homebyfive.framework.core.patterns.scoring.AbstractScoringStrategy;
import com.blueskyminds.homebyfive.framework.core.patterns.scoring.Score;
import com.blueskyminds.homebyfive.framework.core.patterns.scoring.ScoringStrategy;

/**
 * Implements the ScoringStrategy interface to getScore candidate addresses created by the pattern matcher
 *
 * The higher the getScore the better the candidate
 *
 * This is a faily simple scoring algorithm that looks at the relative sequence of allocations.
 *    eg. the street number before the street name is good
 * It rates sequences between VERY-GOOD to EXTREMELY BAD, where extremely bad is a sequence of words that's
 *  never permitted (eg. a StreetSection before a street name)
 *
 * Date Started: 23/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class AddressScoringStrategy extends AbstractScoringStrategy implements ScoringStrategy {
    private static final String THE = "the";

    // ------------------------------------------------------------------------------------------------------

    /** Create a score for the sequence of phrase to bin allocations.
     * The higher the Score, the better the candidate.
     * The scoring algorithm accounts for
     *    the number of phrases allocated
     *    the order of the allocation, in particular:
     *         unitno/streetno before streetname
     *         street name before street type
     *         street name before suburb
     *         street type before streetsection (essential)
     *         street type before suburb
     *         suburb before state/postcode
     **/
    public Score score(PhraseToBinAllocationSequence allocationSequence, int totalWords) {

        int baseScore = 0;  // allocationSequence.size();

        int unitNo = scoreUnitNumber(allocationSequence);
        int streetNo = scoreStreetNumber(allocationSequence);
        int streetName = scoreStreetName(allocationSequence);
        int streetType = scoreStreetType(allocationSequence);
        int suburb = scoreSuburb(allocationSequence);

//        if (baseScore >= 5) {
//            out.println("------------");
//            for (PhraseToBinAllocation allocation : allocationSequence) {
//                out.println(allocation);
//            }
//            out.println("UnitNo:"+unitNo+" streetNo:"+streetNo+" streetName:"+streetName+" streetType:"+streetType+" suburb:"+suburb+ " total:"+(baseScore+unitNo+streetNo+streetName+streetType+suburb));
//        }

        int summedScore = baseScore+unitNo+streetNo+streetName+streetType+suburb;

        // adjust the score based on the proportion of words allocated from the the original input string
        int finalScore;
        int wordCount = allocationSequence.getWordCount();
        if (wordCount > 0) {
            float usageScale = wordCount / (float) totalWords;

            if (summedScore >= 0) {
                finalScore = Math.round(summedScore * usageScale);
            } else {
                finalScore = (int) Math.round(summedScore * (1.0/usageScale));
            }
        } else {
            finalScore = Integer.MIN_VALUE;
        }
        
        return new Score(finalScore, allocationSequence.getFuzziness());
    }


    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    /** Calculates a Score for the unit number */
    private int scoreUnitNumber(PhraseToBinAllocationSequence allocationSequence) {
        PhraseSequence result;
        int firstScore = -1;
        int secondScore = -1;
        int thirdScore = -1;

        // unit number before street name
        result = testSequence(allocationSequence, UnitNumberBin.class, StreetNameBin.class);
        switch (result) {
            case FIRST_BEFORE_SECOND:
                firstScore = GOOD;
                break;
            case FIRST_ADJACENT_TO_SECOND:
                firstScore = GOOD;
                break;
            case FIRST_ONLY:
                firstScore = VERY_BAD;
                break;
            case NONE:
                firstScore = BAD;
                break;
            case SECOND_BEFORE_FIRST:
                firstScore = VERY_BAD;
                break;
            case SECOND_ONLY:
                firstScore = INDIFFERENT;
                break;
        }

        // lot number before street street number
        result = testSequence(allocationSequence, LotNumberBin.class, StreetNumberBin.class);
        switch (result) {
            case FIRST_BEFORE_SECOND:
                secondScore = GOOD;
                break;
            case FIRST_ADJACENT_TO_SECOND:
                secondScore = VERY_GOOD;
                break;
            case FIRST_ONLY:
                secondScore = INDIFFERENT;
                break;
            case NONE:
                secondScore = INDIFFERENT;
                break;
            case SECOND_BEFORE_FIRST:
                secondScore = VERY_BAD;
                break;
            case SECOND_ONLY:
                secondScore = INDIFFERENT;
                break;
        }

        // lot number before street name
        result = testSequence(allocationSequence, LotNumberBin.class, StreetNameBin.class);
        switch (result) {
            case FIRST_BEFORE_SECOND:
                thirdScore = GOOD;
                break;
            case FIRST_ADJACENT_TO_SECOND:
                thirdScore = GOOD;
                break;
            case FIRST_ONLY:
                thirdScore = VERY_BAD;
                break;
            case NONE:
                thirdScore = BAD;
                break;
            case SECOND_BEFORE_FIRST:
                thirdScore = VERY_BAD;
                break;
            case SECOND_ONLY:
                thirdScore = INDIFFERENT;
                break;
        }

        return firstScore + secondScore + thirdScore;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Calculates a Score for the street number */
    private int scoreStreetNumber(PhraseToBinAllocationSequence allocationSequence) {
        PhraseSequence result;
        int score = -1;

        // street number before street name
        result = testSequence(allocationSequence, StreetNumberBin.class, StreetNameBin.class);

        switch (result) {
            case FIRST_BEFORE_SECOND:
                score = GOOD;
                break;
            case FIRST_ADJACENT_TO_SECOND:
                score = VERY_GOOD;
                break;
            case FIRST_ONLY:
                score = VERY_BAD;
                break;
            case NONE:
                score = BAD;
                break;
            case SECOND_BEFORE_FIRST:
                score = VERY_BAD;
                break;
            case SECOND_ONLY:
                score = BAD;
                break;
        }

        return score;
    }

    /** This flag is used to track the special case of a street name starting with The - it allows the
     * street type to be ignored */
    private boolean streetNameIsThe;

    // ------------------------------------------------------------------------------------------------------

    /** Calculates a Score for the street number */
    private int scoreStreetName(PhraseToBinAllocationSequence allocationSequence) {
        PhraseSequence result;
        int firstScore = -1;
        int secondScore = -1;
        streetNameIsThe = false;

        // street name before street type
        result = testSequence(allocationSequence, StreetNameBin.class, StreetTypeBin.class);

        switch (result) {
            case FIRST_BEFORE_SECOND:
                firstScore = GOOD;
                break;
            case FIRST_ADJACENT_TO_SECOND:
                firstScore = VERY_GOOD;
                break;
            case FIRST_ONLY:
                firstScore = BAD;
                break;
            case NONE:
                firstScore = BAD;
                break;
            case SECOND_BEFORE_FIRST:
                firstScore = VERY_BAD;
                break;
            case SECOND_ONLY:
                firstScore = VERY_BAD;
                break;
        }

        // street name before suburb name
        result = testSequence(allocationSequence, StreetNameBin.class, SuburbNameBin.class);

        switch (result) {
            case FIRST_BEFORE_SECOND:
                if (allocationSequence.before(StreetNameBin.class, StreetTypeBin.class)) {
                    secondScore = GOOD;
                } else {
                    secondScore = BAD;
                }
                break;
            case FIRST_ADJACENT_TO_SECOND:
                secondScore = VERY_BAD;
                break;
            case FIRST_ONLY:
                secondScore = BAD;
                break;
            case NONE:
                secondScore = VERY_BAD;
                break;
            case SECOND_BEFORE_FIRST:
                secondScore = EXTREMELY_BAD;
                break;
            case SECOND_ONLY:
                secondScore = INDIFFERENT;
                break;
        }

        PhraseToBinAllocation allocation = allocationSequence.getAllocationTo(StreetNameBin.class);
        if (allocation != null) {
            Phrase phrase = allocation.getPhrase();
            if (phrase.getStringLiteral().startsWith(THE)) {
                // reset the score
                firstScore = GOOD;
                secondScore = GOOD;
                streetNameIsThe = true;
            }
        }

        return firstScore+secondScore;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Calculates a Score for the street type*/
    private int scoreStreetType(PhraseToBinAllocationSequence allocationSequence) {
        PhraseSequence result;
        int firstScore = -1;
        int secondScore = -1;
        int thirdScore = -1;

        // street type before street section
        result = testSequence(allocationSequence, StreetTypeBin.class, StreetSectionBin.class);

        switch (result) {
            case FIRST_BEFORE_SECOND:
                firstScore = GOOD;
                break;
            case FIRST_ADJACENT_TO_SECOND:
                firstScore = VERY_GOOD;
                break;
            case FIRST_ONLY:
                firstScore = GOOD;
                break;
            case NONE:
                if (!streetNameIsThe) {
                    firstScore = BAD;
                } else {
                    // no street type is ok
                    firstScore = INDIFFERENT;
                }
                break;
            case SECOND_BEFORE_FIRST:
                firstScore = EXTREMELY_BAD;
                break;
            case SECOND_ONLY:
                firstScore = EXTREMELY_BAD;
                break;
        }

        // street type before suburb
        result = testSequence(allocationSequence, StreetTypeBin.class, SuburbNameBin.class);

        switch (result) {
            case FIRST_BEFORE_SECOND:
                secondScore = GOOD;
                break;
            case FIRST_ADJACENT_TO_SECOND:
                secondScore = GOOD;
                break;
            case FIRST_ONLY:
                secondScore = BAD;
                break;
            case NONE:
                secondScore = VERY_BAD;
                break;
            case SECOND_BEFORE_FIRST:
                secondScore = EXTREMELY_BAD;
                break;
            case SECOND_ONLY:
                secondScore = INDIFFERENT;
                break;
        }

        // street section before suburb
        result = testSequence(allocationSequence, StreetSectionBin.class, SuburbNameBin.class);

        switch (result) {
            case FIRST_BEFORE_SECOND:
                thirdScore = GOOD;
                break;
            case FIRST_ADJACENT_TO_SECOND:
                thirdScore = VERY_GOOD;
                break;
            case FIRST_ONLY:
                thirdScore = EXTREMELY_BAD;
                break;
            case NONE:
                thirdScore = INDIFFERENT;
                break;
            case SECOND_BEFORE_FIRST:
                thirdScore = EXTREMELY_BAD;
                break;
            case SECOND_ONLY:
                thirdScore = INDIFFERENT;
                break;
        }

        return firstScore + secondScore + thirdScore;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Calculates a Score for the suburb */
    private int scoreSuburb(PhraseToBinAllocationSequence allocationSequence) {
        PhraseSequence result;
        int firstScore = -1;
        int secondScore = -1;

        // suburb before state
        result = testSequence(allocationSequence, SuburbNameBin.class, StateBin.class);

        switch (result) {
            case FIRST_BEFORE_SECOND:
                firstScore = GOOD;
                break;
            case FIRST_ADJACENT_TO_SECOND:
                firstScore = VERY_GOOD;
                break;
            case FIRST_ONLY:
                firstScore = GOOD;
                break;
            case NONE:
                firstScore = VERY_BAD;
                break;
            case SECOND_BEFORE_FIRST:
                firstScore = VERY_BAD;
                break;
            case SECOND_ONLY:
                firstScore = BAD;
                break;
        }

        // suburb before postcode
        result = testSequence(allocationSequence, SuburbNameBin.class, PostCodeBin.class);

        switch (result) {
            case FIRST_BEFORE_SECOND:
                secondScore = GOOD;
                break;
            case FIRST_ADJACENT_TO_SECOND:
                secondScore = GOOD;
                break;
            case FIRST_ONLY:
                secondScore = GOOD;
                break;
            case NONE:
                secondScore = VERY_BAD;
                break;
            case SECOND_BEFORE_FIRST:
                secondScore = VERY_BAD;
                break;
            case SECOND_ONLY:
                secondScore = BAD;
                break;
        }

        return firstScore + secondScore;
    }

    // ------------------------------------------------------------------------------------------------------
}
