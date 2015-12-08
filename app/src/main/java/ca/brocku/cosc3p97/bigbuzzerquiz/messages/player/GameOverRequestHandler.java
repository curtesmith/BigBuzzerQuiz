package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Participant;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;

public class GameOverRequestHandler extends PlayerRequestHandler {

    public GameOverRequestHandler(Player player) {
        super(player);
    }


    @Override
    public void handle(Request request, Sender replyToSender) {
        List<Participant> players = new ArrayList<>();

        try {

            GameOverRequest gameOverRequest = new GameOverRequest(request.toString());
            JSONArray jsonPlayers = gameOverRequest.getJSONArray("PLAYERS");
            for(int i=0; i<jsonPlayers.length(); i++) {
                JSONObject jsonPlayer = (JSONObject) jsonPlayers.get(i);
                Participant player = new Participant();
                player.name = jsonPlayer.getString("name");
                player.score = jsonPlayer.getInt("score");
                players.add(player);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        player.gameOver(players);
    }
}
