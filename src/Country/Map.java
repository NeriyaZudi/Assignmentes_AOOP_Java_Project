/*
  Authors: Neriya Zudi (ID:207073545)
  Email:  neriyazudi@Gmail.com
  Department of Computer Engineering - Assignment 1 - Advanced Object-Oriented Programming
*/
package Country;


import IO.LogFile;
import UI.LineDecorator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class Map implements Iterable<Settlement>{

    //Data members
    private ArrayList<Settlement> settlement;
    private final int length;//Number of settlements on the map
    private ArrayList<LineDecorator> decorators=new ArrayList();
    private AtomicBoolean isInAction = new AtomicBoolean();//boolean variable to check if the simulation is in operation
    private AtomicBoolean isStopped = new AtomicBoolean();//boolean variable to check if the simulation is in stop
    private AtomicBoolean isMapLoaded = new AtomicBoolean();//boolean variable to check if has a map been loaded
    private CyclicBarrier cyclicBarrier;
    private  static LogFile logFileWriter=null;


    public Map(){
        settlement=new ArrayList<>();
        length=0;
        this.isInAction.set(false);
        this.isMapLoaded.set(false);
    }

    public Map(ArrayList <Settlement> sett, int len) {

        settlement=sett;
        length = len;
        this.isInAction.set(false);
        this.isMapLoaded.set(false);
    }

    //************** getters **************

    public ArrayList<Settlement>  getSettlement() {
        return settlement;
    }

    public int getLength() {
        return length;
    }

    public Boolean getIsInAction(){
        return isInAction.get();
    }

    public Boolean getIsStopped(){
        return isStopped.get();
    }

    public Boolean getIsMapLoaded(){
        return isMapLoaded.get();
    }

    public CyclicBarrier getCyclicBarrier(){return cyclicBarrier;}

    public static LogFile getLogFileWriter() {
        return logFileWriter;
    }

    public ArrayList<LineDecorator> getDecorators() {
        return decorators;
    }

    //************** setters **************

    public void setSettlement(Settlement[] s) {
        settlement = new ArrayList<>();
        Collections.addAll(settlement, s);
    }

    public void setIsInAction(Boolean tf){
        isInAction.set(tf);
    }

    public void setIsStopped(Boolean tf) {
        isStopped.set(tf);
    }

    public void setIsMapLoaded(Boolean tf) {
        isMapLoaded.set(tf);
    }

    public void setCyclicBarrier(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    public static void setLogFile(LogFile log) {
        logFileWriter=log;
    }

    //Initialization - Pasting a percentage of the population in each settlement
    public void InitContagion() {
        for (int i = 0; i < length; i++)
            settlement.get(i).InitContagion();
    }

    public Settlement at(int rowIndex) {
        return settlement.get(rowIndex);
    }

    //print map
    public void printMap() {
        for (int i = 0; i < length; i++)
            System.out.println(settlement.get(i).toString() + "\n");
    }

    //Creation and execution of threads
    public void executeThreads(){

        //Create threads by the amount of settlements on the map
        Thread[] settlementThreads = new Thread[getLength()];
        for (int i = 0; i < settlementThreads.length; i++) {
            settlementThreads[i] = new Thread(getSettlement().get(i));
        }

        for (Thread settlementThread : settlementThreads) {
            settlementThread.start();
        }

    }

    //Map update for all settlements And updating sicks
    public void setupMap(){
        for (Settlement s : settlement) {
            s.setMap(this);
            //Update 1 percent of the population is blue
            for (int i = 0; i < 10; i++)
                    s.InitContagion();
        }
    }

    //Creating decorators for all the connections between settlements
    public void setupDecorators(){
        int counter;
        for (Settlement s:settlement){
            counter=0;
            while (counter<s.getConnectedSettlements().size()) {
                Settlement t = s.getConnectedSettlements().get(counter);
                if(!isExistDecorator(s,t))//If there is a decorator for the specific connection
                    decorators.add(new LineDecorator(s,t));
                counter++;
            }
        }
    }

    //Check if there is a decorator for the connection to avoid duplications
    public boolean isExistDecorator(Settlement o,Settlement t){
        for(LineDecorator d:decorators){
            if(d.getSource().getName().equals(t.getName()) && d.getTarget().getName().equals(o.getName()))
                return true;
        }
        return false;
    }

    @Override
    public Iterator<Settlement> iterator() {
       return settlement.iterator();
    }
}
