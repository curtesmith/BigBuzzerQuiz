package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;


import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;

public class SendPlayerNamesRequestHandler extends PlayerRequestHandler {
    public SendPlayerNamesRequestHandler(Player player) {
        super(player);
    }


    @Override
    public void handle(Request request, Sender replyToSender) {
        try {
            SendPlayerNamesRequest sendPlayerNamesRequest = new SendPlayerNamesRequest(request.toString());
            player.updatePlayerNames(sendPlayerNamesRequest.deserialize());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
