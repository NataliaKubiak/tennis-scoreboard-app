package org.example.util;

import lombok.experimental.UtilityClass;

import java.util.Locale;

@UtilityClass
public class Validator {

    public static boolean isSameName(String name1, String name2) {
        return name1.equals(name2);
    }
}
