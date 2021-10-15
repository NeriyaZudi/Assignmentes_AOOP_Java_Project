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

public class Sick extends Person {

    //Data members
    private long contagiousTime;
    private IVirus virus;

    //ctors
    public Sick(int age, Point location, Settlement settlement, long contagiousTime, IVirus virus) {
        super(age, location, settlement);
        this.contagiousTime = Clock.now();
        this.virus = virus;
    }

    public Sick(Sick other) {
        super(other.getAge(), other.getLocation(), other.getSettlement());
        this.contagiousTime = other.getContagiousTime();
        this.virus = other.getVirus();

    }

    @Override
    public double contagionProbability() {
        return 1;
    }

    @Override
    public Person contagion(IVirus v) {
        throw new UnsupportedOperationException("already sick");
    }

    @Override
    public String toString() {
        return "Sick{" +
                "contagiousTime=" + contagiousTime +
                ", virus=" + virus.toString() +
                '}' + super.toString();
    }

    //getters
    public long getContagiousTime() {
        return contagiousTime;
    }

    public IVirus getVirus() {
        return virus;
    }

    public Person recover() {
        int age = getAge();
        Point location = new Point(getLocation());
        Settlement settlement = getSettlement();
        IVirus virus = getVirus();
        Convalescent convalescent = new Convalescent(age, location, settlement, virus);
        return convalescent;
    }

    public boolean tryToDie() {
        return virus.tryToKill(this);
    }

}
