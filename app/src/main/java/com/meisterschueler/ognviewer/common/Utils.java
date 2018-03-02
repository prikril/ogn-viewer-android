package com.meisterschueler.ognviewer.common;

public class Utils {
    private final static float METERS_TO_FEET = 3.2808398950131f;
    private final static float KMH_TO_MPH = 0.62137119223733f;
    private final static float KMH_TO_KT = 0.53995680346039f;
    private final static float MS_TO_FPM = 196.8504f;

    public static float getHue(float value, float min, float max, int minColor, int maxColor) {
        float hue;
        if (min == max || value <= min) {
            hue = minColor;
        } else if (value > max) {
            hue = maxColor;
        } else {
            float colorValue = (value - min) / (max - min);     // from 0.0 to 1.0
            hue = minColor + colorValue * (maxColor - minColor);
        }

        float result = hue % 360.0f;
        if (result >= 360.0 || result < 0) {
            result = 0;
        }

        return result;
    }

    public static float metersToFeet(float meters) {
        return meters * METERS_TO_FEET;
    }

    public static float kmhToMph(float kmh) {
        return kmh * KMH_TO_MPH;
    }

    public static float kmhToKt(float kmh) {
        return kmh * KMH_TO_KT;
    }

    public static float msToFpm(float ms) {
        return ms * MS_TO_FPM;
    }
}

