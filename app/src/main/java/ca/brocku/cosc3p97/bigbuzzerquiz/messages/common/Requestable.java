package ca.brocku.cosc3p97.bigbuzzerquiz.messages.common;

/**
 * Created by Curtis on 2015-12-07.
 */
public interface Requestable {

    void send();
    void addSender(RequestSender sender);
    void setCallback(Request.Callback callback);
}
