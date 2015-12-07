package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;


import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;

public class BeginGameRequest extends Request {
    public BeginGameRequest(String string) throws JSONException {
        super(string);
    }

    public BeginGameRequest() {
        super();
    }


    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, PlayerMessageInterface.BEGIN_GAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
