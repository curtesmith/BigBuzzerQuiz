package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import android.util.Log;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;

/**
 * Responsible for handling the incoming ready request from the player
 */
public class ReadyRequestHandler  extends HostRequestHandler {
    private static final String TAG = "PlayRequestHandler";


    /**
     * A constructor which takes a reference to the host that it is serving
     * @param host the host which this handler is serving
     */
    public ReadyRequestHandler(Host host) {
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
        ready(replyToSender);
    }


    /**
     * Make a call to the host to let inform that the player is ready.
     * @param sender
     */
    public void ready(Sender sender) {
        Log.i(TAG, "ready: invoked");
        host.ready();
    }

}