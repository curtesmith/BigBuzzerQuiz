package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;


/**
 * An interface the defines constants for the messages that can be sent from the host
 * to a player
 */
public interface PlayerMessageContract {
    String SHOW_QUESTION = "SHOW_QUESTION";
    String INTERRUPT = "INTERRUPT";
    String GAME_OVER = "GAME_OVER";
    String SEND_PLAYER_NAMES = "SEND_PLAYER_NAMES";
}
