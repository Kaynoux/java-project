package main;

import entities.Movie;
import entities.Staff;

import java.util.ArrayList;
import java.util.HashSet;

public class SearchUtils
{
    /**
     * @param title_part what substring a movie title needs to contain
     * @return List of the movies which titles include the title_part
     */
    public static ArrayList<Movie> getMovieByTitle(String title_part, DataManager dM)
    {
        ArrayList<Movie> hits = new ArrayList<>();
        for (Movie movie : dM.getMovies().values())
        {
            // This ignores upper and Lower Case completely for searching
            if (movie.getName().toLowerCase().contains(title_part.toLowerCase()))
            {
                hits.add(movie);
            }
        }
        return hits;
    }

    /**
     * @param name_part  what substring a director name needs to contain
     * @param staff_type 0=actor 1=director
     * @return List of actors which names include the name_part
     */
    public static ArrayList<Staff> getStaffByName(String name_part, int staff_type, DataManager dM)
    {
        ArrayList<Staff> hits = new ArrayList<>();
        for (Staff staff : dM.getStaff(staff_type).values())
        {
            // This ignores upper and Lower Case completely for searching
            if (staff.getName().toLowerCase().contains(name_part.toLowerCase()))
            {
                hits.add(staff);
            }
        }
        return hits;
    }

    /**
     * @param staff_type 0=actor 1=director
     * @return Set of all Movies which contain at least one actor from the input Film
     */
    public static HashSet<Movie> getFilmsByActorsFromFilmID(int movie_id, int staff_type, DataManager dM)
    {
        if (!dM.getMovies().containsKey(movie_id))
        {
            return null;
        }

        HashSet<Movie> hits = new HashSet<>();
        for (Staff staff : dM.getMovies().get(movie_id).getStaffSets(staff_type))
        {
            hits.addAll(staff.getMovies());
        }

        //Remove the input Film because it is not part of its own Network
        hits.remove(dM.getMovies().get(movie_id));
        return hits;
    }

    /**
     * @param staff_type 0=actor 1=director
     * @return Set of all Actors which played in at least one film where the input Actor also played
     */
    public static HashSet<Staff> getStaffByFilmsFromActorID(int actor_id, int staff_type, DataManager dM)
    {
        if (!dM.getStaff(staff_type).containsKey(actor_id))
        {
            return null;
        }

        HashSet<Staff> hits = new HashSet<>();
        for (Movie movie : dM.getStaff(staff_type).get(actor_id).getMovies())
        {
            hits.addAll(movie.getStaffSets(staff_type));
        }

        // Remove the input Actor because it is not part of its own Network
        hits.remove(dM.getStaff(staff_type).get(actor_id));
        return hits;
    }


}
