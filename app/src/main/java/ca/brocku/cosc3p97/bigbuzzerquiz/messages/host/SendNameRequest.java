package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;


/**
 * A request object responsible for sending a request to the host from the player with
 * the name of the player
 */
public class SendNameRequest extends Request {
    public static final String NAME = "NAME";

    /**
     * Constructor which uses the constructor of the super class that allows the internal
     * JSONObject of this class to be populated from the JSON string passed as an argument
     * @param string JSON string used to load this instance
     * @throws JSONException
     */
    public SendNameRequest(String string) throws JSONException {
        super(string);
    }


    /**
     * Constructor
     */
    public SendNameRequest() {
        super();
    }


    /**
     * Override the setIdentifier method with the value ANSWER
     */
    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, HostRequestContract.SEND_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the NAME attribute and assign to it the value passed as an argument
     * @param toBeSerialized the boolean value to assign to the attribute
     */
    @Override
    public void serialize(Object toBeSerialized) {
        try {
            put(NAME, toBeSerialized);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Convert the internal value of the NAME attribute into a String value and return
     * it to the calling method
     */
    @Override
    public Object deserialize() throws JSONException {
        String name = "";

        try {
            name = getString(SendNameRequest.NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return name;
    }
}
