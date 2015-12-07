package ca.brocku.cosc3p97.bigbuzzerquiz.messages;


import ca.brocku.cosc3p97.bigbuzzerquiz.messages.HostProxy;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.JsonMessage;

public interface PlayerMessageInterface {
    String BEGIN = "BEGIN";

    void execute(String string, HostProxy.Callback callback);
    void handleServerResponse(JsonMessage message);
    void createRequest(String requestId, Player.CallbackListener callback);
    void begin();
}
