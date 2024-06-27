import dataTypes.Movie;
import dataTypes.Staff;
import main.DataManager;
import main.SearchUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnitTests
{

    private DataManager handleImport() throws IOException
    {
        DataManager dM = DataManager.INSTANCE;

        //Add Test Movies
        dM.importMovie(new String[]{"1", "Movie1", "Descr1", "genr1", "2001-1-1", "1", "1"});
        dM.importMovie(new String[]{"2", "Movie2", "Descr2", "genr2", "2002-2-2", "2", "2"});
        dM.importMovie(new String[]{"3", "Movie3", "Descr3", "genr3", "2003-3-3", "3", "3"});

        //Add Test Actors
        dM.importStaff(new String[]{"1", "Actor1"}, 0);
        dM.importStaff(new String[]{"2", "Actor2"}, 0);
        dM.importStaff(new String[]{"3", "Actor3"}, 0);

        //Add Test Relations between Actor and Movies
        dM.importStaffIntoMovie(new String[]{"1", "1"}, 0);
        dM.importStaffIntoMovie(new String[]{"1", "2"}, 0);
        dM.importStaffIntoMovie(new String[]{"2", "2"}, 0);
        dM.importStaffIntoMovie(new String[]{"1", "3"}, 0);
        dM.importStaffIntoMovie(new String[]{"2", "3"}, 0);
        dM.importStaffIntoMovie(new String[]{"3", "3"}, 0);

        return dM;
    }


    @Test
    public void testImportData() throws IOException
    {
        DataManager dM = handleImport();

        //If 3 Movies where imported
        assertEquals(3, dM.getMovies().size());
        // If 3 Actors where imported
        assertEquals(3, dM.getStaff(0).size());
        // If Movie3 has 3 Actors
        assertEquals(3, dM.getMovies().get(3).staffSets[0].size());
        // if Actor1 played in 3 Movies
        assertEquals(3, dM.getStaff(0).get(1).getMovies().size());
    }

    @Test
    public void testMovieSearch() throws IOException
    {
        DataManager dM = handleImport();
        ArrayList<Movie> result = SearchUtils.getMovieByTitle("Movie1", dM);

        // If there is 1 Movie with Name Movie1
        assertEquals(1, result.size());
    }

    @Test
    public void testStaffSearch() throws IOException
    {
        DataManager dM = handleImport();
        ArrayList<Staff> result = SearchUtils.getStaffByName("Actor1", 0, dM);

        // If there is 1 Actor with Name Actor1
        assertEquals(1, result.size());
    }

    @Test
    public void testActorNetworkSearch() throws IOException
    {
        DataManager dM = handleImport();
        HashSet<Staff> result = SearchUtils.getStaffByFilmsFromActorID(1, 0, dM);

        //If Actor1 played in 3 Movies
        assertEquals(3, dM.getStaff(0).get(1).getMovies().size());
        // If there are 2 Actors in the Actor Network of Actor1
        assertEquals(2, result.size());
    }

    @Test
    public void testMovieNetworkSearch() throws IOException
    {
        DataManager dM = handleImport();
        HashSet<Movie> result = SearchUtils.getFilmsByActorsFromFilmID(3, 0, dM);

        // If Movie3 has 3 Actors
        assertEquals(3, dM.getMovies().get(3).staffSets[0].size());
        // If there are 2 Movies in the Movie Network of Movie3
        assertEquals(2, result.size());
    }


}
