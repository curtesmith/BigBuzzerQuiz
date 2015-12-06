package ca.brocku.cosc3p97.bigbuzzerquiz;


import org.json.JSONException;

public class SetGameMasterRequest extends Request {
    public static final String SET_GAME_MASTER = "SET_GAME_MASTER";
    public static final String IDENTIFIER = SET_GAME_MASTER;

    public SetGameMasterRequest(String string) throws JSONException {
        super(string);
    }

    public SetGameMasterRequest() {
        super();
    }


    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, SET_GAME_MASTER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
