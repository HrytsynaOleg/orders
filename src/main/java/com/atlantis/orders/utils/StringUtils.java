package com.atlantis.orders.utils;

import javafx.util.Pair;
import java.util.List;
import java.util.regex.Pattern;

public class StringUtils {
    public static final String[] SYMBOLS = {"\t", "\n", "\"", "\\", " ", ".", ",", "/", "-", "_", "{", "}", "[", "]",
            "(", ")", ">", "<", "=", ":", ";", "#"};

    public static final String EMPTY = "";

    public static boolean isBlank(String text) {
        return (text == null || EMPTY.equalsIgnoreCase(text));
    }

    public static boolean isNotBlank(String text) {
        return (text != null && !EMPTY.equalsIgnoreCase(text));
    }

    public static String removeSymbols(String text) {
        if (StringUtils.isBlank(text)) return "";

        for (String symbol : SYMBOLS) {
            text = text.replace(symbol, "");
        }
        return text;
    }

    public static boolean ifNeedRemoveSymbols(String text) {

        for (String symbol : SYMBOLS) {
            if (text.contains(symbol)) return true;
        }
        return false;
    }

    public static String removeQuotas(String text) {

        if (StringUtils.isBlank(text)) return "";
        String result;
        result = text.replace("\"", "");
        result = result.replace("\\", "-");
        result = result.replace("{", "");
        result = result.replace("}", "");
        result = result.replace("<", "");
        result = result.replace(">", "");
        return result;
    }

    public static Boolean compareStringOnPattern(String text, String patterntext) {

        Pattern pattern = Pattern.compile(patterntext);
        return pattern.matcher(text).matches();
    }

    public static Boolean isEmptyQuantity(String value) {

        Boolean result;

        value = value.replace(" ", "");

        switch (value) {
            case "0":
            case "-":
            case "":
            case "0,00":
                result = true;
                break;
            default:
                result = false;
                break;
        }
        return result;
    }

    public static Boolean compareStringIgnoreCase(String text, String matchString) {
        return org.apache.commons.lang3.StringUtils.containsIgnoreCase(text, matchString);
    }

    public static String makeUrlWithParameters(String url, List<Pair<String, String>> parameters) {

        StringBuilder builder = new StringBuilder(url);

        if (parameters.size() > 0) {
            builder.append("?");

            for (Pair<String, String> parameter : parameters) {
                builder.append(parameter.getKey())
                        .append("=")
                        .append(parameter.getValue())
                        .append("&");
            }
        }

        return builder.toString();
    }


}
