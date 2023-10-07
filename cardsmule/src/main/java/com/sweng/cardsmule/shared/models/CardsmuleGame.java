package com.sweng.cardsmule.shared.models;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum CardsmuleGame {
    MAGIC,
    POKEMON,
    YUGIOH;

    private static final List<CardsmuleGame> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    
} 
