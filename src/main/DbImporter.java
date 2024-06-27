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
                    continue;
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
                    case 0 -> dM.importStaff(field, 0);
                    case 1 -> dM.importMovie(field);
                    case 2 -> dM.importStaff(field, 1);
                    case 3 -> dM.importStaffIntoMovie(field, 0);
                    case 4 -> dM.importStaffIntoMovie(field, 1);
                    default -> 3;
                };

                if (debugMode)
                {
                    System.out.println("ImportCode: " + returnCode + "Line:" + line);
                }
            }
        }
    }
}


