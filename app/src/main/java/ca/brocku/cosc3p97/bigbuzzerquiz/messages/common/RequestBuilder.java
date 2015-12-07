package ca.brocku.cosc3p97.bigbuzzerquiz.messages.common;


import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;

public abstract class RequestBuilder {
    public abstract void build(String requestID, Request.Callback callback);
    public abstract void handleResponse(JsonMessage jsonMessage);
}
