package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;


import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;

public class ShowQuestionRequestHandler extends PlayerRequestHandler {

    public ShowQuestionRequestHandler(Player player) {
        super(player);
    }


    @Override
    public void handle(Request request, Sender replyToSender) {
        ShowQuestionRequest showQuestionRequest = null;
        int key;

        try {
            showQuestionRequest = new ShowQuestionRequest(request.toString());
            key = showQuestionRequest.getInt(ShowQuestionRequest.QUESTION_KEY);
            player.showQuestion(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
