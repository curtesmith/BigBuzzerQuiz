package ca.brocku.cosc3p97.bigbuzzerquiz.messages;


public abstract class RequestHandler {

    public abstract void handle(Request request, RequestHandler.Callback callback);

    interface Callback {
        void reply(String result);
    }
}
