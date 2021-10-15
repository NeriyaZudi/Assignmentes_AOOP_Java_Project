/*
  Authors: Neriya Zudi (ID:207073545)
  Email:  neriyazudi@Gmail.com
  Department of Computer Engineering - Assignment 1 - Advanced Object-Oriented Programming
*/
package Population;

import Country.Settlement;
import Location.Point;
import Virus.IVirus;

import java.util.Objects;

public abstract class Person {

    //Data members
    private int age;
    private Point location;
    private Settlement settlement;

    //ctors
    public Person(int age, Point location, Settlement settlement) {
        this.age = age;
        this.location = new Point(location);
        this.settlement = settlement;

    }

    public Person() {
    }

    //abstract methods
    public abstract double contagionProbability();

    public abstract Person contagion(IVirus v);

    @Override
    public String toString() {
        return "Person : " +
                "age=" + getAge() +
                ", location=" + getLocation().toString() +
                ", settlement=" + getSettlement().toString()
                ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return getAge() == person.getAge() && Objects.equals(getLocation(), person.getLocation()) && Objects.equals(getSettlement(), person.getSettlement());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAge(), getLocation(), getSettlement());
    }

    //getters
    public int getAge() {
        return age;
    }

    public Point getLocation() {
        return location;
    }

    //setters
    public void setLocation(Point location) {
        this.location = location;
    }

    public double getDistance(Point point) {
        return Math.sqrt(Math.pow((getLocation().getX() - point.getX()), 2) + (Math.pow((getLocation().getY() - point.getY()), 2)));
    }

    public Settlement getSettlement() {
        return settlement;
    }


}
