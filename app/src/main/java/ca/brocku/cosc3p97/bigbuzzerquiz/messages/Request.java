package ca.brocku.cosc3p97.bigbuzzerquiz.messages;

import org.json.JSONException;

public class Request extends JsonMessage {
    public static final String REQUEST = "REQUEST";


    public Request(String string) throws JSONException {
        super(string);
    }

    public Request() {
        super();
    }


    public void setType() {
        try {
            put(JsonMessage.TYPE, REQUEST);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static boolean is(JsonMessage message) {
        return message.getType().equals(REQUEST);
    }
}


