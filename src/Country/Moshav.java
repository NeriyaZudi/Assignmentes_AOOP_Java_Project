/*
  Authors: Neriya Zudi (ID:207073545)
  Email:  neriyazudi@Gmail.com
  Department of Computer Engineering - Assignment 1 - Advanced Object-Oriented Programming
*/
package Country;

import Location.Location;
import Population.Person;
import java.util.List;

public class Moshav extends Settlement{

    public Moshav(String name, Location location, List<Person> population,int capacity)
    {
        super(name,location,population,capacity);
    }

    @Override
    public RamzorColor calculateRamzorGrade()
    {
        this.P =contagiousPercent();
        this.C = calculateVirusColorRateByType();
        this.ramzorColor=RamzorColor.CalculateColor(C);
        return this.ramzorColor;
    }

    @Override
    public String toString() {
        return "## Moshav  ## \n" + super.toString();
    }
    protected double calculateVirusColorRateByType() {
        return 0.3+ 3*(Math.pow((Math.pow(1.2,C)*(P-0.35)),5));
    }
    private double P;
    private double C;
}
