package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;


import org.json.JSONArray;
import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.database.QuestionContract;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;

/**
 * A request object responsible for sending a request to the player from the host to provide
 * the player with the next question of the quiz
 */
public class ShowQuestionRequest extends Request {
    public static final String TEXT = "TEXT";
    public static final String ANSWERS = "ANSWERS";
    public static final String CORRECT_ANSWER = "CORRECT_ANSWER";
    public static final String CATEGORY_KEY = "CATEGORY_KEY";


    /**
     * Constructor which uses the constructor of the super class that allows the internal
     * JSONObject of this class to be populated from the JSON string passed as an argument
     * @param string JSON string used to load this instance
     * @throws JSONException
     */
    public ShowQuestionRequest(String string) throws JSONException {
        super(string);
    }


    /**
     * Constructor
     */
    public ShowQuestionRequest() {
        super();
    }


    /**
     * Override the setIdentifier method with the value READY
     */
    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, PlayerMessageContract.SHOW_QUESTION);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Create the attributes of the question and assign to it the value passed as an argument
     * @param toBeSerialized the boolean value to assign to the attribute
     */
    @Override
    public void serialize(Object toBeSerialized) {
        QuestionContract question = (QuestionContract) toBeSerialized;
        try {
            put(ShowQuestionRequest.TEXT, question.text);
            JSONArray answers = new JSONArray();
            for(String answer : question.answers) {
                answers.put(answer);
            }
            put(ShowQuestionRequest.ANSWERS, answers);
            put(ShowQuestionRequest.CORRECT_ANSWER, question.indexOfCorrectAnswer);
            put(ShowQuestionRequest.CATEGORY_KEY, question.category);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * Convert the internal value of the question contract attribute into a QuestionContract
     * value and return it to the calling method
     */
    @Override
    public Object deserialize() {
        QuestionContract question = new QuestionContract();
        try {
            question.text = getString(ShowQuestionRequest.TEXT);
            question.indexOfCorrectAnswer = getInt(ShowQuestionRequest.CORRECT_ANSWER);
            question.category = getInt(ShowQuestionRequest.CATEGORY_KEY);
            JSONArray answers = getJSONArray(ShowQuestionRequest.ANSWERS);
            question.answers = new String[answers.length()];
            for(int i=0; i<answers.length(); i++) {
                question.answers[i] = answers.getString(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return question;
    }
}
