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

public class Vaccinated extends Person{

    public Vaccinated(int age, Point location, Settlement settlement,long vaccinationTime)
    {
        super(age,location,settlement);
        this.vaccinationTime=vaccinationTime;
    }
    public long VaccinatedTime()
    {
     return this.getVaccinationTime()- Clock.now();
    }
    public long getVaccinationTime() {
        return vaccinationTime;
    }

    @Override
    public double contagionProbability()
    {
        //Calculate a chance of being infected depending on time
        long time=this.VaccinatedTime();
        if(time < 21 && time >= 0)
            return Math.min(1.0,0.56+0.15*Math.sqrt(21-time));
        else if (time >= 21)
            return Math.max(0.05,1.05/time-14);
        else
            throw new IllegalArgumentException("negative time");

    }

    @Override
    public Person contagion(IVirus v) {

        int age = getAge();
        Point location = new Point(getLocation());
        Settlement settlement=getSettlement();
        Sick s= new Sick(age,location,settlement,vaccinationTime,v);
        return s;
    }

    @Override
    public String toString() {
        return "Vaccinated{" +
                "vaccinationTime=" + getVaccinationTime() +
                '}'+super.toString();
    }

    //Data members
    private long vaccinationTime;
}
