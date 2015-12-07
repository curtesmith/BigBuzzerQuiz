package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;

import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.HostRequestInterface;


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
