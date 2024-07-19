package entities;

import java.util.HashSet;

/**
 * Workers on the Film like Actors or Directors
 */
public class Staff extends Entity
{
    private final HashSet<Movie> movies = new HashSet<>();

    public Staff(int id, String name)
    {
        // Calling superclass constructor
        super(id, name);
    }

    public HashSet<Movie> getMovies()
    {
        return movies;
    }
}
