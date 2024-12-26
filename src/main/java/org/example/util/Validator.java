package org.example.util;

import lombok.experimental.UtilityClass;

import java.util.Locale;
import java.util.regex.Pattern;

@UtilityClass
public class Validator {

    private static final String QUERY_UUID_PATTERN = "^uuid=[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$";

    public static boolean isSameName(String name1, String name2) {
        return name1.equals(name2);
    }

    public static boolean isValidUuidQueryParam(String input) {
        Pattern queryPattern = Pattern.compile(QUERY_UUID_PATTERN);

        if (input == null || input.isEmpty()) {
            return false;
        }

        return queryPattern.matcher(input).matches();
    }
}
