package ca.brocku.cosc3p97.bigbuzzerquiz.messages.common;


/**
 * An interface which exposes that an implementer's of this interface will have the ability
 * to be sent as a message and also to add a sender to itself
 */
public interface Sendable {

    void send();
    void addSender(Sender sender);
}
