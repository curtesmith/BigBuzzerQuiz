package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;


import org.json.JSONArray;
import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.database.Question;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;

public class ShowQuestionRequest extends Request {
    public static final String TEXT = "TEXT";
    public static final String ANSWERS = "ANSWERS";
    public static final String CORRECT_ANSWER = "CORRECT_ANSWER";
    public static final String CATEGORY_KEY = "CATEGORY_KEY";


    public ShowQuestionRequest(String string) throws JSONException {
        super(string);
    }

    public ShowQuestionRequest() {
        super();
    }


    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, PlayerMessageInterface.SHOW_QUESTION);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void serialize(Question question) {
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


    public Question deserialize() {
        Question question = new Question();
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
