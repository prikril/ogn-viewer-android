package com.meisterschueler.ognviewer;

public class Utils {
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
}

