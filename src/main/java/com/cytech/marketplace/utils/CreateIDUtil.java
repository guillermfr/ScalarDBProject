package com.cytech.marketplace.utils;

public class CreateIDUtil {
    public static long createID() {
        long randomNumber = 1000 + (int) (1000 + Math.random() * 8999);
        return System.currentTimeMillis() * 10000 + randomNumber;
    }
}
