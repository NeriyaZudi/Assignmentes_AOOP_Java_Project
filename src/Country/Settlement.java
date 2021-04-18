/*
  Authors: Neriya Zudi (ID:207073545)
  Email:  neriyazudi@Gmail.com
  Department of Computer Engineering - Assignment 1 - Advanced Object-Oriented Programming
*/
package Country;

import Location.*;
import Population.*;
import Virus.IVirus;
import Virus.Random;
import java.util.ArrayList;
import java.util.List;



public abstract class Settlement {

    public Settlement(String name,Location location,List <Person> population,RamzorColor ramzorColor)
    {
        this.name=new String(name);
        this.location=new Location(location);
        this.population=population;
        this.ramzorColor=ramzorColor;
        this.ramzorGrade=0;
        this.sicks=new ArrayList<>();
        this.healthy=new ArrayList<>();
    }
    public Settlement(Settlement other)
    {
        this.name=new String(other.getName());
        this.location=new Location(other.getLocation());
        this.population=other.population;
        this.ramzorColor=other.getRamzorColor();
        this.ramzorGrade=other.getRamzorGrade();

    }

    public abstract RamzorColor calculateRamzorGrade();//abstract method
    @Override
    public String toString() {

        // Printing ramzor color
        RamzorColor color=getRamzorColor();
        String r_color = switch (color) {
            case Green -> Colors.GREEN + "Ramzor Color= " + color+Colors.RESET ;
            case Yellow -> Colors.YELLOW + "Ramzor Color= " + color+Colors.RESET ;
            case Orange -> Colors.RED_BRIGHT + "Ramzor Color= " + color +Colors.RESET;
            default -> Colors.RED + "Ramzor Color= " + color+Colors.RESET ;
        };
        return Colors.BLUE_BOLD+"Name : "+getName()+Colors.RESET+"\nThe location on Map is : " + getLocation().toString()
                +"\npopulation: "+getPopulation().size()+ " ~~~~~ "+r_color
                +" ~~~~~ Number of sicks: "+calcSick();//+" ramzor garde = "+getRamzorGrade();

    }

    //Calculate the percentage of contagious in the population
    public double contagiousPercent()
    {
       int counter=0;
        for (Person person : population)
            if (person instanceof Sick)
                counter++;
        return (double) counter/population.size();
    }
    //Returns a random point by top left point
    public Point randomLocation(Point p,Size s){
        int xRandom,yRandom;
        Random r=new Random();
        xRandom = ((int)(r.getRandomNumber()*s.getWidth()))+p.getX();
        yRandom = ((int)(r.getRandomNumber()*s.getHeight()))+p.getY();
        return new Point(xRandom,yRandom);
    }
    public boolean addPerson(Person p){
        this.population.add(p);
        return true;
    }
    public boolean transferPerson(Person p, Settlement s){
        return true;
    }

    //getters
    public String getName()
    {
        return this.name;
    }
    public Location getLocation()
    {
        return this.location;
    }
    public List<Person> getPopulation()
    {
            return this.population;
    }
    public RamzorColor getRamzorColor()
    {
        return this.ramzorColor;
    }
    public double getRamzorGrade() {
        return ramzorGrade;
    }

    //setters
    public boolean setPopulation(List<Person> other)
    {
        if(other==null)
            return false;
        this.population=other;
        return true;
    }
    public boolean setRamzorColor(RamzorColor newColor)
    {
        this.ramzorColor=newColor;
        return true;
    }
    public boolean setRamzorGrade(double newGrade) {
        this.ramzorGrade=newGrade;
        return true;

    }

    //Method for calculating the number of sicks
    public int calcSick()
    {
        int sickCounter=0;
        for (Person person : population)
            if (person instanceof Sick)
                sickCounter++;

        return sickCounter;
    }

    //Replacing a healthy person with a sick person with a particular virus
    public void changeHealtStatus(Person p, IVirus v)
    {
        for(int i=0;i<population.size();i++) {
            if(population.get(i).equals(p)) {
                Person newsick = population.get(i).contagion(v);
                population.set(i,newsick);
            }
        }
    }
    //Creating arrays for sicks and healthy people
    public void makeArray()
    {
        this.sicks.clear();
        this.healthy.clear();
        for(int i=0;i<population.size();i++)//Going through the entire population
        {
            if(population.get(i) instanceof Sick)
                sicks.add((Sick) population.get(i));//Add to an array of sick
            else if(population.get(i) instanceof Healthy)
                healthy.add((Healthy) population.get(i));//Add to an array of healthy people
            else
                continue;
        }
    }
    //Simulation Run
    public void simulateSettlement()
    {
        makeArray();//Call a method to create arrays
        int numberOfSick=sicks.size();
        final int NumberOfAttempts=6;//Number of infection attempts
        int j,randomPerson,numberOfHealthy;
        Random r=new Random();

        for (Sick sick : sicks)//Going through every sick
        {
            j = 0;
            while (j < NumberOfAttempts) {//Attempts to Contagion
                numberOfHealthy = healthy.size();//Number of healthy people
                //Select a random index in the range up to the healthy amount and try to Contagion
                randomPerson = r.getRandomInRange((numberOfHealthy - 1), 0);

                if(healthy.size()==0)//if all the population is sick
                    return;

                if (sick.getVirus().tryToContagion(sick, healthy.get(randomPerson))) {
                    //If the infection is successful
                    changeHealtStatus(healthy.get(randomPerson), sick.getVirus());
                    //Erasing a sick person from the healthy system
                    healthy.remove(randomPerson);
                }
                j++;
            }
        }
        //Update a new ramzor color for each settlement after contagion
        setRamzorColor(calculateRamzorGrade());
    }

    //Data members
    private String name;
    private final Location location;
    private List<Person> population;
    private RamzorColor ramzorColor;
    private double ramzorGrade;
    private List<Healthy> healthy;
    private List<Sick> sicks;



}
