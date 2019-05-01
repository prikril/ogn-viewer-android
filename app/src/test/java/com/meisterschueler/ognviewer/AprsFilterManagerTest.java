package com.meisterschueler.ognviewer;

import com.meisterschueler.ognviewer.common.AprsFilterManager;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AprsFilterManagerTest {

    private static final double DELTA = 1e-15;

    @Test
    public void simpleIntegers() {
        AprsFilterManager.Circle result = AprsFilterManager.parse("r/52/13/100");
        assertEquals(result.getLat(), 52, DELTA);
        assertEquals(result.getLon(), 13, DELTA);
        assertEquals(result.getRadius(), 100, DELTA);
    }

    @Test
    public void simpleFloats() {
        AprsFilterManager.Circle result = AprsFilterManager.parse("r/+52.513/-13.500/100.041");
        assertEquals(result.getLat(), 52.513, DELTA);
        assertEquals(result.getLon(), -13.500, DELTA);
        assertEquals(result.getRadius(), 100.041, DELTA);
    }

    @Test
    public void uglyFloats() {
        AprsFilterManager.Circle result = AprsFilterManager.parse("r/+052./-.500/100");
        assertEquals(result.getLat(), 52.0, DELTA);
        assertEquals(result.getLon(), -0.500, DELTA);
        assertEquals(result.getRadius(), 100.0, DELTA);
    }

    @Test
    public void invalidFloat() {
        AprsFilterManager.Circle result = AprsFilterManager.parse("r//13/100");
        assertNull(result);
    }
}
