package com.blueskyminds.housepad.web.plugin.converters;

import org.apache.struts2.util.StrutsTypeConverter;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.math.BigDecimal;

import com.opensymphony.xwork2.conversion.TypeConversionException;
import com.blueskyminds.framework.measurement.Area;
import com.blueskyminds.framework.measurement.UnitsOfArea;

/**
 * Todo; the string->area conversion is crap and not required.  the comonents of an Area should be converted separately.
 * Date Started: 10/06/2008
 */
public class AreaConverter extends StrutsTypeConverter {

    public Object convertFromString(Map context, String[] values, Class toClass) throws TypeConversionException {
        Object result = null;
        if (Area.class.equals(toClass)) {
            if ((values != null) && (values.length > 0)) {
                if (values.length == 1) {
                    if (values[0] != null && values[0].length() > 0) {
                        try {
                            result = new Area(new BigDecimal(values[0]), UnitsOfArea.SquareMetre);
                        } catch (NumberFormatException e) {
                            throw new TypeConversionException("Invalid number", e);
                        }
                    }
                } else {
                    Area[] array = new Area[values.length];
                    try {
                        for (int i = 0; i < values.length; i++) {
                            if (values[i] != null && values[i].length() > 0) {
                                array[i] = new Area(new BigDecimal(values[0]), UnitsOfArea.SquareMetre);
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
            return ((Area) o).getAmount().toString();
        } else {
            return null;
        }
    }
}