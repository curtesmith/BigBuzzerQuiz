package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;


/**
 * A request object responsible for sending a request to the player from the host to inform
 * the player that the list of players has been updated
 */
public class SendPlayerNamesRequest extends Request {
    public static final String NAMES = "NAMES";


    /**
     * Constructor which uses the constructor of the super class that allows the internal
     * JSONObject of this class to be populated from the JSON string passed as an argument
     * @param string JSON string used to load this instance
     * @throws JSONException
     */
    public SendPlayerNamesRequest(String string) throws JSONException {
        super(string);
    }

    /**
     * Constructor
     */
    public SendPlayerNamesRequest() {
        super();
    }


    /**
     * Override the setIdentifier method with the value READY
     */
    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, PlayerMessageContract.SEND_PLAYER_NAMES);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Create the NAMES attribute and assign to it the value passed as an argument
     * @param toBeSerialized the boolean value to assign to the attribute
     */
    @Override
    public void serialize(Object toBeSerialized) {
        List<String> names = (List<String>) toBeSerialized;
        try {
            JSONArray jsonNames = new JSONArray();
            for(String name : names) {
                jsonNames.put(name);
            }
            put(SendPlayerNamesRequest.NAMES, jsonNames);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Convert the internal value of the NAMES attribute into a List<String>
     * value and return it to the calling method
     */
    @Override
    public Object deserialize() {
        List<String> names = new ArrayList<>();
        try {
            JSONArray jsonNames = getJSONArray(SendPlayerNamesRequest.NAMES);

            for (int i = 0; i < jsonNames.length(); i++) {
                names.add((String) jsonNames.get(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return names;
    }

}
