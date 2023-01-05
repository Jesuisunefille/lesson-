package com.cindy.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author cindy
 */
public class NullUtil {

    public static boolean hasNull(Object... params) {
        for (Object param : params) {
            if (param == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasBlank(String... params) {
        for (String param : params) {
            if (StringUtils.isBlank(param)) {
                return true;
            }
        }
        return false;
    }
}
