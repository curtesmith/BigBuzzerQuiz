package ca.brocku.cosc3p97.bigbuzzerquiz;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import android.support.v7.app.AppCompatActivity;

/**
 * This class provides the methods to create and initialize the database for the first time as
 * well as inserting the question categories, questions and answers
 *
 * Since I don't know where exactly we will need this, the code is here and we can put it
 * wherever we want
 *
 * Created by Maike on 30.11.2015.
 */
public class DatabaseInitializer extends AppCompatActivity{

    public void initializeDatabase(){

        SQLiteDatabase QuizDatabase;

        // Create the Database
        QuizDatabase = openOrCreateDatabase("QuizDatabase", MODE_PRIVATE, null);
        //    QuizDatabase.execSQL("DROP Table QuizDatabase");

        // Create both tables
        QuizDatabase.execSQL(getString(R.string.createTableCategoriesQuery));
        QuizDatabase.execSQL(getString(R.string.createTableQuestionsQuery));

        // Input Value in tables
        insertCategories("Categories", QuizDatabase);
        insertArtQuestions("Questions", QuizDatabase);
        insertScienceQuestions("Questions", QuizDatabase);
        insertGeographyQuestions("Questions", QuizDatabase);

    }

    private void insertCategories(String tableName, SQLiteDatabase databaseName){
        String art = "INSERT INTO " + tableName + " VALUES('Art and Culture')";
        String science = "INSERT INTO " + tableName + " VALUES('Science')";
        String geography = "INSERT INTO " + tableName + " VALUES('Geography')";

        databaseName.execSQL(art);
        databaseName.execSQL(science);
        databaseName.execSQL(geography);
    }

    private void insertArtQuestions(String tableName, SQLiteDatabase databaseName) {

        String art1 = "INSERT INTO " + tableName + " VALUES(" +
                "'Who was the original author of Dracula?', 'Bram Stoker', 'Emily Bronte', " +
                "'Charles Dickens', 'J.K.Rowling', 0, 1)";
        String art2 = "INSERT INTO " + tableName + " VALUES(" +
                "'What is the first book of the Old Testament called?', 'Moses', 'Genesis', 'Lukas', " +
                "'David', 1, 1)";
        String art3 = "INSERT INTO " + tableName + " VALUES(" +
                "'Who painted the Mona Lisa?', 'Michelangelo', 'Rembrandt', 'Leonardo Da Vinci', " +
                "'Sandro Boticelli', 2, 1)";
        String art4 = "INSERT INTO " + tableName + " VALUES(" +
                "'Who is the author of the book \'Nineteen Eighty Four\'?', 'Thomas Hardy', " +
                "'Emile Zola', 'George Orwell', 'Walter Scott', 2, 1)";
        String art5 = "INSERT INTO " + tableName + " VALUES(" +
                "'The first time a television programm to be broadcast in India was in... ?', " +
                "'1959', '1965', '1976', '1957', 0, 1)";

        String art6 = "INSERT INTO " + tableName + " VALUES(" +
                "'Who wrote the book \'War and Peace\'?', " +
                "'Leo Tolstoy', 'Mahatma Gandhi', 'Charles Dickens', 'Kipling', 0, 1)";
        String art7 = "INSERT INTO " + tableName + " VALUES(" +
                "'How many times has the Mona Lisa been stolen?', '8', '10', '1', '5', 2, 1)";
        String art8 = "INSERT INTO " + tableName + " VALUES(" +
                "'Pop Art originated in which city?', 'Amsterdam', 'New York', 'Frankfurt', " +
                "'London', 3, 1)";
        String art9 = "INSERT INTO " + tableName + " VALUES(" +
                "'Which art movement claimed to be anti-art?', 'Dada', 'Cubism', 'Art Nouveau', " +
                "'Fauvism', 0, 1)";
        String art10 = "INSERT INTO " + tableName + " VALUES(" +
                "'Leonardi Da Vinci invented which of these items?', 'Kites', 'High heels', " +
                "'Gunpowder', 'Wine cork', 1, 1)";

        databaseName.execSQL(art1);
        databaseName.execSQL(art2);
        databaseName.execSQL(art3);
        databaseName.execSQL(art4);
        databaseName.execSQL(art5);

        databaseName.execSQL(art6);
        databaseName.execSQL(art7);
        databaseName.execSQL(art8);
        databaseName.execSQL(art9);
        databaseName.execSQL(art10);
    }

