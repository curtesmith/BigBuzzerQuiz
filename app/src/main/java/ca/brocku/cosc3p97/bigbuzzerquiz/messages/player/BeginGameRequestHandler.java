package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;


import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;

public class BeginGameRequestHandler extends PlayerRequestHandler {

    public BeginGameRequestHandler(Player player) {
        super(player);
    }

    @Override
    public void handle(Request request, Request.Callback callback) {
        //ignore the request object

        player.beginGame();
    }
}
