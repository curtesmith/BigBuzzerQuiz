package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Participant;


public class GameOverRequest extends Request {
    public GameOverRequest(String string) throws JSONException {
        super(string);
    }

    public GameOverRequest() {
        super();
    }


    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, PlayerMessageContract.GAME_OVER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void setPlayers(List<Participant> players) {
        try {
            JSONArray jsonPlayers = new JSONArray();
            for (Participant player : players) {
                JSONObject jsonPlayer = new JSONObject();
                jsonPlayer.put("name", player.name);
                jsonPlayer.put("score", player.score);
                jsonPlayers.put(jsonPlayer);
            }
            put("PLAYERS", jsonPlayers);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
