package entities;

import java.time.LocalDate;
import java.util.HashSet;

public class Movie
{
    private final int id;
    private final String title;
    private final String description;
    private final String genre;
    private final LocalDate releaseDate;
    private final float rating;
    private final int ratingCount;
    //Index 0:Actors 1:Directors on the film
    private final HashSet<Staff>[] staffSets = new HashSet[2];

    public Movie(int id, String title, String description, String genre, LocalDate releaseDate, float rating, int ratingCount)
    {
        staffSets[0] = new HashSet<>();
        staffSets[1] = new HashSet<>();
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.ratingCount = ratingCount;
    }

    public HashSet<Staff> getStaffSets(int staff_type)
    {
        return staffSets[staff_type];
    }

    public int getRatingCount()
    {
        return ratingCount;
    }

    public float getRating()
    {
        return rating;
    }

    public LocalDate getReleaseDate()
    {
        return releaseDate;
    }

    public String getGenre()
    {
        return genre;
    }

    public String getDescription()
    {
        return description;
    }

    public String getTitle()
    {
        return title;
    }

    public int getId()
    {
        return id;
    }


}
