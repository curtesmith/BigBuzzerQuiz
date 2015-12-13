package ca.brocku.cosc3p97.bigbuzzerquiz.messages.common;

/**
 * An abstract class that all response handlers will extend from and override the handle method
 */
public abstract class ResponseHandler {
    public Request.Callback callback;

    public ResponseHandler() {
    }

    /**
     * Invoked when a request message has been received and needs to be processed
     * @param response the response message contents
     * @param replyToSender a reference to the sender object that submitted the response
     */
    public abstract void handle(Response response, Sender replyToSender);


    /**
     * A reference to a callback delegate that will be invoked once the response has been handled
     * @param callback the reference to the callback delegate
     */
    public void setCallback(Request.Callback callback) {
        this.callback = callback;
    }
}
