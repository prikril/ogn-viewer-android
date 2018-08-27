package com.meisterschueler.ognviewer.common;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AprsFilterManager {
    public static Circle parse(String aprs_filter) {
        String re_float = "[+-]?\\d*\\.?\\d*";
        String re_range = "^r/(" + re_float + ")/(" + re_float + ")/(" + re_float + ")$";

        Pattern pattern = Pattern.compile(re_range);
        Matcher matcher = pattern.matcher(aprs_filter);

        Circle result = null;
        if (matcher.matches()) {
            result = new Circle();
            result.lat = Double.parseDouble(matcher.group(1));
            result.lon = Double.parseDouble(matcher.group(2));
            result.radius = Double.parseDouble(matcher.group(3));
        }

        return result;
    }

    public static String latLngToAprsFilter(double lat, double lon) {
        return String.format(Locale.US, "r/%1$.3f/%2$.3f/%3$.1f", lat, lon, 100.0);
    }

    public static class Circle {
        double lat;
        double lon;
        double radius;


        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }

        public double getRadius() {
            return radius;
        }

    }

}