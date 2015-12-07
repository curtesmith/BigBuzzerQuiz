package ca.brocku.cosc3p97.bigbuzzerquiz.messages;

import org.json.JSONException;


public class PlayRequest extends Request {
    public PlayRequest(String string) throws JSONException {
        super(string);
    }

    public PlayRequest() {
        super();
    }


    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, HostMessageInterface.PLAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
