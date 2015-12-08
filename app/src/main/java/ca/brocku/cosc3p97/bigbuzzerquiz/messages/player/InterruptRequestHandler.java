package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;

import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;


public class InterruptRequestHandler extends PlayerRequestHandler {
    private static final String TAG = "InterruptRequestHandler";

    public InterruptRequestHandler(Player player) {
        super(player);
    }


    @Override
    public void handle(Request request, Sender replyToSender) {

        try {
            InterruptRequest interruptRequest = new InterruptRequest(request.toString());

            if(interruptRequest.is(InterruptRequest.InterruptionType.TIMEOUT)) {
                player.timeout();
            } else if (interruptRequest.is(InterruptRequest.InterruptionType.SOMEBODY_SUCCEEDED)) {
                player.youLose();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
