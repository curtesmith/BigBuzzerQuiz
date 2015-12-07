package ca.brocku.cosc3p97.bigbuzzerquiz.messages;


import org.json.JSONException;
import org.json.JSONObject;

public class JsonMessage extends JSONObject {
    public static final String TYPE = "TYPE";
    public static final String IDENTIFIER = "IDENTIFIER";

    public JsonMessage(String string) throws JSONException {
        super(string);
    }


    public JsonMessage() {
        super();
        setType();
        setIdentifier();
    }


    public String getType() {
        try {
            return getString(TYPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }


    protected void setType() {
        throw new RuntimeException("Stub");
    }


    public String getIdentifier() {
        try {
            return getString(IDENTIFIER);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }


    protected void setIdentifier() {
        throw new RuntimeException("Stub");
    }

}
