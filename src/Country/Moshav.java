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

    public Moshav(String name, Location location, List<Person> population, RamzorColor ramzorColor)
    {
        super(name,location,population,ramzorColor);
    }

    @Override
    public RamzorColor calculateRamzorGrade() {
        double c = getRamzorGrade();//Calculate contagious percentage
        double p = contagiousPercent();//Calculate a new ramzor grade
        double new_c=0.3+3*(Math.pow((Math.pow(1.2,c)*(p-0.35)),5));
        this.setRamzorGrade(new_c);
        //Returns a new ramzor color
        if(new_c <= 0.4)
            return RamzorColor.Green;
        else if(new_c <= 0.6)
            return RamzorColor.Yellow;
        else if(new_c <= 0.8)
            return RamzorColor.Orange;
        else
            return RamzorColor.Red;
    }

    @Override
    public String toString() {
        return "## Moshav Settlement ## " + super.toString();
    }
}
