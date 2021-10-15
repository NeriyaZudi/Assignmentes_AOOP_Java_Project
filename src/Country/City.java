/*
  Authors: Neriya Zudi (ID:207073545)
  Email:  neriyazudi@Gmail.com
  Department of Computer Engineering - Assignment 1 - Advanced Object-Oriented Programming
*/
package Country;


import Location.Location;
import Population.Person;

import java.util.List;

public class City extends Settlement {


    private double P;
    private double C;


    public City(String name, Location location, List<Person> population, int capacity,Map map) {
        super(name, location, population, capacity,map);
    }

    @Override

    public RamzorColor calculateRamzorGrade() {
        this.P = contagiousPercent();
        this.C = calculateVirusColorRateByType();
        this.ramzorColor = RamzorColor.CalculateColor(C);
        return this.ramzorColor;
    }

    protected double calculateVirusColorRateByType() {
        return 0.2 * (Math.pow(4, 1.25 * P));
    }

    @Override
    public String toString() {
        return "## City Settlement ## " + super.toString();
    }
}
