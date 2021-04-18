/*
  Authors: Neriya Zudi (ID:207073545)
  Email:  neriyazudi@Gmail.com
  Department of Computer Engineering - Assignment 1 - Advanced Object-Oriented Programming
*/
package Virus;

public class Random {
    public Random()
    {
        this.RandomNumber=Math.random();;
    }
    public  double getRandomNumber()
    {
        this.RandomNumber=Math.random();
        return this.RandomNumber;
    }
    public int getRandomInRange(int max,int min)
    {
        RandomNumber = (Math.random()*(max-min+1)+min);
        return  (int)RandomNumber;
    }
    private double RandomNumber;
}
