package com.bitresolution.jpm.utils;

import org.junit.Test;

import java.util.List;

import static com.bitresolution.jpm.utils.ReverseStringMatcher.reverseOf;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StringUtilsTest {

    @Test
    public void shouldReverseSimpleStrings() {
        //given
        List<String> strings = asList(
                "abc",
                "12aaa21",
                "awdrg-thuko;[\'"
        );
        //then
        for(String string : strings) {
            assertThat(StringUtils.reverse(string), is(reverseOf(string)));
        }
    }
}
