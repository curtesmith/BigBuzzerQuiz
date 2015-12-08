package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import android.util.Log;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;

public class ReadyRequestHandler  extends HostRequestHandler {
    private static final String TAG = "PlayRequestHandler";

    public ReadyRequestHandler(Host host) {
        super(host);
    }


    @Override
    public void handle(Request request, Sender replyToSender) {
        ready(replyToSender);
    }


    public void ready(Sender sender) {
        Log.i(TAG, "ready: invoked");
        host.ready();
    }

}