package ca.brocku.cosc3p97.bigbuzzerquiz.messages.common;


/**
 * An abstract class that all request handlers will extend from and override the handle method
 */
public abstract class RequestHandler {


    /**
     * Invoked when a request message has been received and needs to be processed
     * @param request the request message contents
     * @param replyToSender a reference to the sender object that submitted the request
     */
    public abstract void handle(Request request, Sender replyToSender);

}
