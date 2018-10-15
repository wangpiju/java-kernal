package com.hs3.utils;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;

public class NumUtils {
    private static final Logger logger = LoggerFactory.getLogger(NumUtils.class);
    private static final Random random = getRandomInstance();

    public static Integer getMax(int n1, int... args) {
        int[] arrayOfInt;
        int j = (arrayOfInt = args).length;
        for (int i = 0; i < j; i++) {
            Integer n = Integer.valueOf(arrayOfInt[i]);
            if (n.intValue() > n1) {
                n1 = n.intValue();
            }
        }
        return Integer.valueOf(n1);
    }

    public static Integer getMin(int n1, int... args) {
        int[] arrayOfInt;
        int j = (arrayOfInt = args).length;
        for (int i = 0; i < j; i++) {
            Integer n = Integer.valueOf(arrayOfInt[i]);
            if (n.intValue() < n1) {
                n1 = n.intValue();
            }
        }
        return Integer.valueOf(n1);
    }

    public static Random getRandomInstance() {
        Random dom = null;
        OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long times = Runtime.getRuntime().freeMemory() + osmb.getFreePhysicalMemorySize();

        long seed = System.currentTimeMillis() + 105823825408L + times;
        try {
            dom = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
            dom = new Random();
        }
        dom.setSeed(seed);
        return dom;
    }

    public static int getRandom(int min, int max) {
        return getRandom(random, min, max);
    }

    public static int getRandom(Random rand, int min, int max) {
        return rand.nextInt(max - min + 1) + min;
    }

    public static int toInt(Object d, int def) {
        try {
            return Integer.parseInt(d.toString());
        } catch (Exception e) {
        }
        return def;
    }

    public static int toInt(Object d) {
        return toInt(d, 0);
    }

    public static float toFloat(Object d, float def) {
        try {
            return Float.parseFloat(d.toString());
        } catch (Exception e) {
        }
        return def;
    }

    public static float toFloat(Object d) {
        return toFloat(d, 0.0F);
    }

    public static double toDouble(Object d, double def) {
        try {
            return Double.parseDouble(d.toString());
        } catch (Exception e) {
        }
        return def;
    }

    public static double toDouble(Object d) {
        return toDouble(d, 0.0D);
    }
}
