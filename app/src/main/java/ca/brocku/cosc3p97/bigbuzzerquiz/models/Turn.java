package ca.brocku.cosc3p97.bigbuzzerquiz.models;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Turn implements TimeoutListener {
    private static final String TAG = "Turn";
    private static final int MAXTIME = 10000;
    public boolean blocked;
    private List<Participant> participants;
    private Thread timer;
    private List<TimeoutListener> timeoutListeners = new ArrayList<>();


    public Turn(List<Participant> participants, TimeoutListener timeoutListener) {
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
                blocked = true;
                stopTimer();
                return true;
            }
        }

        return false;
    }


    private boolean areAllBlocked(List<Participant> participants) {
        boolean result = true;

        for(Participant participant : participants) {
            if(!participant.isBlocked()) {
                Log.i(TAG, String.format("areAllBlocked: %s is not blocked", participant.name));
                result = false;
                break;
            }
        }

        return result;
    }


    public void block(boolean isCorrect) {
        blocked = isCorrect;

        if (blocked) {
            stopTimer();
        }
    }


    public void stopTimer() {
        if(timer != null) {
            timer.interrupt();
            timer = null;
        }
    }


    public void startTimer() {
        timer = new Thread(new Timer(MAXTIME, new TimeoutListener() {
            @Override
            public void onTimeout() {
                for(TimeoutListener timeoutListener : timeoutListeners) {
                    timeoutListener.onTimeout();
                }
            }
        }));

        timer.start();
    }


    @Override
    public void onTimeout() {
        block(true);
    }
}

