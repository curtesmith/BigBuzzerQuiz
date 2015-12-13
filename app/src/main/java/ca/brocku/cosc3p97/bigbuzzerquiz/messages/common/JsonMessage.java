package ca.brocku.cosc3p97.bigbuzzerquiz.messages.common;


import org.json.JSONException;
import org.json.JSONObject;


/**
 * Base class from which to build the request and response messages which extends the
 * JSONObject class
 */
public class JsonMessage extends JSONObject {
    public static final String TYPE = "TYPE";
    public static final String IDENTIFIER = "IDENTIFIER";


    /**
     * A constructor which exposes the JSONObject super class constructor
     * @param string
     * @throws JSONException
     */
    public JsonMessage(String string) throws JSONException {
        super(string);
    }


    /**
     * A separate constructor the will impose that any derived class will implement the
     * setType and setIdentifier methods
     */
    public JsonMessage() {
        super();
        setType();
        setIdentifier();
    }


    /**
     * Retrieves the TYPE attribute
     * @return
     */
    public String getType() {
        try {
            return getString(TYPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }


    /**
     * Intended to set the TYPE attribute of the object which any derived class must override
     * and implement
     */
    protected void setType() {
        throw new RuntimeException("Stub");
    }


    /**
     * Retrieves the IDENTIFIER attribute
     * @return
     */
    public String getIdentifier() {
        try {
            return getString(IDENTIFIER);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }


    /**
     * Intended to set the IDENTIFIER attribute of the object which any derived class must
     * override
     */
    protected void setIdentifier() {
        throw new RuntimeException("Stub");
    }

}
