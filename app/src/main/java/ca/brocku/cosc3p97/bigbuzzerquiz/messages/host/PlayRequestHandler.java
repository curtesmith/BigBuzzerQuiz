package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;

import android.util.Log;

import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.GameContract;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;


/**
 * Responsible for handling the incoming play request from the player
 */
public class PlayRequestHandler extends HostRequestHandler {
    private static final String TAG = "PlayRequestHandler";

    /**
     * A constructor which takes a reference to the host that it is serving
     * @param host the host which this handler is serving
     */
    public PlayRequestHandler(Host host) {
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
            PlayRequest playRequest = new PlayRequest(request.toString());
            GameContract game = (GameContract) playRequest.deserialize();

            play(game);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * Invoke the appropriate method of the host to process this request from the player
     * @param game a reference to the game details that the player is requesting to play
     */
    public void play(GameContract game) {
        Log.i(TAG, "play: invoked");
        host.play(game);
    }

}
