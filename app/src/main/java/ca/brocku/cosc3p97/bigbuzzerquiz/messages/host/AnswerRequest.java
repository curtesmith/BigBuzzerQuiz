package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;

import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JSONMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;


public class AnswerRequest extends Request {
    public AnswerRequest(String string) throws JSONException {
        super(string);
    }

    public AnswerRequest() {
        super();
    }


    public void setIdentifier() {
        try {
            put(JSONMessage.IDENTIFIER, HostRequestInterface.ANSWER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setCorrect(boolean correct) {
        try {
            put("CORRECT", correct);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}