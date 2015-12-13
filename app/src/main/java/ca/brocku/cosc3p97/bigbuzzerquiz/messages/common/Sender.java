package ca.brocku.cosc3p97.bigbuzzerquiz.messages.common;


/**
 * An interface which exposes an implementer's ability to send a message
 */
public interface Sender {
    void send(String message);
}
