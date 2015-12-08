package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;

import android.util.Log;

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
        play();
    }


    public void play() {
        Log.i(TAG, "play: invoked");
        host.play();
    }

}
