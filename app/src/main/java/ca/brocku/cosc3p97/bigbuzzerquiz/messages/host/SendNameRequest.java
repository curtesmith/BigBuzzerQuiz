package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;

public class SendNameRequest extends Request {
    public static final String NAME = "NAME";

    public SendNameRequest(String string) throws JSONException {
        super(string);
    }

    public SendNameRequest() {
        super();
    }


    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, HostRequestContract.SEND_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
