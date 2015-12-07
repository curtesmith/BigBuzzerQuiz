package ca.brocku.cosc3p97.bigbuzzerquiz.messages.common;


public abstract class RequestBuilder {
    public abstract void build(String requestID, Request.Callback callback);
}
