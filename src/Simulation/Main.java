/*
  Authors: Neriya Zudi (ID:207073545)
  Email:  neriyazudi@Gmail.com
  Department of Computer Engineering - Assignment 1 - Advanced Object-Oriented Programming
*/
package Simulation;

import java.awt.*;
import java.io.*;
import Country.Colors;
import Country.Map;
import Country.RamzorColor;
import IO.SimulationFile;



public class Main {
    public static void main(String args[]) {

        final int simulationsNumber = 5;

        File file = null;
        try {
            file = loadFileFunc();
            if (file == null)
                throw new FileNotFoundException("file not found");
            SimulationFile s = new SimulationFile(file);//Read data from the file
            Map map = s.getMap();//Create a map
            printMap(map);//Print a map before initialization
            /*map.InitContagion();//Infecting 1 percent of the population
            printMap(map);////Print a map after initialization
            //Running simulations
            for (int i = 0; i < simulationsNumber; i++) {
                System.out.println("~~~~~~~~~~~~~ Simulation " + (i + 1) + " ~~~~~~~~~~~~~");
                map.SimulateMap();
                printMap(map);//Print an updated map
            }*/
            printNumberOfSicks(map);//Print multiple patients per settlement
            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
        } /*finally {
            if (file != null)
                file.close();
        }*/

    }
    //print map
    private static void printMap(Map map) {
        for(int i=0;i<map.getLength();i++)
            System.out.println(map.getSettlement()[i].toString()+"\n");
    }
    //Load a file from an external source
    private static File loadFileFunc() {
        FileDialog fd = new FileDialog((Frame) null, "Please choose a file:", FileDialog.LOAD);
        fd.setVisible(true);

        if (fd.getFile() == null)
            return null;
        File f = new File(fd.getDirectory(), fd.getFile());
        System.out.println(f.getPath());
        return f;
    }

    //Print multiple patients per settlement
    private static void printNumberOfSicks(Map map)
    {
        RamzorColor color;
        for(int i=0;i<map.getLength();i++) {
            color = map.getSettlement()[i].getRamzorColor();
            switch (color) {
                case Green -> System.out.println(Colors.GREEN + map.getSettlement()[i].getName() + "- Number of confirmed sicks: " + map.getSettlement()[i].calcSick() + Colors.RESET);
                case Yellow -> System.out.println(Colors.YELLOW + map.getSettlement()[i].getName() + "- Number of confirmed sicks: " + map.getSettlement()[i].calcSick() + Colors.RESET);
                case Orange -> System.out.println(Colors.RED_BRIGHT + map.getSettlement()[i].getName() + "- Number of confirmed sicks: " + map.getSettlement()[i].calcSick() + Colors.RESET);
                default -> System.out.println(Colors.RED + map.getSettlement()[i].getName() + "- Number of confirmed sicks: " + map.getSettlement()[i].calcSick() + Colors.RESET);
            }
        }

    }

}

