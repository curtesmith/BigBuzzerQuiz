package ca.brocku.cosc3p97.bigbuzzerquiz.messages;

import org.json.JSONException;

public class GetPlayersRequest extends Request {
    public GetPlayersRequest(String string) throws JSONException {
        super(string);
    }

    public GetPlayersRequest() {
        super();
    }


    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, HostMessageInterface.GET_PLAYERS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
