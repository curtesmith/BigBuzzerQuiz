package ca.brocku.cosc3p97.bigbuzzerquiz;

import org.json.JSONException;

public class GetPlayersRequest extends Request {
    public static final String GET_PLAYERS = "GET_PLAYERS";
    public static final String IDENTIFIER = GET_PLAYERS;

    public GetPlayersRequest(String string) throws JSONException {
        super(string);
    }

    public GetPlayersRequest() {
        super();
    }


    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, GET_PLAYERS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
