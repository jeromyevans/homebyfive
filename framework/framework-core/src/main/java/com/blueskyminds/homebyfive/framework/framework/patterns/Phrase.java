package com.blueskyminds.homebyfive.framework.framework.patterns;

/**
 * Represents one or more sequence of words with a record of where they came from with respect to the original
 * word list
 *
 * Date Started: 18/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */

public class Phrase {
    private int firstIndex;
    private int lastIndex;
    private String phrase;

    /**
     * @param phrase
     * @param firstIndex  index of the first word
     * @param lastIndex   index of the last word
     */
    public Phrase(String phrase, int firstIndex, int lastIndex) {
        this.firstIndex = firstIndex;
        this.lastIndex = lastIndex;
        this.phrase = phrase;
    }


    public String getStringLiteral() {
        return phrase;
    }

    public int getFirstIndex() {
        return firstIndex;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    /** Get the number of words used in this phrase */
    public int getNoOfWords() {
        return lastIndex - firstIndex + 1;
    }

    // --------------------------------------------------------------------------------------------------

    /** Determines whether this phrase overlaps the other phrase by comparing the start and end positions.
     * 
     * @param other
     * @return true if they do overlap
     */
    public boolean overlaps(Phrase other) {
        boolean overlaps = true;

        if (this.lastIndex < other.firstIndex) {
            // okay
            overlaps = false;
        } else {
            if (this.firstIndex > other.lastIndex) {
                // okay
                overlaps = false;
            }
        }
        return overlaps;
    }

    // --------------------------------------------------------------------------------------------------

    public String toString() {
        return phrase;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Phrase phrase1 = (Phrase) o;

        if (firstIndex != phrase1.firstIndex) return false;
        if (lastIndex != phrase1.lastIndex) return false;
        if (!phrase.equals(phrase1.phrase)) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = firstIndex;
        result = 31 * result + lastIndex;
        result = 31 * result + phrase.hashCode();
        return result;
    }
}