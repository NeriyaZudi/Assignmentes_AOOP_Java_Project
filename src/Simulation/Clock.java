/*
  Authors: Neriya Zudi (ID:207073545)
  Email:  neriyazudi@Gmail.com
  Department of Computer Engineering - Assignment 1 - Advanced Object-Oriented Programming
*/
package Simulation;

public class Clock {

    private static long current;
    private static int tick_per_day = 1;

    public static long now() {
        return current;
    }

    public static void nextTick() {
        current+=tick_per_day;
    }

    public static int calculatePassedTime(long startTime) {
        return Math.round((int) ((startTime - now()) / tick_per_day));//method which calculate the passed time.
    }

    public static void setTick_per_day(int value) {
        tick_per_day = value;
    }


}
