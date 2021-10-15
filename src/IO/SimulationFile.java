/*
  Authors: Neriya Zudi (ID:207073545)
  Email:  neriyazudi@Gmail.com
  Department of Computer Engineering - Assignment 1 - Advanced Object-Oriented Programming
*/
package IO;


import Country.*;
import Location.Location;
import Location.Point;
import Location.Size;
import Population.Healthy;
import Population.Person;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationFile {

    //Data members
    private Map map;
    private SettlementFactory settlementFactory=new SettlementFactory();

    public SimulationFile(File file) throws Exception {

        //Create variables for reading from a file
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);


        //int settlementCounter = Integer.parseInt(br.readLine());//Quantity of settlements by first-line read in the file
        ArrayList<Settlement> array = new ArrayList<>();//Set up an array of settlements of the appropriate size
        String type, name = null;
        Settlement settlement = null;
        int x, y, width, height, PopulationQuantity, index;
        List<String> strings = new ArrayList<>();
        List<String> connector = new ArrayList<>();
        this.map=new Map();


        String splitText[] = null;//String for splitting words in a file
        String s = br.readLine();


        //Loop by the amount of settlements/lines in a file
        while (s != null) {

            List<Person> population = new ArrayList<>();
            splitText = s.split(";");//Remove the symbol ";"
            for (int j = 0; j < splitText.length; j++)
                splitText[j] = splitText[j].trim();//Remove spaces


            //A case where the row contains a settlement type
            if (!splitText[0].equals("#")) {
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


                //Create an instance by settlement type using Factory
                settlement = settlementFactory.getSettlement(type,name, location, population, capacity,map);

                //Define the initial population amount in each settlement
                index = 0;
                while (index < PopulationQuantity) {//Defining 99 percent of the population as healthy
                    int age = CalculateAge();//Calculation of population ages
                    Point randomLocation = settlement.randomLocation(point, size);//Create a random location
                    //Add to the list of people in the settlement as a healthy person
                    population.add(new Healthy(age, randomLocation, settlement));
                    index++;
                }
                settlement.setPopulation(population);
                array.add (settlement);//Insert the generated instance into the array
                s = br.readLine();//Move to the next line
            } else {
                //A case where the row contains connections between settlements
                strings.add(splitText[1]);//Adding a first settlement name to the array
                connector.add(splitText[2]);//Adding a second settlement name to the array
                s = br.readLine();//Move to the next line
            }
        }

        //loading connections between settlements
        array = loadConnection(strings, connector, array);
        this.map = new Map(array, array.size());

    }

    //getters
    public Map getMap() {
        return this.map;
    }

    // Mathod for calculating age by formula with Normal distribution
    public int CalculateAge() {
        double y = Math.random() * 4;
        Random x = new Random();
        double val;
        do {
            val = x.nextGaussian();

        } while (val <= -1 || val >= 1);


        return (int) ((5 * ((val * 6) + 9)) + y);

    }

    private ArrayList<Settlement> loadConnection(List<String> strings, List<String> connector, ArrayList<Settlement> array) {
        ArrayList<Settlement> updateArray = array;
        int connectLength = strings.size();
        int index = 0;
        while (index < updateArray.size()) {//Running on the entire set of settlements
            for (int i = 0; i < connectLength; i++) {//Running on the name sets of connected settlements
                //Creating objects according to the names of the settlements
                Settlement one = connect(strings.get(i), updateArray);
                Settlement two = connect(connector.get(i), updateArray);
                //Check whether the settlement in the index exists in the array of connections
                if (updateArray.get(index).getName().equals(one.getName())) {
                    //Adding the second settlement to the array of connections of the first settlement
                    updateArray.get(index).getConnectedSettlements().add(two);
                }
                if (updateArray.get(index).getName().equals(two.getName())) {
                    //Adding the first settlement to the array of connections of the second settlement
                    updateArray.get(index).getConnectedSettlements().add(one);
                }
            }
            index++;
        }
        return updateArray;
    }

    //A method that receives a settlement name and a settlement array and returns a settlement object from the array by name
    private Settlement connect(String name, ArrayList<Settlement> array) {
        for (Settlement settlement : array) {
            if (settlement.getName().equals(name))
                return settlement;

        }
        return null;//If there is no settlement by that name
    }

}
