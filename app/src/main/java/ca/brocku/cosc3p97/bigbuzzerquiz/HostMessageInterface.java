package ca.brocku.cosc3p97.bigbuzzerquiz;


public interface HostMessageInterface {
    String GET_PLAYERS = "GET_PLAYERS";

    void execute(String string, PlayerProxy.Callback callback);

    void getPlayers(GetPlayersCallback callback);

    interface GetPlayersCallback {
        void done(Response response);
    }
}
