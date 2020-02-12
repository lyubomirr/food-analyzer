package bg.sofia.uni.fmi.mjt.foodanalyzer.server.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {
    public static List<String> getWords(String input) {
        return Arrays.stream(input.trim()
                .split(Constants.WHITESPACE_SPLIT_REGEX))
                .map(word -> cleanUp(word))
                .collect(Collectors.toList());
    }

    private static String cleanUp(String word) {
        return word.toLowerCase()
                .replaceAll(Constants.WORD_CLEANUP_REGEX, Constants.EMPTY_STRING);
    }

    public static boolean doesStringContainsAllTokens(String value, List<String> tokens) {
        var stringTokens = Arrays.asList(value.toLowerCase().split(Constants.WHITESPACE_SPLIT_REGEX));
        return tokens.stream().allMatch(stringTokens::contains);
    }
}
