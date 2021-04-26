/*
  Authors: Neriya Zudi (ID:207073545)
  Email:  neriyazudi@Gmail.com
  Department of Computer Engineering - Assignment 1 - Advanced Object-Oriented Programming
*/
package Country;

import Location.Location;
import Population.Person;
import java.util.List;

public class Kibbutz extends Settlement{
    public Kibbutz(String name, Location location, List<Person> population,int capacity)
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

    protected double calculateVirusColorRateByType() {
        return 0.45+(Math.pow(Math.pow(1.5,C)*(P-0.4),3));
    }
    @Override
    public String toString() {
        return "## Kibbutz ## \n" + super.toString();
    }

    private double P;
    private double C;
}
