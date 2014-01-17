package com.bitresolution.jpm.utils;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

class ReverseStringMatcher extends BaseMatcher<String> {

    private final String input;

    public static Matcher<String> reverseOf(final String input) {
        return new ReverseStringMatcher(input);
    }

    public ReverseStringMatcher(String input) {
        this.input = input;
    }

    @Override
    public boolean matches(Object o) {
        String string = (String) o;
        return new StringBuilder(input).reverse().toString().equals(string);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("reverse should return ").appendValue(new StringBuilder(input).reverse().toString());
    }
}
