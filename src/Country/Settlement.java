/*
  Authors: Neriya Zudi (ID:207073545)
  Email:  neriyazudi@Gmail.com
  Department of Computer Engineering - Assignment 1 - Advanced Object-Oriented Programming
*/
package Country;



import Location.Location;
import Location.Point;
import Location.Size;
import Population.*;
import Simulation.Clock;
import Virus.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


public abstract class Settlement implements Runnable {


    //Data members
    private final String name;
    private final Location location;
    protected RamzorColor ramzorColor;
    private Map map;
    private List<Person> population;
    private List<Healthy> healthy;
    private List<Sick> sicks;
    private List<Convalescent> convalescents;
    private List<Vaccinated> vaccinateds;
    private int capacity;
    private ArrayList<Settlement> connectedSettlements;
    AtomicLong vaccinationDoses=new AtomicLong();
    AtomicInteger percent=new AtomicInteger();
    AtomicInteger deadCounter=new AtomicInteger();


    public Settlement(String name, Location location, List<Person> population, int capacity,Map map) {
        this.name = name;
        this.location = new Location(location);
        this.population = population;
        this.sicks = new ArrayList<>();
        this.healthy = new ArrayList<>();
        this.vaccinateds = new ArrayList<>();
        this.convalescents = new ArrayList<>();
        this.capacity = capacity;
        this.connectedSettlements = new ArrayList<>();
        this.ramzorColor = RamzorColor.Green;
        this.map=map;
    }

    public abstract RamzorColor calculateRamzorGrade();//abstract method

