package main;

import entities.Movie;
import entities.Staff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;


public class Main
{
    // Set to true to toggle global Debug mode
    private final static boolean debugMode = false;

    public static void main(String[] args)
    {
        //Create Data Manager Singleton
        DataManager dM = DataManager.INSTANCE;
        DbImporter dbImporter = new DbImporter();

        //Try importing data from db file
        try
        {
            dbImporter.importData(debugMode);
        } catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        // Only the first argument is needed
        String arg = args[0];
        String[] parts = arg.split("=");
        parts[0] = parts[0].trim();
        parts[1] = parts[1].replace("\"", "").trim();

        switch (parts[0])
        {
            case "--filmsuche":
                movieSearch(parts[1], dM);
                break;
            case "--schauspielersuche":
                actorSearch(parts[1], dM);
                break;
            case "--filmnetzwerk":
                movieNetwork(dM, Integer.parseInt(parts[1]));
                break;
            case "--schauspielernetzwerk":
                actorNetwork(dM, Integer.parseInt(parts[1]));
                break;
        }
    }

    private static void actorNetwork(DataManager dM, int actor_id)
    {
        if (!dM.getStaff(0).containsKey(actor_id))
        {
            System.out.println("ID does not belong to an actor");
            return;
        }

        // Get all Actors which took part in Films where the given Actor played in
        HashSet<Staff> actor_results = SearchUtils.getStaffByFilmsFromActorID(actor_id, 0, dM);

        // Print all Films from the given Actor and separate them by ","
        System.out.print("Filme: ");
        System.out.println(dM.getStaff(0).get(actor_id).getMovies().stream().map(movie -> movie.getTitle()).collect(Collectors.joining(", ")));

        //Print all actor_results and seperate them by ","
        System.out.print("\nSchauspieler: ");
        System.out.println(actor_results.stream().map(actor -> actor.getName()).collect(Collectors.joining(",")));
    }

    private static void movieNetwork(DataManager dM, int movie_id)
    {
        if (!dM.getMovies().containsKey(movie_id))
        {
            System.out.println("ID does not belong to a movie");
            return;
        }

        HashSet<Movie> movie_results = SearchUtils.getFilmsByActorsFromFilmID(movie_id, 0, dM);

        // Print all Actors which played in the given Film and separate them by ","
        System.out.print("Schauspieler: ");
        System.out.println(dM.getMovies().get(movie_id).staffSets[0].stream().map(actor -> actor.getName()).collect(Collectors.joining(", ")));

        // Print all movie_results and separate by ","
        System.out.print("\nFilme: ");
        System.out.println(movie_results.stream().map(movie -> movie.getTitle()).collect(Collectors.joining(",")));
    }

    private static void actorSearch(String arg, DataManager dM)
    {
        ArrayList<Staff> actor_results = SearchUtils.getStaffByName(arg, 0, dM);

        if (actor_results.isEmpty())
        {
            System.out.println("No results found");
            return;
        }

        for (Staff actor : actor_results)
        {
            System.out.println("ID: " + actor.getId() + " Name: " + actor.getName());
        }
    }

    private static void movieSearch(String arg, DataManager dM)
    {
        ArrayList<Movie> movie_results = SearchUtils.getMovieByTitle(arg, dM);

        if (movie_results.isEmpty())
        {
            System.out.println("No results found");
            return;
        }

        for (Movie movie : movie_results)
        {
            System.out.println("ID: " + movie.getId() + " Title: " + movie.getTitle() + " Release: " + movie.getReleaseDate() +
                    " Genre: " + movie.getGenre() + "Rating: " + movie.getRating() + " Rating Count: " + movie.getRatingCount() + " Description: " + movie.getDescription());
        }
    }
}