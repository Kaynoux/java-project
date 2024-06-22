package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DbImporter
{
    DataManager dM = DataManager.INSTANCE;

    /**
     * Which part of DB is currently being read
     * -1: no import Mode
     * 0: new Actors
     * 1: new Films
     * 2: new Directors
     * 3: Actor Movie Relation
     * 4: Director Movie Relation
     */
    private int currentImportMode = -1;

    // 1 Stat Tracker for every single of part of the DB file
    DbPartStats[] ec = new DbPartStats[5];

    public void importData(boolean debugMode) throws IOException
    {
        // Load db file from resources
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("movieproject2024.db");

        //Trying to read file at filename location from
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream)))
        {
            String line;
            //While EOF isnt reached
            while ((line = br.readLine()) != null)
            {
                //Check if a new part of DB file is reached
                if (line.contains("New_Entity"))
                {
                    String dbHeader = line.substring("New_Entity:".length()).trim();

                    //Change the importMode
                    switch (dbHeader)
                    {
                        case "\"actor_id\",\"actor_name\"":
                            currentImportMode = 0;
                            break;
                        case "\"movie_id\",\"movie_title\",\"movie_plot\",\"genre_name\",\"movie_released\",\"movie_imdbVotes\",\"movie_imdbRating\"":
                            currentImportMode = 1;
                            break;
                        case "\"director_id\",\"director_name\"":
                            currentImportMode = 2;
                            break;
                        case "\"actor_id\",\"movie_id\"":
                            currentImportMode = 3;
                            break;
                        case "\"director_id\",\"movie_id\"":
                            currentImportMode = 4;
                            break;
                    }
                }

                // Splits the remaining String into its fields and remove " and whitespaces
                String[] field = line.split("\",\"");
                for (int i = 0; i < field.length; i++)
                {
                    field[i] = field[i].replace("\"", "").trim();
                }

                // Calls the specific Import Function based on the currentImport Mode
                int returnCode = switch (currentImportMode)
                {
                    case 0 -> ImportUtils.importStaff(field, 0, dM);
                    case 1 -> ImportUtils.importMovie(field, dM);
                    case 2 -> ImportUtils.importStaff(field, 1, dM);
                    case 3 -> ImportUtils.importStaffIntoMovie(field, 0, dM);
                    case 4 -> ImportUtils.importStaffIntoMovie(field, 1, dM);
                    default -> 3;
                };

                // Adds to the Import Statistics
                if (debugMode && currentImportMode != -1)
                {
                    ec[currentImportMode].errors[returnCode]++;
                }
            }
        }
        if (debugMode) printStats();
    }

    /**
     * Debug Function to print the amount of correct and incorrect Data after importing
     */
    private void printStats()
    {
        System.out.println("Actor Count: " + dM.staffMaps[0].size() + " Successfully: " + ec[0].errors[0] + " Blank " + ec[0].errors[1] + " Duplicate: " + ec[0].errors[2] + " KeyNotFound: " + ec[0].errors[3]);
        System.out.println("Movie Count: " + dM.movies.size() + " Successfully: " + ec[1].errors[0] + " Blank: " + ec[1].errors[1] + " Duplicate: " + ec[1].errors[2] + " KeyNotFound: " + ec[2].errors[3]);
        System.out.println("Director Count: " + dM.staffMaps[1].size() + " Successfully: " + ec[2].errors[0] + " Blank: " + ec[2].errors[1] + " Duplicate: " + ec[2].errors[2] + " KeyNotFound: " + ec[1].errors[3]);
        System.out.println("Actor Movie Relation" + " Successfully: " + ec[3].errors[0] + " Blank: " + ec[3].errors[1] + " KeyNotFound: " + ec[3].errors[3]);
        System.out.println("Director Movie Relation" + " Successfully: " + ec[4].errors[0] + " Blank: " + ec[4].errors[1] + " KeyNotFound: " + ec[4].errors[3]);
        int correctLines = ec[0].errors[0] + ec[1].errors[0] + ec[2].errors[0] + ec[3].errors[0] + ec[4].errors[0];
        int incorrectLines = ec[0].errors[1] + ec[1].errors[1] + ec[2].errors[1] + ec[0].errors[2] + ec[1].errors[2] + ec[2].errors[2] + ec[3].errors[1] + ec[3].errors[3] + ec[4].errors[1] + ec[4].errors[3];
        int totalLines = 88810;
        int analysedLines = correctLines + incorrectLines;
        int missingLines = totalLines - analysedLines - 5;
        System.out.println("Total Lines: " + totalLines + " Analysed Lines: " + analysedLines + " Correct Lines: " + correctLines + " Incorrect Lines: " + incorrectLines + " Identifier Lines: 5" + " Missing Lines : " + missingLines);
    }
}


