package ca.brocku.cosc3p97.bigbuzzerquiz.messages.common;

import org.json.JSONException;


/**
 * Intended to be a super class that will only be extended by message classes that
 * are specifically managing requests from the host or from the player
 */
public class Request extends JsonMessage {
    public static final String REQUEST = "REQUEST";
    private Callback callback;


    /**
     * A constructor which exposes the constructor of the superclass
     * @param string JSON string that will be used to create the underlying JSONObject
     * @throws JSONException
     */
    public Request(String string) throws JSONException {
        super(string);
    }


    /**
     * A constructor that calls the super class constructor and imposes that the setType and
     * setIdentifier methods have been overridden
     */
    public Request() {
        super();
    }


    /**
     * Assign a value of REQUEST to the TYPE attribute
     */
    public void setType() {
        try {
            put(JsonMessage.TYPE, REQUEST);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Compares the current object to another object passed as and argument and returns a
     * boolean value to indicate if the object passed as an argument is a REQUEST
     * @param message the message object to compare to
     * @return boolean true if the argument is a REQUEST, otherwise return false
     */
    public static boolean is(JsonMessage message) {
        return message.getType().equals(REQUEST);
    }


    /**
     * An interface that exposes a reply method which can be used by a request object
     * to delegate a callback method that can be invoked if the request expects a response
     * from the target object
     */
    public interface Callback {
        void reply(Object result);
    }
}


