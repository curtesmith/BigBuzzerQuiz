package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;


import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;

public interface PlayerMessageInterface {
    String BEGIN_GAME = "BEGIN_GAME";

    void handleServerResponse(JsonMessage message);
    void createRequest(String requestID, Player.CallbackListener callback);
    void beginGame();
}
