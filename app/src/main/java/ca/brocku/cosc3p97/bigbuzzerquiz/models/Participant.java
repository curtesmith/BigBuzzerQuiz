package ca.brocku.cosc3p97.bigbuzzerquiz.models;


import android.util.Log;

public class Participant {
    private static final String TAG = "Participant";
    public String name;
    public int score;
    private boolean blocked;


    public boolean isBlocked() {
        return blocked;
    }


    public void block() {
        Log.i(TAG, String.format("block: invoked on player name %s", name));
        blocked = true;
    }


    public void unblock() {
        blocked = false;
    }


    public void adjustScore(boolean isCorrect) {
        if(isCorrect) {
            score++;
        } else {
            score--;
        }
    }
}
