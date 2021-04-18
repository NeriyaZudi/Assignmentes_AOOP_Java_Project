/*
  Authors: Neriya Zudi (ID:207073545)
  Email:  neriyazudi@Gmail.com
  Department of Computer Engineering - Assignment 1 - Advanced Object-Oriented Programming
*/
package IO;


import Country.*;
import Location.*;
import Population.Healthy;
import Population.Person;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationFile  {

    public SimulationFile(File file) throws Exception {

        //Create variables for reading from a file
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        int settlementCounter = Integer.parseInt(br.readLine());//Quantity of settlements by first-line read in the file
        Settlement[] array = new Settlement[settlementCounter];//Set up an array of settlements of the appropriate size
        String type, name = null;
        Settlement settlement = null;
        int x, y, width, height, PopulationQuantity,i=0,index;


        String splitText[] = null;//String for splitting words in a file
        String s = br.readLine();//Read first line

        //Loop by the amount of settlements/lines in a file
        while (s != null || i < settlementCounter) {

            List<Person> population = new ArrayList<>();
            splitText = s.split(";");//Remove the symbol ";"
            for (int j = 0; j < splitText.length; j++)
                        splitText[j] = splitText[j].trim();//Remove spaces

            //Placement for appropriate variables in order
            type = splitText[0];
            name = splitText[1];
            x = Integer.parseInt(splitText[2]);
            y = Integer.parseInt(splitText[3]);
            width = Integer.parseInt(splitText[4]);
            height = Integer.parseInt(splitText[5]);
            PopulationQuantity = Integer.parseInt(splitText[6]);

            //Create a location from the received values
            Point point = new Point(x, y);
            Size size = new Size(width, height);
            Location location = new Location(point, size);

            //Create an instance by settlement type
            settlement = switch (type) {
                case "Moshav" -> new Moshav(name, location, population, RamzorColor.Green);
                case "Kibbutz" -> new Kibbutz(name, location, population, RamzorColor.Green);
                case "City" -> new City(name, location, population, RamzorColor.Green);
                default -> throw new Exception("Error, Invalid Type");
            };


            //Define the initial population amount in each settlement
            index = 0;
            while (index < PopulationQuantity) {
                int age = CalculateAge();//Calculation of population ages
                Point randomLocation = settlement.randomLocation(point, size);//Create a random location
                //Add to the list of people in the settlement as a healthy person
                population.add(new Healthy(age,randomLocation,settlement));
                index++;
            }
            settlement.setPopulation(population);

                array[i] = settlement;//Insert the generated instance into the array
                i++;
                s = br.readLine();//Move to the next line

        }
        this.map=new Map(array,settlementCounter);
        this.length=settlementCounter;


    }

    //getters
    public Map getMap() {
        return this.map;
    }
    public int getLength() {
        return length;
    }

    // Mathod for calculating age by formula with Normal distribution
    public int CalculateAge()
    {
        double y =  Math.random()*4;
        Random x = new Random();
        double val;
        do {
            val = x.nextGaussian();

        } while (val <= -1 || val>=1);


        return (int)((5*((val*6)+9))+y);

    }

    //Data members
        private Map map;
        private int length;
}
