package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Response;

public interface HostRequestInterface {
    String GET_PLAYERS = "GET_PLAYERS";
    String PLAY = "PLAY";
    String READY = "READY";

    Response getPlayers();
    void play();
}
