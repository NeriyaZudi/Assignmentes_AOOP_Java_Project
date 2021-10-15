/*
  Authors: Neriya Zudi (ID:207073545)
  Email:  neriyazudi@Gmail.com
  Department of Computer Engineering - Assignment 1 - Advanced Object-Oriented Programming
*/
package Virus;

import Population.Person;
import Population.Sick;
//from Population import Person;

public interface IVirus {
    double contagionProbability(Person p);

    boolean tryToContagion(Person p1, Person p2);

    boolean tryToKill(Sick p);

    boolean isEqual(IVirus virus);
}

