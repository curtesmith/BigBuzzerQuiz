package ca.brocku.cosc3p97.bigbuzzerquiz;


public interface ClientRequestHandler {
    void execute(Request request);
    void onGetPlayers(Request request);
    void onSetGameMaster(Request request);
}