    private void insertScienceQuestions(String tableName, SQLiteDatabase databaseName){
        String science1 = "INSERT INTO " + tableName + " VALUES(" +
                "'What is the biggest Planet in our System?', 'The Sun', 'Jupiter', 'Saturn', " +
                "'Pluto', 1, 2)";
        String science2 = "INSERT INTO " + tableName + " VALUES(" +
                "'Who wrote the book \'The Origin of Species\'?', 'Sir Alexander Fleming', " +
                "'Stephen Hawking', 'Charles Darwin', 'Louis Pasteur', 2, 2)";
        String science3 = "INSERT INTO " + tableName + " VALUES(" +
                "'The Sun is a ... ?', 'Satellite', 'Comet', 'Star', 'Huge Planet', 2, 2)";
        String science4 = "INSERT INTO " + tableName + " VALUES(" +
                "'Which rare element would you associate with Marie and Pierre Curie?', 'Radium', " +
                "'Gold', 'Uranium', 'Platinum', 0, 2)";
        String science5 = "INSERT INTO " + tableName + " VALUES(" +
                "'According to Apollo astronauts, the moon smells like... ?', 'Cheese', " +
                "'Burnt Gunpowder', 'Gasoline', 'Coffee grounds', 1, 2)";

        String science6 = "INSERT INTO " + tableName + " VALUES(" +
                "'The hardest substance available on earth is...?', 'Gold', 'Iron', 'Diamond', " +
                "'Platinum', 2, 2)";
        String science7 = "INSERT INTO " + tableName + " VALUES(" +
                "'Tetraethyl lead is used as ... ?', 'Pain Killer', " +
                "'Fire Extinguisher', 'Mosquito Repellent', 'Petrol Additive', 3, 2)";
        String science8 = "INSERT INTO " + tableName + " VALUES(" +
                "'If a barometer is going down it is an indication of?', 'Snow', 'Storm', " +
                "'Intense Heat', 'Rainfall', 3, 2)";
        String science9 = "INSERT INTO " + tableName + " VALUES(" +
                "'One Kilometer is equal to how many miles?', '0.84', '0.5', '1.6', '0.62', 3, 2)";
        String science10 = "INSERT INTO " + tableName + " VALUES(" +
                "'Deep blue colour is imparted to glass by the presence of?', 'Cupirc oxide', " +
                "'Nickel Oxide', 'Cobalt Oxide', 'Iron Oxide', 2, 2)";

        databaseName.execSQL(science1);
        databaseName.execSQL(science2);
        databaseName.execSQL(science3);
        databaseName.execSQL(science4);
        databaseName.execSQL(science5);

        databaseName.execSQL(science6);
        databaseName.execSQL(science7);
        databaseName.execSQL(science8);
        databaseName.execSQL(science9);
        databaseName.execSQL(science10);
    }

    private void insertGeographyQuestions(String tableName, SQLiteDatabase databaseName){

        String geography1 = "INSERT INTO " + tableName + " VALUES(" +
                "'The Nile River is the longest river in the world. Which one is the next longest?', " +
                "'Yangtze River', 'Congo River', 'Amazon River', 'Hunang He', 2, 3)";
        String geography2 = "INSERT INTO " + tableName + " VALUES(" +
                "'What is the 10th most spoken language worldwide?', 'German', 'Bengali', 'Russian', " +
                "'Portuguese', 0, 3)";
        String geography3 = "INSERT INTO " + tableName + " VALUES(" +
                "'What is the name of Hong Kong\'s metro system?', 'Metrorail', 'RTA Rapid Transit', " +
                "'Docklands Light Railway', 'MTR', 3, 3)";
        String geography4 = "INSERT INTO " + tableName + " VALUES(" +
                "'The second longest coastline (after Canada) is where?', 'Chile', 'Australia', " +
                "'Russia', 'Indonesia', 3, 3)";
        String geography5 = "INSERT INTO " + tableName + " VALUES(" +
                "'Approximately how many people live in Greenland?', '44 000', '32 000', '85 000', " +
                "'57 000', 3, 3)";

        String geography6 = "INSERT INTO " + tableName + " VALUES(" +
                "'Which country contains the most languages?', 'Papua New Guinea', 'China', " +
                "'Australia', 'Switzerland', 0, 3)";
        String geography7 = "INSERT INTO " + tableName + " VALUES(" +
                "'Brisbane is the capital of which Australian State?', 'New South Wales', " +
                "'Queensland', 'Tasmania', 'Wester Australia', 1, 3)";
        String geography8 = "INSERT INTO " + tableName + " VALUES(" +
                "'Mount Denali is the highest peak in the USA. In which state can you find it?', " +
                "'New York', 'Alaska', 'New Mexico', 'Hawaii', 1, 3)";
        String geography9 = "INSERT INTO " + tableName + " VALUES(" +
                "'Which country\'s name means literally \'Land of Silver\'?', " +
                "'Paraguay', 'Chile', 'Columbia', 'Argentina', 3, 3)";
        String geography10 = "INSERT INTO " + tableName + " VALUES(" +
                "'Which Italian city was almost destroyed by the flood in 1966?', 'Milan', " +
                "'Florence', 'Venice', 'Rome', 1, 3)";

        databaseName.execSQL(geography1);
        databaseName.execSQL(geography2);
        databaseName.execSQL(geography3);
        databaseName.execSQL(geography4);
        databaseName.execSQL(geography5);

        databaseName.execSQL(geography6);
        databaseName.execSQL(geography7);
        databaseName.execSQL(geography8);
        databaseName.execSQL(geography9);
        databaseName.execSQL(geography10);
    }
}
