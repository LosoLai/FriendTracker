package com.example.loso.friendtracker.Model;

import java.util.Locale;

/**
 * Stores walk time in 2 formats as provided by google distance matrix API
 * <p>
 * Created by Lettisia George on 01/10/2017.
 */

public class WalkTime {
    public static final double INVALID_WALK_TIME = -100.0;
    public static final String INVALID_WALK_STRING = "unknown";
    private double numericTime;
    private String stringTime;

    public WalkTime(double numericTime, String stringTime) {
        this.numericTime = numericTime;
        this.stringTime = stringTime;
    }

    public WalkTime() {
        numericTime = INVALID_WALK_TIME;
        stringTime = INVALID_WALK_STRING;
    }

    public double getNumericTime() {
        return numericTime;
    }

    public void setNumericTime(double numericTime) {
        this.numericTime = numericTime;
    }

    public boolean invalid() {
        return numericTime == INVALID_WALK_TIME || stringTime == INVALID_WALK_STRING;
    }

    public String getStringTime() {
        return stringTime;
    }

    public void setStringTime(String stringTime) {
        this.stringTime = stringTime;
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%s, %.3fs", stringTime, numericTime);
    }


}
