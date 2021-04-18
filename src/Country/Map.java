/*
  Authors: Neriya Zudi (ID:207073545)
  Email:  neriyazudi@Gmail.com
  Department of Computer Engineering - Assignment 1 - Advanced Object-Oriented Programming
*/
package Country;


import Population.*;
import Simulation.Clock;
import Virus.*;

public class Map {

    public Map(Settlement settlement[] , int length) {

        this.settlement = settlement;
        this.length = length;
    }


    public Settlement[] getSettlement() {
        return settlement;
    }
    public int getLength() {
        return length;
    }

    //Initialization - Pasting a percentage of the population in each settlement
    public void InitContagion()
    {
        final double precent = 0.01;
        int contagionIdex=0,population,randomVirus;
        IVirus variant;
        Random r=new Random();

        for(int i=0;i<getLength();i++) {//Running on all the settlements on the map
            population = settlement[i].getPopulation().size();//Population size of each settlement
            contagionIdex=0;

            while (contagionIdex < (population * precent)) {//Running up to the size of a percentage of the population
                //Select a random number for selecting a variant type to paste
                randomVirus = r.getRandomInRange(3, 1);
                switch (randomVirus) {
                    case 1 -> {//ChineseVariant
                        variant = new ChineseVariant();
                        //Returns a copy of a person's data while becoming sick
                        Person changeSick = this.settlement[i].getPopulation().get(contagionIdex).contagion(variant);
                        //Changing a healthy person to sick in the population array
                        this.settlement[i].getPopulation().set(contagionIdex, changeSick);
                    }
                    case 2 -> {//BritishVariant
                        variant = new BritishVariant();
                        //Returns a copy of a person's data while becoming sick
                        Person changeSick = this.settlement[i].getPopulation().get(contagionIdex).contagion(variant);
                        //Changing a healthy person to sick in the population array
                        this.settlement[i].getPopulation().set(contagionIdex, changeSick);
                    }
                    default -> {//SouthAfricanVariant
                        variant = new SouthAfricanVariant();
                        //Returns a copy of a person's data while becoming sick
                        Person changeSick = this.settlement[i].getPopulation().get(contagionIdex).contagion(variant);
                        //Changing a healthy person to sick in the population array
                        this.settlement[i].getPopulation().set(contagionIdex, changeSick);
                    }
                }
                contagionIdex++;
            }
            //Update a new ramzor color for each settlement after contagion
            settlement[i].setRamzorColor(settlement[i].calculateRamzorGrade());
        }
    }

    public void SimulateMap()
    {
        //Map simulation
        for (int i=0;i<length;i++)
            settlement[i].simulateSettlement();


        Clock.nextTick();//Promoting a clock tick
    }

    //Data members
    private final Settlement[] settlement;
    private int length;//Number of settlements on the map


}
