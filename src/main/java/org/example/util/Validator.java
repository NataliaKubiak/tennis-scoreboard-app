package org.example.util;

import lombok.experimental.UtilityClass;

import java.util.Locale;

@UtilityClass
public class Validator {

    public static boolean isSameName(String name1, String name2) {
        return name1.toLowerCase(Locale.ROOT).equals(name2.toLowerCase(Locale.ROOT));
    }
}
