package com.blueskyminds.homebyfive.framework.framework.datetime;

/**
 * Periodic time-span enumerations
 *
 * Date Created: 25 Jun 2006
 *
 */

public enum PeriodTypes {
   OnceOff,
   Second,
   Minute,
   Hour,
   Day,
   Week,
   Fortnight,
   Month,
   Quarter,
   Year;

    /**
     * The number occurrences in a 365 day year.
     *
     * @return the integer value
     */
    public int occurrencesInOneYear() {
        int occurrences = 0;
        switch (this) {
            case Day:
                occurrences = 365;
                break;
            case Fortnight:
                occurrences = 26;
                break;
            case Hour:
                occurrences = 8760;
                break;
            case Minute:
                occurrences = 525600;
                break;
            case Month:
                occurrences = 12;
                break;
            case OnceOff:
                occurrences = 1;
                break;
            case Quarter:
                occurrences = 4;
                break;
            case Second:
                occurrences = 31536000;
                break;
            case Week:
                occurrences = 52;
                break;
            case Year:
                occurrences = 1;
                break;
        }
        return occurrences;
    }
}
