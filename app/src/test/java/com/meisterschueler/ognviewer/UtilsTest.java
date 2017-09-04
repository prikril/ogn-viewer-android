package com.meisterschueler.ognviewer;

import android.graphics.Color;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {
    @Test
    public void getColor() {
        int color = Utils.getColor(0, 0, 100, 0, 6);
        Assert.assertEquals(color, Color.rgb(255, 0, 0));

        color = Utils.getColor(2, 1, 7, 0, 6);
        Assert.assertEquals(color, Color.rgb(255, 255, 0));
    }
}
