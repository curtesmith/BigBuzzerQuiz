package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;

import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;

/**
 * A request object responsible for sending a request to the host from the player requesting
 * the list of names of the players that are connected to the host.
 */
public class GetPlayersRequest extends Request {


    /**
     * Constructor
     */
    public GetPlayersRequest() {
        super();
    }


    /**
     * Override the setIdentifier method with the value GET_PLAYERS
     */
    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, HostRequestContract.GET_PLAYERS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
