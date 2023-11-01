package com.sweng.cardsmule.shared.models;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Grade {
    Poor(1), NearMint(2), Mint(3), Good(4), Excellent(5);
    private final int value;
    private static final List<Grade> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    Grade(int value) {
        this.value = value;
    }

    public static Grade getGrade(int value) {
    	switch (value) {
        case 1:
            return Poor;
        case 2:
            return NearMint;
        case 3:
            return Mint;
        case 4:
            return Good;
        case 5:
            return Excellent;
        default:
            return null;
    	}
    }
    
    public int getValue() {
        return value;
    }
    
    public static Grade getRandomGrade()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
