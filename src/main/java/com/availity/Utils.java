package com.availity;

public class Utils {

    public static boolean isEmpty(final String s) {
        return s == null || s.isBlank();
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }
}
