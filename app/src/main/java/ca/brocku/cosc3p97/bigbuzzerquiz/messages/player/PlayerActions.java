package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;


import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.models.Participant;

public interface PlayerActions {
    void showQuestion();
    void timeout();
    void gameOver(List<Participant> players);
}
