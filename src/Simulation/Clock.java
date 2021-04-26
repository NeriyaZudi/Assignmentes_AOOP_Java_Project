/*
  Authors: Neriya Zudi (ID:207073545)
  Email:  neriyazudi@Gmail.com
  Department of Computer Engineering - Assignment 1 - Advanced Object-Oriented Programming
*/
package Simulation;

public class Clock {

    public static long now(){
        return current;
    }
    public static void nextTick(){
        current++;

    }

    private static long current;
    private static int tick_per_day =1;

    public static int calculatePassedTime(long starttime)
    {
        return Math.round((int)((starttime-now())/tick_per_day));//method which calculate the passed time.
    }



}
