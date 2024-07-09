package main;

import entities.Movie;
import entities.Staff;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

//This is a Enum Singleton to Store all the Data
public enum DataManager
{
    INSTANCE;

    // HashMaps are used to allow quick and performant Access via the Key (which is the movie ID)
    private HashMap<Integer, Movie> movies = new HashMap<>();
    // Actors (staffMap[0) and Directors (staffMap[1]) are stored in Array because they have the same propertys
    // This allows to use the same function on both Directors and Actors with the index as a function parameter
    private HashMap<Integer, Staff>[] staffMaps = new HashMap[2];

    DataManager()
    {
        staffMaps[0] = new HashMap<>(); // actors
        staffMaps[1] = new HashMap<>(); // directors
    }

    /**
     * Movie Getter
     */
    public HashMap<Integer, Movie> getMovies()
    {
        return movies;
    }

    /**
     * Staff Getter
     */
    public HashMap<Integer, Staff> getStaff(int staff_type)
    {
        return staffMaps[staff_type];
    }

    /**
     * @param fields     0:ID 1:Name
     * @param staff_type 0:Actor 1:Director
     * @return 0=success otherwise error code
     */
    public int importStaff(String[] fields, int staff_type)
    {
        // Error 1: ID or name blank
        if (fields[0].isBlank() || fields[1].isBlank()) return 1;

        int id = Integer.parseInt(fields[0]);
        String name = fields[1];

        // Error 2: Staff already there
        if (staffMaps[staff_type].containsKey(id)) return 2;

        Staff staff = new Staff(id, name);
        staffMaps[staff_type].put(id, staff);
        return 0;
    }

    /**
     * @param fields 0:ID 1:Title 2:Descr. 3:Genre 4:Date 5:RatingCount 6:Rating
     * @return 0=success otherwise error code
     */
    public int importMovie(String[] fields)
    {
        // Error 1: ID, Title or Description blank
        if (fields[0].isBlank() || fields[1].isBlank() || fields[2].isBlank()) return 1;

        int id = Integer.parseInt(fields[0]);
        String title = fields[1];
        String description = fields[2];
        String genre = fields[3];
        LocalDate releaseDate = StringToLocalDate(fields[4]);

        // Rating Count is 0 by default if no Rating Count given
        int ratingCount = 0;
        if (!fields[5].isBlank())
        {
            ratingCount = Integer.parseInt(fields[5]);
        }

        float rating = 0;
        if (!fields[6].isBlank())
        {
            rating = Float.parseFloat(fields[6]);
        }

        // Error 2: Movie already there
        if (movies.containsKey(id)) return 2;

        Movie movie = new Movie(id, title, description, genre, releaseDate, rating, ratingCount);
        movies.put(id, movie);
        return 0;
    }

    /**
     * @param fields     0:StaffID 1:MovieID
     * @param staff_type 0:Actor 1:Director
     * @return 0=success otherwise error code
     */
    public int importStaffIntoMovie(String[] fields, int staff_type)
    {
        // Error 1: ID of Movie or Staff is missing
        if (fields[0].isBlank() || fields[1].isBlank()) return 1;

        int staff_id = Integer.parseInt(fields[0]);
        int movie_id = Integer.parseInt(fields[1]);

        // Error 3: Movie ID or Staff ID does not exist
        if (!staffMaps[staff_type].containsKey(staff_id) || !movies.containsKey(movie_id))
        {
            return 3;
        }

        // Add Staff to Movie
        movies.get(movie_id).getStaffSets(staff_type).add(staffMaps[staff_type].get(staff_id));
        // Add Movie to Staff
        staffMaps[staff_type].get(staff_id).getMovies().add(movies.get(movie_id));

        return 0;
    }

    /**
     * @return null if not successfully
     */
    private static LocalDate StringToLocalDate(String dateString)
    {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try
        {
            return LocalDate.parse(dateString, dateFormat);
        } catch (DateTimeParseException e)
        {
            return null;
        }
    }
}
