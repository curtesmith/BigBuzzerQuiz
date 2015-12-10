package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

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
            List<Integer> keys = new ArrayList<>();
            for(int i = 0; i<jsonKeys.length(); i++) {
                keys.add(jsonKeys.getInt(i));
            }

            play(numberOfQuestions, keys);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public void play(int numberOfQuestions, List<Integer> keys) {
        Log.i(TAG, "play: invoked");
        host.play(numberOfQuestions, keys);
    }

}
