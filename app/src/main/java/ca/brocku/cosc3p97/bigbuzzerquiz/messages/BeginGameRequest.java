package ca.brocku.cosc3p97.bigbuzzerquiz.messages;


import org.json.JSONException;

public class BeginGameRequest extends Request {
    public BeginGameRequest(String string) throws JSONException {
        super(string);
    }

    public BeginGameRequest() {
        super();
    }


    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, PlayerMessageInterface.BEGIN_GAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
