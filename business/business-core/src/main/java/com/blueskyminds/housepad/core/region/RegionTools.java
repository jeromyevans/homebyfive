package com.blueskyminds.housepad.core.region;

import org.apache.commons.lang.StringUtils;
import com.blueskyminds.homebyfive.framework.framework.tools.text.StringTools;

/**
 * Date Started: 5/01/2008
 * <p/>
 * History:
 */
public class RegionTools {

    public static String encode(String value) {
        return StringUtils.lowerCase(StringTools.escapeForHTTP(value));
    }
}
