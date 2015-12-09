package ca.brocku.cosc3p97.bigbuzzerquiz.models;


import java.util.ArrayList;
import java.util.List;

public class Question implements TimeoutListener {
    private static final int MAXTIME = 5000;
    public boolean blocked;
    private List<Participant> participants;
    private Thread timer;
    private List<TimeoutListener> timeoutListeners = new ArrayList<>();


    public Question(List<Participant> participants, TimeoutListener timeoutListener) {
        this.participants = participants;
        unblock(this.participants);
        blocked = false;
        timeoutListeners.add(timeoutListener);
        timeoutListeners.add(this);
    }


    public void unblock(List<Participant> participants) {
        for(Participant participant : participants) {
            participant.unblock();
        }
    }


    public boolean isBlocked() {
        if(blocked) {
            return true;
        } else {
            if (areAllBlocked(participants)) {
                return true;
            }
        }

        return false;
    }


    private boolean areAllBlocked(List<Participant> participants) {
        boolean result = true;

        for(Participant participant : participants) {
            if(!participant.isBlocked()) {
                result = false;
                break;
            }
        }

        return result;
    }


    public void block(boolean isCorrect) {
        blocked = isCorrect;

        if (blocked && timer != null) {
            stopTimer();
        }
    }


    public void stopTimer() {
        timer.interrupt();
        timer = null;
    }


    public void startTimer() {
        timer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(MAXTIME);
                    for (TimeoutListener timeoutListener : timeoutListeners) {
                        timeoutListener.onTimeout();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        timer.start();
    }


    @Override
    public void onTimeout() {
        block(true);
    }
}

