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


    /**
     * A constructor which takes a reference to the player that it is serving
     * @param player the player which this handler is serving
     */
    public PlayerRequestHandler(Player player) {
        this.player = player;
    }


    /**
     * Handle the request passed as an argument. If a response is necessary then it can be
     * built and then passed back the calling host using the replyToSender reference passed
     * as an argument.
     * @param request the request details
     * @param replyToSender a reference to the host that is making the request
     */
    @Override
    public void handle(Request request, Sender replyToSender) {
        throw new RuntimeException("Stub");
    }
}
