package ca.brocku.cosc3p97.bigbuzzerquiz.models;


public class Participant {
    public String name;
    public int score;
    private boolean blocked;


    public boolean isBlocked() {
        return blocked;
    }


    public void block() {
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
