package com.dogedev.doge.utils;

import java.util.Random;


//From superblaubeere27's clientbase
public class NumberUtils {
    private static final Random RANDOM = new Random();

    public static int random(int min, int max) {
        if (max <= min) return min;

        return RANDOM.nextInt(max - min) + min;
    }
}
