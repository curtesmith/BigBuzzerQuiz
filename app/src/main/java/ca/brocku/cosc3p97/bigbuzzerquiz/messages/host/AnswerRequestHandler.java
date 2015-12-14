package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;

import android.util.Log;

import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;


/**
 * Responsible for handling the incoming answer request from the player
 */
public class AnswerRequestHandler extends HostRequestHandler {
    private static final String TAG = "AnswerRequestHandler";


    /**
     * A constructor which takes a reference to the host that it is serving
     * @param host the host which this handler is serving
     */
    public AnswerRequestHandler(Host host) {
        super(host);
    }


    /**
     * Handle the request passed as an argument. If a response is necessary then it can be
     * built and then passed back the calling player using the replyToSender reference passed
     * as an argument.
     * @param request the request details
     * @param replyToSender a reference to the player that is making the request
     */
    @Override
    public void handle(Request request, Sender replyToSender) {
         try {
             AnswerRequest answerRequest = new AnswerRequest(request.toString());
             boolean isCorrect = (boolean) answerRequest.deserialize();
             answer(isCorrect, host.getPlayerIndex(replyToSender));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Invoke the appropriate method of the host to process this request from the player
     * @param correct did the player answer the current question correctly
     * @param playerIndex the player list index of the player making the request to the host
     */
    public void answer(boolean correct, int playerIndex) {
        Log.i(TAG, String.format("answer: invoked with argument [%s]", correct));
        host.handleAnswer(correct, playerIndex);
    }

}
