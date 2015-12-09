package ca.brocku.cosc3p97.bigbuzzerquiz.models;


public class Timer implements Runnable {
    long sleepTime;
    TimeoutListener listener;

    public Timer(long sleepTime, TimeoutListener listener) {
        this.sleepTime = sleepTime;
        this.listener = listener;
    }


    @Override
    public void run() {
        try {
            Thread.currentThread();
            Thread.sleep(sleepTime);
            listener.onTimeout();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
