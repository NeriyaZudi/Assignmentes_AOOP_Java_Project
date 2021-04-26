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
}
