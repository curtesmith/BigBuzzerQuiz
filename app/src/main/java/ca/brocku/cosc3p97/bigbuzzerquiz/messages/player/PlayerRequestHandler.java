package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;


import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.RequestHandler;

/**
 * Handler in the Player for requests from the Host
 */
public class PlayerRequestHandler extends RequestHandler {
    @Override
    public void handle(Request request, Request.Callback callback) {
        throw new RuntimeException("Stub");

    }
}
