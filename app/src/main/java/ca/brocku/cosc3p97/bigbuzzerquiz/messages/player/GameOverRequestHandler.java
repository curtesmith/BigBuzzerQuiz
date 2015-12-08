package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;


import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;

public class GameOverRequestHandler extends PlayerRequestHandler {

    public GameOverRequestHandler(Player player) {
        super(player);
    }


    @Override
    public void handle(Request request, Sender replyToSender) {
        // TODO: 2015-12-08 add logic to convert the request data to a list of players 
        player.gameOver(null);
    }
}
