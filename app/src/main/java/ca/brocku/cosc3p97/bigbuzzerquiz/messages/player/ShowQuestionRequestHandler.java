package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;


import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;

public class ShowQuestionRequestHandler extends PlayerRequestHandler {

    public ShowQuestionRequestHandler(Player player) {
        super(player);
    }


    @Override
    public void handle(Request request, Sender replyToSender) {
        player.showQuestion();
    }
}
