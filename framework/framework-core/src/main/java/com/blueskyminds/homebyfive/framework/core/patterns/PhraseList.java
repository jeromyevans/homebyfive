package com.blueskyminds.homebyfive.framework.core.patterns;

import com.blueskyminds.homebyfive.framework.core.tools.text.StringTools;

import java.util.List;
import java.util.LinkedList;

import org.apache.commons.lang.StringUtils;

/**
 * Contains immutable information about the phrases decomposed from an input string
 *
 * Date Started: 15/07/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class PhraseList {

    /**
     * Optimization that limits the number of words permitted in a phrase.  This can make a significant
     * performance improvement for large text but means it won't match any pattern containing more than this
     * many parts
     */
    private static final int MAX_WORDS_IN_A_PHRASE = 4;

    /** The number of words in the input string */
    private int noOfWords;

    /** the phrases in the input string */
    private List<Phrase> phraseList;

    public PhraseList(String inputStream) {
        extractPhrases(inputStream);
    }

    protected PhraseList(List<Phrase> phrases, int noOfWords) {
        this.phraseList = phrases;
        this.noOfWords = noOfWords;
    }

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
        phraseList = new LinkedList<Phrase>();

        // remember how many words there are
        noOfWords = wordList.length;

        // setup the initial phrase list (single woirds)
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
        // phrases are added from longest to shortest
        for (int length = 1; length <= Math.min(noOfWords, MAX_WORDS_IN_A_PHRASE); length++) {
//        for (int length = Math.min(noOfWords, MAX_WORDS_IN_A_PHRASE); length >= 0; length--) {
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

    /**
     * Create a new PhraseList containing only the words from start to end inclusive 
     * @param start
     * @param end
     * @return
     */
    private PhraseList trim(int start, int end) {
        if (start <= end) {
            List<Phrase> sublist = new LinkedList<Phrase>();
            int noOfWords = 0;
            for (Phrase candidate : phraseList) {
                if (candidate.getFirstIndex() >= start && candidate.getLastIndex() <= end) {
                    sublist.add(candidate);
                    if (candidate.getNoOfWords() > noOfWords) {
                        noOfWords = candidate.getNoOfWords();
                    }
                }
            }
            return new PhraseList(sublist, noOfWords);
        } else {
            return emptyList();
        }
    }

    /**
     * Returns a new PhraseList trimmed left of the right side of the phrase. The result excludes the phrase.
     *
     * @param phase
     * @return
     */
    public PhraseList trimLeft(Phrase phase) {
        int start = phase.getLastIndex()+1;
        int end = phraseList.size() - 1;
        return trim(start, end);
    }

    /**
     * Returns a new PhraseList trimmed left of the right side of the phrase. The result includes the phrase.
     *
     * @param phase
     * @return
     */
    public PhraseList trimLeftKeeping(Phrase phase) {
        int start = phase.getFirstIndex();
        int end = phraseList.size() - 1;
        return trim(start, end);
    }

    /**
     * Returns a new PhraseList trimmed right of the left side of the phrase. The result excludes the phrase.
     *
     * @param phase
     * @return
     */
    public PhraseList trimRight(Phrase phase) {
        int start = 0;
        int end = phase.getFirstIndex()-1;
        return trim(start, end);
    }

    /**
     * Returns a new PhraseList trimmed right of the right side of the phrase. The result includes the phrase.
     *
     * @param phase
     * @return
     */
    public PhraseList trimRightKeeping(Phrase phase) {
        int start = 0;
        int end = phase.getLastIndex();
        return trim(start, end);
    }

    protected static PhraseList emptyList() {
        return new PhraseList(new LinkedList<Phrase>(), 0);
    }

    public List<Phrase> asList() {
        return phraseList;
    }

    public String toString() {
        return StringUtils.join(phraseList, ",");
    }

    public int size() {
        return phraseList.size();
    }

    public String getLongestPhrase() {
        Phrase longest = null;
        for (Phrase phrase : phraseList) {
            if (longest == null || phrase.getNoOfWords() > longest.getNoOfWords()) {
                longest = phrase;
            }
        }
        if (longest != null) {
            return longest.toString();
        } else {
            return null;
        }
    }

    public int getNoOfWords() {
        return noOfWords;
    }
}
