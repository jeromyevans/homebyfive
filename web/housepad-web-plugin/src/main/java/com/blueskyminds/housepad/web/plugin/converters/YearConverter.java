package com.blueskyminds.housepad.web.plugin.converters;

import org.apache.struts2.util.StrutsTypeConverter;

import java.util.Map;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.ParseException;

import com.opensymphony.xwork2.conversion.TypeConversionException;

/**
 * A data converter that accepts a year as a single value
 *
 * Date Started: 9/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */

public class YearConverter extends StrutsTypeConverter {

    /**
     * Converts one or more String values to the specified class.
     *
     * @param context the action context
     * @param values  the String values to be converted, such as those submitted from an HTML form
     * @param toClass the class to convert to
     * @return the converted object
     */
    public Object convertFromString(Map context, String[] values, Class toClass) {
        Date result = null;
        if (values != null) {
            if (values.length != 1) {
                super.performFallbackConversion(context, values, toClass);
            }
            if (values.length > 0) {
                try {
                    int yearInt = Integer.parseInt(values[0]);
                    if (yearInt < 1900) {
                        yearInt+= 1900;
                    }
                    Calendar calendar = GregorianCalendar.getInstance();
                    calendar.set(yearInt, 0, 1, 0, 0, 0);
                    result = calendar.getTime();
                } catch (NumberFormatException e) {
                    throw new TypeConversionException("Not the expected year format", e);
                }
            }
        }
        return result;
    }

    /**
     * Converts the specified object to a String.
     *
     * @param context the action context
     * @param o       the object to be converted
     * @return the converted String
     */
    public String convertToString(Map context, Object o) {
        if (o instanceof Date) {
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime((Date) o);
            return Integer.toString(calendar.get(Calendar.YEAR));
        } else {
            return null;
        }
    }
}
