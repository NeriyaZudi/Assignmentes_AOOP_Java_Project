/*
  Authors: Neriya Zudi (ID:207073545)
  Email:  neriyazudi@Gmail.com
  Department of Computer Engineering - Assignment 1 - Advanced Object-Oriented Programming
*/
package Population;

import Country.Settlement;
import Location.Point;
import Simulation.Clock;
import Virus.IVirus;

public class Healthy extends Person{

    public Healthy(int age, Point location, Settlement settlement)
    {
        super(age,location,settlement);
    }


    @Override
    public double contagionProbability() {
        return getProbability();
    }

    @Override
    public Person contagion(IVirus v) {
        int age = getAge();
        Point location = new Point(getLocation());
        Settlement settlement=getSettlement();
        long contagiousTime=1;
        Sick s= new Sick(age,location,settlement,contagiousTime,v);
        return s;
    }

    @Override
    public String toString() {
        return "Healthy" + super.toString();
    }

    public Person vaccinate(){

        int age = getAge();
        Point location = new Point(getLocation());
        Settlement settlement=getSettlement();
        long time = Clock.now();
        Vaccinated v=new Vaccinated(age,location,settlement,time);
        return v;
    }
    public static double getProbability() {
        return probability;
    }

    private static final double probability=1.0;
}
