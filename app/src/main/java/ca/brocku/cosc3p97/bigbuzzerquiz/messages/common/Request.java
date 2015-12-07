package ca.brocku.cosc3p97.bigbuzzerquiz.messages.common;

import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;

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


    public interface Callback {
        void reply(Object result);
    }
}


