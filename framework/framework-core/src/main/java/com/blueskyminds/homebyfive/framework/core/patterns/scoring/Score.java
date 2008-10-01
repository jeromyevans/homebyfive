package com.blueskyminds.homebyfive.framework.core.patterns.scoring;

import java.text.DecimalFormat;

/**
 * Represents a relative score for a ScoringStrategy
 * If two scores are equal in value, the fuzziness is used to choose the better one in compareTo
 *
 * Date Started: 27/10/2007
 * <p/>
 * History:
 */
public class Score implements Comparable {

    private int score;
    private float fuzziness;


    /** Create a score with no fuzzy component */
    public Score(int score) {
        this.score = score;
        fuzziness = 0.0f;
    }

    public Score(int score, float fuzziness) {
        this.score = score;
        this.fuzziness = fuzziness;
    }

    public int getScore() {
        return score;
    }

    public float getFuzziness() {
        return fuzziness;
    }

    /**
     * If two scores are equal in value, the fuzziness is used to choose the better one in compareTo
     * Less fuzziness is a higher score
     *
     * @param o
     * @return
     */
    public int compareTo(Object o) {
        if (o instanceof Score) {
            Score other = (Score) o;
            if (this.score < other.score) {
                return -1;
            } else {
                if (this.score > other.score) {
                    return 1;
                } else {
                    if (this.fuzziness < other.fuzziness) {
                        return 1;
                    } else {
                        if (this.fuzziness > other.fuzziness) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                }
            }
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return score + " ["+new DecimalFormat("0.00").format(fuzziness)+"]";
    }
}
