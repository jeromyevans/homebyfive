package com.blueskyminds.homebyfive.framework.core.tools;

import java.util.*;

/**
 * Tools for generating random anythings
 *
 * Date Started: 1/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class RandomTools {

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the RandomTools with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------


    /** Generate a random enumeration */
    public static <E extends Enum> E randomEnum(Class<E> e) {
        return randomEnum(e, (E[]) null);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns true if the list of enumeration values contains the enumeration value E */
    private static <E extends Enum> boolean contains(E value, E... values) {

        boolean contains = false;

        for (E v : values) {
            if (v.equals(value)) {
                // found an instnace
                contains = true;
                break;
            }
        }

        return contains;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Generate a random enumeration, exlcuding the values specified*/
    public static <E extends Enum> E randomEnum(Class<E> e, E... excludes) {
        Random generator = new Random();
        E[] values = e.getEnumConstants();
        E result = null;
        List<E> subset = new LinkedList<E>();

        if (excludes != null) {
            // exclude values
            for (E value : values) {
                if (!contains(value, excludes)) {
                    subset.add(value);
                }
            }
        } else {
            subset.addAll(Arrays.asList(values));
        }

        if (subset.size() > 0) {
            result = subset.get(generator.nextInt(subset.size()));
        }

        return result;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Random integer between min and max, inclusive */
    public static int randomInt(int min, int max) {
        Random generator = new Random();
        return min+generator.nextInt((max-min)+1);
    }

    // ------------------------------------------------------------------------------------------------------

    public static double randomDouble(double min, double max) {
        Random generator = new Random();
        return min+(generator.nextDouble() * (max-min));
    }

    // ------------------------------------------------------------------------------------------------------

    /** Random valid date within two years (inclusive) */
    public static Date randomDate(int firstYear, int lastYear) {

        int year = randomInt(firstYear,  lastYear);
        int month = randomInt(0, 11);
        Calendar cal = new GregorianCalendar(year, month, 1, 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, randomInt(1, cal.getActualMaximum(Calendar.DAY_OF_MONTH)));

        return cal.getTime();
    }
    
    // ------------------------------------------------------------------------------------------------------

}
