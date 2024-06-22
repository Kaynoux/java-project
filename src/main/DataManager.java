package main;

import java.util.HashMap;

//This is a Enum Singleton to Store all the Data
public enum DataManager
{
    INSTANCE;

    // HashMaps are used to allow quick and performant Access via the Key (which is the movie ID)
    public HashMap<Integer, Movie> movies = new HashMap<>();
    // Actors (staffMap[0) and Directors (staffMap[1]) are stored in Array because they have the same propertys
    // This allows to use the same function on both Directors and Actors with the index as a function parameter
    public HashMap<Integer, Staff>[] staffMaps = new HashMap[2];

    DataManager()
    {
        staffMaps[0] = new HashMap<>(); // actors
        staffMaps[1] = new HashMap<>(); // directors
    }
}
