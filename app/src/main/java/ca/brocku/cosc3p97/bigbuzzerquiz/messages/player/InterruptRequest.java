package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;


import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;


/**
 * A request object responsible for sending a request to the player from the host to interrupt
 * the player's turn and inform them that their turn has timed out, that everyone answered the
 * question incorrectly or that someone has already answered the question correctly
 */
public class InterruptRequest extends Request {
    public static final String INTERRUPT_TYPE = "INTERRUPTION_TYPE";
    public static final String PLAYER_NAME = "PLAYER_NAME";

    public enum InterruptionType {
        TIMEOUT, EVERYONE_FAILED, SOMEBODY_SUCCEEDED
    }


    /**
     * Constructor which uses the constructor of the super class that allows the internal
     * JSONObject of this class to be populated from the JSON string passed as an argument
     * @param string JSON string used to load this instance
     * @throws JSONException
     */
    public InterruptRequest(String string) throws JSONException {
        super(string);
    }


    /**
     * Constructor
     */
    public InterruptRequest() {
        super();
    }


    /**
     * Override the setIdentifier method with the value READY
     */
    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, PlayerMessageContract.INTERRUPT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Compares the current instance to an InterruptionType passed as an argument
     * and response with a boolean value of true if the type matches or false if there
     * is no match
     * @param interruptionType the InterruptionType to compare to
     * @return boolean true if the types match otherwise false
     */
    public boolean is(InterruptRequest.InterruptionType interruptionType) {
        boolean compare = false;

        try {
            compare = deserialize().equals(interruptionType.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return compare;

    }

    /**
     * Create the INTERRUPT_TYPE attribute and assign to it the value passed as an argument
     * @param toBeSerialized the boolean value to assign to the attribute
     */
    @Override
    public void serialize(Object toBeSerialized) {
        InterruptionType type = (InterruptionType) toBeSerialized;
        try {
            put(INTERRUPT_TYPE, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert the internal value of the INTERRUPT_TYPE attribute into an InterruptionType
     * value and return it to the calling method
     */
    @Override
    public Object deserialize() throws JSONException {
        return get(INTERRUPT_TYPE);
    }

}
