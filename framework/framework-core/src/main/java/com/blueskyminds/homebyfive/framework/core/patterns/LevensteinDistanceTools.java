package com.blueskyminds.homebyfive.framework.core.patterns;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.math.BigDecimal;
import java.math.MathContext;

import com.blueskyminds.homebyfive.framework.core.tools.filters.FilterTools;
import com.blueskyminds.homebyfive.framework.core.tools.filters.Filter;
import com.blueskyminds.homebyfive.framework.core.tools.Named;
import com.blueskyminds.homebyfive.framework.core.alias.Aliased;
import com.blueskyminds.homebyfive.framework.core.analysis.statistics.BasicStats;
import com.blueskyminds.homebyfive.framework.core.analysis.statistics.ComputeAdapter;
import com.blueskyminds.homebyfive.framework.core.analysis.AnalysisTools;

/**
 * Provides methods for filtering collections of strings based on Levenstein Distance
 *
 * Date Started: 18/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class LevensteinDistanceTools {

    private static final Log LOG = LogFactory.getLog(LevensteinDistanceTools.class);

    private static DoubleMetaphone encoder = new DoubleMetaphone();

    /**
     * Finds a closely matching value from a set of candidates, returning the best matches in order of
     *  precedence.
     *
     * The implementation applies a levenstein distance calculation for every candidate and returns the
     * closest matching result(s).
     *
     * The closest matching results are:
     *   - any results better than 3 standard deviations from the mean distance; or
     *   - if none of the above, any results better than 2 standard deviations from the mean distance; or
     *   - if none of the above, any results better than 1 standard deviation from the mean distance;
     *   - otherwise null
     *
     * The results with the closest distance are then matched using a Metaphone and reject is they have
     *  different phonetic characters than the name
     *
     * @param name           string value to search for
     * @param candidates      array of strings to search
     * @return                zero or more matches, first is the best match
     */
    public static List<String> match(String name, String[] candidates) {
        List<StringDistance> distances = new LinkedList<StringDistance>();
        List<StringDistance> phoneticallyMatched = new LinkedList<StringDistance>();
        List<String> accepted;
        int distance;

        if (LOG.isDebugEnabled()) {
            LOG.debug("matching '"+name+"' against "+candidates.length+" candidates");
        }

        name = StringUtils.lowerCase(name);

        if (candidates.length > 0) {
            for (String candidate : candidates) {
                // use the only name
                distance = calculateDemerauLevenshteinDistance(name, StringUtils.lowerCase(candidate));
                StringDistance stringDistance = new StringDistance(distance, candidate);
                distances.add(stringDistance);
                if (phoneticallySimilar(name, candidate)) {
                    phoneticallyMatched.add(stringDistance);
                }
            }

            // compute the statistics of the distances
            BasicStats stats = AnalysisTools.computeStats(distances, new ComputeAdapter() {
                public BigDecimal valueOf(Object object) {
                    return new BigDecimal(((StringDistance) object).getDistance());
                }
            });

             // now cut-off at a reasonable relative maximum distance and filter to the closest            
            MaximumDistanceFilter<StringDistance> maxDistanceFilter = new MaximumDistanceFilter<StringDistance>(name);
            List<StringDistance> acceptedDistances = filterToClosestDistances(distances, stats, maxDistanceFilter);

            Collections.sort(acceptedDistances);

            accepted = filterBest(acceptedDistances, phoneticallyMatched, maxDistanceFilter, new ResultAdapter<String, StringDistance>() {
                public String getValue(StringDistance distance) {
                    return distance.getValueUsed();
                }
            });            
        } else {
            accepted = new LinkedList<String>();
        }

        return accepted;
    }

    /**
     * Finds a closely matching name from a set of candidates, returning the best matches in order of
     *  precedence.
     *
     * Checks the Named name AND Aliased name(s) for the Candidates.  Names are compared in lowercase
     *
     * The implementation applies a levenstein distance calculation for every candidate and returns the
     * closest matching result(s).
     * The closest matching results are:
     *   - any results better than 3 standard deviations from the mean distance; or
     *   - if none of the abouve, any results better than 2 standard deviations from the mean distance; or
     *   - if none of the above, any results better than 1 standard deviation from the mean distance;
     *   - otherwise null
     *
     * @param name            value to search for
     * @param candidates      set of named objects to seach
     * @return                zero or more matches, first is the best match
     */
    public static <T extends Named> List<T> matchName(String name, Collection<T> candidates) {
        List<NamedDistance> distances = new LinkedList<NamedDistance>();
        List<NamedDistance> phoneticallyMatched = new LinkedList<NamedDistance>();
        List<T> accepted;
        int distance;

        name = StringUtils.lowerCase(name);

        if (LOG.isDebugEnabled()) {
            LOG.debug("matching '"+name+"' against "+candidates.size()+" Named candidates");
        }

        if (candidates.size() > 0) {
            // populate the distance and phonetic population
            for (Named candidate : candidates) {
                if (candidate != null) {
                    if (candidate instanceof Aliased) {
                        // use all the names and aliases
                        for (String alias : ((Aliased) candidate).getNames()) {
                            if (alias.length() > 2) {
                                distance = calculateDemerauLevenshteinDistance(name, StringUtils.lowerCase(alias.toLowerCase()));
                                NamedDistance namedDistance = new NamedDistance(distance, alias, candidate);
                                distances.add(namedDistance);
                                if (phoneticallySimilar(name, alias)) {
                                    phoneticallyMatched.add(namedDistance);
                                }
                            } else {
                                // too short - only accept an exact match
                                if (name.equalsIgnoreCase(alias)) {
                                    distances.add(new NamedDistance(0, alias, candidate));
                                }
                            }
                        }
                    } else {
                        if (candidate.getName().length() > 0) {
                            // use the only name
                            distance = calculateDemerauLevenshteinDistance(name, StringUtils.lowerCase(candidate.getName()));
                            NamedDistance namedDistance = new NamedDistance(distance, name, candidate);
                            distances.add(namedDistance);
                            if (phoneticallySimilar(name, candidate.getName())) {
                                phoneticallyMatched.add(namedDistance);
                            }
                        } else {
                            if (name.equalsIgnoreCase(candidate.getName())) {
                                distances.add(new NamedDistance(0, name, candidate));
                            }
                        }
                    }
                }
            }

            // compute the statistics of the distances
            BasicStats stats = AnalysisTools.computeStats(distances, new ComputeAdapter() {
                public BigDecimal valueOf(Object object) {
                    return new BigDecimal(((NamedDistance) object).getDistance());
                }
            });

             // now cut-off at a reasonable relative maximum distance and filter to the closest
            MaximumDistanceFilter<NamedDistance> maxDistanceFilter = new MaximumDistanceFilter<NamedDistance>(name);
            List<NamedDistance> acceptedDistances = filterToClosestDistances(distances, stats, maxDistanceFilter);

            Collections.sort(acceptedDistances);

            accepted = filterBest(acceptedDistances, phoneticallyMatched, maxDistanceFilter, new ResultAdapter<T, NamedDistance>() {
                public T getValue(NamedDistance distance) {
                    return (T) distance.getNamed();
                }
            });
        } else {
            accepted = new LinkedList<T>();
        }

        return accepted;
    }

    private interface ResultAdapter<T, D> {
        T getValue(D distance);
    }

    /**
     * Filters the best matches out of the distance and phonetic results
     *
     * The best are (in order of precedence):
     *   - within the accepted distance and phonetically matched; or failing that:
     *   - phonetically matched and within the maximum permitted distance; or failing that:
     *   - within the accepted distance
     *
     * */
    private static <T, D extends Distance> List<T> filterBest(List<D> acceptedDistances, List<D> phoneticallyMatched, MaximumDistanceFilter<D> maxDistanceFilter, ResultAdapter<T, D> adapter) {
        List<T> accepted = new ArrayList<T>(phoneticallyMatched.size() + acceptedDistances.size());

        // First priority:  acceptable distance and phonetically matched
        if (phoneticallyMatched.size() > 0) {
            for (D distance : acceptedDistances) {
                if (phoneticallyMatched.contains(distance)) {
                    accepted.add(adapter.getValue(distance));
                }
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("   distance and phonetics matching accepted "+accepted.size()+" candidates");
        }

        if (accepted.size() == 0) {

            // try to be a little more liberal
            if (phoneticallyMatched.size() > 0) {
                // sounds similar but distance was excessive - use a relative cutoff
                acceptedDistances = FilterTools.getMatching(phoneticallyMatched, maxDistanceFilter);

                for (D distance : acceptedDistances) {
                    accepted.add(adapter.getValue(distance));
                }
            } else {
                // no phonetics match at all but the spelling is close
                for (D distance : acceptedDistances) {
                    accepted.add(adapter.getValue(distance));
                }
            }
        }
        return accepted;
    }

    /**
     * Filter the distances to the closest values not exceeding the maxmium
     * 
     * @param distances
     * @param stats
     * @param maxDistanceFilter
     * @return
     */
    private static <T extends Distance> List<T> filterToClosestDistances(List<T> distances, BasicStats stats, Filter<T> maxDistanceFilter) {
        List<T> acceptedDistances = new LinkedList<T>();

        // calculate the z-scores (normalized to a mean of zero and stddev of 1
        // the z-score is the distance from the population mean in units of the population standard deviation
        if (calculateZScores(distances, stats)) {

            // We want negative z-scores (distances are always positive and we want closer to zero)
            // see z-tables
            BigDecimal zCutOffs[] = { new BigDecimal("-2.06"), /* best 2% */
                                      new BigDecimal("-1.65"), /* best 5% */
                                      new BigDecimal("-1.29"),  /* best 10% */};

            int maxAttempts = 3;
            int attempts = 0;
            while ((acceptedDistances.size() == 0) && (attempts < maxAttempts)) {
                BigDecimal cutoff = zCutOffs[attempts];
                ZScoreFilter<T> filter = new ZScoreFilter<T>(cutoff);
                acceptedDistances = FilterTools.getMatching(distances, filter);
                attempts++;
            }
        } else {
            // there is no stddev or stdev is zero - we'll just work with the relative distance
            acceptedDistances = distances;
        }

        return FilterTools.getMatching(acceptedDistances, maxDistanceFilter);
    }

    /**
     * Calculate the Z-score for each distance
     *
     * The z-score is the distance from the population mean in units of the population standard deviation
     *
     * @param distances
     * @param stats
     * @return true if the z-scores could be calculated
     */
    private static <T extends Named> boolean calculateZScores(List<? extends Distance> distances, BasicStats stats) {
        if ((stats.getStdDev() != null) && (stats.getStdDev().compareTo(BigDecimal.ZERO) > 0)) {
            for (Distance namedDistance : distances) {
                namedDistance.setZScore((new BigDecimal(namedDistance.getDistance()).subtract(stats.getMean())).divide(stats.getStdDev(), MathContext.DECIMAL64));                
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * The relative distance cut-off is used when there's no stddev available.  It is
     *   - 50% of the length of the word;  todo: this sucks
     *   - and at least 1
     */
    private static <T extends Named> int relativeCutoff(String name) {
        return Math.max(1, name.length() / 2);
    }


    /**
     * Filters a collection of NamedDistance Entries by the maximum number of edits permitted.
     *
     * The number of edits allowed is relative to the length of the string matched.
     * The purpose is to reduce the liklihood that short words are matched to very long words too soon.
     *
     */
    private static class MaximumDistanceFilter<D extends Distance> implements Filter<D> {

        private String name;

        public MaximumDistanceFilter(String name) {
            this.name = name;

        }

        public boolean accept(D distance) {
            int cutOff = relativeCutoff(distance.getLiteralMatch());
            return distance.getDistance() <= cutOff;
        }
    }

    /**
     * Filters a collection of Distance entries by their Z-score.
     * Accepts values with Z-score less than or equal to the cutoff
     */
    private static class ZScoreFilter<T extends Distance> implements Filter<T> {

        private BigDecimal cutOff;

        public ZScoreFilter(BigDecimal cutOff) {
            this.cutOff = cutOff;
        }


        public boolean accept(Distance rd) {
            return rd.getZScore().compareTo(cutOff) <= 0;
        }
    }

    private static interface Distance {
         int getDistance();
         BigDecimal getZScore();
         String getLiteralMatch();
         void setZScore(BigDecimal zScore);
    }

    /** Encapsulates the distance and underlying Named object */
    private static class NamedDistance implements Comparable, Distance {

        public static final NamedDistance ZERO = new NamedDistance(0, null, null);

        private int distance;
        private String nameUsed;
        private Named named;
        private BigDecimal zScore;

        /**
         *
         * @param distance
         * @param nameUsed     if the namedobject has multiple names, this identifies which name was used for the distance
         * @param namedObject
         */
        public NamedDistance(int distance, String nameUsed, Named namedObject) {
            this.distance = distance;
            this.nameUsed = nameUsed;
            this.named = namedObject;
        }

        public int getDistance() {
            return distance;
        }

        public String getNameUsed() {
            return nameUsed;
        }

        public Named getNamed() {
            return named;
        }

        /** The literal string that was matched */
        public String getLiteralMatch() {
            return nameUsed;
        }

        public BigDecimal getZScore() {
            return zScore;
        }

        public void setZScore(BigDecimal zScore) {
            this.zScore = zScore;
        }

        public int compareTo(Object o) {
            return ((Integer) distance).compareTo(((NamedDistance) o).getDistance());
        }
    }


    /** Enapsulates the distance and underlying String object */
    private static class StringDistance implements Comparable, Distance {

        public static final StringDistance ZERO = new StringDistance(0, null);

        private int distance;
        private String valueUsed;
        private BigDecimal zScore;

        /**
         *
         * @param distance
         * @param valueUsed     if the namedobject has multiple names, this identifies which name was used for the distance
         */
        public StringDistance(int distance, String valueUsed) {
            this.distance = distance;
            this.valueUsed = valueUsed;
        }

        public int getDistance() {
            return distance;
        }

        public String getValueUsed() {
            return valueUsed;
        }

        public BigDecimal getZScore() {
            return zScore;
        }

        public void setZScore(BigDecimal zScore) {
            this.zScore = zScore;
        }

        /** The literal string that was matched */
        public String getLiteralMatch() {
            return valueUsed;
        }

        public int compareTo(Object o) {
            return ((Integer) distance).compareTo(((StringDistance) o).getDistance());
        }
    }

    /**
     * Calculates the minimum number of operations needed to transform str1 into str2
     *
     * This implementation uses a large two-dimensional array and will run out of memory when comparing
     * large strings.  A better implementation is in StringUtils (it's only here for comparison to the
     *  Damerau version)
     *
     * http://en.wikipedia.org/wiki/Levenshtein_distance
     *
     * @param str1
     * @param str2
     * @return
     */
    public static int calculateLevenshteinDistance(String str1, String str2) {

         // d is a table with lenStr1+1 rows and lenStr2+1 columns
        int lenStr1 = str1.length();
        int lenStr2 = str2.length();

        int[][] d = new int[lenStr1+1][lenStr2+1];

        char ch;

        // i and j are used to iterate over str1 and str2
        int i, j, cost;

        for (i = 0; i <= lenStr1; i++) {
            d[i][0] = i;
        }

        for (j = 1; j <= lenStr2; j++) {
            d[0][j] = j;
        }

        for (i = 1; i <= lenStr1; i++) {
            ch = str1.charAt(i-1);
            for (j = 1; j <= lenStr2; j++) {
                if (str2.charAt(j-1) == ch) {
                    cost = 0;
                } else {
                    cost = 1;
                }
                d[i][j] = Math.min(Math.min(
                        d[i-1][j] + 1,            // deletion
                        d[i][j-1] + 1),           // insertion
                        d[i-1][j-1] + cost);      // substitution
            }
        }

        return d[lenStr1][lenStr2];
    }

    /**
     * This variation calculates the minimum number of operations needed to transform str1 into str2
     *  with an additional enhancement over the Levenshtein algorithm in that transposition counts as
     *  one edit operation instead of two
     *
     * This implementation uses a large two-dimensional array and will run out of memory when comparing
     * large strings.
     *
     * http://en.wikipedia.org/wiki/Damerau-Levenshtein_distance
     *
     * @param str1
     * @param str2
     * @return
     */
    public static int calculateDemerauLevenshteinDistance(String str1, String str2) {

         // d is a table with lenStr1+1 rows and lenStr2+1 columns
        int lenStr1 = str1.length();
        int lenStr2 = str2.length();

        int[][] d = new int[lenStr1+1][lenStr2+1];

        char ch, ch2;

        // i and j are used to iterate over str1 and str2
        int i, j, cost;

        for (i = 0; i <= lenStr1; i++) {
            d[i][0] = i;
        }

        for (j = 1; j <= lenStr2; j++) {
            d[0][j] = j;
        }

        for (i = 1; i <= lenStr1; i++) {
            ch = str1.charAt(i-1);
            
            for (j = 1; j <= lenStr2; j++) {
                ch2 = str2.charAt(j - 1);
                if (ch2 == ch) {
                    cost = 0;
                } else {
                    cost = 1;

                }
                d[i][j] = Math.min(Math.min(
                        d[i-1][j] + 1,            // deletion
                        d[i][j-1] + 1),           // insertion
                        d[i-1][j-1] + cost);      // substitution

                if ((i > 1) && (j > 1) && (str2.charAt(j-2) == ch) && (str1.charAt(i-2) == ch2)) {
                    d[i][j] = Math.min(d[i][j], d[i-2][j-2]+cost);  // transposition
                }
            }
        }

        return d[lenStr1][lenStr2];
    }

    /**
     * Determines whether s1 and s2 are similar as determined by the match() method
     * 
     * @return true if they are similar, false if not close enough
     */
    public static boolean isSimilar(String s1, String s2) {
        return match(s1, new String[] {s2}).size() > 0;
    }

    /**
     * Determines whether s1 is similar to the Named object or its aliases as determined by the
     * match() method
     *
     * @return true if they are similar, false if not close enough
     */
    public static boolean isSimilar(String s1, Named named) {
        Set<Named> namedSet = new HashSet<Named>();
        namedSet.add(named);

        return matchName(s1, namedSet).size() > 0;
    }

    /**
     * True if s1 and s2 sound similar (by double metaphone)
     *
     * They are similar if:
     *   - they have the same double metaphone; or failing that
     *   - they start with the same double metaphone and one is a subset (shorter) than the other
     *
     * @param s1
     * @param s2
     * @return
     */
    private static boolean phoneticallySimilar(String s1, String s2) {
        if (StringUtils.isNotBlank(s1) && StringUtils.isNotBlank(s2)) {
            String reference = encoder.doubleMetaphone(s1);
            String metaphone = encoder.doubleMetaphone(s2);
            if (reference.equals(metaphone)) {
                return true;
            } else {
                if (reference.length() < metaphone.length()) {
                    return metaphone.startsWith(reference);
                } else {
                    if (metaphone.length() < reference.length()) {
                        return reference.startsWith(metaphone);
                    }
                }
            }
        }
        return false;
    }

    /**
     * Calculates the double mataphone (phonetic code) of the string
     *
     * @param s1
     * @return
     */
    public static String calculateDoubleMetaphone(String s1) {
        return encoder.doubleMetaphone(s1);
    }

    /**
     * Calculates the double mataphone (phonetic code) of the string
     *
     * @param s1
     * @return
     */
    public static String[] calculateDoubleMetaphones(String[] s1) {
        String[] result = new String[s1.length];
        for (int index = 0; index < s1.length; index++) {
            result[index] = encoder.doubleMetaphone(s1[index]);
        }
        return result;
    }

}
