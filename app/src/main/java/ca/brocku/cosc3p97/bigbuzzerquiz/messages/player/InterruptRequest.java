package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;


import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;

public class InterruptRequest extends Request {
    public static final String INTERRUPT_TYPE = "INTERRUPTION_TYPE";
    public static final String PLAYER_NAME = "PLAYER_NAME";

    public InterruptRequest(String string) throws JSONException {
        super(string);
    }


    public enum InterruptionType {
        TIMEOUT, EVERYONE_FAILED, SOMEBODY_SUCCEEDED
    }


    public InterruptRequest() {
        super();
    }


    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, PlayerMessageContract.INTERRUPT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public boolean is(InterruptRequest.InterruptionType interruptionType) {
        boolean compare = false;

        try {
            compare = get(INTERRUPT_TYPE).equals(interruptionType.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return compare;

    }

}
