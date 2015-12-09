package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;


import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JSONMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;

public class ShowQuestionRequest extends Request {
    public ShowQuestionRequest(String string) throws JSONException {
        super(string);
    }

    public ShowQuestionRequest() {
        super();
    }


    public void setIdentifier() {
        try {
            put(JSONMessage.IDENTIFIER, PlayerMessageInterface.SHOW_QUESTION);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
