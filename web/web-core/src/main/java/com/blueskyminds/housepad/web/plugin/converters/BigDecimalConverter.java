package com.blueskyminds.housepad.web.plugin.converters;

import org.apache.struts2.util.StrutsTypeConverter;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.math.BigDecimal;

import com.opensymphony.xwork2.conversion.TypeConversionException;

/**
 * Date Started: 30/04/2008
 */
public class BigDecimalConverter extends StrutsTypeConverter {

    public Object convertFromString(Map context, String[] values, Class toClass) throws TypeConversionException {
        Object result = null;
        if (BigDecimal.class.equals(toClass)) {
            if ((values != null) && (values.length > 0)) {
                if (values.length == 1) {
                    if (values[0] != null && values[0].length() > 0) {
                        try {
                            result = new BigDecimal(values[0]);
                        } catch (NumberFormatException e) {
                            throw new TypeConversionException("Invalid number", e);
                        }
                    }
                } else {
                    BigDecimal[] array = new BigDecimal[values.length];
                    try {
                        for (int i = 0; i < values.length; i++) {
                            if (values[i] != null && values[i].length() > 0) {
                                array[i] = new BigDecimal(values[i]);
                            }
                        }
                    } catch (NumberFormatException e) {
                        throw new TypeConversionException("Invalid number", e);
                    }
                }
            }
        }
        return result;
    }

    public String convertToString(Map context, Object o) {
        if (o != null) {
            return o.toString();
        } else {
            return null;
        }
    }
}
