package ca.brocku.cosc3p97.bigbuzzerquiz.database;


/**
 * Describes the contents of the Questions database table that can be used to pass through
 * the application
 */
public class QuestionContract {
    public String text;
    public String[] answers;
    public int indexOfCorrectAnswer;
    public int category;
}
