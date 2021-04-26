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
        List <String> strings=new ArrayList<>();
        List <String> connector=new ArrayList<>();


        String splitText[] = null;//String for splitting words in a file
        String s = br.readLine();//Read first line

        //Loop by the amount of lines in a file
        while (s != null && !s.equals("")) {

            List<Person> population = new ArrayList<>();
            splitText = s.split(";");//Remove the symbol ";"
            for (int j = 0; j < splitText.length; j++)
                        splitText[j] = splitText[j].trim();//Remove spaces

            //A case where the row contains a settlement type
            if(!splitText[0].equals("#")){
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
                final double maxPeople = 1.3;
                int capacity = (int) (PopulationQuantity * maxPeople);


                //Create an instance by settlement type
                settlement = switch (type) {
                    case "Moshav" -> new Moshav(name, location, population, capacity);
                    case "Kibbutz" -> new Kibbutz(name, location, population, capacity);
                    case "City" -> new City(name, location, population, capacity);
                    default -> throw new Exception("Error, Invalid Type");
                };

                //Define the initial population amount in each settlement
                index = 0;
                while (index < PopulationQuantity) {
                    int age = CalculateAge();//Calculation of population ages
                    Point randomLocation = settlement.randomLocation(point, size);//Create a random location
                    //Add to the list of people in the settlement as a healthy person
                    population.add(new Healthy(age, randomLocation, settlement));
                    index++;
                }

                settlement.setPopulation(population);

                array[i] = settlement;//Insert the generated instance into the array
                i++;
                s = br.readLine();//Move to the next line
            }
            else {//A case where the row contains connections between settlements
                strings.add(splitText[1]);//Adding a first settlement name to the array
                connector.add(splitText[2]);//Adding a second settlement name to the array
                s = br.readLine();//Move to the next line
            }

        }
        //Loading connections
        array=loadConnection(strings,connector,array);

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
    //A method that receives a settlement name and a settlement array and returns a settlement object from the array by name
    public Settlement connect(String name,Settlement[] array)
    {
        for(int i=0;i<array.length;i++)
        {
            if(array[i].getName().equals(name))
                return array[i];

        }
        return null;//If there is no settlement by that name
    }
    //Mathod for loading connections between settlements
    public Settlement[] loadConnection(List<String> strings,List<String> connector,Settlement[] array ) {
        Settlement[] updateArray = array;
        int connectLength = strings.size();
        int index = 0;
        while (index < updateArray.length) {//Running on the entire set of settlements
            for (int i = 0; i < connectLength; i++) {//Running on the name sets of connected settlements
                //Creating objects according to the names of the settlements
                Settlement one = connect(strings.get(i), updateArray);
                Settlement two = connect(connector.get(i), updateArray);
                //Check whether the settlement in the index exists in the array of connections
                if (updateArray[index].getName().equals(one.getName())) {
                    //Adding the second settlement to the array of connections of the first settlement
                    updateArray[index].getConnectedSettlements().add(two);
                }
                if (updateArray[index].getName().equals(two.getName())) {
                    //Adding the first settlement to the array of connections of the second settlement
                    updateArray[index].getConnectedSettlements().add(one);
                }
            }
            index++;
        }
        return updateArray;
    }
    //Data members
        private Map map;
        private int length;
}
