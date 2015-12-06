package ca.brocku.cosc3p97.bigbuzzerquiz;


import java.util.List;

public interface HostActions {
    void addPlayer(String name);


    void getPlayers(GetPlayersCallback callback);

    interface GetPlayersCallback {
        void callback(List<String> names);
    }
}