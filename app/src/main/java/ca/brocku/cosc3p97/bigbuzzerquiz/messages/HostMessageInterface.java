package ca.brocku.cosc3p97.bigbuzzerquiz.messages;


public interface HostMessageInterface {
    String GET_PLAYERS = "GET_PLAYERS";
    String PLAY = "PLAY";

    void execute(String string, PlayerProxy.Callback callback);
    void createRequest(String requestID);
    Response getPlayers();
    void play();
}
