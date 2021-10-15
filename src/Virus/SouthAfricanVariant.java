/*
  Authors: Neriya Zudi (ID:207073545)
  Email:  neriyazudi@Gmail.com
  Department of Computer Engineering - Assignment 1 - Advanced Object-Oriented Programming
*/
package Virus;

import Population.Person;
import Population.Sick;
import Simulation.Clock;

import java.util.ArrayList;

public class SouthAfricanVariant implements IVirus {

    public SouthAfricanVariant() {
    }


    public static String getName() {
        return "SouthAfrican Variant";
    }

    //Interface Realization
    @Override
    public double contagionProbability(Person p) {
        if (p.getAge() >= 0 && p.getAge() <= 18)
            return 0.6 * p.contagionProbability();
        else
            return 0.5 * p.contagionProbability();
    }

    @Override
    public boolean tryToContagion(Person p1, Person p2) {
        Sick person = new Sick((Sick) p1);
        if ((Clock.now()-person.getContagiousTime())  < 5){
            System.out.println("time less then 5 " + (Clock.now()-person.getContagiousTime()));
            return false;
        }
        double probability = 0;//Probability of infection
        Random r = new Random();
        if (p2 instanceof Sick)//Unable to infect a sick person
            return false;

        double d = p1.getDistance(p2.getLocation());//Calculate distance between two people
        probability = p2.contagionProbability() * Math.min(1, 0.14 * Math.exp(2 - (0.25 * d)));//Calculate the probability of infection


        return probability >= r.getRandomNumber();//Returns true or false if contagion succeeds
    }

    @Override
    public boolean tryToKill(Sick s) {
        double probability = 0;
        double t = Clock.now() - s.getContagiousTime();
        Random r = new Random();
        if (s.getAge() >= 0 && s.getAge() <= 18)
            probability = Math.max(0, 0.05 - (0.01 * 0.05) * Math.pow((t - 15), 2));
        else
            probability = Math.max(0, 0.08 - (0.01 * 0.08) * Math.pow((t - 15), 2));

        return probability >= r.getRandomNumber();
    }

    @Override
    public String toString() {
        return "SouthAfrican Variant";
    }

    public boolean isEqual(IVirus virus) {
        return (this.toString().equals(virus.toString()));

    }
}
