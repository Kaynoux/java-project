package main;

import data_types.Movie;
import data_types.Staff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;


public class Main
{
    // Set to true to toggle Debug mode
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

        if (arg.startsWith("--filmsuche"))
        {
            arg = arg.substring("--filmsuche=".length()).replace("\"", "").trim();

            ArrayList<Movie> movie_results = SearchUtils.SearchMovieByTitle(arg, dM);

            for (Movie movie : movie_results)
            {
                System.out.println("ID: " + movie.id + " Title: " + movie.title + " Release: " + movie.releaseDate + " Rating: " + movie.rating + " Rating Count: " + movie.ratingCount + " Description: " + movie.description);
            }
        }
        else if (arg.startsWith("--schauspielersuche"))
        {
            arg = arg.substring("--schauspielersuche=".length()).replace("\"", "").trim();

            ArrayList<Staff> actor_results = SearchUtils.SearchStaffByName(arg, 0, dM);

            for (Staff actor : actor_results)
            {
                System.out.println("ID: " + actor.id + " Name: " + actor.name);
            }
        }
        else if (arg.startsWith("--filmnetzwerk"))
        {
            int movie_id = Integer.parseInt(arg.substring("--filmnetzwerk=".length()).trim());

            if (!dM.movies.containsKey(movie_id))
            {
                System.out.println("ID does not belong to a movie");
                return;
            }

            HashSet<Movie> movie_results = SearchUtils.getFilmsByActorsFromFilmID(movie_id, 0, dM);

            // Print all Actors which played in the given Film and separate them by ","
            System.out.print("Schauspieler: ");
            System.out.println(dM.movies.get(movie_id).staffSets[0].stream().map(actor -> actor.name).collect(Collectors.joining(", ")));

            // Print all movie_results and separate by ","
            System.out.print("\nFilme: ");
            System.out.println(movie_results.stream().map(movie -> movie.title).collect(Collectors.joining(",")));

        }
        else if (arg.startsWith("--schauspielernetzwerk"))
        {
            int actor_id = Integer.parseInt(arg.substring("--schauspielernetzwerk=".length()).trim());

            if (!dM.staffMaps[0].containsKey(actor_id))
            {
                System.out.println("ID does not belong to an actor");
                return;
            }

            // Get all Actors which took part in Films where the given Actor played in
            HashSet<Staff> actor_results = SearchUtils.getStaffByFilmsFromActorID(actor_id, 0, dM);

            // Print all Films from the given Actor and separate them by ","
            System.out.print("Filme: ");
            System.out.println(dM.staffMaps[0].get(actor_id).movies.stream().map(movie -> movie.title).collect(Collectors.joining(", ")));

            //Print all actor_results and seperate them by ","
            System.out.print("\nSchauspieler: ");
            System.out.println(actor_results.stream().map(actor -> actor.name).collect(Collectors.joining(",")));
        }

    }
}