package main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ImportUtils
{
    /**
     * @param fields     0:ID 1:Name
     * @param staff_type 0:Actor 1:Director
     * @return 0=success otherwise error code
     */
    public static int importStaff(String[] fields, int staff_type, DataManager dM)
    {
        // Error 1: ID or name blank
        if (fields[0].isBlank() || fields[1].isBlank()) return 1;

        int id = Integer.parseInt(fields[0]);
        String name = fields[1];

        // Error 2: Staff already there
        if (dM.staffMaps[staff_type].containsKey(id)) return 2;

        Staff staff = new Staff(id, name);
        dM.staffMaps[staff_type].put(id, staff);
        return 0;
    }

    /**
     * @param fields 0:ID 1:Title 2:Descr. 3:Genre 4:Date 5:RatingCount 6:Rating
     * @return 0=success otherwise error code
     */
    public static int importMovie(String[] fields, DataManager dM)
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
        if (dM.movies.containsKey(id)) return 2;

        Movie movie = new Movie(id, title, description, genre, releaseDate, rating, ratingCount);
        dM.movies.put(id, movie);
        return 0;
    }

    /**
     * @param fields     0:StaffID 1:MovieID
     * @param staff_type 0:Actor 1:Director
     * @return 0=success otherwise error code
     */
    public static int importStaffIntoMovie(String[] fields, int staff_type, DataManager dM)
    {
        // Error 1: ID of Movie or Staff is missing
        if (fields[0].isBlank() || fields[1].isBlank()) return 1;

        int staff_id = Integer.parseInt(fields[0]);
        int movie_id = Integer.parseInt(fields[1]);

        // Error 3: Movie ID or Staff ID does not exist
        if (!dM.staffMaps[staff_type].containsKey(staff_id) || !dM.movies.containsKey(movie_id))
        {
            return 3;
        }

        // Add Staff to Movie
        dM.movies.get(movie_id).staffSets[staff_type].add(dM.staffMaps[staff_type].get(staff_id));
        // Add Movie to Staff
        dM.staffMaps[staff_type].get(staff_id).movies.add(dM.movies.get(movie_id));

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
