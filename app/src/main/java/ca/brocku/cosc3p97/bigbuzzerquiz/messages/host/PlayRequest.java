package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.GameContract;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;

/**
 * A request object responsible for sending a request to the host from the player with
 * the parameters of the game being requested for play (ie. number of questions and the categories)
 */
public class PlayRequest extends Request {
    public static final String NUMBER_OF_QUESTIONS = "NUMBER_OF_QUESTIONS";
    public static final String KEYS = "KEYS";
    public static final String GAME = "GAME";

    /**
     * Constructor which uses the constructor of the super class that allows the internal
     * JSONObject of this class to be populated from the JSON string passed as an argument
     * @param string JSON string used to load this instance
     * @throws JSONException
     */
    public PlayRequest(String string) throws JSONException {
        super(string);
    }


    /**
     * Constructor
     */
    public PlayRequest() {
        super();
    }


    /**
     * Override the setIdentifier method with the value ANSWER
     */
    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, HostRequestContract.PLAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Create the GAME attribute and assign to it the value passed as an argument
     * @param toBeSerialized the boolean value to assign to the attribute
     */
    @Override
    public void serialize(Object toBeSerialized) {
        try {
            GameContract game = (GameContract) toBeSerialized;
            JSONObject json = new JSONObject();
            json.put(NUMBER_OF_QUESTIONS, game.numberOfQuestions);
            JSONArray jsonKeys = new JSONArray();
            for(int key : game.categoryKeys) {
                jsonKeys.put(key);
            }
            json.put(PlayRequest.KEYS, jsonKeys);
            put(GAME, json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Convert the internal value of the GAME attribute into a boolean value and return
     * it to the calling method
     */
    @Override
    public Object deserialize() throws JSONException {
        GameContract game = new GameContract();

        try {
            JSONObject json = getJSONObject(GAME);
            game.numberOfQuestions = json.getInt(PlayRequest.NUMBER_OF_QUESTIONS);
            JSONArray jsonKeys = json.getJSONArray(PlayRequest.KEYS);
            game.categoryKeys = new int[jsonKeys.length()];

            for(int i = 0; i<jsonKeys.length(); i++) {
                game.categoryKeys[i] = jsonKeys.getInt(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return game;
    }
}
