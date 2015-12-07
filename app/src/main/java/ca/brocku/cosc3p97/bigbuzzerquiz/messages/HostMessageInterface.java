package ca.brocku.cosc3p97.bigbuzzerquiz.messages;


import ca.brocku.cosc3p97.bigbuzzerquiz.messages.PlayerProxy;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.Response;

public interface HostMessageInterface {
    String GET_PLAYERS = "GET_PLAYERS";
    String PLAY = "PLAY";

    void execute(String string, PlayerProxy.Callback callback);
    Response getPlayers();
    void play();
}
