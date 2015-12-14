package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;

import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;

/**
 * A request object responsible for sending a request to the host from the player with
 * an answer to the current question and passing along an argument to inform the host
 * if the player was correct or incorrect.
 */
public class AnswerRequest extends Request {
    public static final String CORRECT = "CORRECT";

    /**
     * Constructor
     */
    public AnswerRequest() {
        super();
    }

    /**
     * A constructor which exposes the constructor of the superclass
     * @param string JSON string that will be used to create the underlying JSONObject
     * @throws JSONException
     */
    public AnswerRequest(String string) throws JSONException {
        super(string);
    }


    /**
     * Override the setIdentifier method with the value ANSWER
     */
    @Override
    protected void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, HostRequestContract.ANSWER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Create the CORRECT attribute and assign to it the value passed as an argument
     * @param toBeSerialized the boolean value to assign to the attribute
     */
    @Override
    public void serialize(Object toBeSerialized) {

        try {
            put(AnswerRequest.CORRECT, (boolean) toBeSerialized);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Convert the internal value of the CORRECT attribute into a boolean value and return
     * it to the calling method
     */
    @Override
    public Object deserialize() throws JSONException {
        return getBoolean(AnswerRequest.CORRECT);
    }
}