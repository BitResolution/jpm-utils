package com.bitresolution.jpm.utils;

import edu.umd.cs.findbugs.annotations.NonNull;

public final class StringUtils {

    private StringUtils() {
        throw new IllegalStateException("Can not instantiate StringUtils");
    }

    /**
     * Reverses the given input String. Note this method does not take in to account surrogate pairs.
     * @param input the String to reverse, must not be null.
     * @return the reversed String
     */
    @NonNull
    public static String reverse(@NonNull String input) {
        if(input.length() == 0) {
            return input;
        }

        char[] output = new char[input.length()];
        for(int i = 0; i < input.length(); i++) {
            output[input.length() - i - 1] = input.charAt(i);
        }
        return new String(output);
    }
}
