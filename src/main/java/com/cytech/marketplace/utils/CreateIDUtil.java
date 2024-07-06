package com.cytech.marketplace.utils;

public class CreateIDUtil {
    public static long createID() {
        long randomNumber = (long) (Math.random() * 10);
        return System.currentTimeMillis() * 10 + randomNumber;
    }
}
