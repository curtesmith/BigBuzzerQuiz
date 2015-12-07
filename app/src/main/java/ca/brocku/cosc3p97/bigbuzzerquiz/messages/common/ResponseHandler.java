package ca.brocku.cosc3p97.bigbuzzerquiz.messages.common;


public abstract class ResponseHandler {
    public Request.Callback callback;

    public ResponseHandler() {
    }

    public abstract void handle(Response response, Sender replyToSender);


    public void setCallback(Request.Callback callback) {
        this.callback = callback;
    }
}
