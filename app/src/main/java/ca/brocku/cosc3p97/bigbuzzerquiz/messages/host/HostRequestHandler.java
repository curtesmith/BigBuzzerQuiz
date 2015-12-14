package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.RequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;


/**
 * An abstract class that defines a handler for requests made to the Host
 */
public abstract class HostRequestHandler extends RequestHandler {
    Host host;

    /**
     * Constructor that takes a reference to the host that this handler will be serving
     * @param host the host that the handler will be serving
     */
    public HostRequestHandler(Host host) {
        this.host = host;
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
        throw new RuntimeException("Stub");
    }

}
