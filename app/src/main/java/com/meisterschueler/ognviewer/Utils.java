package com.meisterschueler.ognviewer;

import android.graphics.Color;

public class Utils {
    public static int getColor(int value, int min, int max, int minColor, int maxColor) {
        float colorValue = (value - min) / (max - min);
        colorValue = minColor + colorValue * (maxColor - minColor);

        return getColor(colorValue);
    }

    public static int getColor(float value) {
        if (value <= 0 || value >= 6) {                                     // red (0)
            return Color.rgb(255, 0, 0);
        } else if (value > 0 && value <= 1) {                               // red to yellow (1)
            return Color.rgb(255, (int) (255.0 * value), 0);
        } else if (value > 1 && value <= 2) {                               // yellow to green (2)
            return Color.rgb(255 + (int) ((1.0 - value) * 255.0), 255, 0);
        } else if (value > 2 && value <= 3) {                               // green to cyan (3)
            return Color.rgb(0, 255, (int) ((value - 2.0) * 255.0));
        } else if (value > 3 && value <= 4) {                               // cyan to blue (4)
            return Color.rgb(0, (int) ((4.0 - value) * 255.0), 255);
        } else if (value > 4 && value <= 5) {                               // blue to violett (5)
            return Color.rgb((int) (value - 4.0) * 255, 0, 255);
        } else if (value > 5 && value <= 6) {
            return Color.rgb(255, 0, (int) ((6.0 - value) * 255.0));
        } else {
            return Color.rgb(0, 0, 0);
        }
    }
}

