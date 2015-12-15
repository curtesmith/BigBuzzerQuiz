package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;


import org.json.JSONException;

import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;


/**
 * Responsible for handling the incoming send player names request from the host
 */
public class SendPlayerNamesRequestHandler extends PlayerRequestHandler {

    /**
     * A constructor which takes a reference to the player that it is serving
     * @param player the player which this handler is serving
     */
    public SendPlayerNamesRequestHandler(Player player) {
        super(player);
    }


    /**
     * Handle the request passed as an argument. If a response is necessary then it can be
     * built and then passed back the calling host using the replyToSender reference passed
     * as an argument. This method will call the updatePlayerNames method of the player passing the
     * arguments passed in the request object.
     * @param request the request details
     * @param replyToSender a reference to the host that is making the request
     */
    @Override
    public void handle(Request request, Sender replyToSender) {
        try {
            SendPlayerNamesRequest sendPlayerNamesRequest = new SendPlayerNamesRequest(request.toString());
            player.updatePlayerNames((List<String>)sendPlayerNamesRequest.deserialize());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
