package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;


import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.RequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;

/**
 * Handler in the Player for requests from the Host
 */
public class PlayerRequestHandler extends RequestHandler {
    Player player;

    public PlayerRequestHandler(Player player) {
        this.player = player;
    }


    @Override
    public void handle(Request request, Sender replyToSender) {
        throw new RuntimeException("Stub");
    }
}
