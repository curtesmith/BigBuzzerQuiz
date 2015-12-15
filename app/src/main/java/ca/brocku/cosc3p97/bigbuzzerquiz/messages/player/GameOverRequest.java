package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Participant;


/**
 * A request object responsible for sending a request to the player from the host to inform
 * the player that the game is over
 */
public class GameOverRequest extends Request {
    public static final String PLAYERS = "PLAYERS";


    /**
     * Constructor which uses the constructor of the super class that allows the internal
     * JSONObject of this class to be populated from the JSON string passed as an argument
     * @param string JSON string used to load this instance
     * @throws JSONException
     */
    public GameOverRequest(String string) throws JSONException {
        super(string);
    }


    /**
     * Constructor
     */
    public GameOverRequest() {
        super();
    }


    /**
     * Override the setIdentifier method with the value READY
     */
    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, PlayerMessageContract.GAME_OVER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Create the PLAYERS attribute and assign to it the value passed as an argument
     * @param toBeSerialized the boolean value to assign to the attribute
     */
    @Override
    public void serialize(Object toBeSerialized) {
        try {
            List<Participant> players = (List<Participant>) toBeSerialized;
            JSONArray jsonPlayers = new JSONArray();
            for (Participant player : players) {
                JSONObject jsonPlayer = new JSONObject();
                jsonPlayer.put("name", player.name);
                jsonPlayer.put("score", player.score);
                jsonPlayers.put(jsonPlayer);
            }
            put(PLAYERS, jsonPlayers);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Convert the internal value of the PLAYERS attribute into a List<Participant>
     * value and return it to the calling method
     */
    @Override
    public Object deserialize() throws JSONException {
        List<Participant> players = new ArrayList<>();

        JSONArray jsonPlayers = getJSONArray("PLAYERS");
        for(int i=0; i<jsonPlayers.length(); i++) {
            JSONObject jsonPlayer = (JSONObject) jsonPlayers.get(i);
            Participant player = new Participant();
            player.name = jsonPlayer.getString("name");
            player.score = jsonPlayer.getInt("score");
            players.add(player);
        }

        return players;
    }

}
