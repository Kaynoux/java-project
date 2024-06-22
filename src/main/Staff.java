package main;

import java.util.HashSet;

/**
 * Workers on the Film like Actors or Directors
 */
public class Staff
{
    public int id;
    public String name;
    public HashSet<Movie> movies = new HashSet<>();

    public Staff(int id, String name)
    {
        this.id = id;
        this.name = name;
    }
}
