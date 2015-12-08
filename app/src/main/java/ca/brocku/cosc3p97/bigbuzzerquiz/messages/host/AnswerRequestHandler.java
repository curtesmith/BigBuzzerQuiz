package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;

import android.util.Log;

import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;


public class AnswerRequestHandler extends HostRequestHandler {
    private static final String TAG = "AnswerRequestHandler";

    public AnswerRequestHandler(Host host) {
        super(host);
    }


    @Override
    public void handle(Request request, Sender replyToSender) {
         try {
            answer(request.getBoolean("CORRECT"), host.getPlayerIndex(replyToSender));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void answer(boolean correct, int playerIndex) {
        Log.i(TAG, String.format("answer: invoked with argument [%s]", correct));
        host.adjustPoints(correct, playerIndex);
        host.answer(correct);
    }

}
