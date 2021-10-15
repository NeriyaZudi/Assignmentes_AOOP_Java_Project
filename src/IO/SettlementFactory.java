package IO;

import Country.*;
import Location.Location;
import Population.Person;

import java.util.List;

public class SettlementFactory {
    // Returns a suitable object on demand
    public Settlement getSettlement(String type, String name, Location location, List<Person> population,int capacity,Map map){
        if (type != null) {
            if(type.equals("Moshav")) {
                return new Moshav(name, location, population, capacity, map);
            }
            else if(type.equals("Kibbutz")){
                return new Kibbutz(name, location, population, capacity,map);
            }
            else{
                return new City(name, location, population, capacity,map);
            }
        } else
            return null;

    }
}
