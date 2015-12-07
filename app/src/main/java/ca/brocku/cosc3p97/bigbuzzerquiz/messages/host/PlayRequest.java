package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;

import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;


public class PlayRequest extends Request {
    public PlayRequest(String string) throws JSONException {
        super(string);
    }

    public PlayRequest() {
        super();
    }


    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, HostRequestInterface.PLAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
