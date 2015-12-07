package ca.brocku.cosc3p97.bigbuzzerquiz.messages;


import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;

public interface PlayerMessageInterface {
    String BEGIN_GAME = "BEGIN_GAME";

    void handleServerResponse(JsonMessage message);
    void createRequest(String requestID, Player.CallbackListener callback);
    void beginGame();
}
