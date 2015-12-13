package ca.brocku.cosc3p97.bigbuzzerquiz.messages.common;


import org.json.JSONException;

/**
 * Intended to be a super class that will only be extended by message classes that
 * are specifically managing responses from the host or from the player
 */
public class Response extends JsonMessage {
    public static final String RESPONSE = "RESPONSE";


    /**
     * A constructor which exposes the constructor of the superclass
     * @param string JSON string that will be used to create the underlying JSONObject
     * @throws JSONException
     */
    public Response(String string) throws JSONException {
        super(string);
    }


    /**
     * A constructor that calls the super class constructor and imposes that the setType and
     * setIdentifier methods have been overridden
     */
    public Response() {
        super();
        setType();
    }


    /**
     * Assign a value of RESPONSE to the TYPE attribute
     */
    public void setType() {
        try {
            put(JsonMessage.TYPE, RESPONSE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public Object getResult() {
        throw new RuntimeException("Stub");
    }
}
