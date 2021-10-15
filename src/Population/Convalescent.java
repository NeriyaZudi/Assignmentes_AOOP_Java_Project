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

public class Convalescent extends Person {

    private final double probability = 0.2;
    private IVirus virus;

    public Convalescent(int age, Point location, Settlement settlement, IVirus virus) {
        super(age, location, settlement);
        this.virus = virus;
    }

    public  double getProbability() {
        return probability;
    }

    @Override
    public double contagionProbability() {

        return getProbability();
    }

    @Override
    public Person contagion(IVirus v) {

        int age = getAge();
        Point location = new Point(getLocation());
        Settlement settlement = getSettlement();
        long contagiousTime = Clock.now();
        Sick s = new Sick(age, location, settlement, contagiousTime, v);
        return s;
    }

    @Override
    public String toString() {
        return "Convalescent{" +
                "virus=" + getVirus() +
                '}' + super.toString();
    }

    //getters
    public IVirus getVirus() {
        return virus;
    }
}
