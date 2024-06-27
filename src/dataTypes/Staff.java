package dataTypes;

import java.util.HashSet;

/**
 * Workers on the Film like Actors or Directors
 */
public class Staff
{
    private int id;

    private String name;
    private HashSet<Movie> movies = new HashSet<>();

    public Staff(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public HashSet<Movie> getMovies()
    {
        return movies;
    }
}