    //run method Simulation running on settlement
    @Override
    public void run() {

        AtomicInteger i=new AtomicInteger();//Counter of simulations as an atomic variable to prevent access
        //As long as the run is not in "stop" mode
        while (! map.getIsStopped()) {

            synchronized (map){
                while (!map.getIsInAction()){//As long as no play mode is selected
                    try {//Standby monitor
                        map.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("simulation no. " + i.getAndIncrement());
            SamplingSettlement();
            RecoverySicks();
            SamplingTransfer();
            TryToVaccinate();
            TryToKillSicks();
            makeStatusArray();//Call a method to create arrays

            //Update a new ramzor color for each settlement after contagion
            calculateRamzorGrade();

            System.out.println(this);

            //Scheduling threads up to a certain point
            try {
                this.map.getCyclicBarrier().await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

        }

    }

    //************** Synchronized Methods for simulation  **************

    //Method for healthy infection attempts
    private synchronized void SamplingSettlement() {
        final int NumberOfAttempts = 3;//Number of infection attempts
        Random r = new Random();
        final double precent = 0.2;
        int numberOfPeople = population.size();
        int numberOfSicks = sicks.size();
        int j = 0;
        for (int i = 0; i < numberOfSicks * precent; i++) {
            int sickIndex = r.getRandomInRange(numberOfSicks - 1, 0);
            while (j < NumberOfAttempts) {
                Person randomNotSick = FindRandomNotSick();
                IVirus sourceVirus = sicks.get(sickIndex).getVirus();//Random variant gluing
                ////Obtaining a random variant to which the source virus develops
                IVirus randomMutation = VirusManager.randomContagion(sourceVirus);
                //System.out.println("source Virus "+sourceVirus.toString()+" random "+randomMutation.toString());
                //Attempt to paste the selected variant
                if (randomMutation != null) {
                    if (randomMutation.tryToContagion(sicks.get(sickIndex), randomNotSick)) {
                        changeHealtStatus(randomNotSick, sicks.get(sickIndex).getVirus());
                        //System.out.println("cotigan from:" + sicks.get(sickIndex).getVirus().toString() +
                                //" cotigan to: " + randomMutation.toString() + " seccsesful");
                    }
                    j++;
                }
            }
        }

    }

    //Method for recovery of patients after 25 days
    private synchronized void RecoverySicks() {
        for (int i = 0; i < population.size(); i++) {
            if (population.get(i) instanceof Sick) {
                int days = Clock.calculatePassedTime(((Sick) population.get(i)).getContagiousTime());
                if (days >= 25)//If 25 days have passed since the moment of infection
                    population.set(i, ((Sick) population.get(i)).recover());
            }

        }

    }

    //Method for attempting to transition between connected settlements
    private void SamplingTransfer() {
        Random r = new Random();
        double precent = 0.03;

        for (int i = 0; i < precent * population.size(); i++) {
            int numberOfPeople = population.size();
            int numberOfConnection = connectedSettlements.size();
            if (connectedSettlements.size() == 0)
                return;
            int peopleIndex = r.getRandomInRange(numberOfPeople - 1, 0);
            int connectIndex = r.getRandomInRange(numberOfConnection - 1, 0);

            //in case the transfer between the settlements succeed
            if (transferPerson(population.get(peopleIndex), connectedSettlements.get(connectIndex))) {

                //hashcode is generally a number generated from any object which allows objects to be stored or retrieved very quickly in a Hashtable
                int hashThis=System.identityHashCode(this);
                int hashConnected=System.identityHashCode(connectedSettlements.get(connectIndex));

                //Set up references for synchronization of 2 settlements
                Settlement firstSynch=this;
                Settlement secondSynch=connectedSettlements.get(connectIndex);

                //If the value from the hash table of the second sett is greater than the value of the first sett
                if(hashThis<hashConnected) {
                    firstSynch = connectedSettlements.get(connectIndex);
                    secondSynch = this;
                }

                //Double sync to prevent dead clock
                synchronized (firstSynch){
                        synchronized (secondSynch){
                            connectedSettlements.get(connectIndex).addPerson(population.get(peopleIndex));
                            population.remove(peopleIndex);

                        }
                }

            }
        }
    }

    //Method for vaccinating healthy people according to the number of vaccine doses
    private synchronized void TryToVaccinate() {
        while (this.vaccinationDoses.get() > 0) {//As long as there are vaccine doses
            for (int i = 0; i < population.size(); i++) {//Running on the whole population
                if (population.get(i) instanceof Healthy) {
                    population.set(i, ((Healthy) population.get(i)).vaccinate());
                    this.vaccinationDoses.decrementAndGet();
                    break;
                }
            }

        }
    }

    //Method for  Attempting to kill sicks from the population
    private synchronized void TryToKillSicks() {
        for (Sick sick : sicks) {//Go over all the sicks and end up killing
            if (sick.tryToDie()) {
                this.deadCounter.getAndIncrement();//Increasing atomic variable
                removePerson(sick);//Population update
            }
        }
        // Only in case a file is created by the user
        if(map.getLogFileWriter()!=null) {

            synchronized (map.getLogFileWriter()) {//Sync writing to file
                double per = (double)this.deadCounter.get()/population.size()*100.0;//Current death rate
               long temp = (long) per;// The current percentage of deaths as a Complete percentage

               if(temp > this.percent.get()){//If the percentage of deaths increases by another Complete percentage
                   this.percent.getAndIncrement();//Increasing the variable of a complete percentage of deaths
                   try {
                       map.getLogFileWriter().writeToFile(this);//Write to the file
                   } catch (IOException e) {
                       e.printStackTrace();
                   }

                }
            }
        }
    }

    @Override
    public synchronized String toString() {

        makeStatusArray();

        // Printing ramzor color
        RamzorColor color = getRamzorColor();
        String r_color = switch (color) {
            case Green -> Colors.GREEN + "Ramzor Color= " + color + Colors.RESET;
            case Yellow -> Colors.YELLOW + "Ramzor Color= " + color + Colors.RESET;
            case Orange -> Colors.RED_BRIGHT + "Ramzor Color= " + color + Colors.RESET;
            default -> Colors.RED + "Ramzor Color= " + color + Colors.RESET;
        };
        return Colors.BLUE_BOLD + "Name : " + getName() + Colors.RESET + "\nThe location on Map is : " + getLocation().toString()
                + "\npopulation: " + getPopulation().size() + " ~~~~~ " + r_color
                + " ~~~~~ Number of sicks: " + calcSick() + "\n ~~~~~ Number of vacc : " + vaccinateds.size()
                + " ~~~~~ Number of dead : " + this.getDeadCounter() + "\n ~~~~~ Number of healthy: " + healthy.size();


    }


    //************** setters **************

    public boolean addPerson(Person p) {
        this.population.add(p);
        return true;
    }

    public void removePerson(Person p) {
        for (int i = 0; i < population.size(); i++)
            if (population.get(i).equals(p)) {
                population.remove(i);
            }
    }

    public  boolean transferPerson(Person p, Settlement s) {

        int hashThis=System.identityHashCode(this);
        int hashOther=System.identityHashCode(s);
        double passCheck=0;

        if(hashThis>hashOther){
            synchronized (this){
                synchronized (s){
                    //Multiply probabilities for transition between localities
                    passCheck = p.getSettlement().getRamzorColor().getPassProbability() * s.getRamzorColor().getPassProbability();
                }
            }
        }
        else {
            synchronized (s){
                synchronized (this){
                    //Multiply probabilities for transition between localities
                     passCheck = p.getSettlement().getRamzorColor().getPassProbability() * s.getRamzorColor().getPassProbability();
                }
            }
        }


        Random r = new Random();
        //The settlement reached its quota
        if (s.getPopulation().size() == s.getCapacity())
            return false;
        //If the probability of transition was successful
        return (passCheck > r.getRandomNumber());

    }

    public synchronized void setVaccinationDoses(long doses){
        this.vaccinationDoses.getAndAdd(doses);
    }

    public void setMap(Map map) {
        this.map = map;
    }



    //************** getters **************

    public String getName() {
        return this.name;
    }

    public Location getLocation() {
        return this.location;
    }

    public List<Person> getPopulation() {
        return this.population;
    }

    public void setPopulation(List<Person> population) {
        this.population = population;
    }

    public RamzorColor getRamzorColor() {
        return this.ramzorColor;
    }

    public Color getColor() {
        RamzorColor color = getRamzorColor();
        return switch (color) {
            case Green -> Color.green;
            case Yellow -> Color.yellow;
            case Orange -> Color.orange;
            case Red -> Color.red;
        };
    }

    public List<Healthy> getHealthy() {
        return this.healthy;
    }

    public ArrayList<Settlement> getConnectedSettlements() {
        return connectedSettlements;
    }

    private int getCapacity() {
        return this.capacity;
    }

    public String getType() {
        if (this instanceof City)
            return "City";
        else if (this instanceof Kibbutz)
            return "Kibbutz";
        else
            return "Moshav";

    }

    public long getVaccinationDoses() {
        return this.vaccinationDoses.get();
    }

    public int getDeadCounter() {
        return this.deadCounter.get();
    }

    public Map getMap() {
        return map;
    }

    public int getPercent(){return this.percent.get();}


    //************** Auxiliary Methods **************

    //Method for calculating the number of sicks
    public int calcSick() {
        int sickCounter = 0;
        for (Person person : population)
            if (person instanceof Sick)
                sickCounter++;

        return sickCounter;
    }

    //Method for calculating the number of vaccinated
    public int calcVaccinated() {
        int vacCounter = 0;
        for (Person person : population) {
            if (person instanceof Vaccinated)
                vacCounter++;
        }
        return vacCounter;
    }

    // //Method for calculating sick precent with string
    public String sickPrecentString() {
        double sick = 100 * contagiousPercent();
        String sickString = String.format("%.2f", sick);

        return sickString + "%";
    }


    //Replacing a healthy person with a sick person with a particular virus
    public void changeHealtStatus(Person p, IVirus v) {

        for (int i = 0; i < population.size(); i++) {
            if (population.get(i).equals(p)) {
                Person newsick = population.get(i).contagion(v);
                population.set(i, newsick);
            }
        }
    }

    //Creating arrays for sicks and healthy people
    public void makeStatusArray() {
        this.sicks.clear();
        this.healthy.clear();
        this.convalescents.clear();
        this.vaccinateds.clear();

        for (int i = 0; i < population.size(); i++)//Going through the entire population
        {
            if (population.get(i) instanceof Sick)
                sicks.add((Sick) population.get(i));//Add to an array of sick
            else if (population.get(i) instanceof Healthy)
                healthy.add((Healthy) population.get(i));//Add to an array of healthy people
            else if (population.get(i) instanceof Convalescent)
                convalescents.add((Convalescent) population.get(i));
            else if (population.get(i) instanceof Vaccinated)
                vaccinateds.add((Vaccinated) population.get(i));
        }
    }

    //Method for infecting 0.1 percent of the population
    public void InitContagion() {

        final double precent = 0.001;
        //final double precent = 0.1;
        int contagionIdex = 0, randomVirus;
        IVirus variant;
        Random r = new Random();
        this.makeStatusArray();

        synchronized (this) {
            for (int i = 0; i < this.getPopulation().size() * precent; i++) {
                if(sicks.size()== population.size()){
                    System.out.println("Everyone is already sick");
                    return;
                }
                randomVirus = r.getRandomInRange(3, 1);
                switch (randomVirus) {
                    case 1 -> {//ChineseVariant
                        variant = VirusManager.randomContagion(new ChineseVariant());
                        if(variant!=null) {
                            //System.out.println("from chinese paste " + variant.toString());
                            //Changing a healthy person to sick in the population array
                            this.changeHealtStatus(this.getHealthy().get(i), variant);
                        }

                    }
                    case 2 -> {//BritishVariant
                        variant = VirusManager.randomContagion(new BritishVariant());
                       if(variant!=null) {
                           //System.out.println("from british paste " + variant.toString());
                           //Changing a healthy person to sick in the population array
                           this.changeHealtStatus(this.getHealthy().get(i), variant);
                       }

                    }
                    default -> {//SouthAfricanVariant
                        variant = VirusManager.randomContagion(new SouthAfricanVariant());
                        if(variant!=null) {
                            //System.out.println("from south paste " + variant.toString());
                            //Changing a healthy person to sick in the population array
                            this.changeHealtStatus(this.getHealthy().get(i), variant);
                        }
                    }
                }

            }
            this.makeStatusArray();
            this.calculateRamzorGrade();
        }
    }

    //A method for finding a random non-sick person
    private Person FindRandomNotSick() {
        Random r = new Random();
        for (int i = 0; i < population.size(); i++) {
            int randomIndex = r.getRandomInRange(population.size() - 1, 0);
            if (!(population.get(randomIndex) instanceof Sick))
                return population.get(randomIndex);
            else
                continue;
        }
        return null;
    }

    //Calculate the percentage of contagious in the population
    public synchronized double contagiousPercent() {
        int counter = 0;
        for (Person person : population)
            if (person instanceof Sick)
                counter++;
        return (double) counter / population.size();
    }

    //Returns a random point by top left point
    public Point randomLocation(Point p, Size s) {
        int xRandom, yRandom;
        Random r = new Random();
        xRandom = ((int) (r.getRandomNumber() * s.getWidth())) + p.getX();
        yRandom = ((int) (r.getRandomNumber() * s.getHeight())) + p.getY();
        return new Point(xRandom, yRandom);
    }

}
