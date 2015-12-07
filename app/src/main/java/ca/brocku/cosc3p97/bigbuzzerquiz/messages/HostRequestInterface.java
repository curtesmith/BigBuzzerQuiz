package ca.brocku.cosc3p97.bigbuzzerquiz.messages;


public interface HostRequestInterface {
    String GET_PLAYERS = "GET_PLAYERS";
    String PLAY = "PLAY";

    Response getPlayers();
    void play();
}
