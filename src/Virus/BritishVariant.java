/*
  Authors: Neriya Zudi (ID:207073545)
  Email:  neriyazudi@Gmail.com
  Department of Computer Engineering - Assignment 1 - Advanced Object-Oriented Programming
*/
package Virus;

import Population.Person;
import Population.Sick;
import Simulation.Clock;

public class BritishVariant implements IVirus{

    //Interface Realization
    @Override
    public double contagionProbability(Person p) {
        return 0.7*p.contagionProbability();
    }

    @Override
    public boolean tryToContagion(Person p1, Person p2) {

        double probability=0;//Probability of infection
        Random r =new Random();
        if (p2 instanceof Sick)//Unable to infect a sick person
            return false;

            double d=p1.getDistance(p2.getLocation());//Calculate distance between two people
            probability= p2.contagionProbability() * Math.min(1, (0.14 * Math.exp(2 - (0.25 * d))));//Calculate the probability of infection
            return probability >= r.getRandomNumber();//Returns true or false if contagion succeeds

    }

    @Override
    public boolean tryToKill(Sick s) {
        double probability=0;
        double t= Clock.now()-s.getContagiousTime();
        Random r =new Random();
        if(s.getAge() >=0 && s.getAge() <= 18)
            probability=Math.max(0,0.01-(0.01*0.01)*Math.pow((t-15),2));
        else
            probability=Math.max(0,0.1-(0.01*0.1)*Math.pow((t-15),2));

        if (probability >= r.getRandomNumber())
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "British Variant";
    }
}
