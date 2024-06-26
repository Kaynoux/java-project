package data_types;

import java.time.LocalDate;
import java.util.HashSet;

public class Movie
{
    public int id;
    public String title;
    public String description;
    public String genre;
    public LocalDate releaseDate;
    public float rating;
    public int ratingCount;

    //0:Actors 1:Directors on the film
    public HashSet<Staff>[] staffSets = new HashSet[2];

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
}
