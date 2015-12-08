package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;


import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;

public class TimeoutRequest extends Request {
    public TimeoutRequest(String string) throws JSONException {
        super(string);
    }

    public TimeoutRequest() {
        super();
    }


    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, PlayerMessageInterface.TIMEOUT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
