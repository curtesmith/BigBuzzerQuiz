package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;

public class ReadyRequest extends Request {
    public ReadyRequest(String string) throws JSONException {
        super(string);
    }

    public ReadyRequest() {
        super();
    }


    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, HostRequestInterface.READY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
