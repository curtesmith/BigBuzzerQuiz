package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;


import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;

public class InterruptRequest extends Request {
    public InterruptRequest(String string) throws JSONException {
        super(string);
    }

    public InterruptRequest() {
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
