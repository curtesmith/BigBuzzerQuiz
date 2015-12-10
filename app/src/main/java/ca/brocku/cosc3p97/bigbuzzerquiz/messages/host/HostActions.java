package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import java.util.List;

public interface HostActions {
    void addPlayer(String name);


    void getPlayers(GetPlayersCallback callback);

    interface GetPlayersCallback {
        void reply(List<String> names);
    }

    void play(int numberOfQuesitons, int[] categoryKeys);

    void ready();

    void answer(boolean correct);
}