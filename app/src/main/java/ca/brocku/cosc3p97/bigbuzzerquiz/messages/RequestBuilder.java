package ca.brocku.cosc3p97.bigbuzzerquiz.messages;


public abstract class RequestBuilder {
    public abstract void build(String requestID, Request.Callback callback);
    public abstract void handleResponse(JsonMessage jsonMessage);
}
