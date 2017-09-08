package com.meisterschueler.ognviewer;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {
    @Test
    public void getHue() {
        float hue = Utils.getHue(0, 0, 100, 120, 360);
        Assert.assertEquals(hue, 120, 0.1);

        hue = Utils.getHue(4, 1, 7, 90, 180);
        Assert.assertEquals(hue, 135, 0.1);
    }

    @Test
    public void getHue_exceed() {
        float hue = Utils.getHue(1, 2, 3, 0, 90);
        Assert.assertEquals(hue, 0, 0.1);

        hue = Utils.getHue(2, 0, 1, 0, 90);
        Assert.assertEquals(hue, 90, 0.1);
    }

    @Test
    public void getHue_invalid() {
        float hue = Utils.getHue(1, 0, 0, 0, 360);
        Assert.assertEquals(hue, 0, 0.1);

        hue = Utils.getHue(1, 0, 1, 0, 360);
        Assert.assertEquals(hue, 0, 0.1);
    }
}
