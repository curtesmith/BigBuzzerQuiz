package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;


public class PlayRequestHandler extends HostRequestHandler {
    private static final String TAG = "PlayRequestHandler";

    public PlayRequestHandler(Host host) {
        super(host);
    }


    @Override
    public void handle(Request request, Sender replyToSender) {
        try {
            PlayRequest playRequest = new PlayRequest(request.toString());
            int numberOfQuestions = playRequest.getInt(PlayRequest.NUMBER_OF_QUESTIONS);
            JSONArray jsonKeys = playRequest.getJSONArray(PlayRequest.KEYS);
            int keys[] = new int[jsonKeys.length()];
            for(int i = 0; i<jsonKeys.length(); i++) {
                keys[i] = jsonKeys.getInt(i);
            }

            play(numberOfQuestions, keys);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public void play(int numberOfQuestions, int[] keys) {
        Log.i(TAG, "play: invoked");
        host.play(numberOfQuestions, keys);
    }

}
