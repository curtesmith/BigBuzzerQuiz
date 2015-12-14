package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;


/**
 * A request object responsible for sending a request to the host from the player to inform
 * the host that the player is ready for the next question of the game
 */
public class ReadyRequest extends Request {

    /**
     * Constructor which uses the constructor of the super class that allows the internal
     * JSONObject of this class to be populated from the JSON string passed as an argument
     * @param string JSON string used to load this instance
     * @throws JSONException
     */
    public ReadyRequest(String string) throws JSONException {
        super(string);
    }


    /**
     * Constructor
     */
    public ReadyRequest() {
        super();
    }


    /**
     * Override the setIdentifier method with the value READY
     */
    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, HostRequestContract.READY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
