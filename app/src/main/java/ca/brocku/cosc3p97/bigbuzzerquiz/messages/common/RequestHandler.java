package ca.brocku.cosc3p97.bigbuzzerquiz.messages.common;


import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;

public abstract class RequestHandler {

    public abstract void handle(Request request, Request.Callback callback);

}
