package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;


public class GameOverRequest extends Request {
    public GameOverRequest(String string) throws JSONException {
        super(string);
    }

    public GameOverRequest() {
        super();
    }


    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, PlayerMessageInterface.GAME_OVER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void setPlayers(List<Host.Player> players) {
        try {
            JSONArray jsonPlayers = new JSONArray();
            for (Host.Player player : players) {
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
