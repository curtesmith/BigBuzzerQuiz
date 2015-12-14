package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.GameContract;

/**
 * An interface which describes the methods available for the player to call on the host
 */
public interface HostActions {
    void addPlayer(String name);

    void getPlayers(GetPlayersCallback callback);

    interface GetPlayersCallback {
        void reply(List<String> names);
    }

    void play(GameContract game);

    void ready();

    void answer(boolean correct);

    void sendName(String name);
}