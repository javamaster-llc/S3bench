package com.ca.operations.util;

import java.util.Random;

/**
 * @author jenshadlich@googlemail.com
 * @author tanji11 
 */
public class RandomDataGenerator {

    private final static Random GENERATOR = new Random();

    public static byte[] generate(int size) {
        final byte data[] = new byte[size];
        GENERATOR.nextBytes(data);
        return data;
    }

}
